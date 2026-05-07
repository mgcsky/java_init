package com.example.demo.grpc;

import com.example.demo.fare.grpc.FareGrpcService;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.TimeUnit;


@Component
@ConditionalOnProperty(prefix = "grpc.server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GrpcServerLifecycle implements SmartLifecycle {
    private static final Logger log = LoggerFactory.getLogger(GrpcServerLifecycle.class);
    private final FareGrpcService fareGrpcService;

    @Value("${grpc.server.port:9090}")
    private int port;

    private volatile boolean running;
    private Server server;

    public GrpcServerLifecycle(FareGrpcService fareGrpcService) {
        this.fareGrpcService = fareGrpcService;
    }

    @Override
    public void start() {
        if(running) return;

        try {
            server = NettyServerBuilder.forPort(port)
                    .addService(fareGrpcService)
                    .addService(ProtoReflectionService.newInstance())
                    .build()
                    .start();

            running = true;
            log.info("gRPC server started on port {}", server.getPort());
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to start gRPC server", ex);
        }
    }

    @Override
    public void stop() {
        if (server == null) return;

        server.shutdown();
        try {
            if (!server.awaitTermination(5, TimeUnit.SECONDS))
                server.shutdownNow();
        } catch (InterruptedException ex) {
            server.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            running = false;
        }
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
