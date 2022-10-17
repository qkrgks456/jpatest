package com.example.jpatest.service;

import com.example.jpatest.controller.BookForm;
import com.example.jpatest.domain.item.Book;
import com.example.jpatest.domain.item.Item;
import com.example.jpatest.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(BookForm param) {
        // merge 하지말고 다 업데이트 되니까
        // 이런식으로 dirty checking(변경감지)해서 필요한 컬럼만 업데이트하자
        // save 필요없다 트랜잭션 커밋되면 flush()하고 변경감지한거 다 찾아서 업데이트 해준다
        Book book = (Book) itemRepository.findOne(param.getId());
        book.setPrice(param.getPrice());
        book.setName(param.getName());
        book.setStockQuantity(param.getStockQuantity());
        book.setAuthor(param.getAuthor());
        book.setIsbn(param.getIsbn());
        // 부가적으로 저렇게 setter 이용해서 하지마라
        // 엔티티내에서 다 추적 가능하도록 아래처럼 비즈니스 메서드를 도메인 모델 패턴으로 구현해라
        // book.change(price,name,author)
    }

    public List<Item> findItem() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}
