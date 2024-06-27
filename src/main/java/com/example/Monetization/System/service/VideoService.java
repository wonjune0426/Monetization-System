package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.CreateVideoRequestDto;
import com.example.Monetization.System.dto.request.PauseVideoRequestDto;
import com.example.Monetization.System.dto.response.VideoViewResponseDto;
import com.example.Monetization.System.entity.*;
import com.example.Monetization.System.repository.AdView_historyRepository;
import com.example.Monetization.System.repository.VideoRepository;
import com.example.Monetization.System.repository.VideoView_historyRepository;
import com.example.Monetization.System.repository.Video_Ad_infoRepository;
import com.example.Monetization.System.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoView_historyRepository videoViewHistoryRepository;
    private final AdView_historyRepository adView_historyRepository;
    private final Video_Ad_infoRepository video_ad_infoRepository;
    private final RedisTemplate<String, String> redisTemplate;


    // 문자열로 저장된 값들을 Time 형태로 변화하기 위한 객체
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    // 광고가 재생되는 시점 5분
    private static final LocalTime adPlayTime = LocalTime.parse("00:05:00", formatter);
    private final Video_Ad_infoRepository video_Ad_infoRepository;


    // Video 생성
    public String createVideo(CreateVideoRequestDto createVideoRequestDto, MemberDetailsImpl memberDetails) {
        // 현재 로그인 된 Member 정보를 받아옴
        Member member = memberDetails.getMember();

        // Member의 Authority가 false면 video 등록 불가
        if (!member.isAuthority()) return "판매자 권한이 없어 video등록이 불가 합니다.";

        // requestDto를 통해 받아온 정보로 Video 객체 생성
        Video video = new Video(UUID.randomUUID(), member, createVideoRequestDto.getVideo_name(),
                createVideoRequestDto.getVideo_length(), createVideoRequestDto.getVideo_description(), 0L, false);

        // Video 객체 저장
        videoRepository.save(video);

        return "Video 생성 성공";
    }

    // Video 시청
    public VideoViewResponseDto videoView(UUID videoId, MemberDetailsImpl memberDetails) {
        // 반환할 Response 객체
        VideoViewResponseDto videoViewResponseDto = new VideoViewResponseDto();

        // 현재 로그인한 member 객체
        Member member = memberDetails.getMember();

        // Video의 존재 여부 확인
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 영상입니다.")
        );

        // Video의 삭제 여부 확인
        if (video.isDelete_check()) {
            videoViewResponseDto.setErrorMessage("삭제된 영상입니다.");
            return videoViewResponseDto;
        }

        // Login한 Member와 해당 영상 게시자 Member 정보가 일치하는지 확인
        if (!member.getMember_id().equals(video.getMember().getMember_id())) {
            // 어뷰징 방지를 위한 메서드
            if (!videoViewMember(video.getVideo_id(), member.getMember_id())) {
                VideoView_history videoView_history = new VideoView_history(UUID.randomUUID(), member, video);
                videoViewHistoryRepository.save(videoView_history);
            }
        }

        // 최근 시청기록 조회를 위한 메서드
        String lastWatchTime = lastWatchTimeCheck(video.getVideo_id(), member.getMember_id());

        videoViewResponseDto.setVideo_name(video.getVideo_name());
        videoViewResponseDto.setVideo_length(video.getVideo_length());
        videoViewResponseDto.setVideo_description(video.getVideo_description());
        videoViewResponseDto.setLikes(videoViewResponseDto.getLikes());
        videoViewResponseDto.setLast_watch_time(lastWatchTime);

        return videoViewResponseDto;
    }

    // Vdieo 중단
    public String videoPause(UUID videoId, MemberDetailsImpl memberDetails, PauseVideoRequestDto pauseVideoRequestDto) {
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new IllegalArgumentException("존재 하지 않는 Video입니다.")
        );

        // Client가 전달한 시간이 해당 영상의 길이 보다 길 경우
        LocalTime videoTime = LocalTime.parse(video.getVideo_length(), formatter);
        LocalTime lastWatchTime = LocalTime.parse(pauseVideoRequestDto.getLast_watch_time(), formatter);

        if (lastWatchTime.isAfter(videoTime)) {
            return "영상의 재생시간보다 긴 시간입니다";
        }

        // 어뷰징 방지 확인
        if (!memberDetails.getMember().getMember_id().equals(video.getMember().getMember_id())) {
            if (!videoViewMember(video.getVideo_id(), memberDetails.getMember().getMember_id())) {
                // 광고 시청 횟수 계산
                // 마지막 시청 시간 확인
                Member member = memberDetails.getMember();
                LocalTime saveWatchTime = LocalTime.parse(lastWatchTimeCheck(videoId, member.getMember_id()), formatter);

                // 초단위로 변환
                long saveWatchTimeToSeconds = Duration.between(LocalTime.MIN, saveWatchTime).getSeconds();
                long lastWatchTimeToSeconds = Duration.between(LocalTime.MIN, lastWatchTime).getSeconds();
                long adTimeToSeconds = Duration.between(LocalTime.MIN, adPlayTime).getSeconds();

                // 마지막 시청 시간 / 광고 플레이 시간  - 저장된 마지막 시청 시간 / 광고 플레이 시간
                long adPlayCount = lastWatchTimeToSeconds / adTimeToSeconds - saveWatchTimeToSeconds / adTimeToSeconds;

                Video_Ad_Info video_ad_info = video_Ad_infoRepository.findByVideo(video);
                for (long i = 0; i < adPlayCount; i++) {
                    AdView_history adViewHistory = new AdView_history(UUID.randomUUID(), video_ad_info);
                    adView_historyRepository.save(adViewHistory);
                }
            }
        }

        updateLastWatchTime(videoId, memberDetails.getMember().getMember_id(), pauseVideoRequestDto.getLast_watch_time());
        return "Video 재생시간 update";
    }

    // video를 시청한 기록을 확인하는 메서드
    private boolean videoViewMember(UUID videoId, String memberId) {
        String key = "viedoViewMember : " + videoId + "_" + memberId;

        if (redisTemplate.hasKey(key)) {
            return true;
        } else {
            redisTemplate.opsForValue().set(key, "watched", 30, TimeUnit.SECONDS);

            return false;
        }
    }

    // 마지막 시청 시간을 확인하는 메서드
    private String lastWatchTimeCheck(UUID videoId, String memberId) {
        String key = "lastWatchTime : " + videoId + "_" + memberId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return redisTemplate.opsForValue().get(key);
        } else {
            return "00:00:00";
        }
    }

    // 마지막 시청시간 기록
    private void updateLastWatchTime(UUID videoId, String memberId, String lastWatchTime) {
        String key = "lastWatchTime : " + videoId + "_" + memberId;
        redisTemplate.opsForValue().set(key, lastWatchTime, 1, TimeUnit.DAYS);
    }
}
