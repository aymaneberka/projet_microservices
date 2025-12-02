package ma.ecommerce.notificationservice.service;

import ma.ecommerce.notificationservice.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(NotificationRequest request) {

        log.info("Notification reçue : orderId={}, channel={}, email={}",
                request.getOrderId(), request.getChannel(), request.getEmail());

        if ("EMAIL".equalsIgnoreCase(request.getChannel()) && request.getEmail() != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(request.getEmail());   // 👈 destinataire = client
                message.setSubject("Confirmation de commande #" + request.getOrderId());
                message.setText(request.getMessage());

                mailSender.send(message);

                log.info("Email envoyé à {}", request.getEmail());
            } catch (Exception e) {
                log.error("Erreur lors de l'envoi de l'email : {}", e.getMessage(), e);
            }
        } else {
            log.warn("Canal non supporté ou email manquant. Canal={}, email={}",
                    request.getChannel(), request.getEmail());
        }
    }
}
