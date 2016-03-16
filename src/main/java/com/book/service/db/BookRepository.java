package com.book.service.db;

import java.util.List;

import com.book.service.Book;

public interface BookRepository {

  public List<Book> fetchAllBooks();

}
