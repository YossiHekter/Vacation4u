package Model;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import java.util.Date;
import java.util.Iterator;
import java.util.Observable;

public class Model extends Observable implements IModel {

    private Data_base db;
    private String user_name;

    public Model(){
        db = new Data_base("V4u.db");
        db.createUsersTable();
        db.createVacationTable();
        db.createPaymentTable();
        db.createRequestTable();
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

    public ArrayList<Payment> getMyTransactions() {
        ArrayList<Payment> ans = new ArrayList<>();
        ans = db.getMyTransactions(this.user_name);
        return ans;
    }

    public void exchangeVacation(String otherUsr_VacatinIDX,String exchangeMe){
        if(db.addToRequestsTable("REQUEST","Vacation_IDX",otherUsr_VacatinIDX,exchangeMe)) {
            Object[] args = {"vacation added"};
            setChanged();
            notifyObservers(args);
        }
        else{
            Object[] args = {"add vacation failed"};
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
    public boolean legalUserName(String userName){
        boolean ans = false;

        return ans;
    }

    @Override
    public boolean legalPassword(String userPassword){
        boolean ans = false;
        boolean num = false;
        boolean upper = false;
        if (userPassword.length() < 8)
            ans = false;
        else{
            for (int i=0;i<userPassword.length();i++){
                char c = userPassword.charAt(i);
                if (!num && Character.isDigit(c))
                    num = true;
                else if (!upper && Character.isUpperCase(c))
                    upper = true;
            }
        }
        if (num && upper)
            ans = true;
        return ans;
    }

    @Override
    public boolean legalUserBirthday(LocalDate userBirthday){
        boolean ans = false;
        int age = 0;
        if (userBirthday != null) {
            age =  Period.between(userBirthday, LocalDate.now()).getYears();
            if (age >= 18)
                ans = true;
        }
        return ans;
    }

    @Override
    public boolean legalVacationDate(LocalDate vacationDate){
        boolean ans = false;
        if (vacationDate != null)
            ans = vacationDate.isAfter(LocalDate.now());
        return ans;
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
            f.delete();
        }

        db.Delete("USERS","User_name",userName);
        db.Delete("VACATION","User_name",userName);
        db.Delete("PAYMENT","User_name_seller",userName);
        db.Delete("PAYMENT","User_name_buyer",userName);
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
            //db.Delete("VACATION","Vacation_IDX",values[0]);
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
    public String getTransaction_idx() {
        return db.getTransaction_idx();
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
    public ArrayList<Request> getMyRequests(){
        ArrayList<Request> ans = new ArrayList<Request>();
        ans = db.getMyRequests(this.user_name);
        return ans;
    }

    @Override
    public ArrayList<Request> getAllRequests(){
        ArrayList<Request> ans = new ArrayList<Request>();
        ans = db.getAllRequests();
        return ans;
    }

    @Override
    public void addToRequestDB(String[] values) {
        if(db.Insert("REQUEST",values)) {
            Object[] args = {"vacation added"};
            setChanged();
            notifyObservers(args);
        }
        else{
            Object[] args = {"add vacation failed"};
            setChanged();
            notifyObservers(args);
        }
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

    @Override
    public void deleteRequest(String deleteMe){
        if(db.Delete("REQUEST","Request_IDX",deleteMe)) {
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
    @Override
    public Fly getVacationByIndex(int index){
        Fly flight = null;
        ArrayList<Fly> list = getVacation();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Fly f = (Fly) it.next();
            if (Integer.parseInt(f.getVacation_Index()) == index) {
                flight = f;
                break;
            }
        }
        return flight;
    }
    public String getRequest_idx() {
        return db.getRequest_idx();
    }

    public void updateStatus(String status){
        db.updateStatus(status);
    }

}
