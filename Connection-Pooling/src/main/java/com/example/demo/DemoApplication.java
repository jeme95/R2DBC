package com.example.demo;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {


        // Creating a Connection
        Connection connection = acquireConnection();

        // Doing some stuff with the Connection
        Publisher<? extends Result> execute = connection.createStatement("UPDATE todo.tasks SET completed=1 WHERE  id=4;").execute();
        execute.subscribe(new Subscriber<Result>() {

            @Override
            public void onSubscribe(Subscription subscription) {
                // what to do, when subscription is created
            }

            @Override
            public void onNext(Result result) {
                // what to do, when items published
            }

            @Override
            public void onError(Throwable throwable) {
                // what to do, if error occurred
            }

            @Override
            public void onComplete() {
                // what to do, if streaming accomplished
            }
        });

        // releasing the Connection
        releaseConnection(connection);

        // see below method documentation
        disposePool(connectionPool);

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


    private static Connection acquireConnection() {
        return connectionPool.create().block();
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