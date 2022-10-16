package com.example.jpatest.repository;

import com.example.jpatest.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // @PersistenceContext 왼쪽 어노테이션으로 주입해야 하지만, spring data jpa에서 의존성주입 해준다 (Autowried 되니까 이렇게 사용하자)
    private final EntityManager entityManager;

    // 팩토리 자체를 주입받는 방법 실무에서 거의 쓸일 없다 보면된다 ~
    /*@PersistenceUnit
    private EntityManagerFactory entityManagerFactory;*/

    public void save(Member member) {
        entityManager.persist(member);
    }

    public Member findOne(Long id) {
        // 단건 조회
        return entityManager.find(Member.class, id);
    }

    public List<Member> findAll() {
        // jpql 쿼리랑 거의 유사하나 조금 다름 -> Entity를 쿼리로 조회한다고 보면된다.
        return entityManager.createQuery("select m FROM Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        // jpql 파라미터 반영방법
        return entityManager.createQuery("select m from Member m where m.username = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
