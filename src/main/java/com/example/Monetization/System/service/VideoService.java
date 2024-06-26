package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.CreateVideoRequestDto;
import com.example.Monetization.System.dto.response.VideoViewResponseDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.VideoView_history;
import com.example.Monetization.System.repository.VideoRepository;
import com.example.Monetization.System.repository.VideoView_historyRepository;
import com.example.Monetization.System.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoView_historyRepository videoViewHistoryRepository;
    private final RedisTemplate<String, String> redisTemplate;


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
        videoViewResponseDto.setLast_time(lastWatchTime);

        return videoViewResponseDto;
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

    // 마지막 시청 시간을 기록하는 메서드
    private String lastWatchTimeCheck(UUID videoId, String memberId) {
        String key = "lastWatchTime : " + videoId + "_" + memberId;
        if(redisTemplate.hasKey(key)) {
            return (String) redisTemplate.opsForValue().get(key);
        } else{
            return "00:00";
        }
    }


}