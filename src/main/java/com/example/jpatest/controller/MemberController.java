package com.example.jpatest.controller;

import com.example.jpatest.domain.Address;
import com.example.jpatest.domain.Member;
import com.example.jpatest.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "/members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Validated MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/members/createMemberForm";
        }
        Address address = memberForm.getAddress();
        Member member = new Member(memberForm.getName());
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.memberList());
        return "/members/memberList";
    }


}
