package com.example.jpatest.service;

import com.example.jpatest.domain.Member;
import com.example.jpatest.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트에 걸면 테스트 다 끝나고 rollback 시킨다
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager entityManager;

    // @Rollback(value = false)
    @Test
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setUsername("kim");

        Long saveId = memberService.join(member);
        // entityManager.flush(); // 이렇게 하면 인서트됐다가 롤백
        assertEquals(member, memberRepository.findOne(saveId));

        // 트랜잭션이 커밋이 될때, 인서트문이 실행된다
        // 그런데 Transactional를 테스트에서 쓰면 default 는 롤백임
    }

    @Test
    public void 아이디_중복() {
        Member member1 = new Member();
        member1.setUsername("kim");
        Member member2 = new Member();
        member2.setUsername("kim");

        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

    }

}