package csci2040u.bytecouncil;

public class Account {
    // Attributes
    String username;
    String password;
    String userID;

    // Constructor
    public Account(String username, String password, String userID) {
        this.username = username;
        this.password = password;
        this.userID = userID;
    }

    //Getter for all information in account
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserID() {
        return userID;
    }  

    // toString if we need it
    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
