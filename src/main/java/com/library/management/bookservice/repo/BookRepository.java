package com.library.management.bookservice.repo;

import com.library.management.bookservice.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
