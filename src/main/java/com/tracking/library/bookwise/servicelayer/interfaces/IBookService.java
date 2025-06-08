package com.tracking.library.bookwise.servicelayer.interfaces;

import com.tracking.library.bookwise.entities.Book;
import java.util.*;

public interface IBookService {
    Book addBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, Book updatedBook);
    void deleteBook(Long id);
}
