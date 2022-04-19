package com.example.demo;

import io.r2dbc.spi.*;
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
        Mono<MariadbConnection> monoConnection = createConnection();

        monoConnection.subscribe(connection -> {

            //  example for checking and disabling AutoCommit-Mode: in MariaDB superfluous: see below
            if (connection.isAutoCommit()) {
                connection.setAutoCommit(false);
            }


            // get the current Isolation Level
            System.out.println(connection.getTransactionIsolationLevel().asSql());

            //  changing the Isolation Level
            //  Possible levels: READ_COMMITTED, READ_UNCOMMITTED, REPEATABLE_READ, SERIALIZABLE
            connection.setTransactionIsolationLevel(IsolationLevel.READ_UNCOMMITTED);


            try {
                //  starting a transaction: Using the beginTransaction method on a MariadbConnection object
                //  will automatically disable auto-commit for the connection.
                connection.beginTransaction().subscribe();

                //  executing a transaction for making a money transfer
                MariadbStatement increaseReceiverStmt = connection.createStatement("UPDATE bank.customers SET balance=balance+50 WHERE  id=4 AND NAME='Max';");
                increaseReceiverStmt.execute();
                MariadbStatement decreaseSenderStmt = connection.createStatement("UPDATE bank.customers SET balance=balance-50 WHERE  id=2 AND NAME='Anna';");
                decreaseSenderStmt.execute()
                        .then(connection.commitTransaction())
                        .subscribe();

            } catch (Exception e) {

                //  rolling back all queries within the transaction
                connection.rollbackTransaction().subscribe();
                System.out.println(e.getMessage());
            }

        });


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