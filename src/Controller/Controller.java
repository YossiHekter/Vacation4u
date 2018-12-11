package Controller;

import Model.IModel;
import Model.Model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class Controller implements IController, Observer {

    private IModel model;
    private RUDController rudController;
    private VacationPanelController vacationPanelController;

    // buttons
    public javafx.scene.control.Button btn_login;
    public javafx.scene.control.Button btn_sign_up;
    public javafx.scene.control.Button btn_photo;
    public javafx.scene.control.CheckBox photo_select;
    // text fields
    public javafx.scene.control.TextField txt_id_user;
    public javafx.scene.control.PasswordField txt_id_password;
    public javafx.scene.control.TextField txt_user_first_name;
    public javafx.scene.control.TextField txt_user_last_name;
    public javafx.scene.control.TextField txt_new_username;
    public javafx.scene.control.TextField txt_user_city;
    public javafx.scene.control.PasswordField txt_new_user_password;
    public String photo_path = "";
    // date picker
    public javafx.scene.control.DatePicker date_picker;
    // logo image
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

    public void setRUDController(RUDController rudController){ this.rudController = rudController; }

    public void onKeyReleasedLogin(){
        boolean releasedLogin = (txt_id_user.getText().isEmpty() || txt_id_password.getText().isEmpty());
        btn_login.setDisable(releasedLogin);
    }

    public void onKeyReleasedSignUp(){
        boolean releasedSignUp = (txt_user_first_name.getText().isEmpty() || txt_user_last_name.getText().isEmpty() ||
                txt_new_username.getText().isEmpty() || txt_user_city.getText().isEmpty() ||
                txt_new_user_password.getText().isEmpty() ||
                date_picker.getValue() == null);
        btn_sign_up.setDisable(releasedSignUp);
        onKeyReleasedLoadPhoto();
    }

    public void onKeyReleasedLoadPhoto(){
        boolean releasedLoadPhoto = (txt_new_username.getText().isEmpty());
        btn_photo.setDisable(releasedLoadPhoto);

    }

    //add photo button
    public void loadPhoto() throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose photo file");
        File file = fc.showOpenDialog(null);

        if (file != null) {
            photo_path = file.getAbsolutePath();
            File f = new File(photo_path);
            String type = new MimetypesFileTypeMap().getContentType(f).split("/")[0];
            if (type.equals("image")){
                photo_select.setSelected(true);
            }
            else{
                photo_path = "";
                showAlert("Error", "Please select a valid image file");
            }

        }
        else {
            photo_select.setSelected(false);
        }
    }

    //login button function
    public void login () {
        model.login(txt_id_user.getText(), txt_id_password.getText());
    }

    //sign up button function
    public void signUp () throws IOException {
        if (!photo_path.equals("")){
            File f = new File(photo_path);
            BufferedImage bufferedImage= ImageIO.read(f);
            String type = photo_path.substring(photo_path.lastIndexOf(".")+1);
            ImageIO.write(bufferedImage, type, new File("Resources/users_photo/"+txt_new_username.getText()+"."+type));
            photo_path = "Resources/users_photo/"+txt_new_username.getText()+"."+type;
        }

        String[] values = {txt_new_username.getText() , txt_new_user_password.getText()
                , date_picker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , txt_user_first_name.getText(), txt_user_last_name.getText(), txt_user_city.getText(), photo_path};
        model.signUp(values);
    }

    @Override
    public void update(Observable o, Object arg) {
        try{
            Object obj = ((Object[])arg)[0];
            String str = (String)obj;
            switch(str){
                case "login failed":
                    showAlert("Login Error", "The user name or password is incorrect, please try again.");
                    break;

                case "signUp failed":
                    showAlert("signUp Error","Registration failed\n\r" +
                            "Please try a new username");
                    break;

                case "login succeeded":
                    openRud(txt_id_user.getText());
                    break;

                case "signUp succeeded":
                    openRud(txt_new_username.getText());
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

    private void openRud(String user_name){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/Rud.fxml").openStream());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vacation4U App");
            stage.setScene(new Scene(root, 690, 450));
            root.setStyle("-fx-background-color: white");

            if(rudController == null){
                rudController = fxmlLoader.getController();
                ((Model)model).addObserver(rudController);
            }
            else {
                ((Model)model).deleteObserver(rudController);
                rudController = fxmlLoader.getController();
                ((Model)model).addObserver(rudController);
            }

            rudController.setModel(model);
            rudController.setUser_name(user_name);
            rudController.setController(this);
            rudController.setAbout();
            Stage prim = (Stage) this.btn_sign_up.getScene().getWindow();
            prim.close();
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

    public void OpenVacationPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/VacationPanel.fxml").openStream());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vacation4U App");
            stage.setScene(new Scene(root, 1280, 650));
            root.setStyle("-fx-background-color: white");
            stage.setResizable(true);

            if(vacationPanelController == null){
                vacationPanelController = fxmlLoader.getController();
                ((Model)model).addObserver(vacationPanelController);
            }
            else {
                ((Model)model).deleteObserver(vacationPanelController);
                vacationPanelController = fxmlLoader.getController();
                ((Model)model).addObserver(vacationPanelController);
            }
            model.setUser_name("");
            vacationPanelController.setModel(model);
            vacationPanelController.set();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {

        }
    }
}
