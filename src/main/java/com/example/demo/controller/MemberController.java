package com.example.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MemberController {
    @Autowired
    MemberService memberService;

    @GetMapping("/join_new") // 회원 가입 페이지 연결
    public String join_new() {
        return "join_new"; // .HTML 연결
    }

    @PostMapping("/api/members") // 회원 가입 저장
    public String addmembers(@Valid @ModelAttribute AddMemberRequest request) {
        memberService.saveMember(request);
        return "join_end"; // .HTML 연결
    }

    @GetMapping("/member_login") // 로그인 페이지 연결
    public String member_login() {
        return "login"; // .HTML 연결
    }

    @PostMapping("/api/login_check") // 로그인(아이디, 패스워드) 체크
    public String checkMembers(@ModelAttribute AddMemberRequest request, Model model, HttpServletRequest request2, HttpServletResponse response) {
        try {
            HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
            if (session != null) {
                session.invalidate(); // 기존 세션 무효화
                // Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID 초기화
                // cookie.setPath("/"); // 쿠키 경로
                // cookie.setMaxAge(0); // 쿠키 삭제(0으로 설정)
                // response.addCookie(cookie); // 응답으로 쿠키 전달
            }
            // 11주차 연습문제: 사용자마다 고유한 세션 이름 생성
            String customSessionId = "SESSION_" + UUID.randomUUID().toString();
            Cookie customSessionCookie = new Cookie("CUSTOM_SESSIONID", customSessionId);
            customSessionCookie.setPath("/");
            customSessionCookie.setMaxAge(-1); // 브라우저 종료 시까지 유지
            response.addCookie(customSessionCookie);

            session = request2.getSession(true); // 새로운 세션 생성
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword()); // 패스워드 반환

            // String sessionId = UUID.randomUUID().toString(); // 임의의 고유 ID로 세션 생성
            // String email = request.getEmail(); // 이메일 얻기
            // session.setAttribute("userId", sessionId); // 아이디 이름 설정
            // session.setAttribute("email", email); // 이메일 설정

            session.setAttribute("userId", customSessionId);
            session.setAttribute("email", request.getEmail()); // 사용자 이메일로 세션 관리

            model.addAttribute("member", member); // 로그인 성공 시 회원 정보 전달
            return "redirect:/board_list"; // 로그인 성공 후 이동할 페이지
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }

    @GetMapping("/api/logout") // 로그아웃 버튼 동작
    public String member_logout(Model model, HttpServletRequest request2, HttpServletResponse response) {
        try {
            HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
                // session.invalidate(); // 기존 세션 무효화
                // Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID is the default session cookie name
                // cookie.setPath("/"); // Set the path for the cookie
                // cookie.setMaxAge(0); // Set cookie expiration to 0 (removes the cookie)
                // response.addCookie(cookie); // Add cookie to the response
                // session = request2.getSession(true); // 새로운 세션 생성
                // System.out.println("세션 userId: " + session.getAttribute("userId" )); // 초기화 후 IDE 터미널에 세션 값 출력
            // 11주차 연습문제
            if (session != null) {
                String email = (String) session.getAttribute("email"); // 현재 사용자 이메일 확인

                if (email != null) {
                    session.invalidate(); // 현재 사용자의 세션 무효화

                    // 사용자와 관련된 커스텀 쿠키 삭제
                    Cookie customSessionCookie = new Cookie("CUSTOM_SESSIONID", null);
                    customSessionCookie.setPath("/");
                    customSessionCookie.setMaxAge(0);
                    response.addCookie(customSessionCookie);
                }
            }
                return "login"; // 로그인 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }
}
