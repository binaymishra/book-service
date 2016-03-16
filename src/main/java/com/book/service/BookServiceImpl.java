package com.book.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import com.book.service.db.BookRepository;

@Service("bookService")
public class BookServiceImpl implements BookService {
  private final AbstractApplicationContext context = new AnnotationConfigApplicationContext(DataBaseConfiguration.class);

  private static final Logger LOGGER = Logger.getLogger(BookServiceImpl.class);

  @Override
  public List<Book> findAllBooks() {
    BookRepository repository = context.getBean(BookRepository.class);
    List<Book> books = repository.fetchAllBooks();
    LOGGER.info(String.format("%d 'Book' record found. ", books.size()));
    LOGGER.debug(books);
    return books;
  }

@Override
public Book findBookById(final int id) {
  BookRepository repository = context.getBean(BookRepository.class);
  Book book = repository.fetchBookById(id);
  LOGGER.info(String.format("%d 'Book' record found for id = %d.", book != null? 1 : null, id));
  LOGGER.debug(book);
  return book;
}
  @Override
  public Book createBook(final Book book) {
    BookRepository repository = context.getBean(BookRepository.class);
      int id = repository.insertBook(book);
      book.setId(id);
      LOGGER.info(String.format("1 record is successfully inserted."));
      LOGGER.debug(book);
      return book;
  }
}
