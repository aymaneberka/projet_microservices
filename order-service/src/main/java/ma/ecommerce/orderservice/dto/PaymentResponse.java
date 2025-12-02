package ma.ecommerce.orderservice.dto;

public class PaymentResponse {

    private Long orderId;
    private Double amount;
    private String status;
    private String transactionId;

    public PaymentResponse() {
    }

    public PaymentResponse(Long orderId, Double amount, String status, String transactionId) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.transactionId = transactionId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
