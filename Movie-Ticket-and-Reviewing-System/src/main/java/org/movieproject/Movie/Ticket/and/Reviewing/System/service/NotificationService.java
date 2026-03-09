package org.movieproject.Movie.Ticket.and.Reviewing.System.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.TicketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    ObjectMapper objectMapper = new ObjectMapper();

    public void sendNotification(TicketMessage message) {
        try {
            sendEmailToUser(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sendSMSToUser(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSMSToUser(TicketMessage message) {
        log.info("calling sms service for showDetails: {} seatDetails:{} to number {}", message.getShow(), message.getSeats(), message.getMobile());
    }

    private void sendEmailToUser(TicketMessage message) throws JsonProcessingException {
        log.info("calling sms service for showDetails: {} seatDetails:{} to number {}", message.getShow(), message.getSeats(), message.getMobile());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getEmail());
        mailMessage.setSubject("MovieNow Ticket");
        mailMessage.setText("Show: " + message.getShow() + " Tickets: " + message.getSeats());

        log.info(objectMapper.writeValueAsString(mailMessage));

        javaMailSender.send(mailMessage);
    }
}
