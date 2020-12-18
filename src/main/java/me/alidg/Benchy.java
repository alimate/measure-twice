package me.alidg;

import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import me.alidg.stubs.grpc.EchoServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static me.alidg.stubs.grpc.EchoServiceOuterClass.Message;

public final class Benchy {

    public static void main(String[] args) throws Exception {
        var requests = 10_000;
        var executorService = Executors.newWorkStealingPool();
        var channel = NettyChannelBuilder
            .forAddress("localhost", 9000)
            .usePlaintext()
            .executor(executorService)
            .build();
        var client = EchoServiceGrpc.newFutureStub(channel);

        var latch = new CountDownLatch(requests);

        var request = Message.newBuilder().setContent("Hello").build();
        var startedAt = System.nanoTime();
        for (int i = 0; i < requests; i++) {
            client.echo(request).addListener(latch::countDown, executorService);
        }

        latch.await();
        var duration = System.nanoTime() - startedAt;
        System.out.println("Took: " + duration / 1_000_000.0 + " millis");
        System.out.println("RPS: " + requests * 1_000_000_000.0 / duration);
    }
}
