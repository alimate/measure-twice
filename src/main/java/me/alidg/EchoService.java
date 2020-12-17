package me.alidg;

import io.grpc.stub.StreamObserver;
import me.alidg.stubs.grpc.EchoServiceGrpc.EchoServiceImplBase;
import me.alidg.stubs.grpc.EchoServiceOuterClass.Message;

public final class EchoService extends EchoServiceImplBase {

    @Override
    public void echo(Message request, StreamObserver<Message> responseObserver) {
        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }
}
