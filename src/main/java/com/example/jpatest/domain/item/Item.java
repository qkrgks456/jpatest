package com.example.jpatest.domain.item;

import com.example.jpatest.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속 관계에서의 전략 싱글테이블 전략 상속 받은애들 모두 테이블 하나에
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // 실무에서는 다대다 하지말자 DB에서는 다대다 인지불가 하여 중간테이블을 놓고 다대일,일대다구조로 되는데 중간테이블에 컬럼을 추가할 수 없고
    // 세밀한 쿼리 싫랭이 어렵기 때문에 놉
    // 해결방안으로 중간테이블을 하나 만들고 @ManyToOne @OneToMany 로 매핑해서 사용하자
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
