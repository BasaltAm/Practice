package com.example.practice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    @Entity
    @Table(name = "user")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String firstName;
        private String lastName;

        @Enumerated(EnumType.STRING)
        private UserStatus status;
}
