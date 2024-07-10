package com.example.Monetization.System.controller;

import com.example.Monetization.System.dto.request.video.CreateVideoRequestDto;
import com.example.Monetization.System.dto.request.video.PauseVideoRequestDto;
import com.example.Monetization.System.dto.request.video.UpdateVideoRequestDto;
import com.example.Monetization.System.dto.response.VideoViewResponseDto;
import com.example.Monetization.System.exception.VideoDeleteException;
import com.example.Monetization.System.security.MemberDetailsImpl;
import com.example.Monetization.System.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;

    // 영상 등록
    @PostMapping
    public String createVideo(@RequestBody @Valid CreateVideoRequestDto createVideoRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return videoService.createVideo(createVideoRequestDto, memberDetails);
    }

    // 영상 수정
    @PatchMapping("/{videoId}")
    public String updateVideo(@PathVariable UUID videoId, @RequestBody @Valid UpdateVideoRequestDto updateVideoRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return videoService.updateVideo(videoId,updateVideoRequestDto, memberDetails);
    }

    // 영상 삭제
    @DeleteMapping("/{videoId}")
    public String deleteVideo(@PathVariable UUID videoId,@AuthenticationPrincipal MemberDetailsImpl memberDetails) throws VideoDeleteException {
        return videoService.deleteVide(videoId,memberDetails);
    }

    // 영상 재생
    @GetMapping("/{videoId}")
    @ResponseBody
    public VideoViewResponseDto videoView(@PathVariable UUID videoId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws VideoDeleteException {
        return videoService.videoView(videoId, memberDetails);
    }

    // 영상 중단
    @PostMapping("/{videoId}")
    public String videoPause(@PathVariable  UUID videoId, @AuthenticationPrincipal MemberDetailsImpl memberDetails, @RequestBody PauseVideoRequestDto pauseVideoRequestDto) throws VideoDeleteException {
        return videoService.videoPause(videoId, memberDetails, pauseVideoRequestDto);
    }
}
