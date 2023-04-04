package com.books.repository;

import com.books.model.Book;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface BookRepository extends SolrCrudRepository<Book, String> {
}
