package com.example.jpatest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // 연관관계의 주인은 외래키가 있는 Order 테이블 해당 필드에 매핑되었다고 명시해준다.
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    public Member(String name) {
        this.name = name;
    }
}
