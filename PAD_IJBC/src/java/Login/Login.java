/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import connectivity.DbManager;
import connectivity.QueryManager;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alex
 */
public class Login {
    String username;
    String password;
    QueryManager Qm;
    DbManager db;
    public Login(String username, String password){
        db = new DbManager();
        Qm = new QueryManager();
        this.username = username;
        this.password = password;
    }
    
    public boolean login(String username, String password){
        
        if(Qm.login(username, password) == true){
            return true;
        }
        else{
            return false;
        }
    }
    
    
}
