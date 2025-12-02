package ma.ecommerce.orderservice.dto;

public class PaymentRequest {

    private Long orderId;
    private Double amount;
    private String method;

    public PaymentRequest() {
    }

    public PaymentRequest(Long orderId, Double amount, String method) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
