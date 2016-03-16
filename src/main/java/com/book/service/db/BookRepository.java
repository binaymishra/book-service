package com.book.service.db;

import java.util.List;

import com.book.service.Book;

public interface BookRepository {

  public List<Book> fetchAllBooks();
  public Book fetchBookById(int id);
  public int insertBook(Book book);

}
