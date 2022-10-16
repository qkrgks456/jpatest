package com.example.jpatest.controller;

import com.example.jpatest.domain.Address;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "값이 존재해야만 합니다")
    private String name;

    private String city;
    private String street;
    private String zipCode;

    public Address getAddress() {
        return new Address(city, street, zipCode);
    }
}
