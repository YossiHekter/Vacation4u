package Controller;

import Model.IModel;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class VacationCreateController implements Observer {

    private IModel model;

    public javafx.scene.control.TextField txt_from;
    public javafx.scene.control.TextField txt_to;
    public javafx.scene.control.TextField txt_flightCmp;
    public javafx.scene.control.TextField txt_numOfTrav;
    public javafx.scene.control.TextField txt_price;

    public javafx.scene.control.ChoiceBox cb_luggage;
    public javafx.scene.control.ChoiceBox cb_cabinClass;
    public javafx.scene.control.ChoiceBox cb_vacType;

    public javafx.scene.control.CheckBox cbox_twoWay;
    public javafx.scene.control.CheckBox cbox_roomIncluded;

    public javafx.scene.control.DatePicker date_depart;
    public javafx.scene.control.DatePicker date_return;

    public javafx.scene.control.Slider sld_sleepRank;

    public javafx.scene.control.Button btn_submit;

    public void setModel(IModel model){
        this.model = model;
    }

    public void setUser_name(String user_name){ model.setUser_name(user_name); }

    public void setChoiceBox(){
        cb_cabinClass.setItems(FXCollections.observableArrayList("Economy","Economy plus","Business","First Class"));
        cb_cabinClass.setValue("Economy");

        cb_luggage.setItems(FXCollections.observableArrayList("Handbag only","Up to 23 KG"));
        cb_luggage.setValue("Handbag only");

        cb_vacType.setItems(FXCollections.observableArrayList("Exotic","Urban"));
        cb_vacType.setValue("Exotic");
    }

    public void onKeyReleasedSubmit(){
        boolean releasedSubmit = (txt_from.getText().isEmpty() || txt_to.getText().isEmpty() ||
                txt_flightCmp.getText().isEmpty() || txt_numOfTrav.getText().isEmpty() ||
                txt_price.getText().isEmpty() || date_depart.getValue() == null ||
                (cbox_twoWay.isSelected() && date_return.getValue() == null) ||
                cb_luggage.getValue() == null || cb_cabinClass.getValue() == null ||
                cb_vacType.getValue() == null);
        btn_submit.setDisable(releasedSubmit);

    }

    public void onKeyReleasedReturn(){
        boolean releasedReturn = (!cbox_twoWay.isSelected());
        date_return.setDisable(releasedReturn);
        onKeyReleasedSubmit();
    }

    public void createVacation () {

        //check if all the fields are ok
        if(CheckValidity()) {
            String[] values = {model.getVacation_idx(), model.getUser_name(), txt_from.getText().toUpperCase(), txt_to.getText().toUpperCase(),
                    date_depart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    "", txt_flightCmp.getText().toUpperCase(), txt_price.getText(), txt_numOfTrav.getText(),
                    "" + cb_luggage.getValue(), "" + cb_cabinClass.getValue(), "0",
                    "" + cb_vacType.getValue(), "0", "" + (int)((sld_sleepRank.getValue()*5)/100)};

            if (cbox_twoWay.isSelected()) {
                if (DatesValid()) {
                    values[5] = date_return.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    values[11] = "1";

                    if (cbox_roomIncluded.isSelected())
                        values[13] = "1";
                }
            }
            model.createVacation(values);
        }
    }

    private boolean DatesValid() {
        if(date_depart.getValue().isAfter(date_return.getValue())) {
            showAlert("Error","Return date must be after departure date!");
            return false;
        }
        return true;
    }

    //this function check if all the fields are ok
    private boolean CheckValidity() {
        if((txt_from.getText() == "") || (!isAlphaWord(txt_from.getText().replaceAll(" ",""))) ||
                (txt_to.getText() == "") || (!isAlphaWord(txt_to.getText().replaceAll(" ",""))) ||
                (txt_flightCmp.getText() == "") || (!isAlphaWord(txt_flightCmp.getText().replaceAll(" ",""))) ||
                (txt_numOfTrav.getText() == "") || (!isNumeric(txt_numOfTrav.getText())) ||
                (txt_price.getText() == "") || (!isNumeric(txt_price.getText()))) {
            showAlert("Error","Some fields are empty or incorrect \n please try again");
            return false;
        }
        return true;
    }

    public boolean isAlphaWord(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
            if (!Character.isDigit(c))
                return false;
        return true;
    }


    @Override
    public void update(Observable o, Object arg) {
        try{
            Object obj = ((Object[])arg)[0];
            String str = (String)obj;
            switch(str){
                case "create vacation failed":
                    showAlert("Create Vacation Failed", "Create vacation failed, please try again.");
                    break;

                case "create vacation succeeded":
                    //openRud(txt_id_user.getText());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Create Vacation Succeeded");
                    Optional<ButtonType> reasult = alert.showAndWait();
                    if(reasult.get() == ButtonType.OK)
                        alert.close();
                    Stage prim = (Stage) txt_from.getScene().getWindow();
                    prim.close();
                    break;

            }
        } catch (Exception e){

        }
    }

    private void showAlert(String title, String headerText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        Optional<ButtonType> reasult = alert.showAndWait();
        if(reasult.get() == ButtonType.OK)
            alert.close();
    }
}
