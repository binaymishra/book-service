package com.book.service.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.book.service.Book;

@Repository("bookRepository")
public class BookRepositoryImpl implements BookRepository {

  private JdbcTemplate template;

  @Autowired
  public BookRepositoryImpl(final DataSource dataSource ) {
    template = new JdbcTemplate(dataSource);
  }

  @Override
  public List<Book> fetchAllBooks() {
    return template.query("SELECT ID, NAME FROM BOOK", new RowMapper<Book>() {
      @Override
      public Book mapRow(final ResultSet rs, final int index) throws SQLException {
        final Book book = new Book();
        book.setId(rs.getInt("ID"));
        book.setName(rs.getString("NAME"));
        return book;
      }
    });
  }

}
