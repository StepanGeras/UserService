package org.example.userservice.entity;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public enum Role {
        ROLE_ADMIN, ROLE_USER;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<Book> book;

    private String imageId;

}
