package org.example.userservice.service;

import org.example.userservice.entity.Book;
import org.example.userservice.entity.User;
import org.example.userservice.exception.book.BookNotFoundException;
import org.example.userservice.repo.BookRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepo bookRepo;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(BookRepo bookRepo, UserService userService) {
        this.bookRepo = bookRepo;
        this.userService = userService;
    }

    public List<Book> readBooks() {
        logger.info("Reading all books");
        List<Book> books = bookRepo.findAll();
        logger.info("Found {} books", bookRepo.count());
        return books;
    }

    public void createBook(Book book, Long id) {
        logger.info("Creating a book");
        User user = userService.findById(id);
        book.setUser(user);
        bookRepo.save(book);
        logger.info("Book created");
    }

    public void updateBook(long id, Book updatedBook) {
        logger.info("Updating a book");
        Book book = findBookById(id);
        book.setUser(updatedBook.getUser());
        book.setTitle(updatedBook.getTitle());
        book.setDescription(updatedBook.getDescription());
        bookRepo.save(book);
        logger.info("Book updated");

    }

    public void deleteBook(long id) {
        logger.info("Deleting a book");
        bookRepo.deleteById(id);
        logger.info("Book deleted");
    }

    public Book findBookById(long id) {
        logger.info("Finding a book by id: {}", id);
        Optional<Book> book = bookRepo.findById(id);
        if (book.isPresent()) {
            logger.info("Book found");
            return book.get();
        }
        throw new BookNotFoundException("Book not found");

    }

    public void updateBookImage(Long id, String imageId) {
        Book book = findBookById(id);
        book.setImageId(imageId);
        bookRepo.save(book);
    }

}
