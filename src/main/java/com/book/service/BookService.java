package com.book.service;

import java.util.List;

/**
 * @author Binay Mishra
 *
 */
public interface BookService {

  /**
   * @return
   */
  public List<Book> findAllBooks();

  /**
   * @param id
   * @return
   */
  public Book findBookById(final int id);

  /**
   * @param book
   */
  public void createBook(final Book book);

  /**
   * @param book
   */
  public void updateBook(final Book book);

  /**
   * @param id
   */
  public void removeBookById(final int id);

}
