package com.example.jpatest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private long id;

    private String username;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // 연관관계의 주인은 외래키가 있는 Order 테이블 해당 필드에 매핑되었다고 명시해준다.
    private List<Order> orders = new ArrayList<>();
}
