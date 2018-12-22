package Model;

import javafx.beans.property.SimpleStringProperty;

public class Request {
    private final SimpleStringProperty request_Index;
    private final SimpleStringProperty seller_vacation_Index;
    private final SimpleStringProperty buyer_vacation_Index;
    private final SimpleStringProperty seller;
    private final SimpleStringProperty buyer;
    private final SimpleStringProperty type;



    public Request(String request_Index, String seller_vacation_Index, String buyer_vacation_Index,String seller,
                   String buyer, String type) {
        this.request_Index = new SimpleStringProperty(request_Index);
        this.seller_vacation_Index = new SimpleStringProperty(seller_vacation_Index);
        this.buyer_vacation_Index = new SimpleStringProperty(buyer_vacation_Index);
        this.seller = new SimpleStringProperty(seller);
        this.buyer = new SimpleStringProperty(buyer);
        this.type = new SimpleStringProperty(type);
    }

    public String getRequest_Index() {
        return request_Index.get();
    }

    public SimpleStringProperty request_IndexProperty() {
        return request_Index;
    }

    public void setRequest_Index(String vacation_Index) {
        this.request_Index.set(vacation_Index);
    }

    public String getSeller_vacation_Index() {
        return seller_vacation_Index.get();
    }

    public SimpleStringProperty seller_vacation_IndexProperty() {
        return seller_vacation_Index;
    }

    public void setsetSeller_vacation_Index(String vacation_Index) {
        this.buyer_vacation_Index.set(vacation_Index);
    }

    public String getBuyer_vacation_Index() {
        return buyer_vacation_Index.get();
    }

    public SimpleStringProperty buyer_vacation_IndexProperty() {
        return buyer_vacation_Index;
    }

    public void setBuyer_vacation_Index(String vacation_Index) {
        this.buyer_vacation_Index.set(vacation_Index);
    }

    public String getSeller() {
        return seller.get();
    }

    public SimpleStringProperty sellerProperty() {
        return seller;
    }

    public void setSeller(String user_name) {
        this.seller.set(user_name);
    }

    public String getBuyer() {
        return buyer.get();
    }

    public SimpleStringProperty buyerProperty() {
        return buyer;
    }

    public void setBuyer(String from) {
        this.buyer.set(from);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String to) {
        this.type.set(to);
    }

}
