package com.books.api;

import com.books.model.Book;
import com.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping(value = "/books/{titleOrAuthorOrDescription}")
    Page<Book> findByCustomQuery(
            @PathVariable String titleOrAuthorOrDescription,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return bookRepository.findByTitleOrAuthorOrDescription(titleOrAuthorOrDescription, PageRequest.of(page, size));
    }

}
