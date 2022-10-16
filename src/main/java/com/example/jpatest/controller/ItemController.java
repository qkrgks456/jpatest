package com.example.jpatest.controller;

import com.example.jpatest.domain.item.Book;
import com.example.jpatest.domain.item.Item;
import com.example.jpatest.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "/items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = Book.builder()
                .author(bookForm.getAuthor())
                .isbn(bookForm.getIsbn())
                .name(bookForm.getName())
                .price(bookForm.getPrice())
                .stockQuantity(bookForm.getStockQuantity())
                .build();
        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItem();
        model.addAttribute("items", items);
        return "/items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        model.addAttribute("form", form);
        return "/items/updateItemForm";
    }


    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm bookForm, @PathVariable Long itemId) {
        // 어줍잖게 컨트롤러에서 엔티티 만들지마라 dto따로 만들어서 넘겨라
        /*Book book = Book.builder()
                .id(bookForm.getId())
                .name(bookForm.getName())
                .author(bookForm.getAuthor())
                .isbn(bookForm.getIsbn())
                .stockQuantity(bookForm.getStockQuantity())
                .price(bookForm.getPrice())
                .build();*/

        // merge를 그냥 쓰지마 귀찮다고 itemService.saveItem(book);
        itemService.updateItem(bookForm);
        return "redirect:/items";
    }

}
