package com.example.monetization.system.controller;


import com.example.monetization.system.dto.request.ad.AdAddRequestDto;
import com.example.monetization.system.dto.request.ad.CreateAdRequestDto;
import com.example.monetization.system.entity.MemberRoleEnum;
import com.example.monetization.system.security.MemberDetailsImpl;
import com.example.monetization.system.service.AdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {
    private final AdService adService;

    // 광고 등록
    @PostMapping
    @Secured(MemberRoleEnum.Authority.ADMIN)
    public String createAd(@RequestBody @Valid CreateAdRequestDto createAdRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return adService.createAd(createAdRequestDto,memberDetails);
    }

    // 비디오에 광고 등록
    @PostMapping("/{adId}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public String videoAddAd(@PathVariable UUID adId, @RequestBody AdAddRequestDto adAddRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return adService.videoAddAd(adAddRequestDto,memberDetails,adId);
    }

    //광고 제거
    @DeleteMapping("/{adId}")
    @Secured(MemberRoleEnum.Authority.ADMIN)
    public String deleteAd(@PathVariable UUID adId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return adService.deleteAd(adId,memberDetails);
    }

    // 광고 시청
    @PatchMapping
    public String adView(@RequestParam UUID videoId,@RequestParam UUID adId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return adService.adView(videoId,adId,memberDetails);
    }
}
