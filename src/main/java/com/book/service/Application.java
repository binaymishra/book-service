package com.book.service;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.log4j.Logger;

public class Application {

  private static final Logger LOGGER = Logger.getLogger(Application.class);

    public static void main(final String[] args) {
      try {
        LOGGER.info("Booting up RESTful Book Webservice.....!");
        LOGGER.info("Server is running on 'localhost:9090'");
        Main main = new Main();
        main.enableHangupSupport();
        main.addRouteBuilder(new RouteBuilder() {

        @Override
        public void configure() throws Exception {

          from("jetty:http://0.0.0.0:9090")
            .streamCaching()
            .setBody(simple(index()))
            .end();

          restConfiguration("jetty")
            .host("0.0.0.0")
            .port(9090)
            .bindingMode(RestBindingMode.off)
            .enableCORS(true)
            .dataFormatProperty("prettyPrint", "true");

          rest("service")
            .get("book")
            .produces(MediaType.APPLICATION_JSON)
            .bindingMode(RestBindingMode.json)
            .outType(Book.class)
            .to("direct:findAllBooks");

          rest("service")
          .post("book")
          .consumes(MediaType.APPLICATION_JSON)
          .bindingMode(RestBindingMode.json)
          .type(Book.class)
          .to("direct:createBook");

          rest("service")
          .get("book/{id}")
          .produces(MediaType.APPLICATION_JSON)
          .bindingMode(RestBindingMode.json)
          .outType(Book.class)
          .to("direct:findBookById");


          from("direct:findAllBooks")
          .bean(BookServiceImpl.class, "findAllBooks")
          .endRest();

          from("direct:findBookById")
          .doTry()
            .bean(BookServiceImpl.class, "findBookById(${header.id})")
          .doCatch(RuntimeException.class)
          .setBody(simple("{ ${exception.message} }"))
          .endRest();

          from("direct:createBook")
           .bean(BookServiceImpl.class, "createBook(${body})")
          .endRest();
        }
      });

       // if( main.isStarted()){
       //   Runtime.getRuntime().exec("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe  http://localhost:9090");
       // }
        main.run(args);
      } catch (Exception e) {
        LOGGER.error("Server is running on 'localhost:9090'");
        LOGGER.error("Error ! "+e.getMessage());
      }
  }

  public static String index(){
    return new StringBuilder()
            .append("<a href='http://localhost:9090/service/book'>http://localhost:9090/service/book</a>")
            .append("<br/>")
            .append("<a href='http://localhost:9090/service/book/1'>http://localhost:9090/service/book/1</a>")
            .append("<br/>")
            .append("<a href='http://localhost:9090/service/book/2'>http://localhost:9090/service/book/2</a>")
            .append("<br/>")
            .append("<a href='http://localhost:9090/service/book/3'>http://localhost:9090/service/book/3</a>")
            .append("<br/>")
            .append("<a href='http://localhost:9090/service/book/4'>http://localhost:9090/service/book/4</a>")
            .append("<br/>")
            .append("<a href='http://localhost:9090/service/book/5'>http://localhost:9090/service/book/5</a>")
            .append("<br/>")
            .append("<a href='http://localhost:9090/service/book/6'>http://localhost:9090/service/book/6</a>")
            .append("<br/>")
            .append("<a href='http://localhost:9090/service/book/0'>http://localhost:9090/service/book/0</a>")
            .append("<br/>")
            .append("<a href='http://localhost:9090/service/book/-100'>http://localhost:9090/service/book/-100</a>")
            .append("<br/>")
            .toString();
  }

  public class MediaType{
    public static final String APPLICATION_XML   = "application/xml";
    public static final String APPLICATION_JSON  = "application/json";
  }
}
