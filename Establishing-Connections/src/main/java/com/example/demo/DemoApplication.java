package com.example.demo;

import io.r2dbc.spi.*;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.mariadb.r2dbc.api.MariadbConnection;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {

        // Instantiate a Connection Factory using ConnectionFactories and a connection URL
        connFactoryPerURL();

        // Instantiate a Connection Factory programmatically using MariadbConnectionConfiguration
        connFactoryProg();

        // Instantiate a Connection Factory programmatically using ConnectionFactories
        connFactoryProg2();

        SpringApplication.run(DemoApplication.class, args);

    }


    private static void connFactoryProg() {

        MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder()
                .host("127.0.0.1")
                .port(3306)
                .username("test_user")
                .password("12345")
                .database("todo")
                .build();

        MariadbConnectionFactory connFactoryProg = new MariadbConnectionFactory(conf);

        Mono<MariadbConnection> mariadbConnectionMono = connFactoryProg.create();

        mariadbConnectionMono.subscribe(mariadbConnection1 -> {
            validateConnection(mariadbConnection1, "mariadbConnection1");
            closeConnection(mariadbConnection1);
        });

    }

    private static void connFactoryProg2() {

        ConnectionFactoryOptions connectionFactoryOptions =
                ConnectionFactoryOptions.builder()
                        .option(ConnectionFactoryOptions.DRIVER, "mariadb")
                        .option(ConnectionFactoryOptions.PROTOCOL, "pipes")
                        .option(ConnectionFactoryOptions.HOST, "127.0.0.1")
                        .option(ConnectionFactoryOptions.PORT, 3306)
                        .option(ConnectionFactoryOptions.USER, "test_user")
                        .option(ConnectionFactoryOptions.PASSWORD,
                                "12345").build();

        MariadbConnectionFactory connFactoryProg2 = (MariadbConnectionFactory)
                ConnectionFactories.get(connectionFactoryOptions);

        MariadbConnection mariadbConnection = connFactoryProg2.create().block();

        validateConnection(mariadbConnection, "connFactoryProg2");

        closeConnection(mariadbConnection);

    }

    private static void connFactoryPerURL() {

        ConnectionFactory connFactoryPerURL = ConnectionFactories.get("r2dbc:mariadb://test_user:12345@127.0.0.1:3306/todo");
        Publisher<? extends Connection> publisher = connFactoryPerURL.create();

    }

    private static void validateConnection(MariadbConnection mariadbConnectionMono, String src) {
        Publisher<Boolean> validatePublisher = mariadbConnectionMono.validate(ValidationDepth.LOCAL);
        Mono<Boolean> monoValidated = Mono.from(validatePublisher);
        monoValidated.subscribe(validated -> {
            if (validated) {
                System.out.println("Connection by " + src + " is valid");
            } else {
                System.out.println("Connection is not valid");
            }
        });
    }

    private static void closeConnection(MariadbConnection mariadbConnection) {
        Publisher<Void> closePublisher = mariadbConnection.close();
        Mono<Void> monoClose = Mono.from(closePublisher);
        monoClose.subscribe();
    }


}