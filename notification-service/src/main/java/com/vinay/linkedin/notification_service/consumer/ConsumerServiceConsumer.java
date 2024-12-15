package com.vinay.linkedin.notification_service.consumer;

import com.vinay.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import com.vinay.linkedin.connection_service.event.SendConnectionRequestEvent;
import com.vinay.linkedin.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        log.info("handle connections: handleSendConnectionRequest: {}", sendConnectionRequestEvent);
        String message = "You have received a connection request from user with id: %d"+sendConnectionRequestEvent.getSenderId();

        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        log.info("handle connections: handleAcceptConnectionRequest: {}", acceptConnectionRequestEvent);
        String message = "You connection request has been accepted by the user with id: %d"+acceptConnectionRequestEvent.getReceiverId();

        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
    }
}
