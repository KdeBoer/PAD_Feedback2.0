/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Leerling;

/**
 *
 * @author Alex
 */
public class Leerling {
    private String Naam;
    private String Tussenvoegsel;
    private String Achternaam;
    private String Leerlingnummer;
    private String Klas;
    private String Email;
    private String Wachtwoord;

    public Leerling(String Naam, String Tussenvoegsel, String Achternaam, String Leerlingnummer, String Klas, String Email, String Wachtwoord) {
        this.Naam = Naam;
        this.Tussenvoegsel = Tussenvoegsel;
        this.Achternaam = Achternaam;
        this.Leerlingnummer = Leerlingnummer;
        this.Klas = Klas;
        this.Email = Email;
        this.Wachtwoord = Wachtwoord;
    }

    
    
    
    
    public String getNaam() {
        return Naam;
    }

    public void setNaam(String Naam) {
        this.Naam = Naam;
    }

    public String getTussenvoegsel() {
        return Tussenvoegsel;
    }

    public void setTussenvoegsel(String Tussenvoegsel) {
        this.Tussenvoegsel = Tussenvoegsel;
    }

    public String getAchternaam() {
        return Achternaam;
    }

    public void setAchternaam(String Achternaam) {
        this.Achternaam = Achternaam;
    }

    public String getLeerlingnummer() {
        return Leerlingnummer;
    }

    public void setLeerlingnummer(String Leerlingnummer) {
        this.Leerlingnummer = Leerlingnummer;
    }

    public String getKlas() {
        return Klas;
    }

    public void setKlas(String Klas) {
        this.Klas = Klas;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

}
