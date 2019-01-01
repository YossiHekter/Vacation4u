package Controller;

import Model.Fly;
import Model.IModel;
import Model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyVacationsController implements Observer {

    public ObservableList<Fly> list = FXCollections.observableArrayList();
    public TableView<Fly> table;
    public Button btn_exchange;
    private IModel model;
    public ChoiceBox txt_idxOfVacation;

    private String otherUsr_VacationIDX;
    private String otherUsr_Username;

    @Override
    public void update(Observable o, Object arg) {

    }

    public void setOtherUsr(String otherUsr_Username, String otherUsr_VacationIDX) {
        this.otherUsr_VacationIDX = otherUsr_VacationIDX;
        this.otherUsr_Username = otherUsr_Username;
    }

    public void ExchangeVacation(ActionEvent actionEvent) {
        String exchangeMeIDX = (""+txt_idxOfVacation.getValue());

        //if the user wrote something:
        if((exchangeMeIDX != null) && (!exchangeMeIDX.equals("") && onList(exchangeMeIDX))) {

            //check if this exchange is already exists in the system
            boolean hasSameRequest = false;
            boolean hasInverseRequest = false;
            ArrayList<Request> requests = model.getAllRequests();
            for( Request req : requests) {

                //check if the same exchange exist
                if (otherUsr_VacationIDX.equals(req.getSeller_vacation_Index())
                        & exchangeMeIDX.equals(req.getBuyer_vacation_Index())) {
                    hasSameRequest = true;
                }

                //check if the inverse exchange exist
                if (otherUsr_VacationIDX.equals(req.getBuyer_vacation_Index())
                        & exchangeMeIDX.equals(req.getSeller_vacation_Index())) {
                    hasInverseRequest = true;
                }
            }
            if(hasSameRequest) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Vacation exchange failed");
                alert.setHeaderText("You have already made this exchange request.\n\r" +
                        " Please be patient until the seller responds to the request.");
                alert.showAndWait();
            }else if(hasInverseRequest){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Vacation exchange failed");
                alert.setHeaderText("You have received the same exchange deal by the seller.\n\r" +
                        "Please check your Requests in your Vacation Option.");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to exchange this vacation?");
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No, Sorry", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);

                //checking what the user choosed
                alert.showAndWait().ifPresent((buttonType) -> {
                    if (buttonType == yesButton) {
                        String[] values = {model.getRequest_idx(), otherUsr_VacationIDX, (""+txt_idxOfVacation.getValue()),
                                otherUsr_Username, model.getUser_name(), "exchange"};
                        model.addToRequestDB(values);

                        Stage prim = (Stage) this.btn_exchange.getScene().getWindow();
                        prim.close();
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("sent");
                        alert2.setHeaderText("Your exchange request has been sent to the user");
                        alert2.showAndWait();
                    }
                });
            }
        }
        else {//if the text field is empty:
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a valid vacation number from the table");
            alert.showAndWait();
        }
    }



    public void setModel(IModel model) {
        this.model = model;
    }



    private boolean onList(String text) {
        boolean ans = false;
        for( Fly f : this.list){
            if(text.equals(f.getVacation_Index())){
                ans = true;
                break;
            }
        }
        return ans;
    }

    public void set() {
        String[] str = {"Vacation Index", "User Name", "From", "To", "Depart", "Return Date",
                "Flight Company", "Total Price in $" , "Number Of Tickets", "Luggage",
                "Ticket Type", "Vacation Type", "Sleep Included", "Sleep Rank"};

        TableColumn vac_idx = new TableColumn(str[0]);
        vac_idx.setMinWidth(120);
        vac_idx.setCellValueFactory(
                new PropertyValueFactory<>(str[0]));

        TableColumn user_name = new TableColumn(str[1]);
        user_name.setMinWidth(120);
        user_name.setCellValueFactory(
                new PropertyValueFactory<>(str[1]));

        TableColumn from = new TableColumn(str[2]);
        from.setMinWidth(120);
        from.setCellValueFactory(
                new PropertyValueFactory<>(str[2]));

        TableColumn to = new TableColumn(str[3]);
        to.setMinWidth(120);
        to.setCellValueFactory(
                new PropertyValueFactory<>(str[3]));

        TableColumn depart = new TableColumn(str[4]);
        depart.setMinWidth(110);
        depart.setCellValueFactory(
                new PropertyValueFactory<>(str[4]));

        TableColumn return_date = new TableColumn(str[5]);
        return_date.setMinWidth(110);
        return_date.setCellValueFactory(
                new PropertyValueFactory(str[5]));

        TableColumn flight_company = new TableColumn(str[6]);
        flight_company.setMinWidth(100);
        flight_company.setCellValueFactory(
                new PropertyValueFactory<>(str[6]));

        TableColumn total_price = new TableColumn(str[7]);
        total_price.setMinWidth(80);
        total_price.setCellValueFactory(
                new PropertyValueFactory<>(str[7]));

        TableColumn num_of_tickets = new TableColumn(str[8]);
        num_of_tickets.setMinWidth(60);
        num_of_tickets.setCellValueFactory(
                new PropertyValueFactory<>(str[8]));

        TableColumn luggage = new TableColumn(str[9]);
        luggage.setMinWidth(100);
        luggage.setCellValueFactory(
                new PropertyValueFactory<>(str[9]));

        TableColumn ticket_type = new TableColumn(str[10]);
        ticket_type.setMinWidth(100);
        ticket_type.setCellValueFactory(
                new PropertyValueFactory<>(str[10]));

        TableColumn vac_type = new TableColumn(str[11]);
        vac_type.setMinWidth(100);
        vac_type.setCellValueFactory(
                new PropertyValueFactory<>(str[11]));

        TableColumn sleep_included = new TableColumn(str[12]);
        sleep_included.setMinWidth(60);
        sleep_included.setCellValueFactory(
                new PropertyValueFactory<>(str[12]));

        TableColumn sleep_rank = new TableColumn(str[13]);
        sleep_rank.setMinWidth(60);
        sleep_rank.setCellValueFactory(
                new PropertyValueFactory<>(str[13]));

        list = FXCollections.observableArrayList();

        //connect between col name and to his values
        vac_idx.setCellValueFactory(new PropertyValueFactory<Fly,String>("Vacation_Index"));
        user_name.setCellValueFactory(new PropertyValueFactory<Fly,String>("user_name"));
        from.setCellValueFactory(new PropertyValueFactory<Fly,String>("from"));
        to.setCellValueFactory(new PropertyValueFactory<Fly,String>("to"));
        depart.setCellValueFactory(new PropertyValueFactory<Fly,String>("depart"));
        return_date.setCellValueFactory(new PropertyValueFactory<Fly,String>("return_date"));
        flight_company.setCellValueFactory(new PropertyValueFactory<Fly,String>("flight_comp"));
        total_price.setCellValueFactory(new PropertyValueFactory<Fly,String>("price"));
        num_of_tickets.setCellValueFactory(new PropertyValueFactory<Fly,String>("num_of_travelers"));
        luggage.setCellValueFactory(new PropertyValueFactory<Fly,String>("luggage"));
        ticket_type.setCellValueFactory(new PropertyValueFactory<Fly,String>("cabin_class"));
        vac_type.setCellValueFactory(new PropertyValueFactory<Fly,String>("vac_type"));
        sleep_included.setCellValueFactory(new PropertyValueFactory<Fly,String>("sleep_included"));
        sleep_rank.setCellValueFactory(new PropertyValueFactory<Fly,String>("sleep_rank"));

        //connect between the list and the table
        table.setItems(list);
        // add flights to list
        ArrayList<Fly> flys = model.getVacationToDelete();
        ArrayList<String> index = new ArrayList<String>();
        for( Fly f : flys){
            list.add(f);
            index.add(""+f.getVacation_Index());
        }

        if (index.size()>0){
            txt_idxOfVacation.setItems(FXCollections.observableArrayList(index));
            txt_idxOfVacation.setValue(index.get(0));
        }
        else {
            txt_idxOfVacation.setItems(FXCollections.observableArrayList(index));
        }
        // enter the cols to the table
        table.getColumns().addAll(vac_idx,user_name,from,to,depart,return_date,flight_company,total_price,num_of_tickets,luggage,ticket_type,vac_type,sleep_included,sleep_rank);

    }


}
