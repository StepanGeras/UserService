package org.example.userservice.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.example.userservice.entity.Book;
import org.example.userservice.service.BookService;
import org.example.userservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final GridFsTemplate gridFsTemplate;
    private final ImageService imageService;

    @Autowired
    public BookController(BookService bookService, GridFsTemplate gridFsTemplate, ImageService imageService) {
        this.bookService = bookService;
        this.gridFsTemplate = gridFsTemplate;
        this.imageService = imageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> findAll() {
        List<Book> findAllBook = bookService.readBooks();
        return ResponseEntity.ok(findAllBook);
    }

    @PostMapping("/create")
    public ResponseEntity<Book> create(@RequestBody Book book, @RequestParam Long id) {
        bookService.createBook(book, id);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/update")
    public ResponseEntity<Book> update(@RequestBody Book book, @RequestParam Long id) {
        bookService.updateBook(id, book);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Book> delete(@RequestParam Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.findBookById(id));
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        ObjectId fileId;
        try {
            fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            bookService.updateBookImage(id, fileId.toString());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("Image uploaded successfully with ID: " + fileId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable Long id) throws RuntimeException {

        Book book = bookService.findBookById(id);

        GridFSFile gridFsFile = imageService.fileFindGridFs(book.getImageId());

        GridFsResource resource = gridFsTemplate.getResource(gridFsFile);

        try {
            String fileName = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).replace("+", "%20");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Objects.requireNonNull(gridFsFile.getMetadata()).getString("_contentType")))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName)
                    .body(new InputStreamResource(resource.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Could not download file", e);
        }

    }

}
