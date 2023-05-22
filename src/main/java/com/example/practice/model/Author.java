package com.example.practice.model;

import jakarta.persistence.*;
import com.example.practice.model.Book;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

    @Getter
    @Setter
    @Entity
    @Table(name = "author")
    public class Author {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String firstName;

        private String lastName;

        @JsonBackReference
        @OneToMany(mappedBy = "author",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private List<Book> books;
    }
