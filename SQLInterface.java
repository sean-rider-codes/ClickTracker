/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SeanR
 */
package clicktracker2.pkg0;
import java.sql.*;
//import com.mysql.jdbc.*;

public class SQLInterface {
    
    Connection con = null;
    
    public SQLInterface()
    {
        String user = "srider_366f16";
        String db = "srider_366f16";
        String pass = "kH6PNxhn7d";
        String host = "rei.cs.ndsu.nodak.edu";
        
        try{

            /**con = DriverManager.getConnection("jdbc:mysql://rei.cs.ndsu.nodak.edu:3306/ascherli_366f16", "ascherli_366f16", "tlrhdo4amu");

            //?user=ascherli_366f16&password=tlrhdo4amu&useUnicode=true&characterEncoding=UTF-8
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM CoordinateTable");           

            while(rs.next())
            {
                System.out.println(rs.getString("xCoordinate" + " : " + rs.getString("yCoordinate")));
            }*/
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db + "," + user + "," + pass);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");
            
            while(rs.next())
            {
                System.out.println(rs.getString("name"));
            }
            
        } 
        
        catch(Exception ex)
        {     
            System.out.println("Error: " + ex);
        }
    }
    
    public static void main(String[] args) 
    {
        SQLInterface connect = new SQLInterface();
        
    }
}

