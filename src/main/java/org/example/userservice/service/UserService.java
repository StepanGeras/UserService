package org.example.userservice.service;

import org.example.userservice.dto.UserDto;
import org.example.userservice.entity.Book;
import org.example.userservice.entity.User;
import org.example.userservice.exception.user.UserNotFoundException;
import org.example.userservice.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        logger.info("Find all authors");
        List<User> users = userRepo.findAll();
        logger.info("Found {} authors", users.size());
        return users;
    }

    public void save(UserDto userDto) {
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setRoles(Set.of(User.Role.valueOf(userDto.getRole())));
        logger.info("Save author {}", user);
        userRepo.save(user);
        logger.info("Saved author {}", user);
    }

    public User findByUsername(String username) {
        logger.info("Finding author by username = {}", username);

        return userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Author with username = {} not found", username);
                    return new UserNotFoundException("Author not found with username = " + username);
                });
    }

    public void update(User newUser) {
        logger.info("Updating author with id={}", newUser.getId());
        User existingUser = findById(newUser.getId());

        existingUser.setUsername(newUser.getUsername());

        userRepo.save(existingUser);
        logger.info("Successfully updated author with id={}", newUser.getId());
    }

    public User findById(Long id) {
        logger.info("Finding author by id={}", id);

        return userRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Author with id={} not found", id);
                    return new UserNotFoundException("Author not found with id=" + id);
                });
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Deleting author by id={}", id);

        User user = findById(id);

        userRepo.delete(user);
        logger.info("Successfully deleted author with id={}", id);
    }

    public List<Book> findAllBooksByUserId(Long authorId) {
        logger.info("Find all books by author id {}", authorId);
        List<Book> bookList = userRepo.findAllBooksByUserId(authorId);
        logger.info("Found {} books", bookList.size());
        return bookList;
    }

    public void updateAuthorImage(Long id, String string) {
        User user = findById(id);
        user.setImageId(string);
        userRepo.save(user);
    }

    public boolean exist(User user) {

        User findUser = findByUsername(user.getUsername());

        return findUser != null &&
                passwordEncoder.matches(user.getPassword(), findUser.getPassword());

    }
}
