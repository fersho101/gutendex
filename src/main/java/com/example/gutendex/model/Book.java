package com.example.gutendex.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer downloadCount;

    @ElementCollection
    private List<String> languages;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Author> authors;
}
