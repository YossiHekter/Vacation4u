package Controller;

import Model.IModel;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ReadController implements Observer{

    //Objects
    private IModel model;

    // text fields
    public javafx.scene.control.TextField txt_username;


    public void setModel(IModel model){
        this.model = model;
    }

    public void SearchUser() {
        String userName = txt_username.getText();
        if((userName != null) && (!userName.equals(""))){//if the user wrote something:
            model.search(userName);
        }
        else {//if the text field is empty:
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("you need to fill the text field");
            alert.showAndWait();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            if(((Object[])arg)[0].equals("read")){
                ArrayList<String[]> select = (ArrayList<String[]>)((Object[])arg)[1];
                if(select.size() > 0) {//if we found the user we search:
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("User Info");
                    alert.setHeaderText("Search result");

                    //getting the data from the array list
                    String data = "";
                    String[] str = select.get(0);
                    data = "Username: " + str[0] + "\r\n" +
                            "Name: " + str[3] + "\r\n" +
                            "Last Name: " + str[4] + "\r\n" +
                            "City: " + str[5] + "\r\n" +
                            "Birthdate: " + str[2];

                    //creating new alert with the data:
                    alert.setContentText(data);
                    alert.showAndWait();
                }
                else {//if the select returned null row:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("User Info");
                    alert.setHeaderText("User not found");
                    alert.showAndWait();
                }
            }
        } catch (Exception e){

        }
    }

}