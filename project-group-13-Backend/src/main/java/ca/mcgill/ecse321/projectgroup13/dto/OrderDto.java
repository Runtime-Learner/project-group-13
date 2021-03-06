package ca.mcgill.ecse321.projectgroup13.dto;

import ca.mcgill.ecse321.projectgroup13.model.*;

import java.util.Set;

public class OrderDto{

    private double totalAmount;
    private int orderID;
    private OrderStatus orderStatus;
    private Set<ArtworkDto> artwork;
    //private PaymentDto payment;
    //private ShipmentDto shipment;
    private UserDto user;
    private boolean isDelivery;
    
    public OrderDto() {

    }

    public OrderDto(int orderID, double totalAmount, OrderStatus orderStatus, Set<ArtworkDto> artwork, UserDto user, boolean isDelivery){
        this.totalAmount = totalAmount;
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.artwork = artwork;
//        this.payment = payment;
//        this.shipment = shipment;
        this.user = user;
        this.isDelivery = isDelivery;
    }


//    public void setOrderStatus(OrderStatus value) {
//        this.orderStatus = value;
//    }


    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Set<ArtworkDto> getArtwork() {
        return artwork;
    }

    public void setArtwork(Set<ArtworkDto> artwork1s) {
        this.artwork = artwork1s;
    }

    public UserDto getUser() {
        return user;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }



//    public void setOrderID(String value) {
//        this.orderID = value;
//    }

    public int getOrderID() {
        return orderID;
    }


//    public void setTotalAmount(double value) {
//        this.totalAmount = value;
//    }

    public double getTotalAmount() {
        return totalAmount;
    }

//    public PaymentDto getPayment() {
//        return payment;
//    }

//    public void setPayment(Payment payment) {
//        this.payment = payment;
//    }


//    public ShipmentDto getShipment() {
//        return shipment;
//    }

//    public void setShipment(Set<Shipment> shipments) {
//        this.shipment = shipments;
//    }
    
	public boolean isShipmentMethodIsDelivery() {
		return this.isDelivery;
	}


}
