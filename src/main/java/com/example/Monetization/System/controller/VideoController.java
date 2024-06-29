package com.example.Monetization.System.controller;

import com.example.Monetization.System.dto.request.CreateVideoRequestDto;
import com.example.Monetization.System.dto.request.PauseVideoRequestDto;
import com.example.Monetization.System.dto.response.VideoViewResponseDto;
import com.example.Monetization.System.security.MemberDetailsImpl;
import com.example.Monetization.System.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;

    @PostMapping
    public String createVideo(@RequestBody CreateVideoRequestDto createVideoRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return videoService.createVideo(createVideoRequestDto, memberDetails);
    }

    // 영상 재생
    @GetMapping("/{video_id}")
    @ResponseBody
    public VideoViewResponseDto videoView(@PathVariable(name = "video_id") UUID video_id, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return videoService.videoView(video_id, memberDetails);
    }

    // 영상 중단
    @PatchMapping
    public String videoPause(@RequestParam(name = "video_id") UUID video_id, @AuthenticationPrincipal MemberDetailsImpl memberDetails, @RequestBody PauseVideoRequestDto pauseVideoRequestDto) {
        return videoService.videoPause(video_id, memberDetails, pauseVideoRequestDto);
    }
}
