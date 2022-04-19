package com.example.demo;

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

            MariadbStatement updateStatement = connection.createStatement("UPDATE todo.tasks SET description='set by java' WHERE  id=13;");
            MariadbStatement insertStatement = connection.createStatement("INSERT INTO todo.tasks (description,completed) VALUES ('New Task 1',0);");
            MariadbStatement deleteStatement = connection.createStatement("DELETE FROM todo.tasks WHERE  id=12;");

            Flux<MariadbResult> publisher = insertStatement.execute();

            // Retrieve and print the number of rows affected
            publisher.subscribe(result -> result.getRowsUpdated().
                    subscribe(count -> {
                        System.out.println(count.toString());
                        closeConnection(connection);
                    }));

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


    private static void closeConnection(MariadbConnection mariadbConnection) {
        Publisher<Void> closePublisher = mariadbConnection.close();
        Mono<Void> monoClose = Mono.from(closePublisher);
        monoClose.subscribe();
    }


}