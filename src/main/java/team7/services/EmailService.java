package team7.services;

public interface EmailService {

    void sendEmail(String recipient, String subject, String template);
}
