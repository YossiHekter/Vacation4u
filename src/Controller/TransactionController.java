package Controller;

import Model.IModel;
import Model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TransactionController implements Observer {

    private IModel model;
    public ObservableList<Transaction> list = FXCollections.observableArrayList();
    public TableView<Transaction> table;


    public void setModel(IModel model) {
        this.model = model;
    }

    public void set() {
        String[] str = {"Payment Index", "Seller User Name", "Buyer User Name", "Price", " Date", "Type", "status"};

        TableColumn payment_idx = new TableColumn(str[0]);
        payment_idx.setMinWidth(110);
        payment_idx.setCellValueFactory(
                new PropertyValueFactory<>(str[0]));

        TableColumn seller = new TableColumn(str[1]);
        seller.setMinWidth(110);
        seller.setCellValueFactory(
                new PropertyValueFactory<>(str[1]));

        TableColumn buyer = new TableColumn(str[2]);
        buyer.setMinWidth(110);
        buyer.setCellValueFactory(
                new PropertyValueFactory<>(str[2]));

        TableColumn price = new TableColumn(str[3]);
        price.setMinWidth(110);
        price.setCellValueFactory(
                new PropertyValueFactory<>(str[3]));

        TableColumn date = new TableColumn(str[4]);
        date.setMinWidth(110);
        date.setCellValueFactory(
                new PropertyValueFactory<>(str[4]));

        TableColumn type = new TableColumn(str[5]);
        type.setMinWidth(110);
        type.setCellValueFactory(
                new PropertyValueFactory<>(str[5]));

        TableColumn status = new TableColumn(str[6]);
        status.setMinWidth(110);
        status.setCellValueFactory(
                new PropertyValueFactory<>(str[6]));

        list = FXCollections.observableArrayList();

        //connect between col name and to his values
        payment_idx.setCellValueFactory(new PropertyValueFactory<Transaction,String>("payment_Index"));
        seller.setCellValueFactory(new PropertyValueFactory<Transaction,String>("seller"));
        buyer.setCellValueFactory(new PropertyValueFactory<Transaction,String>("buyer"));
        price.setCellValueFactory(new PropertyValueFactory<Transaction,String>("price"));
        date.setCellValueFactory(new PropertyValueFactory<Transaction,String>("date"));
        type.setCellValueFactory(new PropertyValueFactory<Transaction,String>("type"));
        status.setCellValueFactory(new PropertyValueFactory<Transaction,String>("status"));

        //connect between the list and the table
        table.setItems(list);

        // add transaction to list
        ArrayList<Transaction> transactions = model.getMyTransactions();
        for(Transaction transaction : transactions){
            list.add(transaction);
        }

        // enter the cols to the table
        table.getColumns().addAll(payment_idx, seller,buyer,price,date,type,status);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
