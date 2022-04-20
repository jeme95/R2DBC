package com.example.demo;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.Connection;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {


        // Creating a Connection
        Mono<Connection> connectionMono = acquireConnection();

        connectionMono.subscribe(connection -> {

            //  doing some stuff with the connection

            //  then releasing the Connection
            releaseConnection(connection);


        });

        //  you can also dispose the connection pool when you are done
        //  disposePool(connectionPool);

        SpringApplication.run(DemoApplication.class, args);
    }

    private static final MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder()
            .host("127.0.0.1")
            .port(3306)
            .username("test_user")
            .password("12345")
            .database("todo")
            .build();

    private static final MariadbConnectionFactory connectionFactory = new MariadbConnectionFactory(conf);
    private static final ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.
            builder(connectionFactory)
            .maxIdleTime(Duration.ofMillis(1000))
            .maxSize(5)
            .build();

    private static final ConnectionPool connectionPool = new ConnectionPool(configuration);


    private static Mono<Connection> acquireConnection() {
        return connectionPool.create();
    }

    private static void releaseConnection(Connection connection) {
        connection.close();
    }


    /***
     * Disposing a connection pool: deallocate any and all resources it may be using
     * @param connectionPool: connection pool to be disposed
     */
    private static void disposePool(ConnectionPool connectionPool) {
        connectionPool.dispose();

        //  doing the same thing: the close method simply calls the dispose method
        //  connectionPool.close();
    }


}