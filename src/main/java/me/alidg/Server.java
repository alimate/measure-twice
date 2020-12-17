package me.alidg;

import io.grpc.ServerBuilder;

public final class Server {

    public static void main(String[] args) throws Exception {
        var server = ServerBuilder
            .forPort(9000)
            .addService(new EchoService())
            .build();
        server.start();

        System.out.println("I'm listening to port 9000");
        server.awaitTermination();
    }
}
