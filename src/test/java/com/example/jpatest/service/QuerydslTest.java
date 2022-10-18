package com.example.jpatest.service;

import com.example.jpatest.domain.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.example.jpatest.domain.QMember.member;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslTest {
    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        Member member3 = new Member("member3");
        Member member4 = new Member("member4");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        //member1을 찾아라.
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertEquals(findMember.getUsername(), "member1");
        assertNotEquals(findMember.getUsername(), "member4");
    }

    @Test
    public void startQuerydsl() {
        //member1을 찾아라.
        Member findMember = queryFactory
                .selectFrom(member)
                .where(nameEq("member1")
                        .and(idEq(null))
                        .and(member.id.isNull()))//파라미터 바인딩 처리
                .fetchOne();
        assertEquals(findMember.getUsername(), "member1");
        assertNotEquals(findMember.getUsername(), "member4");

    }

    private BooleanExpression nameEq(String nameCond) {
        return nameCond != null ? member.username.eq(nameCond) : null;
    }

    private BooleanExpression idEq(Long idCond) {
        return idCond != null ? member.id.eq(idCond) : null;
    }

}
