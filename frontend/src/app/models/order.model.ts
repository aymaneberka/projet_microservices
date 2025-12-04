export interface ClientPayload {
  firstName: string;
  lastName: string;
  email: string;
}

export interface CreateOrderRequest {
  productId: number;
  quantity: number;
  paymentMethod: string;
  client: ClientPayload;
}

export interface CreateOrderResponse {
  orderId: number;
  totalAmount: number;
  orderStatus: string;
  paymentStatus: string;
  message: string;
}
