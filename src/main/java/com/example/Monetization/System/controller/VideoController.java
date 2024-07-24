package com.example.monetization.system.controller;


import com.example.monetization.system.dto.request.video.CreateVideoRequestDto;
import com.example.monetization.system.dto.request.video.PauseVideoRequestDto;
import com.example.monetization.system.dto.request.video.UpdateVideoRequestDto;
import com.example.monetization.system.dto.response.VideoViewResponseDto;
import com.example.monetization.system.entity.MemberRoleEnum;
import com.example.monetization.system.security.MemberDetailsImpl;
import com.example.monetization.system.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;

    // 영상 등록
    @Secured(MemberRoleEnum.Authority.SELLER)
    @PostMapping
    public String createVideo(@RequestBody @Valid CreateVideoRequestDto createVideoRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return videoService.createVideo(createVideoRequestDto, memberDetails);
    }

    // 영상 수정
    @PatchMapping("/{videoId}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public String updateVideo(@PathVariable UUID videoId, @RequestBody @Valid UpdateVideoRequestDto updateVideoRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return videoService.updateVideo(videoId,updateVideoRequestDto, memberDetails);
    }

    // 영상 삭제
    @DeleteMapping("/{videoId}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public String deleteVideo(@PathVariable UUID videoId,@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return videoService.deleteVide(videoId,memberDetails);
    }

    // 영상 재생
    @GetMapping("/{videoId}")
    @ResponseBody
    public VideoViewResponseDto videoView(@PathVariable UUID videoId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return videoService.videoView(videoId, memberDetails);
    }

    // 영상 중단
    @PostMapping("/{videoId}")
    public String videoPause(@PathVariable  UUID videoId, @AuthenticationPrincipal MemberDetailsImpl memberDetails, @RequestBody @Valid PauseVideoRequestDto pauseVideoRequestDto) {
        return videoService.videoPause(videoId, memberDetails, pauseVideoRequestDto);
    }
}
