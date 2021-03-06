package Controller;
import Model.Request;
import Model.IModel;
import Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Flight;
import java.io.IOException;
import java.util.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class VacationPanelController implements Observer  {

    public ObservableList<Flight> list = FXCollections.observableArrayList();
    public TableView<Flight> table;
    public Hyperlink hl_home;
    public ChoiceBox tf_idx;
    public TextField tf_dest;
    public Button btn_go;
    public RadioButton rb_request;
    public RadioButton rb_switch;
    public Button btn_search;
    private IModel model;
    private MyVacationsController myVacationsController;

    @Override
    public void update(Observable o, Object arg) {
        try{
            Object obj = ((Object[])arg)[0];
            String str = (String)obj;
            switch(str){
                case "create vacation failed":
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Vacation purchase failed");
                    alert.setHeaderText("A malfunction occurred during the sting, please try again");
                    alert.showAndWait();
                    break;

                case "make payment succeeded":
                    //openRud(txt_id_user.getText());
                    Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert2.setTitle("Vacation purchase successful");
                    alert2.setHeaderText("You bought yourself a new vacation.\r\n" + "Hope you enjoy.");
                    alert2.showAndWait();
                    Stage prim = (Stage) btn_go.getScene().getWindow();
                    prim.close();
                    break;
            }
        } catch (Exception e){

        }
    }

    public void setModel(IModel model) {
        this.model = model;
    }

    public void startProcess () {
        //if it's a guest:
        if (model.getUser_name().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("To continue purchasing a vacation, sign up to Vacation4U");
            //Optional<ButtonType> reasult = alert.showAndWait();
            ButtonType yesButton = new ButtonType("Take me to sign up", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("I Don't want to", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);

            //checking what the user choosed
            alert.showAndWait().ifPresent((buttonType) -> {
                if (buttonType == yesButton) {
                    Stage prim = (Stage) this.hl_home.getScene().getWindow();
                    prim.close();
                }
            });
        }
        else {//if it's signed user:
            if (rb_request.isSelected()) {
                requestFunction();
            }
            if (rb_switch.isSelected()) {
                Stage prim = (Stage) this.hl_home.getScene().getWindow();
                prim.close();
                switchFunction();
            }

        }
    }


    public void switchFunction() {
        try {
            //openning new Stage to show in
            Stage stage = new Stage();
            stage.setTitle("My Vacations");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/MyVacations.fxml").openStream());
            root.setStyle("-fx-background-color: white");
            Scene scene = new Scene(root, 1350, 680);
            stage.setScene(scene);

            //loading the controllers of the new stage:
            if(myVacationsController == null){
                myVacationsController = fxmlLoader.getController();
                ((Model)model).addObserver(myVacationsController);
            }
            else {
                ((Model)model).deleteObserver(myVacationsController);
                myVacationsController = fxmlLoader.getController();
                ((Model)model).addObserver(myVacationsController);
            }
            myVacationsController.setModel(model);
            myVacationsController.set();

            //search for the seller user name
            String Other_UserName = getUserNameByIndex(""+tf_idx.getValue());

            myVacationsController.setOtherUsr(Other_UserName,""+tf_idx.getValue());

            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setRadioSwitch () {
        ToggleGroup t = new ToggleGroup();
        rb_request.setToggleGroup(t);
        rb_switch.setToggleGroup(t);
    }

    public String getUserNameByIndex(String index) {
        ArrayList<Flight> list = model.getVacation();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Flight f = (Flight) it.next();
            if ((f.getVacation_Index()).equals(index)){
                return f.getUser_name();
            }
        }
        return null;
    }

    public void set() {
        String[] str = {"Vacation Index", "User Name", "From", "To", "Depart", "Return Date",
                "Flight Company", "Total Price in $" , "Number Of Tickets", "Luggage",
                "Ticket Type", "Vacation Type", "Sleep Included", "Sleep Rank (Between 0-5)"};

        TableColumn vac_idx = new TableColumn(str[0]);
        vac_idx.setMinWidth(100);
        vac_idx.setCellValueFactory(
                new PropertyValueFactory<>(str[0]));

        TableColumn user_name = new TableColumn(str[1]);
        user_name.setMinWidth(120);
        user_name.setCellValueFactory(
                new PropertyValueFactory<>(str[1]));

        TableColumn from = new TableColumn(str[2]);
        from.setMinWidth(100);
        from.setCellValueFactory(
                new PropertyValueFactory<>(str[2]));

        TableColumn to = new TableColumn(str[3]);
        to.setMinWidth(100);
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
        vac_type.setMinWidth(80);
        vac_type.setCellValueFactory(
                new PropertyValueFactory<>(str[11]));

        TableColumn sleep_included = new TableColumn(str[12]);
        sleep_included.setMinWidth(65);
        sleep_included.setCellValueFactory(
                new PropertyValueFactory<>(str[12]));

        TableColumn sleep_rank = new TableColumn(str[13]);
        sleep_rank.setMinWidth(75);
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
        String user_name_connect = model.getUser_name();
        ArrayList<Flight> flys = model.getVacation();
        ArrayList<String> index = new ArrayList<String>();
        for( Flight f : flys){
            if (user_name_connect.equals("")) {
                list.add(f);
                index.add(""+f.getVacation_Index());

            }
            else {
                if (!f.getUser_name().equals(user_name_connect)){
                    list.add(f);
                    index.add(""+f.getVacation_Index());
                }
            }
        }
        if (index.size()>0){
            tf_idx.setItems(FXCollections.observableArrayList(index));
            tf_idx.setValue(index.get(0));
        }
        else {
            tf_idx.setItems(FXCollections.observableArrayList(index));
        }


        // enter the cols to the table
        table.getColumns().addAll(vac_idx,user_name,from,to,depart,return_date,flight_company,total_price,num_of_tickets,luggage,ticket_type,vac_type,sleep_included,sleep_rank);
        if (!model.getUser_name().equals(""))
            hl_home.setDisable(true);
    }

    public void goHome(){
        Stage prim = (Stage) this.hl_home.getScene().getWindow();
        prim.close();
    }

    //public void onKeyReleasedBuy(){
    //    boolean releasedBuy = (tf_idx.getText().isEmpty());
    //    btn_go.setDisable(releasedBuy);
    //}

    public void onKeyReleasedSearch(){
        boolean releasedSearch = (tf_dest.getText().isEmpty());
        if (tf_dest.getText().isEmpty()){
            this.set();
        }
        btn_search.setDisable(releasedSearch);
    }

    public void searchFunction(){
        String[] str = {"Vacation Index", "User Name", "From", "To", "Depart", "Return Date",
                "Flight Company", "Total Price in $" , "Number Of Tickets", "Luggage",
                "Ticket Type", "Vacation Type", "Sleep Included", "Sleep Rank (Between 0-5)"};

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

        String dest = tf_dest.getText().toUpperCase();
        String user_name_connect = model.getUser_name();
        ArrayList<Flight> flys = model.getVacation();
        ArrayList<String> index = new ArrayList<String>();
        for( Flight f : flys){
            if (dest.equals(f.getTo())) {
                if (user_name_connect.equals("")) {
                    list.add(f);
                    index.add(""+f.getVacation_Index());

                }
                else {
                    if (!f.getUser_name().equals(user_name_connect)){
                        list.add(f);
                        index.add(""+f.getVacation_Index());
                    }
                }
            }
        }
        table.setItems(list);

        if (index.size()>0){
            tf_idx.setItems(FXCollections.observableArrayList(index));
            tf_idx.setValue(index.get(0));
        }
        else {
            tf_idx.setItems(FXCollections.observableArrayList(index));
        }

        table.getColumns().addAll(vac_idx,user_name,from,to,depart,return_date,flight_company,total_price,num_of_tickets,luggage,ticket_type,vac_type,sleep_included,sleep_rank);
    }


    public void requestFunction(){
        if (isNumeric(""+tf_idx.getValue()) && onList(""+tf_idx.getValue())){

            //check if this user alredy request this vacation
            boolean hasSameRequest = false;
            ArrayList<Request> requests = model.getAllRequests();
            for( Request req : requests) {
                if ((""+tf_idx.getValue()).equals(req.getSeller_vacation_Index())
                && model.getUser_name().equals(req.getBuyer())&&
                        ((req.getType().equals("buy")&& rb_request.isSelected()) ||
                        (req.getType().equals("exchange"))&& !rb_request.isSelected())) {
                    hasSameRequest = true;
                }
            }
            if(hasSameRequest){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Vacation purchase failed");
                alert.setHeaderText("You have already made a purchase request for this vacation.\n\r" +
                        " Please be patient until the seller responds to the request.");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to buy ticket number " + tf_idx.getValue() + "?");
                ButtonType yesButton = new ButtonType("Yes!", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No, Sorry", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);
                alert.showAndWait().ifPresent((buttonType) -> {
                    if (buttonType == yesButton) {
                        Stage prim = (Stage) this.hl_home.getScene().getWindow();
                        prim.close();
                        Flight fly = null;
                        for (Flight f : this.list) {
                            if ((""+tf_idx.getValue()).equals(f.getVacation_Index())) {
                                fly = f;
                                break;
                            }
                        }
                        String[] values = {model.getRequest_idx(), ""+tf_idx.getValue(), "",
                                getUserNameByIndex(""+tf_idx.getValue()), model.getUser_name(), "buy"};
                        model.addToRequestDB(values);
                    }
                });
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Vacation index must be a number from the table");
            Optional<ButtonType> reasult = alert.showAndWait();
            if(reasult.get() == ButtonType.OK)
                alert.close();
        }

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

    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
            if (!Character.isDigit(c))
                return false;
        return true;
    }

}
