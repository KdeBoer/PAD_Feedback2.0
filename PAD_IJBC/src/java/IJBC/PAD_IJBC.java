/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IJBC;

import Leerling.Leerling;
import Login.Login;
import java.io.IOException;
import java.io.PrintWriter;
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
    private Object out2;
    
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
        
        
        //out.println("<link rel='stylesheet' type='text/css' href='" + request.getContextPath() +  "/CSS/style.css' />");
        
       
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
                }
                else{
                    s_Template = "Login.vsl";
                    loginFailed = "Ingevulde gegevens kloppen niet!";
                    vv1_Context.put("loginFailed", loginFailed);
                    //vv1_Context.put(username, out2);
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
        } else if(s_Request.equals("/PAD_IJBC/RequestUitnodigingen")){
            s_Template = "uitnodigingen.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
        } else if(s_Request.equals("/PAD_IJBC/RequestInstellingen")){
            s_Template = "instellingenPagina.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
        }  
        //requests feedback page 1
        else if(s_Request.equals("/PAD_IJBC/requestFeedbackpage")){
            s_Template = "index.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
        } 
        //requests feedback page 2 
        else if(s_Request.equals("/PAD_IJBC/requestFeedbackpage2")){
            s_Template = "index2.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
        } 
        //requests feedback page 3
        else if(s_Request.equals("/PAD_IJBC/requestFeedbackpage3")){
            s_Template = "index3.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
        } 
        //requests feedback validating
        else if(s_Request.equals("/PAD_IJBC/requestSendFeedback")){
            //insert feedback check here!
            //if succesfull
            s_Template = "feedbackSuccesfull.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);
            
            //insert error message if not succesfull
            
        }
        //request to return to the home page
        else if(s_Request.equals("/PAD_IJBC/requestHomepage")){
            s_Template = "keuzePagina.vsl";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);            
        }
        
        //request to return to the home page
        else if(s_Request.equals("/PAD_IJBC/requestInvite")){
            //if the invite has been sent
            s_Template = "";
            HttpSession Session = request.getSession();
            String username = (String) Session.getAttribute("username");
            vv1_Context.put("username", username);            
            
            //error if the invite has not been send
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
                String klas = request.getParameter("klas");
                String email = request.getParameter("email");
                String wachtwoord = request.getParameter("wachtwoord");
                String herhaalWachtwoord = request.getParameter("herhaalwachtwoord");
                if(wachtwoord.equals(herhaalWachtwoord)){
                    s_Template = "register.vsl";
                    Leerling leerling = new Leerling(naam, tussenvoegsel, achternaam, leerlingnummer, klas, email, wachtwoord);
                    
                    //if the method returns no values, continue
                    if(leerling.registerCheck(naam, tussenvoegsel, achternaam, leerlingnummer, klas, email, wachtwoord).equals("")){
                        if(leerling.register(naam, tussenvoegsel, achternaam, leerlingnummer, klas, email, wachtwoord) == true)
                        {    
                            s_Template = "Login.vsl";
                            vv1_Context.put("loginFailed", "Account succesvol aangemaakt!");
                        } else {
                            vv1_Context.put("loginFailed", "Account niet aangemaakt!");
                        }
                    } else {
                        vv1_Context.put("errorRegister", "Dit leerlingnummer bestaat al!");
                        s_Template = "register.vsl";
                    }
                } 
                //if the passwords don't match
                else{
                    vv1_Context.put("errorWachtwoord", "Wachtwoorden komen niet overeen!</br>");
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
        
        PrintWriter out2 = response.getWriter();
        
        
        if(template != null){
            template.merge(vv1_Context, out2);
        }
        out2.close();
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
