package com.book.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ApplicationResource implements ResourceLoaderAware {

  private ResourceLoader resourceLoader;

  @Override
  public void setResourceLoader(final ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public String indexPage(){
    StringBuilder contents = new StringBuilder();
    Resource index = resourceLoader.getResource("classpath:index.html");
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(index.getInputStream()));
      String line;
      while((line = reader.readLine()) != null){
        contents.append(line);
      }
    } catch (IOException ioException) {
      contents.append("[index.html] is not found.");
      throw new RuntimeException(contents.toString(), ioException);
    }
    return contents.toString();
  }

}
