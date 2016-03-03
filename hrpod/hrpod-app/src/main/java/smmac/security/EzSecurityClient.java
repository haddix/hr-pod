package smmac.security;

import ezbake.base.thrift.EzSecurityTokenException;
import ezbake.configuration.EzConfiguration;
import ezbake.profile.EzProfile;
import ezbake.profile.UserProfile;
import ezbake.security.client.EzbakeSecurityClient;
import ezbake.profile.ezprofileConstants;
import ezbake.thrift.ThriftClientPool;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

public class EzSecurityClient{
    
    static EzSecurityClient instance;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(EzSecurityClient.class);
    
    private static ThriftClientPool clientPool;
    private static String ezSecurityProjectName = "EzBake"; //TODO (haddix): pull dias project name from prop file
    private EzProfile.Client profileClient = null;
    private EzbakeSecurityClient securityClient;        
    private UserProfile userProfile = null;
    private long resultsHitCount = 0;
    
    /**
     * DIAS is inaccessible error message.
     */

    public EzSecurityClient() {        
        createClient();
    }
    
    void createClient() {
        try {
            EzConfiguration configuration = new EzConfiguration();
            logger.info("in createClient, configuration: {}", configuration.getProperties());
            
            securityClient = new EzbakeSecurityClient(configuration.getProperties());
            clientPool = new ThriftClientPool(configuration.getProperties());
            profileClient = clientPool.getClient(ezprofileConstants.SERVICE_NAME, EzProfile.Client.class);
            userProfile = profileClient.getUserProfile(securityClient.fetchAppToken(ezprofileConstants.SERVICE_NAME), securityClient.fetchTokenForProxiedUser().getUserId());
   
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static EzSecurityClient getInstance() {
        if (instance == null) instance = new EzSecurityClient();
        return instance;
    }
        
    
    public String getUserName(){        
        String userName = null;
        try {
            userName = securityClient.fetchTokenForProxiedUser().getUsername();
        } catch (EzSecurityTokenException e) {
            e.printStackTrace();
        }            
        return userName;
    }
    
    public String getUserDn(){        
        String userDn = null;
        try {
            userDn = securityClient.fetchTokenForProxiedUser().getUserId();
        } catch (EzSecurityTokenException e) {
            e.printStackTrace();
        }            
        return userDn;
    }
    
    public String getUserOrganization(){        
        String userOrg = null;
        try {
            userOrg = securityClient.fetchTokenForProxiedUser().getOrganization();
        } catch (EzSecurityTokenException e) {
            e.printStackTrace();
        }
        return userOrg;
    }

    
    public String getClearance(){
        String clearance = null;
        try {
            clearance = securityClient.fetchTokenForProxiedUser().getAuthorizationLevel();
        } catch (EzSecurityTokenException e) {
            e.printStackTrace();
        }
        return clearance;
    }
    
    public List<String> getFormalAccess() {
        List<String> authList = new ArrayList();
        try {
            authList.addAll(securityClient.fetchTokenForProxiedUser().getAuthorizations().getFormalAuthorizations());            
        } catch (EzSecurityTokenException e) {
            e.printStackTrace();
        }
        return authList;
    }
    
    public String getCitizenship() {
        String citizenship = null;
        try {
            citizenship = securityClient.fetchTokenForProxiedUser().getCitizenship();
        } catch (EzSecurityTokenException e) {
            e.printStackTrace();
        }
        return citizenship;
    }

}
