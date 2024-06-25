package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.CreateRequestVideoDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.repository.VideoRepository;
import com.example.Monetization.System.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    public String createVideo(CreateRequestVideoDto createRequestVideoDto, MemberDetailsImpl memberDetails) {
        // 현재 로그인 된 Member 정보를 받아옴
        Member member = memberDetails.getMember();

        // Member의 Authority가 false면 video 등록 불가
        if (!member.isAuthority()) return "판매자 권한이 없어 video등록이 불가 합니다.";

        // Video의 UUID 생성
        UUID uuid = UUID.randomUUID();

        // requestDto를 통해 받아온 정보로 Video 객체 생성
        Video video = new Video(uuid, member, createRequestVideoDto.getVideo_name(),
                createRequestVideoDto.getVideo_length(), createRequestVideoDto.getVideo_description(), 0L, false);

        // Video 객체 저장
        videoRepository.save(video);

        return "Video 생성 성공";
    }
}
