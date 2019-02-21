package Model;

import java.time.LocalDate;

abstract public class AUser {

    private String userName;
    private String password;
    private LocalDate birthday;
    private String firstName;
    private String lastName;
    private String city;
    private String photo;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
