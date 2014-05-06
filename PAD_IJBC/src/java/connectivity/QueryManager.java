/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Kelly
 */
public class QueryManager {
    
    String s_Template = "";
    public QueryManager(){
        this.db = new DbManager();
    }
    ResultSet rs;
    Statement st;
    DbManager db;
    
    //Login query
    public boolean login(String email, String password){
        String login = "SELECT email, Wachtwoord FROM testaccount where email = '" +
                email + "' AND wachtwoord = '" + password + "'";
        String loggedIn = "";
        try{
            rs = db.doQuery(login);
            if(rs.next()){
                loggedIn = "Logged in succesful";
                System.out.println("Logged in succesful");
                return true;
            }
            else {
                System.out.println("Invalid e-mail and/or password.");
                return false;
            }
            
        }
        catch(SQLException E){
            System.out.println(E.getMessage());
            return false;
        }
        
    }
    
    
}
