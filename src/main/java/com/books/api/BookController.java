package com.books.api;

import com.books.model.Book;
import com.books.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping(value = "/books/{titleOrAuthorOrDescription}")
    Page<Book> findByCustomQuery(
            @PathVariable String titleOrAuthorOrDescription,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Book requested for filter={}, page={}, size={}", titleOrAuthorOrDescription, page, size);
        return bookRepository.findByTitleOrAuthorOrDescription(titleOrAuthorOrDescription, PageRequest.of(page, size));
    }

}
