package Controller;

import Model.Flight;
import Model.IModel;
import Model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MyRequestController implements Observer {

    public ObservableList<Request> requestsList = FXCollections.observableArrayList();
    public TableView<Request> table;
    public Button btn_choose;
    private IModel model;
    public ChoiceBox txt_idxOfVacation;

    @Override
    public void update(Observable o, Object arg) { }

    public void chooseVacation() {
        String reqIndex = (""+txt_idxOfVacation.getValue());
        boolean exchange = false;
        if((reqIndex != null) && (!reqIndex.equals("") && onList(reqIndex))) {
            Alert alert;
            ButtonType rejectButton;
            ButtonType confirmButton;
            String sellerVacIndex = null;
            String buyerVacIndex =null;
            ArrayList<Request> requests = model.getMyRequests();
            for( Request req : requests){
                if(reqIndex.equals(req.getRequest_Index())){
                    sellerVacIndex = req.getSeller_vacation_Index();
                    buyerVacIndex = req.getBuyer_vacation_Index();
                    break;
                }
            }
            final String vacationIndex = sellerVacIndex;
            if(buyingRequest(reqIndex)){

                //buy window
                exchange = false;
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Have you received a payment for this vacation?");
                confirmButton = new ButtonType("confirm", ButtonBar.ButtonData.YES);
                rejectButton = new ButtonType("reject request", ButtonBar.ButtonData.YES);
                ButtonType cancelButton = new ButtonType("cancel", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(confirmButton, rejectButton, cancelButton);

            }else{

                //exchange window
                exchange = true;
                Flight flight = model.getVacationByIndex(Integer.parseInt(buyerVacIndex));
                String from = flight.getFrom();
                String to = flight.getTo();
                String depart = flight.getDepart();
                String return_date = flight.getReturn_date();
                String flight_comp = flight.getFlight_comp();
                String price = flight.getPrice();
                String num_of_travelers = flight.getNum_of_travelers();
                String luggage = flight.getLuggage();
                String cabin_class = flight.getCabin_class();
                String vac_type = flight.getVac_type();
                String sleep_included = flight.getSleep_included();
                String sleep_rank = flight.getSleep_rank();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Vacation details");
                alert.setContentText(
                        "from: " + from + "  " + "to: " + to + "\n\r" +
                        "departure date: " + depart + "  " + "return date: " + return_date + "\n\r" +
                        "flight company: " + flight_comp + "  " + "asking price: " + price + "\n\r" +
                        "number of travelers: "+ num_of_travelers + "  " + "luggage: "+ luggage+ "\n\r"+
                        "class: "+ cabin_class + "  " + "vacation type: " + vac_type + "\n\r" +
                        "sleep: " + sleep_included + "  " + "sleep rank: " + sleep_rank);
                confirmButton = new ButtonType("confirm", ButtonBar.ButtonData.YES);
                rejectButton = new ButtonType("reject request", ButtonBar.ButtonData.YES);
                ButtonType cancelButton = new ButtonType("cancel", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(confirmButton, rejectButton, cancelButton);

            }
            final String buyerVacationIndex = buyerVacIndex;
            final boolean isExchange = exchange;
            alert.showAndWait().ifPresent((buttonType) -> {

                //request rejected
                if (buttonType == rejectButton) {
                    if(onList(reqIndex)) {
                        String[] prop = getRequest(reqIndex);
                        String[] payment = {model.getTransaction_idx(), prop[0], prop[1], prop[2], prop[3], prop[4],
                                "rejected"};
                        model.makePayment(payment);
                        model.deleteRequest(reqIndex);
                    }
                    Stage prim = (Stage) this.btn_choose.getScene().getWindow();
                    prim.close();
                }

                //request approved
                else if(buttonType == confirmButton){
                    if(onList(reqIndex)) {
                        String[] prop = getRequest(reqIndex);
                        String[] payment = {model.getTransaction_idx(), prop[0], prop[1], prop[2], prop[3], prop[4],
                                "confirm"};
                        model.makePayment(payment);
                        model.deleteRequest(reqIndex);

                        //exchange
                        //add another transaction, now the seller is the buyer
                        if(isExchange){
                            String tmp = prop[0];
                            prop[0] = prop[1];
                            prop[1] = tmp;
                            payment[0] = model.getTransaction_idx();
                            payment[1] = prop[0];
                            payment[2] = prop[1];
                            model.makePayment(payment);
                        }

                        //reject the other requests of this vacation
                        int listIndex=0;
                        int listLen = requestsList.size();
                        for (Request req : requestsList) {
                            if(!(reqIndex.equals(req.getRequest_Index())))
                            {
                                if (vacationIndex.equals(req.getSeller_vacation_Index())||
                                        (req.getType().equals("exchange")
                                                && buyerVacationIndex.equals(req.getBuyer_vacation_Index()))) {
                                    prop = getRequest(req.getRequest_Index());
                                    payment[0] = model.getTransaction_idx();
                                    payment[1] = prop[0];
                                    payment[2] = prop[1];
                                    payment[3] = prop[2];
                                    payment[4] = prop[3];
                                    payment[5] = prop[4];
                                    payment[6] = "rejected";
                                    model.makePayment(payment);
                                    model.deleteRequest(req.getRequest_Index());
                                }
                            }

                            listIndex++;
                            if(listLen==listIndex)
                                break;
                        }

                        //delete the vacation from the vacation table
                        model.deleteVacation(vacationIndex);

                        //delete the buyer vacation from the vacation table
                        if (isExchange)
                            model.deleteVacation(buyerVacationIndex);
                    }
                    Stage prim = (Stage) this.btn_choose.getScene().getWindow();
                    prim.close();
                }
            });
        }
        //if the text field is empty
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a valid request number from the table");
            alert.showAndWait();
        }
    }

    private void exitDeletePanel() {
        //Stage stage = (Stage) btn_submit.getScene().getWindow();
        //stage.close();
    }


    public void setModel(IModel model) {
        this.model = model;
    }

    public void set() {
        String[] str = {"Request Index", "My Vacation Index", "Buyer Vacation Index","Buyer User Name", "Type"};

        TableColumn req_idx = new TableColumn(str[0]);
        req_idx.setMinWidth(80);
        req_idx.setCellValueFactory(
                new PropertyValueFactory<>(str[0]));

        TableColumn seller_vac_idx = new TableColumn(str[1]);
        seller_vac_idx.setMinWidth(120);
        seller_vac_idx.setCellValueFactory(
                new PropertyValueFactory<>(str[1]));

        TableColumn buyer_vac_idx = new TableColumn(str[2]);
        buyer_vac_idx.setMinWidth(140);
        buyer_vac_idx.setCellValueFactory(
                new PropertyValueFactory<>(str[2]));

        TableColumn user_name = new TableColumn(str[3]);
        user_name.setMinWidth(120);
        user_name.setCellValueFactory(
                new PropertyValueFactory<>(str[3]));

        TableColumn type = new TableColumn(str[4]);
        type.setMinWidth(120);
        type.setCellValueFactory(
                new PropertyValueFactory<>(str[4]));

        requestsList = FXCollections.observableArrayList();

        //connect between col name and to his values
        req_idx.setCellValueFactory(new PropertyValueFactory<Request,String>("request_Index"));
        seller_vac_idx.setCellValueFactory(new PropertyValueFactory<Request,String>("seller_vacation_Index"));
        buyer_vac_idx.setCellValueFactory(new PropertyValueFactory<Request,String>("buyer_vacation_Index"));
        user_name.setCellValueFactory(new PropertyValueFactory<Request,String>("buyer"));
        type.setCellValueFactory(new PropertyValueFactory<Request,String>("type"));

        //connect between the list and the table
        table.setItems(requestsList);

        // add request to list
        ArrayList<String> index = new ArrayList<String>();
        ArrayList<Request> requests = model.getMyRequests();
        for( Request req : requests){
            requestsList.add(req);
            index.add(""+req.getRequest_Index());
        }

        if (index.size()>0){
            txt_idxOfVacation.setItems(FXCollections.observableArrayList(index));
            txt_idxOfVacation.setValue(index.get(0));
        }
        else {
            txt_idxOfVacation.setItems(FXCollections.observableArrayList(index));
        }

        // enter the cols to the table
        table.getColumns().addAll(req_idx, seller_vac_idx, buyer_vac_idx ,user_name,type);
    }

    private boolean onList(String text) {
        boolean ans = false;
        for( Request req : this.requestsList){
            if(text.equals(req.getRequest_Index())){
                ans = true;
                break;
            }
        }
        return ans;
    }

    private boolean buyingRequest(String text) {
        boolean ans = true;
        for( Request req : this.requestsList){
            if(text.equals(req.getRequest_Index())){
                if(req.getType().equals("exchange"))
                ans = false;
                break;
            }
        }
        return ans;
    }

    private String[] getRequest(String index){
        String[] ans= new String[5];
        String vacation_Index;
        for( Request req : this.requestsList) {
            if (index.equals(req.getRequest_Index())) {
                vacation_Index = req.getSeller_vacation_Index();
                Flight flight = model.getVacationByIndex(Integer.parseInt(vacation_Index));
                LocalDate localDate = LocalDate.now();
                ans[0] = req.getSeller(); //seller
                ans[1] = req.getBuyer(); //buyer
                ans[2] = flight.getPrice(); //price
                ans[3] = DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate); //date
                ans[4] = req.getType(); //buy or exchange
                break;
            }
        }
        return ans;
    }

}


