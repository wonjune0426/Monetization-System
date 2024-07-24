package com.example.monetization.system.service;

import com.example.monetization.system.dto.request.ad.AdAddRequestDto;
import com.example.monetization.system.dto.request.ad.CreateAdRequestDto;
import com.example.monetization.system.entity.*;
import com.example.monetization.system.exception.AdNotFoundException;
import com.example.monetization.system.exception.VideoNotFoundException;
import com.example.monetization.system.repository.read.Read_AdRepository;
import com.example.monetization.system.repository.read.Read_VideoAdRepository;
import com.example.monetization.system.repository.read.Read_VideoRepository;
import com.example.monetization.system.repository.write.Write_AdRepository;
import com.example.monetization.system.repository.write.Write_AdViewHistoryRepository;
import com.example.monetization.system.repository.write.Write_VideoAdRepository;
import com.example.monetization.system.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdService {
    private final Write_AdRepository write_adRepository;
    private final Write_VideoAdRepository write_videoAdRepository;
    private final Write_AdViewHistoryRepository write_adViewHistoryRepository;

    private final Read_AdRepository read_adRepository;
    private final Read_VideoRepository read_videoRepository;
    private final Read_VideoAdRepository read_videoAdRepository;


    public String createAd(CreateAdRequestDto createAdRequestDto) {
        Ad ad = new Ad(createAdRequestDto.getAdName(), createAdRequestDto.getAdDescription());
        write_adRepository.save(ad);

        return "광고 저장 성공";
    }


    public String videoAddAd(AdAddRequestDto adAddRequestDto, MemberDetailsImpl memberDetails, UUID adId) {
        Member member = memberDetails.getMember();

        Ad ad = read_adRepository.findById(adId).orElseThrow(
                () -> new AdNotFoundException("존재 하지 않는 광고 입니다.")
        );

        for (UUID videoId : adAddRequestDto.getVideoIds()) {
            Video video = read_videoRepository.findById(videoId).orElseThrow(
                    () -> new VideoNotFoundException("존재 하지 않는 영상입니다.")
            );

            if (video.getMember().getMemberId().equals(member.getMemberId())){
                VideoAd videoAd = new VideoAd(video,ad);
                write_videoAdRepository.save(videoAd);
            } else{
                return "영상의 소유주가 아닙니다.";
            }

        }

        return "광고를 등록하였습니다.";
    }

    @Transactional
    public String adView(UUID videoId, UUID adId, MemberDetailsImpl memberDetails) {
        Video video = read_videoRepository.findById(videoId).orElseThrow(
                ()-> new VideoNotFoundException("존재하지 않는 영상입니다.")
        );

        Ad ad = read_adRepository.findById(adId).orElseThrow(
                ()-> new AdNotFoundException("존재하지 않는 광고 입니다.")
        );

        Member member = memberDetails.getMember();

        if(!member.getMemberId().equals(video.getMember().getMemberId())){
            VideoAd videoAd = read_videoAdRepository.findVideoAdByVideoAndAd(video,ad).orElseThrow(
                    ()->new IllegalArgumentException("해당 영상에 광고가 등록되지 않았습니다")
            );
            if(videoAd.getDeleteCheck()) return "해당 영상이 제거 되었거나 광고가 제거 되었습니다";
            AdViewHistory adViewHistory = new AdViewHistory(videoAd);
            write_adViewHistoryRepository.save(adViewHistory);
            videoAd.viewUpdate();
        }

        return "광고 조회 수 증가";
    }

    @Transactional
    public String deleteAd(UUID adId) {
        Ad ad = read_adRepository.findById(adId).orElseThrow(
                ()->new AdNotFoundException("존재하지 않는 광고 입니다.")
        );

        ad.delete();

        List<VideoAd> videoAdList = read_videoAdRepository.findAllByAd(ad);
        for(VideoAd videoAd : videoAdList){
            videoAd.delete();
        }

        return "광고가 제거 되었습니다.";
    }
}
