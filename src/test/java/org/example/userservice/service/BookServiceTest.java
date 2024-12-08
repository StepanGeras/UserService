package org.example.userservice.service;

import org.aspectj.lang.annotation.Before;
import org.example.userservice.dto.UserDto;
import org.example.userservice.entity.Book;
import org.example.userservice.entity.User;
import org.example.userservice.exception.book.BookNotFoundException;
import org.example.userservice.repo.BookRepo;
import org.example.userservice.repo.UserRepo;
import org.example.userservice.service.BookService;
import org.example.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private User user;

    @BeforeEach
    public void setUp() {
        book = new Book(1L, "Test Title", "Test Description", "Test Image");
        user = new User(1L, "Test User", "Test password");
    }

    @Test
    void testReadBooks() {
        when(bookRepo.findAll()).thenReturn(List.of(book));
        when(bookRepo.count()).thenReturn(1L);

        List<Book> books = bookService.readBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        verify(bookRepo).findAll();
    }

    @Test
    void testCreateBook() {
        when(userService.findById(1L)).thenReturn(user);

        bookService.createBook(book, 1L);

        verify(bookRepo).save(book);
        assertEquals(user, book.getUser());
    }

    @Test
    void testUpdateBook() {
        Book updatedBook = new Book(1L, "Updated Title", "Updated Description", "Updated Image");
        updatedBook.setUser(user);
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        bookService.updateBook(1L, updatedBook);

        verify(bookRepo).save(book);
        assertEquals("Updated Title", book.getTitle());
        assertEquals("Updated Description", book.getDescription());
        assertEquals(user, book.getUser());
    }

    @Test
    void testFindBookByIdFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.findBookById(1L);

        assertNotNull(foundBook);
        assertEquals(book, foundBook);
    }

    @Test
    void testDeleteBook() {
        bookService.deleteBook(1L);

        verify(bookRepo).deleteById(1L);
    }

}
