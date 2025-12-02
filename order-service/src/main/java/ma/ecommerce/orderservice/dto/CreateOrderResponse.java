package ma.ecommerce.orderservice.dto;

public class CreateOrderResponse {

    private Long orderId;
    private Double totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String message;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(Long orderId, Double totalAmount,
                               String orderStatus, String paymentStatus, String message) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
