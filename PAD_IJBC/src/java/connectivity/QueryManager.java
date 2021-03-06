/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connectivity;

import Leerling.Leerling;
import Vragen.Vragen;
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
    
    
    
    
    public void uitnodigingMelding(String leerlingnummer, VelocityContext vv1_Context){
        int uitnodigingen = 0;
        String uitnodigingChecker = "SELECT COUNT(Targetted_Leerlingnr) FROM uitnodiging WHERE Targetted_Leerlingnr = '" + leerlingnummer + "'";
        rs = db.doQuery(uitnodigingChecker);
        try{
            while(rs.next()){
                uitnodigingen = rs.getInt(1);
                System.out.println("Er zijn: "+uitnodigingen+ " uitnodiging(en");
            } 
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        if(uitnodigingen ==0){
            vv1_Context.put("uitnodiging", "/PAD_IJBC/Images/Uitnodigingen.jpg");
            System.out.println("Geen uitnodiging aanwezig!");
        } else if(uitnodigingen != 0){
            vv1_Context.put("uitnodiging", "/PAD_IJBC/Images/Uitnodiging_Er is een.png");
            System.out.println("Er is een uitnodinging!!");
        }
    }
    
        //checks if someone already filled in a feedback form for the logged in user
    public boolean feedbackCheck2(String leerlingnummer){
        String sql = "SELECT * from vraag WHERE Targetted_Leerlingnr = '" + leerlingnummer + "'";
        rs = db.doQuery(sql);
        try {
            if(rs.next()){
                return true;
            } else {
                return false;
            }
            
        } catch (SQLException e){
            return false;
        }
    }
    
    //checks if someone already filled in a feedback form for the logged in user
    public boolean feedbackCheck(String leerlingnummer){
        String sql = "SELECT * from vraag WHERE Leerling_Leerlingnr = '" + leerlingnummer + "'";
        rs = db.doQuery(sql);
        try {
            if(rs.next()){
                return true;
            } else {
                return false;
            }
            
        } catch (SQLException e){
            return false;
        }
    }
    
    
    //calculated the average results per feedback part
    public void feedbackGemiddelde(String leerlingnummer, VelocityContext vv1_Context ){
        double resultaat1 = 0;
        double resultaat2 = 0;
        double resultaat3 = 0;
                
        String feedbackResults = "SELECT avg(Onderdeel1Punten) as onderdeel1, avg(Onderdeel2Punten) as onderdeel2, avg(Onderdeel3Punten) as onderdeel3 FROM vraag WHERE Leerling_Leerlingnr = '" + leerlingnummer + "'";
        rs = db.doQuery(feedbackResults);
        try{
            while(rs.next()){
                resultaat1 = rs.getInt(1);
                resultaat2 = rs.getInt(2);
                resultaat3 = rs.getInt(3);
            } 
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        Vragen result = new Vragen(resultaat1, resultaat2, resultaat3);
        
        
        
        
        
        //result part1
        if(result.getOnderdeel1() <= 20 && result.getOnderdeel1() >= 18){
            vv1_Context.put("onderdeel1Text", "Volgens de gegeven feedback ben jij erg goed in verantwoordelijkheid dragen. Je neemt altijd verantwoordelijkheid voor je eigen gedrag en voor de groep en komt (haast) nooit te laat. Je levert je werk altijd op tijd in. Verantwoordelijkheid dragen gaat je prima af! Ga zo door!");
            vv1_Context.put("onderdeel1", "4p.png");
            System.out.println("1: ****");
        } else if(result.getOnderdeel1() <= 17 && result.getOnderdeel1() >= 13){
            vv1_Context.put("onderdeel1", "3p.png");
            vv1_Context.put("onderdeel1Text", "Volgens de gegeven feedback ben jij goed in verantwoordelijkheid dragen. Je neemt best wel veel verantwoordelijkheid voor je eigen acties en gedrag ook voor de groep  en je komt heel weinig te laat. Je levert je werk op tijd in. Verantwoordelijkheid dragen gaat je goed goed af! Alleen nog de puntjes op de i zetten en het zal prima worden.");
            System.out.println("1: ***");
        }else if(result.getOnderdeel1() <= 12 && result.getOnderdeel1() >= 8){
            vv1_Context.put("onderdeel1", "2p.png");
            vv1_Context.put("onderdeel1Text", "Volgens de gegeven feedback neem jij af en toe verantwoordelijk op je. Je neemt weinig verantwoordelijkheid voor je eigen acties en gedrag en je komt soms tot vaak te laat. Je levert je werk soms op tijd in. Verantwoordelijkheid dragen gaat je niet zo goed af maar wel oké. Het is nog geen voldoende dus je moet er nog harder aan werken.");
            System.out.println("1: **");
        } else if(result.getOnderdeel1() <= 7 && result.getOnderdeel1() >= 4){
            vv1_Context.put("onderdeel1", "1p.png");
            vv1_Context.put("onderdeel1Text", "Volgens de gegeven feedback ben jij niet goed in verantwoordelijk dragen. Je neemt geen verantwoordelijkheid voor je eigen acties en gedrag en je komt veel te laat. Je levert je werk meestal niet op tijd in. Verantwoordelijkheid dragen gaat je moeilijk af. Het is niet voldoende dus je moet veel beter je best doen.");
            System.out.println("1: *");
        }else {
            System.out.println("1: onder 0");
        }
        
        //result part2
        if(result.getOnderdeel2() <= 12 && result.getOnderdeel2() >= 11){
            vv1_Context.put("onderdeel2", "4p.png");
            vv1_Context.put("onderdeel2Text", "Volgens de gegeven feedback gaat verantwoordelijkheid afleggen je prima af, je komt je afspraken altijd na en houd je mentor, leraren en ouders goed op de hoogte van je behaalde resultaten en dagelijkse bezigheden op school. Dus met verantwoordelijkheid afleggen ben je prima bezig, ga zo door! ");
            System.out.println("2: ****");
        } else if(result.getOnderdeel2() <= 10 && result.getOnderdeel2() >= 8){
            vv1_Context.put("onderdeel2", "3p.png");
            vv1_Context.put("onderdeel2Text", "Volgens de gegeven feedback gaat verantwoordelijkheid afleggen je goed af, je houdt je ouders, leraren en mentor voldoende op de hoogte van je behaalde resultaten en dagelijkse bezigheden op school. je komt je afspraken ook bijna altijd na. je bent dus goed bezig met het afleggen van je verantwoordelijkheden, je zou dit nog iets kunnen verbeteren maar het is zeker al voldoende.");
            System.out.println("2: ***");
        }else if(result.getOnderdeel2() <= 7 && result.getOnderdeel2() >= 5){
            vv1_Context.put("onderdeel2", "2p.png");
            vv1_Context.put("onderdeel2Text", "Volgens de gegeven feedback gaat verantwoordelijkheid afleggen je niet goed tot normaal af, je komt je afspraken soms na en houd je mentor, leraren en ouders soms op de hoogte van je behaalde resultaten en dagelijkse bezigheden op school. Je bent dus af en toe bezig met verantwoordelijkheid afleggen maar het is nog geen voldoende. Dit is niet goed genoeg dus je moet er nog wat aan gaan werken.");
            System.out.println("2: **");
        } else if(result.getOnderdeel2() <= 4 && result.getOnderdeel2() >= 3){
            vv1_Context.put("onderdeel2", "1p.png");
            vv1_Context.put("onderdeel2Text", "Volgens de gegeven feedback gaat verantwoordelijkheid afleggen je slecht af, je komt je afspraken nooit na en houd je mentor,leraren en ouders nooit op de hoogte van je behaalde resultaten en dagelijkse bezigheden op school. Dus met verantwoordelijkheid afleggen ben je niet goed mee bezig en is het geen voldoende en dus moet je er harder aan gaan werken.");
            System.out.println("2: *");
        }else {
            System.out.println("2: onder 0");
        }
        //result part3
        if(result.getOnderdeel3() <= 12 && result.getOnderdeel3() >= 11){
            vv1_Context.put("onderdeel3", "4p.png");
            vv1_Context.put("onderdeel3Text", "Volgens de gegeven feedback neem je heel veel initiatief. Je bent er erg goed in zelfstandig werken en pakt taken ook uit jezelf op. Vaak weet je ook precies wat er nodig is om een taak af te ronden. Kortom, met initiatief nemen ben jij uitstekend bezig! ga zo door!");
            System.out.println("3: ****");
        } else if(result.getOnderdeel3() <= 10 && result.getOnderdeel3() >= 8){
            vv1_Context.put("onderdeel3", "3p.png");
            vv1_Context.put("onderdeel3Text", "Volgens de gegeven feedback neem je genoeg initiatief, Je kunt meestal goed zelfstandig werken en neemt taken soms ook al uit jezelf op je. je bent met initiatief nemen goed bezig, je kunt hier en daar nog wat verbeteren maar het is zeker niet slecht.");
            System.out.println("3: ***");
        }else if(result.getOnderdeel3() <= 7 && result.getOnderdeel3() >= 5){
            vv1_Context.put("onderdeel3", "2p.png");
            vv1_Context.put("onderdeel3Text", "Volgens de gegeven feedback neem je af en toe initiatief. je zou dit kunnen verbeteren door vaker zelfstandig aan het werk te gaan en vaker taken op je te nemen in plaats van te wachten tot je een taak krijgt. Probeer iets meer initiatief te nemen voor de volgende feedback sessie.");
            System.out.println("3: **");
        } else if(result.getOnderdeel3() <= 4 && result.getOnderdeel3() >= 3){
            vv1_Context.put("onderdeel3", "1p.png");
            vv1_Context.put("onderdeel3Text", "Volgens de gegeven feedback toon je haast geen initiatief. Probeer dit voor het volgende feedback moment te verbeteren door zelfstandiger te werken en niet af te wachten tot je taken krijgt maar zelf taken op je neemt. Je zult dit punt echt moeten gaan verbeteren komende tijd.");
            System.out.println("3: *");
        }else {
            System.out.println("3: onder 0");
        }
        
        
        
        
        
    }
    
    
    
    //deletes the invite after the form has been filled in
    public int insertResultaat(String eigenNummer, String targetNummer, int onderdeel1, int onderdeel2, int onderdeel3, VelocityContext context){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String insert = "INSERT INTO pad.vraag VALUES('" + eigenNummer + "','" + targetNummer + "','" + sdf.format(date).toString() + "','" + onderdeel1 + "','" + onderdeel2 + "','" + onderdeel3 + "')";
        String deleteUitnodiging = "DELETE FROM uitnodiging WHERE Leerling_Leerlingnr ='" + targetNummer + "' AND Targetted_Leerlingnr ='" + eigenNummer + "'";
        db.doInsert(deleteUitnodiging);
        return db.doInsert(insert);

        
    }
    
   
    
    
    
    //sends the invitation
    public int invitationList(String Leerlingnummer, HttpServletRequest request, VelocityContext vv1_Context) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String targettedLeerlingnr = request.getParameter("userSelect");
        if(targettedLeerlingnr != null){
            //checks if the invitation already exists
            String inviteCheck = "SELECT * FROM uitnodiging WHERE Leerling_Leerlingnr = '" + Leerlingnummer + "' AND Targetted_Leerlingnr = '" + targettedLeerlingnr + "'";
            rs = db.doQuery(inviteCheck);
            try {
                //if the invitation does not exist, add it to the database
                if(!rs.next()){
                    String sendInvite = "INSERT INTO uitnodiging VALUES('" + Leerlingnummer + "','" + targettedLeerlingnr + "','" + sdf.format(date).toString() + "')";
                    //record added
                    int result = db.doInsert(sendInvite);
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
        } else {
            return 0;
        }
    }
    //another check to see if the invite has already been processed
    //and cannot be repeated until deleted
    public int invitationList2(String Leerlingnummer, HttpServletRequest request) {
        String targettedLeerlingnr = request.getParameter("userSelect");
        System.out.println(Leerlingnummer);
        System.out.println(targettedLeerlingnr);
        String inviteCheck = "SELECT * FROM vraag WHERE Leerling_Leerlingnr = '" + Leerlingnummer + "' AND Targetted_Leerlingnr = '" + targettedLeerlingnr + "'";
        rs = db.doQuery(inviteCheck);
        try {
            if(rs.next()){
                return 0;
            } else {
                return 1;
            }
        } catch (SQLException e){
            return 1;
        }
        
    }
    
    
    //first name 
    public String getFeedbackVoornaam(String leerlingnummer){
        String getNaam = "SELECT voornaam, tussenvoegsel, achternaam FROM leerling where Leerlingnr = '" + leerlingnummer + "'";
        rs = db.doQuery(getNaam);
        try {
            if(rs.next()){
                String naam = rs.getString("voornaam");
                       // rs.getString("tussenvoegsel") +
                       // rs.getString("achternaam");
                return naam;
                
            } else {
                return "";
            }
        } catch (SQLException ex) {
            Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex);
            return "";
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
        vv1_Context.put("klas", "Leerlingen in klas: " + klas);
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
