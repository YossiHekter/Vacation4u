package Model;

import java.time.LocalDate;

public class User extends AUser {

    public User(String userName, String password, LocalDate birthday, String firstName, String lastName, String city, String photo){
        this.setUserName(userName);
        this.setPassword(password);
        this.setBirthday(birthday);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setCity(city);
        this.setPhoto(photo);
    }
}
