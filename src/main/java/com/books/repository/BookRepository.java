package com.books.repository;

import com.books.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface BookRepository extends SolrCrudRepository<Book, String> {

    @Query("title:*?0* OR author:*?0* OR description:*?0*")
    Page<Book> findByTitleOrAuthorOrDescription(String searchTerm, Pageable pageable);

}
