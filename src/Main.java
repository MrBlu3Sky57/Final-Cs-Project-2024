import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:sqlite:data/taftr.db");
        Statement stmt = con.createStatement();

        stmt.execute("CREATE TABLE restaurants (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + 
            "name TEXT NOT NULL," +
            "location TEXT NOT NULL);");

     }     
}
