package org.example.userservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String title;
    private String description;
    private String imageId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @JsonBackReference
    @ToString.Exclude
    private User user;

    public Book(long id, String title, String description, String imageId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }
}
