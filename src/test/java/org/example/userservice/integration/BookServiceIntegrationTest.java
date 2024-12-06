package org.example.userservice.integration;

import org.example.userservice.entity.Book;
import org.example.userservice.entity.User;
import org.example.userservice.repo.BookRepo;
import org.example.userservice.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class BookServiceIntegrationTest {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private BookService bookService;

    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        bookRepo.deleteAll();
    }

    @Test
    void testCreateBook() {
        Book book = new Book(1L, "Test title", "Test description", "Test image");
        User user = new User(1L, "Test username", "Tset password");
        book.setUser(user);

        bookService.createBook(book, 1L);

        List<Book> books = bookService.findAllBook();
        assertEquals(1, books.size());
        assertEquals("Integration Test", books.get(0).getTitle());
    }

    @Test
    void testFindAllBooks() {
        Book book1 = new Book(1L, "Book 1", "Description 1", "Image 1");
        Book book2 = new Book(2L, "Book 2", "Description 2", "Image 2");
        bookRepo.saveAll(List.of(book1, book2));

        List<Book> books = bookService.findAllBook();

        assertEquals(2, books.size());
    }

    @Test
    void testFindBookById() {
        Book book = new Book(1L, "Book 1", "Description 1", "Image 1");
        Book savedBook = bookRepo.save(book);

        Book foundBook = bookService.findBookById(savedBook.getId());

        assertNotNull(foundBook);
        assertEquals(savedBook.getId(), foundBook.getId());
    }

    @Test
    void testUpdateBookImage() {
        Book book = new Book(1L, "Book 1", "Description 1", "Image 1");
        Book savedBook = bookRepo.save(book);

        bookService.updateBookImage(savedBook.getId(), "New Image");

        Book updatedBook = bookService.findBookById(savedBook.getId());
        assertEquals("New Image", updatedBook.getImageId());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book(1L, "Book 1", "Description 1", "Image 1");
        Book savedBook = bookRepo.save(book);

        bookService.deleteBook(savedBook.getId());

        Optional<Book> deletedBook = bookRepo.findById(savedBook.getId());
        assertTrue(deletedBook.isEmpty());
    }

}
