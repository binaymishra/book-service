package com.book.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
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
            .process(new Processor() {
              @Override
              public void process(final Exchange exchange) throws Exception {
                final Message message = exchange.getOut();
                String contents = new String(Files.readAllBytes(Paths.get("./src/main/resources/index.html")),
                    StandardCharsets.UTF_8);
                message.setBody(contents, String.class);
              }
            }).log("${body}")
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
