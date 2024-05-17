import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:sqlite:data/taftr.db");
        Statement stmt = con.createStatement();
        
     }     
}
