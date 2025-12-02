package ma.ecommerce.notificationservice.controller;

import ma.ecommerce.notificationservice.dto.NotificationRequest;
import ma.ecommerce.notificationservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendNotification(@RequestBody NotificationRequest request) {
        service.send(request);
    }
}
