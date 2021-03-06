package com.book.service;

import java.util.List;

public interface BookService {

  public List<Book> findAllBooks();

  public Book findBookById(final int id);

  public void createBook(final Book book);

  public void updateBook(final Book book);

  public void removeBookById(final int id);

}
