package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import Model.AUser;

public interface IModel {

    void login(String userName , String userPassword);
    void signUp(String[] values);
    boolean legalPassword(String userPassword);
    boolean legalUserBirthday(LocalDate userBirthday);
    boolean legalVacationDate(LocalDate vacationDate);
    void search(String userName);
    void deleteUser(String userName);
    void updateUserData(String[] updateData);
    boolean searchUserName(String userName);
    String getUser_name();
    void setUser_name(String user_name);
    ArrayList<String[]> bringDetailsOfUser (String user);
    void createVacation(String[] values);
    String getVacation_idx();
    String getTransaction_idx();
    void deleteVacation(String deleteMe);
    void deleteRequest(String deleteMe);
    ArrayList<Flight> getVacation();
    Flight getVacationByIndex(int index);
    ArrayList<Flight> getVacationToDelete();
    ArrayList<Request> getMyRequests();
    ArrayList<Request> getAllRequests();
    void makePayment(String[] values);
    String get_photo(String userName);
    String getRequest_idx();
    void addToRequestDB(String[] values);
    ArrayList<Transaction> getMyTransactions();
    boolean legalUserName(String userName);

}
