package com.books.api;

import com.books.model.Book;
import com.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping(value = "/books")
    Page<Book> findAll() {
        Page<Book> books = bookRepository.findAll(PageRequest.of(0, 10));
        return books;
    }

    @PostMapping(value = "/books/init")
    public void addBooks() {
        bookRepository.save(new Book("Building Microservices: Designing Fine-Grained Systems", "1492034029", "Sam Newman", "test"));
        bookRepository.save(new Book("Domain-Driven Design: Tackling Complexity in the Heart of Software", "0321125215", "Eric Evans", "test"));
        bookRepository.save(new Book("Implementing Domain-Driven Design", "9780321834577", "Vaughn Vernon", "test"));
    }

}
