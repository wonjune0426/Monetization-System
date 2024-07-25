package com.example.monetization.system.service;


import com.example.monetization.system.dto.request.video.CreateVideoRequestDto;
import com.example.monetization.system.dto.request.video.PauseVideoRequestDto;
import com.example.monetization.system.dto.request.video.UpdateVideoRequestDto;
import com.example.monetization.system.dto.response.ResponseEntityDto;
import com.example.monetization.system.dto.response.VideoViewResponseDto;
import com.example.monetization.system.entity.Member;
import com.example.monetization.system.entity.Video;
import com.example.monetization.system.entity.VideoAd;
import com.example.monetization.system.entity.VideoViewHistory;
import com.example.monetization.system.exception.VideoDeleteException;
import com.example.monetization.system.exception.VideoNotFoundException;
import com.example.monetization.system.repository.read.Read_VideoAdRepository;
import com.example.monetization.system.repository.read.Read_VideoRepository;
import com.example.monetization.system.repository.write.Write_VideoAdRepository;
import com.example.monetization.system.repository.write.Write_VideoRepository;
import com.example.monetization.system.repository.write.Write_VideoViewHistoryRepository;
import com.example.monetization.system.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final RedisTemplate<String, Long> redisTemplate;

    private final Write_VideoRepository write_videoRepository;
    private final Write_VideoViewHistoryRepository write_videoViewHistoryRepository;
    private final Write_VideoAdRepository write_videoAdRepository;

    private final Read_VideoRepository read_videoRepository;
    private final Read_VideoAdRepository read_videoAdRepository;

    // Video 생성
    public ResponseEntity<ResponseEntityDto<Void>> createVideo(CreateVideoRequestDto createVideoRequestDto, MemberDetailsImpl memberDetails) {
        // 현재 로그인 한 Member 정보를 받아옴
        Member member = memberDetails.getMember();

        //  requestDto를 통해 받아온 정보로 Video 객체 생성
        Video video = new Video(member, createVideoRequestDto.getVideoName(), createVideoRequestDto.getVideoDescription(), createVideoRequestDto.getVideoLength());

        //  Video 객체 저장
        write_videoRepository.save(video);

        return ResponseEntity.ok(new ResponseEntityDto<>("video 생성 성공"));
    }

    @Transactional
    public ResponseEntity<ResponseEntityDto<Void>> updateVideo(UUID videoId, UpdateVideoRequestDto updateVideoRequestDto, MemberDetailsImpl memberDetails) {
        // 현재 로그인 한 Member 정보
        Member member = memberDetails.getMember();

        // video 존재 여부 확인
        Video video = read_videoRepository.findById(videoId).orElseThrow(
                ()->new VideoNotFoundException("존재하지 않는 영상입니다")
        );

        //삭제 여부 확인
        if(video.getDeleteCheck()) throw new VideoDeleteException("삭제된 영상입니다.");

        // video를 게사한 member와 로그인한 member 일치 여부 확인
        if(!member.getMemberId().equals(video.getMember().getMemberId()))
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseEntityDto<>("본인의 영상만 수정할 수 있습니다."));


        video.update(updateVideoRequestDto.getVideoName(),updateVideoRequestDto.getVideoDescription());
        write_videoRepository.save(video);
        return ResponseEntity.ok(new ResponseEntityDto<>("영상 수정 성공"));
    }

    // 비디오 삭제
    @Transactional
    public ResponseEntity<ResponseEntityDto<Void>> deleteVideo(UUID videoId, MemberDetailsImpl memberDetails) throws VideoDeleteException {
        // 현재 로그인 한 Member 정보
        Member member = memberDetails.getMember();

        // video 존재 여부 확인
        Video video = read_videoRepository.findById(videoId).orElseThrow(
                ()->new VideoNotFoundException("존재하지 않는 영상입니다")
        );

        //삭제 여부 확인
        if(video.getDeleteCheck()) throw new VideoDeleteException("삭제된 영상입니다.");

        // video를 게사한 member와 로그인한 member 일치 여부 확인
        if(!member.getMemberId().equals(video.getMember().getMemberId()))
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseEntityDto<>("본인의 영상만 수정할 수 있습니다."));

        video.delete();
        List<VideoAd> videoAdList = read_videoAdRepository.findAllByVideo(video);
        for(VideoAd videoAd : videoAdList) {
            videoAd.delete();
            write_videoAdRepository.save(videoAd);
        }

        return ResponseEntity.ok(new ResponseEntityDto<>("영상 삭제 성공"));
    }

    // Video 시청
    public VideoViewResponseDto videoView(UUID videoId, MemberDetailsImpl memberDetails){
        // 반환할 Response 객체
        VideoViewResponseDto videoViewResponseDto = new VideoViewResponseDto();

        // 현재 로그인한 member 객체
        Member member = memberDetails.getMember();

        // Video의 존재 여부 확인
        Video video = read_videoRepository.findById(videoId).orElseThrow(
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
    public ResponseEntity<ResponseEntityDto<Void>> videoPause(UUID videoId, MemberDetailsImpl memberDetails, PauseVideoRequestDto pauseVideoRequestDto) {
        // 로그인한 member 확인
        Member member = memberDetails.getMember();

        // 중단 시간
        Long pauseTime = pauseVideoRequestDto.getPauseTime();

        // video 확인
        Video video = read_videoRepository.findById(videoId).orElseThrow(
                () -> new VideoNotFoundException("존재하지 않는 영상입니다.")
        );

        //삭제 여부 확인
        if(video.getDeleteCheck()) throw new VideoDeleteException("삭제된 영상입니다.");

        // 어뷰징 방지를 위한 검증
        if(!member.getMemberId().equals(video.getMember().getMemberId()) && videoViewMember(videoId,member.getMemberId())){
            Long watchTime = pauseTime - lastWatchTimeCheck(videoId, member.getMemberId());
            VideoViewHistory videoViewHistory = new VideoViewHistory(member,video,watchTime);
            write_videoViewHistoryRepository.save(videoViewHistory);
            video.totalViewUpdate();
            write_videoRepository.save(video);
        }

        // 중단 시간이 영상의 길이와 같을 경우 0으로 초기화
        if(Objects.equals(pauseTime, video.getVideoLength())) pauseTime = 0L;

        updateLastWatchTime(videoId, member.getMemberId(), pauseTime);
        return ResponseEntity.ok(new ResponseEntityDto<>("영상이 중지 되었습니다."));
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
