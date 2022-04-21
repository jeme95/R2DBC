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

            //      binding by index
            MariadbStatement selectStatement = connection.createStatement("SELECT id, description AS task, completed FROM todo.tasks WHERE id > ?");
            selectStatement.bind(0, 3);

            //  alternative: binding by name
            //  selectStatement.bind("id",5);


            Flux<MariadbResult> publisher = selectStatement.execute();

            publisher.flatMap(result -> result.map((row, metadata) -> {
                        Integer id = row.get(0, Integer.class);
                        String descriptionFromAlias = row.get("task", String.class);
                        String isCompleted = (row.get(2, Boolean.class) == true) ? "Yes" : "No";
                        return String.format("ID: %s - Description: %s -Completed: %s", id, descriptionFromAlias, isCompleted);

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