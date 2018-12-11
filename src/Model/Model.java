package Model;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.Observable;

public class Model extends Observable implements IModel {

    private Data_base db;
    private String user_name;

    public Model(){
        db = new Data_base("V4u.db");
        db.createUsersTable();
        db.createVacationTable();
        db.createPaymentTable();
    }

    @Override
    public void login(String userName, String userPassword) {

        boolean login = db.checkPassword(userName, userPassword);
        if(login){
            Object[] args = {"login succeeded"};
            setChanged();
            notifyObservers(args);
        }
        else{
            Object[] args = {"login failed"};
            setChanged();
            notifyObservers(args);
        }

    }

    @Override
    public void signUp(String[] values){
        if(db.Insert("USERS", values)) {
            Object[] args = {"signUp succeeded"};
            setChanged();
            notifyObservers(args);
        }
        else{
            Object[] args = {"signUp failed"};
            setChanged();
            notifyObservers(args);
        }
    }

    @Override
    public void search(String userToSearch) {
        ArrayList<String[]> select = db.Read("USERS","User_name", userToSearch);
        Object[] args = {"read", select};
        setChanged();
        notifyObservers(args);
    }

    @Override
    public void deleteUser(String userName) {
        String url = get_photo(userName);
        if (!url.equals("")){
            File f = new File(url);
            System.out.println(f.delete());
        }

        db.Delete("USERS","User_name",userName);
        //notify to the RUD Controller that the user deleted so the user
        //will exit to the main menu
        setChanged();
        Object[] args = {"user deleted"};
        notifyObservers(args);

    }

    @Override
    public String get_photo(String userName){
        String url = "";
        ArrayList<String[]> select  = db.Read("USERS","User_name",userName);
        String[] str = select.get(0);
        url = str[6];
        return url;
    }

    @Override
    public void updateUserData(String[] updateData){
        if(!updateData[2].equals(""))
            db.Update("USERS", "Password", updateData[2], "User_name", updateData[0]);
        if(!updateData[3].equals(""))
            db.Update("USERS", "First_name", updateData[3], "User_name", updateData[0]);
        if(!updateData[4].equals(""))
            db.Update("USERS", "Last_name", updateData[4], "User_name", updateData[0]);
        if(!updateData[5].equals(""))
            db.Update("USERS", "City", updateData[5], "User_name", updateData[0]);
        if(!updateData[6].equals(""))
            db.Update("USERS", "Birthday", updateData[6], "User_name", updateData[0]);
        if(!updateData[7].equals(""))
            db.Update("USERS", "Photo", updateData[7], "User_name", updateData[0]);
        if(!updateData[1].equals(""))
            db.Update("USERS", "User_name", updateData[1], "User_name", updateData[0]);

        setChanged();
        Object[] args = {"user update"};
        notifyObservers(args);
    }

    @Override
    public boolean searchUserName(String userToSearch){
        ArrayList<String[]> select = db.Read("USERS","User_name", userToSearch);
        return select.isEmpty();
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    // this function returns arraylist of all details that belongs to the user
    public ArrayList<String[]> bringDetailsOfUser (String user){
        ArrayList<String[]> ans = new ArrayList<String[]>();
        ans = db.Read("USERS","User_name", user);
        return ans;
    }

    @Override
    public void createVacation(String[] values){
        if(db.Insert("VACATION", values)) {
            Object[] args = {"create vacation succeeded"};
            setChanged();
            notifyObservers(args);
        }
        else{
            Object[] args = {"create vacation failed"};
            setChanged();
            notifyObservers(args);
        }
    }

    @Override
    public void makePayment(String[] values){
        if(db.Insert("PAYMENT", values)) {
            Object[] args = {"make payment succeeded"};
            db.Delete("VACATION","Vacation_IDX",values[0]);
            setChanged();
            notifyObservers(args);
        }
        else{
            Object[] args = {"make payment failed"};
            setChanged();
            notifyObservers(args);
        }
    }

    @Override
    public String getVacation_idx() {
        return db.getVacation_idx();
    }

    @Override
    public ArrayList<Fly>  getVacation(){
        ArrayList<Fly> ans = new ArrayList<Fly>();
        ans = db.getVacations();
        return ans;
    }

    @Override
    public ArrayList<Fly>  getVacationToDelete(){
        ArrayList<Fly> ans = new ArrayList<Fly>();
        ans = db.getVacationToDelete(this.user_name);
        return ans;
    }

    @Override
    public void deleteVacation(String deleteMe) {
        if(db.Delete("VACATION","Vacation_IDX",deleteMe)) {
            Object[] args = {"vacation deleted"};
            setChanged();
            notifyObservers(args);
        }
        else{
            Object[] args = {"delete vacation failed"};
            setChanged();
            notifyObservers(args);
        }
    }

}
