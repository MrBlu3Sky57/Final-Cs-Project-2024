import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<String, User> users = new HashMap<String, User>();
        Helpers.addUser("Aaron", "password", users);
        users = Helpers.getUsers();

        for (Map.Entry<String, User> pair : users.entrySet()) {
            System.out.println("ID: " + pair.getKey() + " || USERNAME: " + pair.getValue().getUsername() + "|| PASSWORD: " + pair.getValue().getPassword() + "\n");
        }
        
    }     
}
