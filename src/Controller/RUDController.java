package Controller;

import Model.IModel;
import Model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

public class RUDController implements Observer{

    private IModel model;
    private ReadController readController;
    private UpdateController updateController;
    private Controller controller;
    private VacationPanelController vacationPanelController;
    private VacationCreateController vacationCreateController;
    private VacationDeleteController vacationDeleteController;

    public javafx.scene.control.MenuBar menu;
    public javafx.scene.image.ImageView iv_about;
    public javafx.scene.image.ImageView photo_img;


    public void setModel(IModel model) { this.model = model; }

    public void setAbout(){
        try {
            InputStream is_about = new FileInputStream("Resources/about_company.jpg");
            Image logo = new Image(is_about);
            iv_about.setImage(logo);

            String url = model.get_photo(model.getUser_name());
            if (!url.equals("")){
                File f = new File(url);
                InputStream is = new FileInputStream(url);
                Image user_img = new Image(is);
                photo_img.setImage(user_img);
                is.close();
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser_name(String user_name){ model.setUser_name(user_name); }

    public void setController(Controller controller){ this.controller = controller; }

    public void EditProfile() {
        try {
            //openning new Stage to show in
            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/Update.fxml").openStream());
            root.setStyle("-fx-background-color: white");
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);

            //loading the controllers of the new stage:
            if(updateController == null){
                updateController = fxmlLoader.getController();
                ((Model)model).addObserver(updateController);
            }
            else {
                ((Model)model).deleteObserver(updateController);
                updateController = fxmlLoader.getController();
                ((Model)model).addObserver(updateController);
            }
            updateController.setModel(model);
            updateController.setLogo();

            //call the function that shows the details of the user
            updateController.searchUser(model.getUser_name());

            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SearchFunction() {
        try {
            //openning new Stage to show in
            Stage stage = new Stage();
            stage.setTitle("Search");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/Read.fxml").openStream());
            root.setStyle("-fx-background-color: white");
            Scene scene = new Scene(root, 500, 180);
            stage.setScene(scene);

            //loading the controllers of the new stage:
            if(readController == null){
                readController = fxmlLoader.getController();
                ((Model)model).addObserver(readController);
            }
            else {
                ((Model)model).deleteObserver(readController);
                readController = fxmlLoader.getController();
                ((Model)model).addObserver(readController);
            }
            readController.setModel(model);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

    public void DeleteUser() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete your user?");
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No, Sorry", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton,noButton);

        //checking what the user choose
        alert.showAndWait().ifPresent((buttonType) -> {
            if(buttonType == yesButton){
                model.deleteUser(model.getUser_name());
            } });
    }

    public void sign_out(){
        if(readController != null)
            ((Model)model).deleteObserver(readController);
        if(updateController != null)
            ((Model)model).deleteObserver(updateController);
        if(vacationPanelController != null)
            ((Model)model).deleteObserver(vacationPanelController);
        if(vacationCreateController != null)
            ((Model)model).deleteObserver(vacationCreateController);
        if(vacationDeleteController != null)
            ((Model)model).deleteObserver(vacationDeleteController);
        Stage prim = (Stage) menu.getScene().getWindow();
        prim.close();
        openMainView();
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            Object obj = ((Object[])arg)[0];
            String str = (String)obj;
            if(str.equals("user deleted")) {
                sign_out();
            }
        } catch (Exception e) {

        }
    }

    private void openMainView(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Vacation4U");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/sample.fxml").openStream());
            Scene scene = new Scene(root, 600, 450);
            stage.setScene(scene);
            root.setStyle("-fx-background-color: white");
            stage.setResizable(false);
            //-------
            if(controller == null){
                controller = fxmlLoader.getController();
                ((Model)model).addObserver(controller);
            }
            else {
                ((Model)model).deleteObserver(controller);
                controller = fxmlLoader.getController();
                ((Model)model).addObserver(controller);
            }
            controller.setModel(model);
            controller.setRUDController(this);
            controller.setLogo();
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
            vacationPanelController.setModel(model);
            vacationPanelController.set();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {

        }
    }

    public void OpenNewVacation() {
        try {

            Stage stage = new Stage();
            stage.setTitle("Vacation4U");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/VacationCreate.fxml").openStream());
            Scene scene = new Scene(root, 600, 450);
            stage.setScene(scene);
            root.setStyle("-fx-background-color: white");
            stage.setResizable(false);
            //-------
            if(vacationCreateController == null){
                vacationCreateController = fxmlLoader.getController();
                ((Model)model).addObserver(vacationCreateController);
            }
            else {
                ((Model)model).deleteObserver(vacationCreateController);
                vacationCreateController = fxmlLoader.getController();
                ((Model)model).addObserver(vacationCreateController);
            }
            vacationCreateController.setModel(model);
            vacationCreateController.setUser_name(model.getUser_name());
            vacationCreateController.setChoiceBox();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {

        }
    }

    public void OpenDeleteVacation() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/VacationDelete.fxml").openStream());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vacation4U - Delete Vacation");
            stage.setScene(new Scene(root, 1280, 670));
            root.setStyle("-fx-background-color: white");
            stage.setResizable(true);

            //-------
            if(vacationDeleteController == null){
                vacationDeleteController = fxmlLoader.getController();
                ((Model)model).addObserver(vacationDeleteController);
            }
            else {
                ((Model)model).deleteObserver(vacationDeleteController);
                vacationDeleteController = fxmlLoader.getController();
                ((Model)model).addObserver(vacationDeleteController);
            }
            vacationDeleteController.setModel(model);
            vacationDeleteController.set();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {

        }
    }


}
