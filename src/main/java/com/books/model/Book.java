package com.books.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "book")
public class Book {

    @Id
    @Field
    private String id;

    @Field
    private final String title;

    @Field
    private final String isbn;

    @Field
    private final String author;

    public Book(String title, String isbn, String author) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

}
