package com.tracking.library.bookwise.servicelayer;

import com.tracking.library.bookwise.entities.Book;
import com.tracking.library.bookwise.repositories.BookRepository;
import com.tracking.library.bookwise.servicelayer.interfaces.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService implements IBookService {

    @Autowired
    private BookRepository bookRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Override
    public Book addBook(Book book) {
        book.setAvailable(true);
        Book savedBook = bookRepository.save(book);
        logger.info("Added new book with ID: {}", savedBook.getId());
        return savedBook;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        logger.info("Fetched all books, total count: {}", books.size());
        return books;
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    logger.info("Fetched book with ID: {}", id);
                    return book;
                })
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new RuntimeException("Book not found with ID: " + id);
                });
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            logger.error("Attempted to delete non-existing book with ID: {}", id);
            throw new RuntimeException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
        logger.info("Deleted book with ID: {}", id);
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Attempted to update non-existing book with ID: {}", id);
                    return new RuntimeException("Book not found with id: " + id);
                });

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setAvailable(updatedBook.isAvailable());

        Book savedBook = bookRepository.save(existingBook);
        logger.info("Updated book with ID: {}", id);
        return savedBook;
    }
}
