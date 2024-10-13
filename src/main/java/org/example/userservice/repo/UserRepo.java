package org.example.userservice.repo;

import org.example.userservice.entity.Book;
import org.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query("SELECT b FROM Book b WHERE b.user.id = :authorId")
    List<Book> findAllBooksByUserId(@Param("authorId") Long authorId);
    Optional<User> findByUsername(String username);

}
