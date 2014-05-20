/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    
    //initial query before registering
    public boolean registerCheck(String Naam, String Tussenvoegsel, String Achternaam, String Leerlingnummer, String Klas, String Email, String Wachtwoord){
        String registerCheck = "SELECT Leerlingnr from leerling where Leerlingnr = '" + Leerlingnummer + "'";
        try{
            rs = db.doQuery(registerCheck);
            if(rs.next()){
                return false;
            }
            else {
                return true;
            }
        }
        catch(SQLException E){
            System.out.println(E.getMessage());
            return true;
        }
    }
    
    //register query
    public int register(String Naam, String Tussenvoegsel, String Achternaam, String Leerlingnummer, String Klas, String Email, String Wachtwoord){

        int result = 0;
        
        String register = "INSERT INTO leerling VALUES('" 
        + Leerlingnummer + "','" 
        + Email + "','" 
        + Naam + "','" 
        + Achternaam + "','" 
        + Wachtwoord + "','"
        + Klas + "','"
        + Tussenvoegsel + "')";
        result = db.doInsert(register);
        return result;
    }
    
    
    
    
    //Login query
    public boolean login(String email, String password){
        System.out.println(email);
        System.out.println(password);
        String login = "SELECT * FROM leerling WHERE Emailadres = '" +
                email + "' AND wachtwoord = '" + password + "'";
        String loggedIn = "";
        try{
            rs = db.doQuery(login);
            if(rs.next()){
                loggedIn = "Logged in succesful";
                System.out.println("Logged in succesful");
                return true;
            }
            else  {
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
