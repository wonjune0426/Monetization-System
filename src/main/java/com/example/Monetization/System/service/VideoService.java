package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.CreateVideoRequestDto;
import com.example.Monetization.System.dto.request.PauseVideoRequestDto;
import com.example.Monetization.System.dto.response.VideoViewResponseDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.VideoView_History;
import com.example.Monetization.System.repository.VideoRepository;
import com.example.Monetization.System.repository.VideoView_HistoryRepository;
import com.example.Monetization.System.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoView_HistoryRepository videoViewHistoryRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // 광고가 재생되는 시점 5분
    private static final Duration adTime = parseDuration("00:05:00");

    // Video 생성
    public String createVideo(CreateVideoRequestDto createVideoRequestDto, MemberDetailsImpl memberDetails) {
        // 현재 로그인 된 Member 정보를 받아옴
        Member member = memberDetails.getMember();

        // Member의 Authority가 false면 video 등록 불가
        if (!member.isAuthority()) return "판매자 권한이 없어 영상 들록이 불가 합니다.";

        // requestDto를 통해 받아온 정보로 Video 객체 생성
      // Video video = new Video(UUID.randomUUID(), member, createVideoRequestDto.getVideo_name(),
        //       createVideoRequestDto.getVideo_length(), createVideoRequestDto.getVideo_description(), "00:00:00", false);

        // Video 객체 저장
         //   videoRepository.save(video);

        return "Video 생성 성공";
    }

    // Video 시청
    @Transactional
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

        // 어뷰징 방지를 위한 메서드
        // Login한 Member와 해당 영상 게시자 Member 정보가 일치하는지 확인 및 어뷰징 방지
        if (!member.getMember_id().equals(video.getMember().getMember_id()) && !videoViewMember(video.getVideo_id(), member.getMember_id())) {
            VideoView_History videoView_history = videoViewHistoryRepository.findByMemberAndVideo(member, video)
                    .orElseGet(() -> {
                        VideoView_History newVideoView_history = new VideoView_History();
                        newVideoView_history.setMember(member);
                        newVideoView_history.setVideo(video);
                        newVideoView_history.setView_count(0L);
                        return videoViewHistoryRepository.save(newVideoView_history); // 새 엔티티 저장
                    });
            videoView_history.setView_count(videoView_history.getView_count() + 1);
        }

        // 최근 시청기록 조회를 위한 메서드
        String lastWatchTime = lastWatchTimeCheck(video.getVideo_id(), member.getMember_id());

        videoViewResponseDto.setVideo_name(video.getVideo_name());
//        videoViewResponseDto.setVideo_length(video.getVideo_length());
        videoViewResponseDto.setVideo_description(video.getVideo_description());
        videoViewResponseDto.setLast_watch_time(lastWatchTime);

        return videoViewResponseDto;
    }

    // Vdieo 중단
    @Transactional
    public String videoPause(UUID videoId, MemberDetailsImpl memberDetails, PauseVideoRequestDto pauseVideoRequestDto) {
        // video 확인
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new IllegalArgumentException("존재 하지 않는 Video입니다.")
        );

        // 로그인한 member 확인
        Member member = memberDetails.getMember();

        String requestWatchTime = pauseVideoRequestDto.getLast_watch_time();

        // 전달받은 마지막 시청 시간
        Duration lastWatchTime = parseDuration(requestWatchTime);

        // 영상의 총 길이
//        Duration video_length = parseDuration(video.getVideo_length());

        // 저장된 마지막 시청 시간
        Duration saveWatchTime = parseDuration(lastWatchTimeCheck(video.getVideo_id(), member.getMember_id()));


 //       if (lastWatchTime.compareTo(video_length) > 0) {
//            return "마지막 중단 시점이 영상의 길이보다 깁니다.";
 //       } else if (video_length.equals(lastWatchTime)) requestWatchTime = "00:00:00";

        // 어뷰징 방지
        if (!member.getMember_id().equals(video.getMember().getMember_id()) && !videoViewMember(video.getVideo_id(), member.getMember_id())) {
            Duration totalPlusTime = saveWatchTime.plus(lastWatchTime);
   //         video.setTotal_playtime(formatDuration(totalPlusTime));
        }

        updateLastWatchTime(videoId, memberDetails.getMember().getMember_id(), requestWatchTime);
        return "Video 재생시간 update";
    }

    // video를 시청한 기록을 확인하는 메서드
    private boolean videoViewMember(UUID videoId, String memberId) {
        String key = "viedoViewMember : " + videoId + "_" + memberId;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
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

    // HH:mm:ss 형식의 문자열을 Duration으로 변환하는 메서드
    private static Duration parseDuration(String timeString) throws DateTimeParseException {
        String[] parts = timeString.split(":");
        if (parts.length != 3) {
            throw new DateTimeParseException("Invalid time format", timeString, 0);
        }
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);

        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

    // Duration을 HH:mm:ss 형식의 문자열로 변환하는 메서드
    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
