package com.example.jpatest.service;

import com.example.jpatest.domain.Member;
import com.example.jpatest.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional // readOnly = false (default)
    public Long join(Member member) {
        validMember(member); // 중복체크
        memberRepository.save(member);
        // 영속성 컨텍스트에 키 보장되어있음
        return member.getId();
    }

    public void validMember(Member member) {
        // 문제 있으면 exception
        List<Member> members = memberRepository.findByName(member.getName());
        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    // 조회하는곳에서는 readOnly 걸리는게 성능에 도움이 된다
    @Transactional(readOnly = true)
    public List<Member> memberList() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
