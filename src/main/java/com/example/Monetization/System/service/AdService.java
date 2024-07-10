//package com.example.Monetization.System.service;
//
//import com.example.Monetization.System.dto.request.AdAddRequestDto;
//import com.example.Monetization.System.dto.request.CreateAdRequestDto;
//import com.example.Monetization.System.security.MemberDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class AdService {
//    private final AdRepository adRepository;
//    private final VideoRepository videoRepository;
//    private final Video_AdRepository video_AdRepository;
//
//    public String createAd(CreateAdRequestDto createAdRequestDto, MemberDetailsImpl memberDetails) {
//        Member member = memberDetails.getMember();
//
//        if (!member.isAuthority()) return "판매자 권한이 없어 광고 등록이 불가 합니다.";
//
//        Ad ad = new Ad(UUID.randomUUID(), createAdRequestDto.getAd_name(), createAdRequestDto.getAd_description(), createAdRequestDto.getAd_price(), false);
//        adRepository.save(ad);
//
//        return "광고 저장 성공";
//    }
//
//
//    public String videoAddAd(AdAddRequestDto adAddRequestDto, MemberDetailsImpl memberDetails, UUID adId) {
//        Member member = memberDetails.getMember();
//        String memberId = member.getMember_id();
//        if (!member.isAuthority()) return "판매자 권한이 없어 ad 등록이 불가 합니다.";
//
//        Ad ad = adRepository.findById(adId).orElseThrow(
//                () -> new IllegalArgumentException("존재 하지 않는 광고 입니다.")
//        );
//
//        for (UUID videoId : adAddRequestDto.getVideoIds()) {
//            Video video = videoRepository.findById(videoId).orElseThrow(
//                    () -> new IllegalArgumentException("존재 하지 않는 영상입니다.")
//            );
//
//            if (video.getMember().getMember_id().equals(memberId)){
//                Video_Ad videoAd = new Video_Ad();
//                videoAd.setAd(ad);
//                videoAd.setVideo(video);
//                videoAd.setView_count(0L);
//                video_AdRepository.save(videoAd);
//            } else{
//                return "영상의 소유주가 아닙니다.";
//            }
//
//        }
//
//        return "비디오 광고 관계 매핑 성공";
//    }
//
//    @Transactional
//    public String adView(UUID videoId, UUID adId, MemberDetailsImpl memberDetails) {
//        Video video = videoRepository.findById(videoId).orElseThrow(
//                ()-> new IllegalArgumentException("존재하지 않는 영상입니다.")
//        );
//
//        Ad ad = adRepository.findById(adId).orElseThrow(
//                ()-> new IllegalArgumentException("존재하지 않는 광고 입니다.")
//        );
//
//        Member member = memberDetails.getMember();
//
//        if(!member.getMember_id().equals(video.getMember().getMember_id())){
//            Video_Ad videoAd = video_AdRepository.findByVideoAndAd(video,ad).orElseThrow(
//                    ()->new IllegalArgumentException("영상에 광고가 등록되지 않았습니다.")
//            );
//
//            videoAd.setView_count(videoAd.getView_count() + 1);
//        }
//
//        return "광고 조회 수 증가";
//    }
//}
