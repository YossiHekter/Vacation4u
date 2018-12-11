package Controller;

import Model.IModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class UpdateController implements Observer{

    private IModel model;

    public javafx.scene.control.Button btn_edit;
    public javafx.scene.control.TextField txt_username;
    public javafx.scene.control.TextField txt_password;
    public javafx.scene.control.TextField txt_firstname;
    public javafx.scene.control.TextField txt_lastname;
    public javafx.scene.control.TextField txt_city;
    public javafx.scene.control.DatePicker datePicker_date_of_birth;
    public javafx.scene.image.ImageView img_logo;

    public void setModel(IModel model){
        this.model = model;
    }

    public void setLogo(){
        try {
            Image logo = new Image(Controller.class.getClassLoader().getResourceAsStream("logo.jpeg"));
            img_logo.setImage(logo);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser_name(String user_name){ model.setUser_name(user_name); }

    public void edit() throws IOException {
        if(txt_username.getText().isEmpty() && txt_password.getText().isEmpty() &&
                txt_firstname.getText().isEmpty() && txt_lastname.getText().isEmpty() &&
                txt_city.getText().isEmpty() && datePicker_date_of_birth.valueProperty().isNull().get()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit Error");
            alert.setHeaderText("You must enter at least one update parameter");
            alert.showAndWait();
        }
        else if((!txt_username.getText().isEmpty() && !model.searchUserName(txt_username.getText()))
                && !txt_username.getText().equals(model.getUser_name())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit Error");
            alert.setHeaderText("The username already exists in the system\r\n" +
                    "Please select a different username");
            alert.showAndWait();
        }
        else {
            String[] data = new String[8];
            data[0] = model.getUser_name();
            if(!txt_username.getText().isEmpty()) {
                data[1] = txt_username.getText();
                setUser_name(txt_username.getText());
                String url = model.get_photo(data[0]);
                data[7] = "";
                if (!url.equals("") && !data[0].equals(data[1])){
                    File f = new File(url);
                    BufferedImage bufferedImage= ImageIO.read(f);
                    String type = url.substring(url.lastIndexOf(".")+1);
                    ImageIO.write(bufferedImage, type, new File("Resources/users_photo/"+data[1]+"."+type));
                    data[7] = "Resources/users_photo/"+data[1]+"."+type;
                    f.delete();
                }
            }
            else {
                data[1] = "";
                data[7] = "";
            }
            if(!txt_password.getText().isEmpty())
                data[2] = txt_password.getText();
            else
                data[2]="";

            if(!txt_firstname.getText().isEmpty())
                data[3] = txt_firstname.getText();
            else
                data[3]="";

            if(!txt_lastname.getText().isEmpty())
                data[4] = txt_lastname.getText();
            else
                data[4]="";

            if(!txt_city.getText().isEmpty())
                data[5] = txt_city.getText();
            else
                data[5]="";

            if(datePicker_date_of_birth.valueProperty().isNotNull().get())
                data[6] = datePicker_date_of_birth.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            else
                data[6]="";

            model.updateUserData(data);
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            if (((Object[]) arg)[0].equals("user update")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update user info");
                alert.setHeaderText("Update succeeded");
                alert.showAndWait();
            }
        } catch (Exception e){

        }
    }

    //this function will show the all details of the user in edit page
    public void searchUser (String user){
        ArrayList<String[]> currUser = model.bringDetailsOfUser(user);
        String[] arr = currUser.get(0);
        txt_username.setText(arr[0]);
        txt_password.setText(arr[1]);
        datePicker_date_of_birth.setPromptText(arr[2]);
        txt_firstname.setText(arr[3]);
        txt_lastname.setText(arr[4]);
        txt_city.setText(arr[5]);
    }
}
