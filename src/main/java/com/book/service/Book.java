package com.book.service;

import java.io.Serializable;

public class Book implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private int id;
  private String name;

  public Book() {  }

  public Book(final int id, final String name) {
    super();
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Book [id=" + id + ", name=" + name + "]";
  }

}
