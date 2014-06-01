/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vragen;

/**
 *
 * @author Alex
 */
public class Vragen {

    double Onderdeel1;
    double Onderdeel2;
    double Onderdeel3;

    public Vragen(double Onderdeel1, double onderdeel2, double onderdeel3){
        this.Onderdeel1 = Onderdeel1;
        this.Onderdeel2 = Onderdeel2;
        this.Onderdeel3 = Onderdeel3;
    }
    
    public double getOnderdeel1() {
        return Onderdeel1;
    }

    public double getOnderdeel2() {
        return Onderdeel2;
    }

    public double getOnderdeel3() {
        return Onderdeel3;
    }
    public void setOnderdeel1(double Onderdeel1) {
        this.Onderdeel1 = Onderdeel1;
    }

    public void setOnderdeel2(double Onderdeel2) {
        this.Onderdeel2 = Onderdeel2;
    }

    public void setOnderdeel3(double Onderdeel3) {
        this.Onderdeel3 = Onderdeel3;
    }

}
