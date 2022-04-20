package com.example.demo;

import io.r2dbc.spi.*;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.mariadb.r2dbc.api.MariadbConnection;
import org.mariadb.r2dbc.api.MariadbResult;
import org.mariadb.r2dbc.api.MariadbStatement;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {


        // establishing a Connection
        Mono<MariadbConnection> monoConnection = createConnection();

        monoConnection.subscribe(connection -> {

            MariadbStatement insertStatement = connection.createStatement("INSERT INTO todo.tasks (description) VALUES ('New Task')")
                    .returnGeneratedValues("id");

            Flux<MariadbResult> publisher = insertStatement.execute();

            publisher.flatMap(result -> result.map((row, metadata) -> {
                        Object id = row.get("id");
                        return String.format("ID: %s", id);
                    }))
                    .subscribe(System.out::println);
            
        });




//        keep the application running, otherwise the main thread may end before
//        the result has been retrieved / before the statement was executed
        SpringApplication.run(DemoApplication.class, args);
    }

    private static Mono<MariadbConnection> createConnection() {

        MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder()
                .host("127.0.0.1")
                .port(3306)
                .username("test_user")
                .password("12345")
                .database("todo")
                .build();

        MariadbConnectionFactory connFactoryProg = new MariadbConnectionFactory(conf);

        return connFactoryProg.create();

    }


    private static void validateConnection(MariadbConnection mariadbConnectionMono) {
        Publisher<Boolean> validatePublisher = mariadbConnectionMono.validate(ValidationDepth.LOCAL);
        Mono<Boolean> monoValidated = Mono.from(validatePublisher);
        monoValidated.subscribe(validated -> {
            if (validated) {
                System.out.println("Connection is valid");
            } else {
                System.out.println("Connection is not valid");
            }
        });
    }


}