package Model;

import javafx.beans.property.SimpleStringProperty;

public class Transaction {

    private final SimpleStringProperty payment_Index;
    private final SimpleStringProperty seller;
    private final SimpleStringProperty buyer;
    private final SimpleStringProperty price;
    private final SimpleStringProperty date;
    private final SimpleStringProperty type;
    private final SimpleStringProperty status;


    public Transaction(String payment_Index, String seller, String buyer, String price, String date,
                    String type, String status) {
        this.payment_Index = new SimpleStringProperty(payment_Index);
        this.price = new SimpleStringProperty(price);
        this.date = new SimpleStringProperty(date);
        this.seller = new SimpleStringProperty(seller);
        this.buyer = new SimpleStringProperty(buyer);
        this.type = new SimpleStringProperty(type);
        this.status = new SimpleStringProperty(status);

    }

    public String getPayment_Index() {
        return payment_Index.get();
    }

    public SimpleStringProperty payment_IndexProperty() {
        return payment_Index;
    }

    public void setPayment_Index(String payment_Index) {
        this.payment_Index.set(payment_Index);
    }

    public String getSeller() {
        return seller.get();
    }

    public SimpleStringProperty sellerProperty() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller.set(seller);
    }

    public String getBuyer() {
        return buyer.get();
    }

    public SimpleStringProperty buyerProperty() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer.set(buyer);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getStatus() { return type.get(); }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }





}
