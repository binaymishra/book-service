package com.book.service;

import java.util.List;

public interface BookService {

  public List<Book> findAllBooks();

  public Book findBookById(final int id);

  public void createBook(final Book book);

}
