package com.example.practice.controller;
import com.example.practice.model.Author;
import com.example.practice.model.Book;
import com.example.practice.model.User;
import com.example.practice.model.request.AuthorCreationRequest;
import com.example.practice.model.request.BookCreationRequest;
import com.example.practice.model.request.BookLendRequest;
import com.example.practice.model.request.UserCreationRequest;
import com.example.practice.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/api/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping("/book")
    public ResponseEntity readBooks(@RequestParam(required = false) String isbn) {
        if (isbn == null) {
            return ResponseEntity.ok(libraryService.readBooks());
        }
        return ResponseEntity.ok(libraryService.readBook(isbn));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> readBook (@PathVariable Long bookId) {
        return ResponseEntity.ok(libraryService.readBook(bookId));
    }

    @PostMapping("/book")
    public ResponseEntity<Book> createBook (@RequestBody BookCreationRequest request) {
        return ResponseEntity.ok(libraryService.createBook(request));
    }


    @PatchMapping("/book/{bookId}")
    public ResponseEntity<Book> updateBook (@PathVariable("bookId") Long bookId, @RequestBody BookCreationRequest request) {
        return ResponseEntity.ok(libraryService.updateBook(bookId, request));
    }

    @PostMapping("/author")
    public ResponseEntity<Author> createAuthor (@RequestBody AuthorCreationRequest request) {
        return ResponseEntity.ok(libraryService.createAuthor(request));
    }

    @GetMapping("/author")
    public ResponseEntity<List<Author>> readAuthors () {
        return ResponseEntity.ok(libraryService.readAuthors());
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteBook (@PathVariable Long bookId) {
        libraryService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user")
    public ResponseEntity<User> createMember (@RequestBody UserCreationRequest request) {
        return ResponseEntity.ok(libraryService.createUser(request));
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> readUsers () {
        return ResponseEntity.ok(libraryService.readUsers());
    }

    @PatchMapping("/member/{memberId}")
    public ResponseEntity<User> updateUser (@RequestBody UserCreationRequest request, @PathVariable Long userId) {
        return ResponseEntity.ok(libraryService.updateUser(userId, request));
    }

    @PostMapping("/book/lend")
    public ResponseEntity<List<String>> lendABook(@RequestBody BookLendRequest bookLendRequests){
        return ResponseEntity.ok(libraryService.lendABook(bookLendRequests));
    }
}