package com.example.demo1313.controller;

import com.example.demo1313.domain.LoginForm;
import com.example.demo1313.domain.Member;
import com.example.demo1313.repository.MemberRepository;
import com.example.demo1313.service.LoginService;
import com.example.demo1313.service.Service;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private Service service;


    @GetMapping("/add")
    public String save1(@ModelAttribute("member") Member member) {
        return "addMember";

    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("member") Member member, BindingResult result, Model model) {

        Optional<Member> existingMember = memberRepository.findByLoginId(member.getLoginId());
        if (existingMember.isPresent()) {
            model.addAttribute("message", "이미 사용 중인 로그인 ID입니다.");
            model.addAttribute("searchUrl", "/members/add"); // 회원가입 페이지로 리다이렉트
            return "message";
        }

        // 비밀번호 강도 검증 추가 가능 (옵션)
        if (!isPasswordStrong(member.getPassword())) {
            model.addAttribute("message", "비밀번호가 너무 약합니다.");
            model.addAttribute("searchUrl", "/members/add");
            return "message";
        }

        if (result.hasFieldErrors("loginId")) {
            model.addAttribute("message", "최소 4글자이상 작성해주세요.");
            return "message";
        }
        if (result.hasFieldErrors("email")) {
            model.addAttribute("message", "이메일 형식을 준수해주세요.");
            return "message";
        }
        if (result.hasFieldErrors("name")) {
            model.addAttribute("message", "이름을 입력해주세요.");
            return "message";
        }


        memberRepository.save(member);

        System.out.println("저장된값 :" + (memberRepository.findAll()).toString());
        model.addAttribute("message", "회원가입 완료");
        model.addAttribute("searchUrl", "/members/login");

        return "message";
    }

    private boolean isPasswordStrong(String password) {
        // 비밀번호 강도 검증 로직
        return password.length() >= 8 && // 최소 길이
                password.matches(".*[A-Z].*") && // 대문자 포함
                password.matches(".*[a-z].*") && // 소문자 포함
                password.matches(".*\\d.*") && // 숫자 포함
                password.matches(".*[!@#$%^&*].*"); // 특수문자 포함
    }


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String loginForm2(@ModelAttribute("loginForm") LoginForm form, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, HttpSession session) {
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());


        if (loginMember == null) {
            model.addAttribute("message", "아이디,비밀번호가 일치하지않습니다.");
            model.addAttribute("searchUrl", "/members/login");
            return "message";
        }
        session.setAttribute("loginMember", loginMember);

        model.addAttribute("list", service.list(pageable));
        return "home";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginMember");
        return "redirect:/";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            return "redirect:/members/login";
        }
        model.addAttribute("list", service.list(pageable));
        return "home";
    }

    @GetMapping("/findId")
    public String findId() {
        return "find/findId";
    }

    @PostMapping("/findId")
    public String findId2(@RequestParam("name") String name, @RequestParam("email") String email, Model model) {
        Optional<Member> member = memberRepository.findByNameAndEmail(name, email);
        if (member.isPresent()) {
            String loginId = member.get().getLoginId();
            //이메일 전송 로직을 짠다.
            model.addAttribute("message", "이메일로 전송되었습니다.(" + loginId + ")");
            model.addAttribute("searchUrl", "/members/login");
        } else {
            model.addAttribute("meassage", "해당 정보와 일치하는 회원이 없습니다.");
            model.addAttribute("searchUrl", "/members/findId");
        }
        return "message";
    }

    @GetMapping("/findPassword")
    public String findPassword1() {
        return "find/findPassword";
    }

    @PostMapping("/findPassword")
    public String findPassword(@RequestParam("loginId") String loginId, @RequestParam("email") String email, Model model, HttpSession session) {
        Optional<Member> member = memberRepository.findByLoginIdAndEmail(loginId, email);
        if (member.isPresent()) {

            session.setAttribute("resetLoginId", loginId);
            session.setAttribute("resetEmail", email);


            model.addAttribute("message", "비밀번호를 재설정 하십시오. ");
            model.addAttribute("searchUrl", "/members/rePassword");
        } else {
            model.addAttribute("message", "일치하는 회원이 없습니다.");
            model.addAttribute("searchUrl", "/members/findPassword");
        }

        return "message";
    }

    @GetMapping("/rePassword")
    public String rePassword() {
        return "find/rePassword";
    }

    @PostMapping("/rePassword")
    public String rePassword2(@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, HttpSession session, Model model) {

        String loginId = (String) session.getAttribute("resetLoginId");
        String email = (String) session.getAttribute("resetEmail");

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("searchUrl", "/members/rePassword");
            return "message";
        }

        // 비밀번호 강도 검증 추가
        if (!isPasswordStrong(newPassword)) {
            model.addAttribute("message", "비밀번호가 너무 약합니다. (대문자, 소문자, 숫자, 특수문자 포함, 8자 이상이어야 합니다.)");
            model.addAttribute("searchUrl", "/members/rePassword");
            return "message";
        }

        // 아이디와 이메일로 회원 조회
        Optional<Member> member = memberRepository.findByLoginIdAndEmail(loginId, email);
        if (member.isPresent()) {
            Member resetMember = member.get();

            // 새 비밀번호 설정 (암호화 로직은 생략)
            resetMember.setPassword(newPassword);
            memberRepository.save(resetMember);

            // 세션에서 데이터 제거
            session.removeAttribute("resetLoginId");
            session.removeAttribute("resetEmail");

            model.addAttribute("message", "비밀번호가 성공적으로 재설정되었습니다.");
            model.addAttribute("searchUrl", "/members/login");
            return "message";
        } else {
            model.addAttribute("message", "해당 정보와 일치하는 회원이 없습니다.");
            model.addAttribute("searchUrl", "/members/findPassword");
            return "message";
        }
    }
}




