//package com.example.Monetization.System.controller;
//
//import com.example.Monetization.System.dto.request.AdAddRequestDto;
//import com.example.Monetization.System.dto.request.CreateAdRequestDto;
//import com.example.Monetization.System.security.MemberDetailsImpl;
//import com.example.Monetization.System.service.AdService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/ads")
//public class AdController {
//    private final AdService adService;
//
//    @PostMapping
//    public String createAd(@RequestBody CreateAdRequestDto createAdRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
//        return adService.createAd(createAdRequestDto,memberDetails);
//    }
//
//    @PostMapping("/{ad_id}")
//    public String videoAddAd(@PathVariable UUID ad_id, @RequestBody AdAddRequestDto adAddRequestDto,  @AuthenticationPrincipal MemberDetailsImpl memberDetails){
//        return adService.videoAddAd(adAddRequestDto,memberDetails,ad_id);
//    }
//
//    @PatchMapping
//    public String adView(@RequestParam UUID videoId,@RequestParam UUID adId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
//        return adService.adView(videoId,adId,memberDetails);
//    }
//}
