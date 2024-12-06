package org.example.userservice.repo;

import org.example.userservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    Book findBookById(Long id);
    List<Book> findAllByUserId(Long id);
}


