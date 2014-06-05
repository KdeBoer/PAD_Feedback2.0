/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IJBC;

import Leerling.Leerling;
import Login.Login;
import Vragen.Vragen;
import connectivity.QueryManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 *
 * @author Alex
 */
public class PAD_IJBC extends HttpServlet {

    private static final String VELOCITY_TEMPLATES_PREFIX = "/vsl";
    private Object out;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	
        Properties _Properties = new Properties();

        _Properties.setProperty("resource.loader", "webapp");
        _Properties.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
        _Properties.setProperty("webapp.resource.loader.path", VELOCITY_TEMPLATES_PREFIX);

        ServletContext _ServletContext = getServletContext();
        Velocity.setApplicationAttribute("javax.servlet.ServletContext", _ServletContext);
        try {
            Velocity.init(_Properties);
        } catch (Exception ex) {
            Logger.getLogger(PAD_IJBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            
            
            
            
            
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        VelocityContext vv1_Context = new VelocityContext();
        
        
        
        
       
        String s_RequestURL = request.getRequestURL().toString();
        String s_RequestURI = request.getRequestURI();
        String s_ContextPath = request.getContextPath();

        String s_Request_Prefix = s_ContextPath;
        String s_Request = s_RequestURI.substring(s_Request_Prefix.length(), s_RequestURI.length());
        if (!s_Request.startsWith("/")) {
            s_Request = "/" + s_Request;
        }
        
        System.out.println("Servlet Context: " + s_ContextPath + " request URL: " + s_RequestURL + " URI: " + s_RequestURI + " Request: " + s_Request);
        vv1_Context.put("action", s_Request);

        String s_Template = "";
        String loginFailed = "";
        String registerFailed = "";
        
        //sets the initial error messages to 0
        vv1_Context.put("errorRegister", "");
        vv1_Context.put("loginFailed", "");
        vv1_Context.put("errorEmail", "");
        vv1_Context.put("errorKlas", "");
        vv1_Context.put("errorVoornaam", "");
        vv1_Context.put("errorAchternaam", "");
        vv1_Context.put("errorLeerlingnummer", "");
        vv1_Context.put("errorSendInvite","");
        vv1_Context.put("sendInviteSuccess","");
        
        
        //sets the initial register values to 0
        vv1_Context.put("voornaam", "");
        vv1_Context.put("tussenvoegsel", "");
        vv1_Context.put("achternaam", "");
        vv1_Context.put("klas", "");
        vv1_Context.put("leerlingnummer", "");
        vv1_Context.put("email", "");
        vv1_Context.put("vraag1Error", "");
        vv1_Context.put("vraag2Error", "");
        vv1_Context.put("vraag3Error", "");
        vv1_Context.put("feedbackError", "");
        vv1_Context.put("errorInvite", "");
        
        //sets the initial values 
        vv1_Context.put("targettedLeerling","");
        
        QueryManager qm = new QueryManager();
        
        vv1_Context.put("errorWachtwoord", "");
        if (s_Request.equals("/PAD_IJBC/REQ_LOGIN")) {
            s_Template = "Login.vsl";
        }        
        else if (s_Request.equals("/PAD_IJBC/LOGIN")) {        
            String username = request.getParameter("gebruikersnaam");
            String password = request.getParameter("wachtwoord");
            if (username == null || username.isEmpty()){
                loginFailed = "Geen gebruikersnaam ingevoerd!";
                s_Template = "Login.vsl";
                vv1_Context.put("loginFailed", loginFailed);
            } else if (password == null || password.isEmpty()){
                loginFailed = "Geen wachtwoord ingevoerd!";
                vv1_Context.put("loginFailed", loginFailed);
                s_Template = "Login.vsl";
            } 
            else if (username != null && password != null){
                Login login = new Login(username, password);
                if(login.login(username, password) == true){
                    s_Template = "keuzePagina.vsl";
                    vv1_Context.put("username", username);
                    HttpSession Session = request.getSession();
                    Session.setAttribute("username", username);
                    
                    
                    String leerlingnr = qm.leerlingNummer(username, password);
                    HttpSession leerlingNummer = request.getSession();
                    leerlingNummer.setAttribute("leerlingnummer", leerlingnr);

                }
                else{
                    s_Template = "Login.vsl";
                    loginFailed = "Ingevulde gegevens kloppen niet!";
                    vv1_Context.put("loginFailed", loginFailed);
                }
                
            }else{
                s_Template = "Login.vsl";
                loginFailed = "Ingevulde gegevens kloppen niet!";
                vv1_Context.put("loginFailed", loginFailed);
            }
        }
        //standard checks if other pages are called
        else if(s_Request.equals("/PAD_IJBC/RequestFeedback")) {
            s_Template = "feedbackVragen.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);  
            
            
            
        } 
        
        
        
        
        else if(s_Request.equals("/PAD_IJBC/requestFeedbackpageSelf")) {
            s_Template = "vDragen.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);  
            
            
            vv1_Context.put("feedbackVoor", "Jezelf!");
            
            
        }
        
        
        
        
        
        else if(s_Request.equals("/PAD_IJBC/requestInvite")){
                s_Template = "uitnodigingen.vsl";
                HttpSession Session = request.getSession();
                String username = (String) Session.getAttribute("username");
                vv1_Context.put("username", username); 
                
                HttpSession leerlingNummer = request.getSession();
                String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
                vv1_Context.put("leerlingnummer", leerlingnr);
                qm.invitationList(leerlingnr, request, vv1_Context);
                
        } else if(s_Request.equals("/PAD_IJBC/RequestUitnodigingen")){
            s_Template = "uitnodigingenBekijken.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
            qm.getInviteList(vv1_Context, leerlingnr, request); 
            
            
        }  else if(s_Request.equals("/PAD_IJBC/processInvite")){
            //checks if something is selected
            s_Template = "vDragen.vsl";
            
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
            

            //no user selected!
            if(request.getParameter("userSelect") == null){
                s_Template = "uitnodigingenBekijken.vsl";
                qm.getInviteList(vv1_Context, leerlingnr, request); 
                vv1_Context.put("errorInvite", "Geen leerling geselecteerd!");
            } else {
                String feedbackVoor = qm.getFeedbackVoornaam(request.getParameter("userSelect"));
                vv1_Context.put("feedbackVoor", feedbackVoor);
                HttpSession feedbackVoorSession = request.getSession();
                feedbackVoorSession.setAttribute("feedbackVoor", feedbackVoor);
            }
            
            
            HttpSession targettedLeerlingNummer = request.getSession();
            targettedLeerlingNummer.setAttribute("targettedNummer", request.getParameter("userSelect"));
            


            
            
            
        //resultaat onderdeel 1
        } else if(s_Request.equals("/PAD_IJBC/vDragen1")){
            
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            

            HttpSession feedbackVoorSession = request.getSession();
            String feedbackVoorLeerling = (String) feedbackVoorSession.getAttribute("feedbackVoor");
            vv1_Context.put("feedbackVoor", feedbackVoorLeerling);           
                        
            if(request.getParameter("vraag1") == null || request.getParameter("vraag2") == null || request.getParameter("vraag3") == null || request.getParameter("vraag4") == null || request.getParameter("vraag5") == null){
                s_Template = "vDragen.vsl";    
                vv1_Context.put("vraag1Error", "Niet alle antwoorden zijn ingevuld!");
            } else {
                int vraag1Antwoord = Integer.parseInt(request.getParameter("vraag1"));
                int vraag2Antwoord = Integer.parseInt(request.getParameter("vraag2"));
                int vraag3Antwoord = Integer.parseInt(request.getParameter("vraag3"));
                int vraag4Antwoord = Integer.parseInt(request.getParameter("vraag4"));
                int vraag5Antwoord = Integer.parseInt(request.getParameter("vraag5"));
                int resultaatOnderdeel1 = vraag1Antwoord + vraag2Antwoord + vraag3Antwoord + vraag4Antwoord + vraag5Antwoord;
                
                s_Template = "vAfleggen.vsl";
                HttpSession resultaatOnderdeel1Session = request.getSession();
                resultaatOnderdeel1Session.setAttribute("resultaat1", resultaatOnderdeel1);
            }
        } 
        //resultaat onderdeel 2
        else if(s_Request.equals("/PAD_IJBC/vAfleggen1")){
            
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            HttpSession feedbackVoorSession = request.getSession();
            String feedbackVoorLeerling = (String) feedbackVoorSession.getAttribute("feedbackVoor");
            vv1_Context.put("feedbackVoor", feedbackVoorLeerling);           
                        
            if(request.getParameter("vraag1") == null || request.getParameter("vraag2") == null || request.getParameter("vraag3") == null){
                s_Template = "vAfleggen.vsl";    
                vv1_Context.put("vraag2Error", "Niet alle antwoorden zijn ingevuld!");
            } else {
                int vraag1Antwoord = Integer.parseInt(request.getParameter("vraag1"));
                int vraag2Antwoord = Integer.parseInt(request.getParameter("vraag2"));
                int vraag3Antwoord = Integer.parseInt(request.getParameter("vraag3"));
                int resultaatOnderdeel2 = vraag1Antwoord + vraag2Antwoord + vraag3Antwoord;
                System.out.println(resultaatOnderdeel2);
                s_Template = "iNemen.vsl";
                HttpSession resultaatOnderdeel2Session = request.getSession();
                resultaatOnderdeel2Session.setAttribute("resultaat2", resultaatOnderdeel2);

                
            }
        } 
        //resultaat onderdeel 3
        else if(s_Request.equals("/PAD_IJBC/iNemen")){
            
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            HttpSession feedbackVoorSession = request.getSession();
            String feedbackVoorLeerling = (String) feedbackVoorSession.getAttribute("feedbackVoor");
            vv1_Context.put("feedbackVoor", feedbackVoorLeerling);           
                        
            if(request.getParameter("vraag1") == null || request.getParameter("vraag2") == null || request.getParameter("vraag3") == null){
                s_Template = "iNemen.vsl";    
                vv1_Context.put("vraag3Error", "Niet alle antwoorden zijn ingevuld!");
            } else {
                int vraag1Antwoord = Integer.parseInt(request.getParameter("vraag1"));
                int vraag2Antwoord = Integer.parseInt(request.getParameter("vraag2"));
                int vraag3Antwoord = Integer.parseInt(request.getParameter("vraag3"));
                int resultaatOnderdeel3 = vraag1Antwoord + vraag2Antwoord + vraag3Antwoord;
                System.out.println(resultaatOnderdeel3);

                
                
                
                HttpSession resultaatOnderdeel1Session = request.getSession();
                int resultaatOnderdeel1 = (int) resultaatOnderdeel1Session.getAttribute("resultaat1");

                HttpSession resultaatOnderdeel2Session = request.getSession();
                int resultaatOnderdeel2 = (int) resultaatOnderdeel2Session.getAttribute("resultaat2");
                
                HttpSession resultaatOnderdeel3Session = request.getSession();
                resultaatOnderdeel3Session.setAttribute("resultaat3", resultaatOnderdeel3);
                
                
                HttpSession leerlingNummer = request.getSession();
                String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
                vv1_Context.put("leerlingnummer", leerlingnr);
                
                //gets the number of the student you give feedback to
                
                HttpSession targettedLeerlingNummer = request.getSession();
                String llnummer = (String) targettedLeerlingNummer.getAttribute("targettedNummer");

                if(qm.insertResultaat(llnummer, leerlingnr, resultaatOnderdeel1, resultaatOnderdeel2, resultaatOnderdeel3, vv1_Context) == 1){
                    s_Template = "feedbackSuccesfull.vsl";
                } else {
                    s_Template = "iNemen.vsl";
                    vv1_Context.put("vraag3Error", "Feedback niet verstuurd!");
                }
            
                
                
                
                
                
            }
        } else if(s_Request.equals("/PAD_IJBC/searchKlas")){
                
                String klas = request.getParameter("selecteerKlas");
                try{qm.getLeerlingList(klas, vv1_Context);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }
                s_Template = "uitnodigingen.vsl";
                HttpSession Session = request.getSession();
                String username = (String) Session.getAttribute("username");
                vv1_Context.put("username", username);
                
                
                
                HttpSession leerlingNummer = request.getSession();
                String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
                vv1_Context.put("leerlingnummer", leerlingnr);
                
                
            }else if(s_Request.equals("/PAD_IJBC/RequestInstellingen")){
                s_Template = "instellingenPagina.vsl";
                HttpSession Session = request.getSession();
                String username = (String) Session.getAttribute("username");
                vv1_Context.put("username", username);
                
                
                HttpSession leerlingNummer = request.getSession();
                String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
                vv1_Context.put("leerlingnummer", leerlingnr);
                
                
        }  
       
        else if(s_Request.equals("/PAD_IJBC/requestFeedbackpage")){
            s_Template = "uitnodigingen.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
        } 
        //requests feedback page 2 
        else if(s_Request.equals("/PAD_IJBC/requestFeedbackpage2")){
            s_Template = "index2.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
        } 
        //requests feedback page 3
        else if(s_Request.equals("/PAD_IJBC/requestFeedbackpage3")){
            s_Template = "index3.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
        } 
        //requests feedback validating
        else if(s_Request.equals("/PAD_IJBC/requestSendFeedback")){
            //insert feedback check here!
            //if succesfull
            s_Template = "feedbackSuccesfull.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
            
            //insert error message if not succesfull
            
        }
        //request to return to the home page
        else if(s_Request.equals("/PAD_IJBC/requestHomepage")){
            s_Template = "keuzePagina.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);   
            
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
        } //else if(s_Request.equals("/PAD_IJBC/RequestResultaat")){
           // s_Template = "resultatenPagina.vsl";
           // HttpSession Session = request.getSession();
           // String username = (String) Session.getAttribute("username");
           // vv1_Context.put("username", username);  
        //}
        
        else if(s_Request.equals("/PAD_IJBC/RequestResultaat")){
            s_Template = "resultatenPagina.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            HttpSession onderdeel1Session = request.getSession();
            String onderdeel1 = (String) onderdeel1Session.getAttribute("onderdeel1");
            HttpSession onderdeel2Session = request.getSession();
            String onderdeel2 = (String) onderdeel1Session.getAttribute("onderdeel2");
            HttpSession onderdeel3Session = request.getSession();
            String onderdeel3 = (String) onderdeel1Session.getAttribute("onderdeel3");
            
            
            
            //checks if someone already filled in a feedback form for the logged in user
            if(qm.feedbackCheck(leerlingnr) == true) {
            //gets the average results
                qm.feedbackGemiddelde(leerlingnr, vv1_Context);
                s_Template = "resultatenPagina.vsl";
                System.out.println("Records found!");
            } else {
                System.out.println("No records found");
                s_Template = "keuzePagina.vsl";
                vv1_Context.put("feedbackError", "Niemand heeft je nog feedback gegeven!");
            }
            qm.feedbackGemiddelde(leerlingnr, vv1_Context);
            //vv1_Context.put("onderdeel1", onderdeel1);
                
            

            
            
            vv1_Context.put("username", username);              
        }
        
        else if(s_Request.equals("/PAD_IJBC/categorie01")){
            s_Template = "categorie1.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
        } 
        
        else if(s_Request.equals("/PAD_IJBC/categorie02")){
            s_Template = "categorie2.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
        } 
        
        else if(s_Request.equals("/PAD_IJBC/categorie03")){
            s_Template = "categorie3.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            
            HttpSession leerlingNummer = request.getSession();
            String leerlingnr = (String) leerlingNummer.getAttribute("leerlingnummer");
            vv1_Context.put("leerlingnummer", leerlingnr);
        } 
        

        //request to go to the register page
        else if(s_Request.equals("/PAD_IJBC/REGISTER")){
            s_Template = "register.vsl";
        }
        else if(s_Request.equals("/PAD_IJBC/registerLeerling")){
            
            
                String naam = request.getParameter("voornaam");
                String tussenvoegsel = request.getParameter("tussenvoegsel");
                String achternaam = request.getParameter("achternaam");
                String leerlingnummer = request.getParameter("leerlingnummer");
                String klas = request.getParameter("selecteerNiveau");
                String email = request.getParameter("email");
                String wachtwoord = request.getParameter("wachtwoord");
                String herhaalWachtwoord = request.getParameter("herhaalwachtwoord");
                
                //register input checks
                if(naam.length() == 0){
                    vv1_Context.put("errorVoornaam", "Naam niet ingevoerd!</br>");
                    saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);
                    
                    s_Template = "register.vsl";
                }
                else if(achternaam.length() == 0){
                    vv1_Context.put("errorAchternaam", "Achternaam niet ingevoerd!</br>");
                    saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);
                    s_Template = "register.vsl";
                }
                else if(leerlingnummer.length() == 0){
                    vv1_Context.put("errorLeerlingnummer", "Leerlingnummer niet ingevoerd!</br>");
                    saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);
                    s_Template = "register.vsl";
                }
                else if(klas.length() == 0){
                    vv1_Context.put("errorKlas", "Klas niet ingevoerd!!</br>");
                    saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);
                    s_Template = "register.vsl";
                }
                else if(email.length() == 0){
                    vv1_Context.put("errorEmail", "E-mailadres niet ingevoerd!</br>");
                    saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);
                    s_Template = "register.vsl";
                }
                                                                                                
                //password length requirements check
                else if(wachtwoord.length() < 6 || herhaalWachtwoord.length() < 6){
                    vv1_Context.put("errorWachtwoord", "Wachtwoord moet minimaal 6 tekens bevatten!</br>");
                    saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);
                    s_Template = "register.vsl";
                }
                else if(wachtwoord.length() > 20 || herhaalWachtwoord.length() > 20){
                    vv1_Context.put("errorWachtwoord", "Wachtwoord mag maximaal 20 tekens bevatten!</br>");
                    saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);                    
                    s_Template = "register.vsl";
                }
                else if(wachtwoord.equals(herhaalWachtwoord)){
                    s_Template = "register.vsl";
                    //creates a new "leerling" object 
                    Leerling leerling = new Leerling(naam, tussenvoegsel, achternaam, leerlingnummer, klas, email, wachtwoord);
                    
                    //checks if the filled in studentnumber already exists
                    if(leerling.registerCheck(naam, tussenvoegsel, achternaam, leerlingnummer, klas, email, wachtwoord).equals("")){
                        //if the boolean returns true, the student has been added
                        if(leerling.register(naam, tussenvoegsel, achternaam, leerlingnummer, klas, email, wachtwoord) == true)
                        {    
                            s_Template = "Login.vsl";
                            vv1_Context.put("loginFailed", "Account succesvol aangemaakt!");
                        } else {
                            vv1_Context.put("errorRegister", "Account niet aangemaakt!");
                            s_Template = "register.vsl";
                        }
                    } else {
                        vv1_Context.put("errorLeerlingnummer", "Dit leerlingnummer bestaat al!<br>");
                        saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);
                        s_Template = "register.vsl";
                    }
                } 
                //if the passwords don't match
                else{
                    vv1_Context.put("errorWachtwoord", "Wachtwoorden komen niet overeen!</br>");
                    saveValues(vv1_Context, naam, tussenvoegsel, achternaam, leerlingnummer, klas, email);
                    s_Template = "register.vsl";
                }
            
            
        } 
        
        

        
        
        
        
        
        
        
        
        
        
        
        
        Template template = null;

        try {

            template = Velocity.getTemplate(s_Template);
        } catch (ResourceNotFoundException rnfe) {
            Logger.getLogger(PAD_IJBC.class.getName()).log(Level.SEVERE, null, rnfe);
            // couldn't find the template
        } catch (ParseErrorException pee) {
            Logger.getLogger(PAD_IJBC.class.getName()).log(Level.SEVERE, null, pee);
            // syntax error: problem parsing the template
        } catch (MethodInvocationException mie) {
            Logger.getLogger(PAD_IJBC.class.getName()).log(Level.SEVERE, null, mie);
            // something invoked in the template
            // threw an exception
        } catch (Exception e) {
            Logger.getLogger(PAD_IJBC.class.getName()).log(Level.SEVERE, null, e);
        }
        
        PrintWriter out = response.getWriter();
        
        
        if(template != null){
            template.merge(vv1_Context, out);
        }
        out.close();
    }
    //resets the register values after an error
    public void saveValues(VelocityContext vv1_Context, String voornaam, String tussenvoegsel, String achternaam, String nummer, String klas, String email){
        vv1_Context.put("voornaam",voornaam);
        vv1_Context.put("tussenvoegsel",tussenvoegsel);
        vv1_Context.put("achternaam",achternaam);
        vv1_Context.put("leerlingnummer",nummer);
        vv1_Context.put("klas",klas);
        vv1_Context.put("email",email);
    }
    
    
    

   

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
