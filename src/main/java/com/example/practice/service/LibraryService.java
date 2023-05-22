package com.example.practice.service;

import com.example.practice.model.Author;
import com.example.practice.model.Book;
import com.example.practice.model.Lend;
import com.example.practice.model.LendStatus;
import com.example.practice.model.User;
import com.example.practice.model.UserStatus;
import com.example.practice.model.request.AuthorCreationRequest;
import com.example.practice.model.request.BookCreationRequest;
import com.example.practice.model.request.BookLendRequest;
import com.example.practice.model.request.UserCreationRequest;
import com.example.practice.repository.AuthorRepository;
import com.example.practice.repository.BookRepository;
import com.example.practice.repository.LendRepository;
import com.example.practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final LendRepository lendRepository;
    private final BookRepository bookRepository;

    public Book readBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Cant find any book under given ID");
    }

    public List<Book> readBooks() {
        return bookRepository.findAll();
    }

    public Book readBook(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Cant find any book under given ISBN");
    }

    public Book createBook(BookCreationRequest book) {
        Optional<Author> author = authorRepository.findById(book.getAuthorId());
        if (!author.isPresent()) {
            throw new EntityNotFoundException("Author Not Found");
        }
        Book bookToCreate = new Book();
        BeanUtils.copyProperties(book, bookToCreate);
        bookToCreate.setAuthor(author.get());
        return bookRepository.save(bookToCreate);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public User createUser(UserCreationRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    public User updateUser (Long id, UserCreationRequest request) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException("User not present in the database");
        }
       User user = optionalUser.get();
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        return userRepository.save(user);
    }

    public Author createAuthor (AuthorCreationRequest request) {
        Author author = new Author();
        BeanUtils.copyProperties(request, author);
        return authorRepository.save(author);
    }

    public List<String> lendABook (BookLendRequest request) {

        Optional<User> userForId = userRepository.findById(request.getUserId());
        if (!userForId.isPresent()) {
            throw new EntityNotFoundException("user not present in the database");
        }

        User user = userForId.get();
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("User is not active to proceed a lending.");
        }

        List<String> booksApprovedToBurrow = new ArrayList<>();

        request.getBookIds().forEach(bookId -> {

            Optional<Book> bookForId = bookRepository.findById(bookId);
            if (!bookForId.isPresent()) {
                throw new EntityNotFoundException("Cant find any book under given ID");
            }

            Optional<Lend> burrowedBook = lendRepository.findByBookAndStatus(bookForId.get(), LendStatus.BURROWED);
            if (!burrowedBook.isPresent()) {
                booksApprovedToBurrow.add(bookForId.get().getName());
                Lend lend = new Lend();
                lend.setUser(userForId.get());
                lend.setBook(bookForId.get());
                lend.setStatus(LendStatus.BURROWED);
                lend.setStartOn(Instant.now());
                lend.setDueOn(Instant.now().plus(30, ChronoUnit.DAYS));
                lendRepository.save(lend);
            }

        });
        return booksApprovedToBurrow;
    }


    public List<Author> readAuthors() {
        return authorRepository.findAll();
    }

    public Book updateBook(Long bookId, BookCreationRequest request) {
        Optional<Author> author = authorRepository.findById(request.getAuthorId());
        if (!author.isPresent()) {
            throw new EntityNotFoundException("Author Not Found");
        }
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            throw new EntityNotFoundException("Book Not Found");
        }
        Book book = optionalBook.get();
        book.setIsbn(request.getIsbn());
        book.setName(request.getName());
        book.setAuthor(author.get());
        return bookRepository.save(book);
    }

    public List<User> readUsers() {
        return userRepository.findAll();
    }
}