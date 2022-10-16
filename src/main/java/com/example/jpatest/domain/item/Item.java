package com.example.jpatest.domain.item;

import com.example.jpatest.domain.Category;
import com.example.jpatest.exception.NotEnoughStockException;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속 관계에서의 전략 싱글테이블 전략 상속 받은애들 모두 테이블 하나에
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // 실무에서는 다대다 하지말자 DB에서는 다대다 인지불가 하여 중간테이블을 놓고 다대일,일대다구조로 되는데 중간테이블에 컬럼을 추가할 수 없고
    // 세밀한 쿼리 실행이 어렵기 때문에 놉
    // 해결방안으로 중간테이블을 하나 만들고 @ManyToOne @OneToMany 로 매핑해서 사용하자
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직 최대한 엔티티안에 직접 들어가는게 좋다
    // Setter를 이용해서 바깥으로 꺼내서 로직을 새우기 보다는 이런식으로 내부에서 진행되도록 응축하는게 좋다 (객체지향적)
    // stock 재고 수량 증가
    public void addStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    public void removeStock(int stockQuantity) {
        int result = this.stockQuantity - stockQuantity;
        if (result < 0) throw new NotEnoughStockException("need more stock");
        this.stockQuantity = result;
    }

}
