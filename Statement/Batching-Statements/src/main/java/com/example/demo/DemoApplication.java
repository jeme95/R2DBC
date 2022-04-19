package com.example.demo;

import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.mariadb.r2dbc.api.MariadbConnection;
import org.mariadb.r2dbc.api.MariadbStatement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {


        // establishing a Connection
        Mono<MariadbConnection> connectionMono = createConnection();
        connectionMono.subscribe(connection -> {

            MariadbStatement updateStatement = connection.createStatement("UPDATE todo.tasks SET description=? WHERE  id=?;");
            updateStatement.bind(0, "batching statement 1").bind(1, 3).add();
            updateStatement.bind(0, "batching statement 2").bind(1, 4).add();
            updateStatement.bind(0, "batching statement 3").bind(1, 5);

            // Retrieve and print the number of rows affected
            updateStatement.execute().subscribe(result -> result.getRowsUpdated().
                    subscribe(count -> System.out.println(count.toString())));

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


}