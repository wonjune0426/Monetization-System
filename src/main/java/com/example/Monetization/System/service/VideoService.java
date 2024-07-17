package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.video.CreateVideoRequestDto;
import com.example.Monetization.System.dto.request.video.PauseVideoRequestDto;
import com.example.Monetization.System.dto.request.video.UpdateVideoRequestDto;
import com.example.Monetization.System.dto.response.VideoViewResponseDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.VideoAd;
import com.example.Monetization.System.entity.VideoViewHistory;
import com.example.Monetization.System.exception.VideoDeleteException;
import com.example.Monetization.System.exception.VideoNotFoundException;
import com.example.Monetization.System.repository.VideoAdRepository;
import com.example.Monetization.System.repository.VideoRepository;
import com.example.Monetization.System.repository.VideoViewHistoryRepository;
import com.example.Monetization.System.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoViewHistoryRepository videoViewHistoryRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private final VideoAdRepository videoAdRepository;

    // Video 생성
    public String createVideo(CreateVideoRequestDto createVideoRequestDto, MemberDetailsImpl memberDetails) {
        // 현재 로그인 한 Member 정보를 받아옴
        Member member = memberDetails.getMember();

        //  requestDto를 통해 받아온 정보로 Video 객체 생성
        Video video = new Video(member, createVideoRequestDto.getVideoName(), createVideoRequestDto.getVideoDescription(), createVideoRequestDto.getVideoLength());

        //  Video 객체 저장
        videoRepository.save(video);

        return "Video 생성 성공";
    }

    @Transactional
    public String updateVideo(UUID videoId, UpdateVideoRequestDto updateVideoRequestDto, MemberDetailsImpl memberDetails) {
        // 현재 로그인 한 Member 정보
        Member member = memberDetails.getMember();

        // video 존재 여부 확인
        Video video = videoRepository.findById(videoId).orElseThrow(
                ()->new VideoNotFoundException("존재하지 않는 영상입니다")
        );

        //삭제 여부 확인
        if(video.getDeleteCheck()) return "삭제된 영상입니다.";

        // video를 게사한 member와 로그인한 member 일치 여부 확인
        if(!member.getMemberId().equals(video.getMember().getMemberId())) return "본인의 영상만 수정할 수 있습니다.";

        video.update(updateVideoRequestDto.getVideoName(),updateVideoRequestDto.getVideoDescription());
        return "비디오 수정 성공";
    }

    // 비디오 삭제
    @Transactional
    public String deleteVide(UUID videoId, MemberDetailsImpl memberDetails) throws VideoDeleteException {
        // 현재 로그인 한 Member 정보
        Member member = memberDetails.getMember();

        // video 존재 여부 확인
        Video video = videoRepository.findById(videoId).orElseThrow(
                ()->new VideoNotFoundException("존재하지 않는 영상입니다")
        );

        //삭제 여부 확인
        if(video.getDeleteCheck()) throw new VideoDeleteException("삭제된 영상입니다.");

        // video를 게사한 member와 로그인한 member 일치 여부 확인
        if(!member.getMemberId().equals(video.getMember().getMemberId())) return "본인의 영상만 삭제할 수 있습니다";

        video.delete();
        List<VideoAd> videoAdList = videoAdRepository.findAllByVideo(video);
        for(VideoAd videoAd : videoAdList) {
            videoAd.delete();
        }

        return "비디오 삭제 성공";
    }

    // Video 시청
    public VideoViewResponseDto videoView(UUID videoId, MemberDetailsImpl memberDetails){
        // 반환할 Response 객체
        VideoViewResponseDto videoViewResponseDto = new VideoViewResponseDto();

        // 현재 로그인한 member 객체
        Member member = memberDetails.getMember();

        // Video의 존재 여부 확인
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new VideoNotFoundException("존재하지 않는 영상입니다.")
        );

        //삭제 여부 확인
        if(video.getDeleteCheck()) throw new VideoDeleteException("삭제된 영상입니다.");

        // 최근 시청기록 조회를 위한 메서드
        Long lastWatchTime = lastWatchTimeCheck(video.getVideoId(), member.getMemberId());

        videoViewResponseDto.setVideoName(video.getVideoName());
        videoViewResponseDto.setVideoLength(video.getVideoLength());
        videoViewResponseDto.setVideoDescription(video.getVideoDescription());
        videoViewResponseDto.setLastWatchTime(lastWatchTime);

        return videoViewResponseDto;
    }

    // Vdieo 중단
    @Transactional
    public String videoPause(UUID videoId, MemberDetailsImpl memberDetails, PauseVideoRequestDto pauseVideoRequestDto) {
        // 로그인한 member 확인
        Member member = memberDetails.getMember();

        // 중단 시간
        Long pauseTime = pauseVideoRequestDto.getPauseTime();

        // video 확인
        Video video = videoRepository.findById(videoId).orElseThrow(
                () -> new VideoNotFoundException("존재하지 않는 영상입니다.")
        );

        //삭제 여부 확인
        if(video.getDeleteCheck()) throw new VideoDeleteException("삭제된 영상입니다.");

        // 어뷰징 방지를 위한 검증
        if(!member.getMemberId().equals(video.getMember().getMemberId()) && videoViewMember(videoId,member.getMemberId())){
            Long watchTime = pauseTime - lastWatchTimeCheck(videoId, member.getMemberId());
            VideoViewHistory videoViewHistory = new VideoViewHistory(member,video,watchTime);
            videoViewHistoryRepository.save(videoViewHistory);
            video.totalViewUpdate();
        }

        // 중단 시간이 영상의 길이와 같을 경우 0으로 초기화
        if(Objects.equals(pauseTime, video.getVideoLength())) pauseTime = 0L;

        updateLastWatchTime(videoId, member.getMemberId(), pauseTime);
        return "Vide 시청 기록 완료";
    }

    // video를 시청한 기록을 확인하는 메서드
    private boolean videoViewMember(UUID videoId, Long memberId) {
        String key = "viedoViewMember : " + videoId + "_" + memberId;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return true;
        } else {
            redisTemplate.opsForValue().set(key, 30L, 30, TimeUnit.SECONDS);
            return false;
        }
    }

    // 마지막 시청 시간을 확인하는 메서드
    private Long lastWatchTimeCheck(UUID videoId, Long memberId) {
        String key = "lastWatchTime : " + videoId + "_" + memberId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return redisTemplate.opsForValue().get(key);
        } else {
            redisTemplate.opsForValue().set(key,0L, 1, TimeUnit.DAYS);
            return 0L;
        }
    }

    // 마지막 시청시간 기록
    private void updateLastWatchTime(UUID videoId, Long memberId, Long lastWatchTime) {
        String key = "lastWatchTime : " + videoId + "_" + memberId;
        redisTemplate.opsForValue().set(key, lastWatchTime, 1, TimeUnit.DAYS);
    }
}
