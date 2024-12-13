package com.example.demo.model.service;

import com.example.demo.model.domain.Member;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data 
public class AddMemberRequest {
    @NotBlank // 공백x
    @Pattern(regexp = "^[a-zA-Z가-힣]+$") // 특수문자x
    private String name;

    @NotBlank // 공백x
    @Email // 이메일 형식
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$") // 패턴(8글자이상, 대소문자 포함)
    private String password;

    @Pattern(regexp = "^\\d+$") // 패턴(숫자만 가능)
    @Min(value = 19) // 19이상
    @Max(value = 90) // 90이하
    private String age;

    @NotEmpty // 공백은 허용, Null과 빈 문자열 허용x
    private String mobile;

    @NotEmpty // 공백은 허용, Null과 빈 문자열 허용x
    private String address;

    public Member toEntity(){ // Member 생성자를 통해 객체 생성
        return Member.builder()
            .name(name)
            .email(email)
            .password(password)
            .age(age)
            .mobile(mobile)
            .address(address)
            .build();
    }
}
