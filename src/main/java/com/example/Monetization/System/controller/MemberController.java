package com.example.Monetization.System.controller;

import com.example.Monetization.System.dto.request.SignupRequestDto;
import com.example.Monetization.System.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody @Valid SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        // Validation
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(!fieldErrors.isEmpty()){
            for(final FieldError fieldError : fieldErrors){
                log.error(fieldError.getField()+": "+fieldError.getDefaultMessage());
                return fieldError.getDefaultMessage();
            }
        }
        memberService.signup(signupRequestDto);
        return "회원가입 성공";
    }



}
