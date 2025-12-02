package ma.ecommerce.orderservice.dto;

public class NotificationRequest {

    private Long orderId;
    private String message;
    private String channel;
    private String email;

    public NotificationRequest() {
    }

    public NotificationRequest(Long orderId,
                               String message,
                               String channel,
                               String email) {
        this.orderId = orderId;
        this.message = message;
        this.channel = channel;
        this.email = email;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
