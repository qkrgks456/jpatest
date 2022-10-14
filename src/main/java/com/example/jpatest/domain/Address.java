package com.example.jpatest.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor // 값 타입 같은경우 Getter만 이용하고 모든값초기화 생성자만 해놔서 불변의 객체로 만들자
@NoArgsConstructor(access = AccessLevel.PROTECTED) // but jpa에서는 리플렉션이나 프록시 같은 기술을 이용하는데 기본생성자가 필요하므로 기본생성자도 생성
public class Address {
    private String city;
    private String street;
    private String zipCodes;
}
