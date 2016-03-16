package com.book.service;

import java.util.List;

public interface BookService {

  public List<Book> findAllBooks();

  public Book findBookById(final int id);

  public Book createBook(final Book book);

}
