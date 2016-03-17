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
            .doTry()
              .bean(BookServiceImpl.class, "index")
            .doCatch(RuntimeException.class)
            .setBody(simple("{ ${exception.message} }"))
            .end();

          restConfiguration("jetty")
            .host("0.0.0.0")
            .port(9090)
            .bindingMode(RestBindingMode.off)
            .enableCORS(true)
            .dataFormatProperty("prettyPrint", "true");

          rest("service")
            .get("book/all")
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
          .put("book")
          .consumes(MediaType.APPLICATION_JSON)
          .bindingMode(RestBindingMode.json)
          .type(Book.class)
          .to("direct:updateBook");

          rest("service")
          .get("book/{id}")
          .produces(MediaType.APPLICATION_JSON)
          .bindingMode(RestBindingMode.json)
          .outType(Book.class)
          .to("direct:findBookById");

          rest("service")
          .delete("book/{id}")
          .produces(MediaType.APPLICATION_JSON)
          .bindingMode(RestBindingMode.json)
          .outType(Book.class)
          .to("direct:deleteBookById");


          from("direct:findAllBooks")
          .doTry()
            .bean(BookServiceImpl.class, "findAllBooks")
          .doCatch(RuntimeException.class)
          .setBody(simple("{ ${exception.message} }"))
          .endRest();

          from("direct:findBookById")
          .doTry()
            .bean(BookServiceImpl.class, "findBookById(${header.id})")
          .doCatch(RuntimeException.class)
          .setBody(simple("{ ${exception.message} }"))
          .endRest();

          from("direct:deleteBookById")
          .doTry()
            .bean(BookServiceImpl.class, "removeBookById(${header.id})")
          .doCatch(RuntimeException.class)
          .setBody(simple("{ ${exception.message} }"))
          .endRest();

          from("direct:createBook")
          .doTry()
           .bean(BookServiceImpl.class, "createBook(${body})")
          .doCatch(IllegalArgumentException.class)
          .setBody(simple("{ ${exception.message} }"))
          .endRest();

          from("direct:updateBook")
          .doTry()
            .bean(BookServiceImpl.class, "updateBook(${body})")
          .doCatch(IllegalArgumentException.class)
          .setBody(simple("{ ${exception.message} }"))
          .endRest();
        }
      });

       // if( main.isStarted()){
       //   Runtime.getRuntime().exec("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe  http://localhost:9090");
       // }
        main.run(args);
      } catch (Exception e) {
        LOGGER.error("Error ! "+e.getMessage());
      }
  }

  /*
   * Other Configurations.
   */
  public class MediaType{
    public static final String APPLICATION_XML   = "application/xml";
    public static final String APPLICATION_JSON  = "application/json";
  }

}
