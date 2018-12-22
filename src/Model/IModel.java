package Model;

import java.io.IOException;
import java.util.ArrayList;

public interface IModel {

    void login(String userName , String userPassword);
    void signUp(String[] values);
    void search(String userName);
    void deleteUser(String userName);
    void updateUserData(String[] updateData);
    boolean searchUserName(String userName);
    String getUser_name();
    void setUser_name(String user_name);
    ArrayList<String[]> bringDetailsOfUser (String user);
    void exchangeVacation(String otherUsr_VacatinIDX,String exchangeMe);
    void createVacation(String[] values);
    String getVacation_idx();
    String getTransaction_idx();
    void deleteVacation(String deleteMe);
    void deleteRequest(String deleteMe);
    ArrayList<Fly> getVacation();
    Fly getVacationByIndex(int index);
    ArrayList<Fly> getVacationToDelete();
    ArrayList<Request> getMyRequests();
    ArrayList<Request> getAllRequests();
    void makePayment(String[] values);
    String get_photo(String userName);
    String getRequest_idx();
    void addToRequestDB(String[] values);
    ArrayList<Payment> getMyTransactions();
    void updateStatus(String status);

}
