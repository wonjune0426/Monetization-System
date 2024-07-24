package com.example.monetization.system.controller;


import com.example.monetization.system.dto.request.ad.AdAddRequestDto;
import com.example.monetization.system.dto.request.ad.CreateAdRequestDto;
import com.example.monetization.system.dto.response.ResponseEntityDto;
import com.example.monetization.system.entity.MemberRoleEnum;
import com.example.monetization.system.security.MemberDetailsImpl;
import com.example.monetization.system.service.AdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseEntityDto<Void>> createAd(@RequestBody @Valid CreateAdRequestDto createAdRequestDto) {
        return adService.createAd(createAdRequestDto);
    }

    // 비디오에 광고 등록
    @PostMapping("/{adId}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public ResponseEntity<ResponseEntityDto<Void>> videoAddAd(@PathVariable UUID adId, @RequestBody AdAddRequestDto adAddRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return adService.videoAddAd(adAddRequestDto,memberDetails,adId);
    }

    //광고 제거
    @DeleteMapping("/{adId}")
    @Secured(MemberRoleEnum.Authority.ADMIN)
    public ResponseEntity<ResponseEntityDto<Void>> deleteAd(@PathVariable UUID adId){
        return adService.deleteAd(adId);
    }

    // 광고 시청
    @PatchMapping
    public ResponseEntity<ResponseEntityDto<Void>> adView(@RequestParam UUID videoId,@RequestParam UUID adId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return adService.adView(videoId,adId,memberDetails);
    }
}
