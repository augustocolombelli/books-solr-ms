package com.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@SpringBootApplication
@EnableSolrRepositories
public class BooksSolrMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksSolrMsApplication.class, args);
	}

}
