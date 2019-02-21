package Controller;

import Model.Flight;
import Model.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class VacationDeleteController implements Observer {

    public ObservableList<Flight> list = FXCollections.observableArrayList();
    public TableView<Flight> table;
    public Button btn_delete;
    private IModel model;
    public ChoiceBox txt_idxOfVacation;

    @Override
    public void update(Observable o, Object arg) {
        try {
            Object obj = ((Object[])arg)[0];
            String str = (String)obj;
            if(str.equals("vacation deleted")) {
                exitDeletePanel();
            }
        } catch (Exception e) {

        }
    }

    public void deleteVacation() {
        String deleteMe = (""+txt_idxOfVacation.getValue());

        //if the user wrote something:
        if((deleteMe != null) && (!deleteMe.equals("") && onList(deleteMe))) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this vacation?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No, Sorry", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);

            //checking what the user choosed
            alert.showAndWait().ifPresent((buttonType) -> {
                if (buttonType == yesButton) {
                    model.deleteVacation(deleteMe);
                    Stage prim = (Stage) this.btn_delete.getScene().getWindow();
                    prim.close();
                }
            });
        }
        else {//if the text field is empty:
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a valid vacation number from the table");
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
        vac_idx.setCellValueFactory(new PropertyValueFactory<Flight,String>("Vacation_Index"));
        user_name.setCellValueFactory(new PropertyValueFactory<Flight,String>("user_name"));
        from.setCellValueFactory(new PropertyValueFactory<Flight,String>("from"));
        to.setCellValueFactory(new PropertyValueFactory<Flight,String>("to"));
        depart.setCellValueFactory(new PropertyValueFactory<Flight,String>("depart"));
        return_date.setCellValueFactory(new PropertyValueFactory<Flight,String>("return_date"));
        flight_company.setCellValueFactory(new PropertyValueFactory<Flight,String>("flight_comp"));
        total_price.setCellValueFactory(new PropertyValueFactory<Flight,String>("price"));
        num_of_tickets.setCellValueFactory(new PropertyValueFactory<Flight,String>("num_of_travelers"));
        luggage.setCellValueFactory(new PropertyValueFactory<Flight,String>("luggage"));
        ticket_type.setCellValueFactory(new PropertyValueFactory<Flight,String>("cabin_class"));
        vac_type.setCellValueFactory(new PropertyValueFactory<Flight,String>("vac_type"));
        sleep_included.setCellValueFactory(new PropertyValueFactory<Flight,String>("sleep_included"));
        sleep_rank.setCellValueFactory(new PropertyValueFactory<Flight,String>("sleep_rank"));

        //connect between the list and the table
        table.setItems(list);
        // add flights to list
        ArrayList<Flight> flys = model.getVacationToDelete();
        ArrayList<String> index = new ArrayList<String>();
        for( Flight f : flys){
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

    private boolean onList(String text) {
        boolean ans = false;
        for( Flight f : this.list){
            if(text.equals(f.getVacation_Index())){
                ans = true;
                break;
            }
        }
        return ans;
    }
}
