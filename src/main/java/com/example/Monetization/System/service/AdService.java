package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.ad.AdAddRequestDto;
import com.example.Monetization.System.dto.request.ad.CreateAdRequestDto;
import com.example.Monetization.System.entity.Ad;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.VideoAd;
import com.example.Monetization.System.exception.AdNotFoundException;
import com.example.Monetization.System.exception.VideoNotFoundException;
import com.example.Monetization.System.repository.AdRepository;
import com.example.Monetization.System.repository.VideoAdRepository;
import com.example.Monetization.System.repository.VideoRepository;
import com.example.Monetization.System.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdService {
    private final AdRepository adRepository;
    private final VideoRepository videoRepository;
    private final VideoAdRepository videoAdRepository;


    public String createAd(CreateAdRequestDto createAdRequestDto, MemberDetailsImpl memberDetails) {
        Ad ad = new Ad(createAdRequestDto.getAdName(), createAdRequestDto.getAdDescription());
        adRepository.save(ad);

        return "광고 저장 성공";
    }


    public String videoAddAd(AdAddRequestDto adAddRequestDto, MemberDetailsImpl memberDetails, UUID adId) {
        Member member = memberDetails.getMember();

        Ad ad = adRepository.findById(adId).orElseThrow(
                () -> new AdNotFoundException("존재 하지 않는 광고 입니다.")
        );

        for (UUID videoId : adAddRequestDto.getVideoIds()) {
            Video video = videoRepository.findById(videoId).orElseThrow(
                    () -> new VideoNotFoundException("존재 하지 않는 영상입니다.")
            );

            if (video.getMember().getMemberId().equals(member.getMemberId())){
                VideoAd videoAd = new VideoAd(video,ad);
                videoAdRepository.save(videoAd);
            } else{
                return "영상의 소유주가 아닙니다.";
            }

        }

        return "광고를 등록하였습니다.";
    }

    @Transactional
    public String adView(UUID videoId, UUID adId, MemberDetailsImpl memberDetails) {
        Video video = videoRepository.findById(videoId).orElseThrow(
                ()-> new VideoNotFoundException("존재하지 않는 영상입니다.")
        );

        Ad ad = adRepository.findById(adId).orElseThrow(
                ()-> new AdNotFoundException("존재하지 않는 광고 입니다.")
        );

        Member member = memberDetails.getMember();

        if(!member.getMemberId().equals(video.getMember().getMemberId())){
            VideoAd videoAd = videoAdRepository.findVideoAdByVideoAndAd(video,ad).orElseThrow(
                    ()->new IllegalArgumentException("해당 영상에 광고가 등록되지 않았습니다")
            );
            if(videoAd.getDeleteCheck()) return "해당 영상이 제거 되었거나 광고가 제거 되었습니다";
            videoAd.viewUpdate();
        }

        return "광고 조회 수 증가";
    }

    @Transactional
    public String deleteAd(UUID adId, MemberDetailsImpl memberDetails) {
        Ad ad = adRepository.findById(adId).orElseThrow(
                ()->new AdNotFoundException("존재하지 않는 광고 입니다.")
        );

        ad.delete();

        List<VideoAd> videoAdList = videoAdRepository.findAllByAd(ad);
        for(VideoAd videoAd : videoAdList){
            videoAd.delete();
        }

        return "광고가 제거 되었습니다.";
    }
}
