/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IJBC;

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
        
        if (s_Request.equals("/PAD_IJBC/REQ_LOGIN")) {
            s_Template = "Login.vsl";
        }        
        else if (s_Request.equals("/PAD_IJBC/LOGIN")) {        
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println(username);
            System.out.println(password);
            if (username == null || username.isEmpty()){
                    System.out.println("Please type in your username");
                    s_Template = "Login.vsl";
            }
            else if (password == null || password.isEmpty()){
                    System.out.println("Please type in your password");
                    s_Template = "Login.vsl";
            }
            else if (username != null && password != null){
                Login login = new Login(username, password);
                if(login.login(username, password) == true){
                    s_Template = "keuzePagina.vsl";
                    vv1_Context.put("username", username);
                }
                else{
                    s_Template = "Login.vsl";
                }
                /*
                 * Do here some exception or error catching
                 */
                /*
                 * If no error has occured, the login has been successful.
                 * In that case we return a new velocity template, like Login_OK.vsl
                 */
                
            }else{
                
            }
        }
        else if (s_Request.equals("/PAD_IJBC/NEW_ACTION")) {
            /*
             * Implement here some code and get a new velocity template like NEW_ACTION.vsl
             */
                s_Template = "NEW_ACTION.vsl";
        }

        Template template = null;
         //vv1_Context.put("errorCode", 1234);
         //vv1_Context.put("errorCode", "Dit wordt meegegeven aan velocity");

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
