package com.book.service.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.book.service.Book;

@Repository("bookRepository")
public class BookRepositoryImpl implements BookRepository {

  private static final String BOOK_BY_ID_SQL  = "SELECT ID, NAME FROM BOOK WHERE ID = ?";
  private static final String BOOK_UPDATE_SQL = "UPDATE BOOK SET NAME = ? WHERE ID = ?";
  private static final String BOOK_DELETE_SQL = "DELETE FROM BOOK WHERE ID = ?";
  private static final String ALL_BOOK_SQL    = "SELECT ID, NAME FROM BOOK";


  private JdbcTemplate template;
  private SimpleJdbcInsert insertTemplate;

  @Autowired
  public BookRepositoryImpl(final DataSource dataSource ) {
    template = new JdbcTemplate(dataSource);
    insertTemplate = new SimpleJdbcInsert(dataSource)
                        .withTableName("BOOK")
                        .usingGeneratedKeyColumns("ID");
  }

  @Override
  public List<Book> fetchAllBooks() {
    return template.query(ALL_BOOK_SQL, new RowMapper<Book>() {
      @Override
      public Book mapRow(final ResultSet rs, final int index) throws SQLException {
        final Book book = new Book();
        book.setId(rs.getInt("ID"));
        book.setName(rs.getString("NAME"));
        return book;
      }
    });
  }

  @Override
  public Book fetchBookById(final int id) {
    return template.queryForObject(BOOK_BY_ID_SQL, new RowMapper<Book>() {
      @Override
      public Book mapRow(final ResultSet rs, final int index) throws SQLException {
        final Book book = new Book();
        book.setId(rs.getInt("ID"));
        book.setName(rs.getString("NAME"));
        return book;
      }
    }, id);
  }

  @Override
  @Transactional
  public int insertBook(final Book book) {
    Map<String, Object> parameters = new HashMap<String, Object>(1);
    parameters.put("NAME", book.getName());
    final int id = insertTemplate.executeAndReturnKey(parameters).intValue();
    return id;
  }

  @Override
  @Transactional
  public int update(final Book book) {
    return template.update(BOOK_UPDATE_SQL,
        book.getName(),
        book.getId());
  }

  @Override
  @Transactional
  public int delete(final int id) {
    return template.update(BOOK_DELETE_SQL, id);
  }

}
