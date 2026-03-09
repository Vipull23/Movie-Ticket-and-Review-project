package org.movieproject.Movie.Ticket.and.Reviewing.System.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.TicketMessage;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class NotificationConsumer {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    NotificationService notificationService;

    @KafkaListener(topics = {"TICKET_BOOKED"}, groupId = "ticketGroup")
    public void setNotificationService(String message) throws JsonProcessingException {
        log.info("message ->{}", message);
        TicketMessage msg = objectMapper.readValue(message, TicketMessage.class);

        notificationService.sendNotification(msg);
    }
}
