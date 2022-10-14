package com.example.jpatest.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("M") // 구분값 기본 세팅
public class Movie extends Item {
    private String director;
    private String actor;
}
