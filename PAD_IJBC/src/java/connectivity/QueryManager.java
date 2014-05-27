/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectivity;

import Leerling.Leerling;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.apache.velocity.VelocityContext;

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
    
    //creates a list of students
    /*public void studentList(String klas){
        //String searchStudents = "SELECT voornaam, klas FROM leerling WHERE klas = '" + klas + "'";
        System.out.println(klas);
        //return studentList;
    }*/
    
    
    
    public List<String> studentList(String klas, VelocityContext vv1_Context) {
        String searchStudents = "SELECT Voornaam, Achternaam, Leerlingnr, Klas FROM leerling WHERE klas = '" + klas + "'";
        List<String> studentList = new ArrayList<String>();
        List<String> leerlingLijst = new ArrayList<String>();
        
        rs = db.doQuery(searchStudents);
        
        String leerling = "";
        String leerlingNummers = "";
        try{
            while(rs.next()){
                
                leerling += rs.getString("Leerlingnr"); 
                /*leerling += (" ");
                leerling += rs.getString("voornaam");
                leerling += (" ");
                leerling += rs.getString("achternaam");*/
            
                studentList.add(leerling);
                leerling = "";
                
                /*leerlingNummers += rs.getString("voornaam");
                leerlingNummers += (" ");
                leerlingNummers += rs.getString("achternaam");
                leerlingNummers += (" ");
                leerlingNummers += rs.getString("Leerlingnr");                
                leerlingLijst.add(leerlingNummers);
                leerlingNummers = "";
                
                leerling += rs.getString("Leerlingnr");
                studentList.add(leerling);
                leerling = "";*/
                
            } 
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        
        for(String e : studentList){
            System.out.println(e);
        }
        
        vv1_Context.put("leerlingLijst", studentList);
        return studentList;
    }
    
    
    
    //sends the invitation
    public int invitationList(String Leerlingnummer, HttpServletRequest request, VelocityContext vv1_Context) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String targettedLeerlingnr = request.getParameter("userSelect");
        //checks if the invitation already exists
        String inviteCheck = "SELECT * FROM uitnodiging WHERE Leerling_Leerlingnr = '" + Leerlingnummer + "' AND Targetted_Leerlingnr = '" + targettedLeerlingnr + "'";
        rs = db.doQuery(inviteCheck);
        try {
            //if the invitation does not exist, add it to the database
            if(!rs.next()){
                String sendInvite = "INSERT INTO uitnodiging VALUES('" + Leerlingnummer + "','" + targettedLeerlingnr + "','" + sdf.format(date).toString() + "')";
                //record added
                int result = db.doInsert(sendInvite);
                System.out.println(result);
                if(result == 1){
                    vv1_Context.put("sendInviteSuccess", "Uitnodiging succesvol verstuurd!");
                    return result;
                } else {
                    vv1_Context.put("errorSendInvite", "Uitnodiging niet verstuurd!");
                    return result;
                }
            }  else {
                vv1_Context.put("errorSendInvite", "Je hebt al een uitnodiging naar deze leerling verstuurd!");
                return 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex);
            vv1_Context.put("errorSendInvite", "Uitnodiging niet verstuurd!");
            return 0;
        }
    }
    
    public String getFeedbackVoornaam(String leerlingnummer){
        String getNaam = "SELECT voornaam, tussenvoegsel, achternaam FROM leerling where Leerlingnr = '" + leerlingnummer + "'";
        rs = db.doQuery(getNaam);
        try {
            if(rs.next()){
                String naam = rs.getString("voornaam") +
                        rs.getString("tussenvoegsel") +
                        rs.getString("achternaam");
                return naam;
                
            } else {
                return "ROT OP";
            }
        } catch (SQLException ex) {
            Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex);
            return "ROT OP";
        }
    }
    
    
    public List<Leerling> getInviteList(VelocityContext vv1_Context, String leerlingnummer, HttpServletRequest request){
        List<Leerling> uitnodigingList = new ArrayList<Leerling>();
        try {
            String sql = "Select Leerling_Leerlingnr, Targetted_Leerlingnr, tussenvoegsel, voornaam, Achternaam FROM leerling, uitnodiging WHERE uitnodiging.Leerling_Leerlingnr = Leerling.Leerlingnr AND uitnodiging.Targetted_Leerlingnr = '" + leerlingnummer + "'";
            
            rs = db.doQuery(sql);
            
            
            
            while (rs.next()) {
                uitnodigingList.add(new Leerling(rs.getString("voornaam"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getString("Leerling_Leerlingnr")));
            }
        } catch (SQLException e) {
            System.out.println(DbManager.SQL_EXCEPTION + e.getMessage());
        }
        for(Leerling e: uitnodigingList){            
                System.out.println(e);          
        }
        vv1_Context.put("uitnodigingList", uitnodigingList);
        
        
        return uitnodigingList;
    }
    
    
    
    
    //
    public List<Leerling> getLeerlingList(String klas, VelocityContext vv1_Context) {
        List<Leerling> leerlingList = new ArrayList<Leerling>();
        try {
            String sql = "SELECT Voornaam, tussenvoegsel, Achternaam, Leerlingnr, Klas FROM leerling WHERE klas = '" + klas + "'";
            rs = db.doQuery(sql);
            while (rs.next()) {
                leerlingList.add(new Leerling(rs.getString("voornaam"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getString("leerlingnr")));
            }
        } catch (SQLException e) {
            System.out.println(DbManager.SQL_EXCEPTION + e.getMessage());
        }
        vv1_Context.put("leerlingList", leerlingList);
        return leerlingList;
    }
    
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

        
        String register = "INSERT INTO leerling VALUES('" 
        + Leerlingnummer + "','" 
        + Email + "','" 
        + Naam + "','" 
        + Achternaam + "','" 
        + Wachtwoord + "','"
        + Klas + "','"
        + Tussenvoegsel + "')";
        return db.doInsert(register);
        
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
    
    //Login query
    public String leerlingNummer(String email, String password){
        String login = "SELECT Leerlingnr FROM leerling WHERE Emailadres = '" +
                email + "' AND wachtwoord = '" + password + "'";
        try{
            rs = db.doQuery(login);
            if(rs.next()){
                return rs.getString("Leerlingnr");
            }
            else  {
                return "";
            }
        }
        catch(SQLException E){
            System.out.println(E.getMessage());
            return "";
        }
    }
    
    
}
