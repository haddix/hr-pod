package smmac.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import smmac.security.EzSecurityClient;


/**
 *
 * @author haddixjc
 */
public class EzSecurityServlet extends HttpServlet {
    
    public final static String COLLECTION_NAME = "smartdata";
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(EzSecurityServlet.class);


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      doPost(request, response);
   }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
                        
        response.setHeader("Content-Type", "application/json");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        
        PrintWriter out = response.getWriter();
        
        JSONObject securityJSON = new JSONObject();
        JSONObject userJSON = new JSONObject();
        try {                        
            EzSecurityClient ezsecurity = new EzSecurityClient().getInstance();
            userJSON.put("dn", ezsecurity.getUserDn());
            userJSON.put("cn", ezsecurity.getUserName());
            userJSON.put("org", ezsecurity.getUserOrganization());
            userJSON.put("clearance", ezsecurity.getClearance());
            securityJSON.put("user", userJSON);
            out.println(securityJSON);
        } catch (Exception e) {
            securityJSON.append("status", "in 1st exception");
            String msg = e.getMessage();
            securityJSON.append("status", msg);
            e.printStackTrace();
            try {
                securityJSON.append("status", "in 2nd try");                
                out.println(securityJSON);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        finally{
            out.close();
        }
 
    }
}