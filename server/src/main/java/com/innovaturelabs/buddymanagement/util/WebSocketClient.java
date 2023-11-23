package com.innovaturelabs.buddymanagement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient {

    Logger log = LoggerFactory.getLogger(WebSocketClient.class);
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to WebSocket server");
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("message recieved");

        // Compare the received message with the expected data
        if (message.equals("Expected data")) {
            log.info("expected data recieved");
        } else {
            log.error("Incorrect data received");
        }
        // Close the WebSocket connection after receiving a message
        closeSession();
    }

    @OnClose
    public void onClose() {
        System.out.println("WebSocket connection closed");
    }

    public void connect(String serverUri) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, new URI(serverUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSession() {
        // Close the WebSocket session
        // This will trigger the @OnClose method
        // if the server initiates the closing handshake
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String serverUri = "ws://localhost:8080/buddy/management";
        WebSocketClient client = new WebSocketClient();
        client.connect(serverUri);
        client.onClose();
    }
}
