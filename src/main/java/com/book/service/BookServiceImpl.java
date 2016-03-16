package com.book.service;

import java.util.Arrays;
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
    int size = repository.fetchAllBooks().size();
    LOGGER.debug(String.format("%d 'Book' record found. ", size));
    return repository.fetchAllBooks();
  }

@Override
public Book findBookById(final int id) {
  if(id > 0 && id < 6)
    return  books().get(id - 1);
  throw new RuntimeException(String.format("Record not found for id = %d.", id));
}


@Override
public void createBook(final Book book) {
  LOGGER.debug(book);
  LOGGER.info("Input data is received. need a DB to insert.");
}

private static List<Book> books(){
  return Arrays.asList(
      new Book(1, "Hadoop The Definitive Guide"),
      new Book(2, "Functional Programming In Java"),
      new Book(3, "Java SE 8 for the Really Impatient"),
      new Book(4, "Working Effectively with Legacy Code"),
      new Book(5, "Java 8 Lambdas Pragmatic Functional Programming"));
}}
