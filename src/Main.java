import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:data/sample.db");

        Statement stmt = conn.createStatement();

        stmt.execute(
        "CREATE TABLE Contacts (" +
            "contact_id INTEGER PRIMARY KEY AUTOINCREMENT, " +  
            "first_name TEXT NOT NULL, " +
            "last_name TEXT NOT NULL, " +
            "phone TEXT NOT NULL, " + 
            "email TEXT NOT NULL" +
        ")"  
        );
     }     
}
