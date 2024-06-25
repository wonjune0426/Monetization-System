package com.example.Monetization.System.controller;

import com.example.Monetization.System.dto.request.CreateRequestVideoDto;
import com.example.Monetization.System.security.MemberDetailsImpl;
import com.example.Monetization.System.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;

    @PostMapping
    public String createVideo(@RequestBody CreateRequestVideoDto createRequestVideoDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return videoService.createVideo(createRequestVideoDto,memberDetails);
    }
}
