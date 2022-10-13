package com.example.jpatest.shop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemberRepoTest {

    @Autowired
    MemberRepo memberRepo;

    @Rollback(value = false) // 이거 걸어주면 DB 들어감
    @Test
    @Transactional // 테스트에 있으면 롤백시킴
    public void testMember() {
        Member member = new Member();
        member.setUsername("memberA");
        Long saveId = memberRepo.save(member);

        Member find = memberRepo.find(saveId);

        assertThat(find.getId()).isEqualTo(member.getId());
        assertThat(find.getUsername()).isEqualTo(member.getUsername());
        assertThat(find).isEqualTo(member);
        System.out.println("member == find" + (member == find));
    }
}