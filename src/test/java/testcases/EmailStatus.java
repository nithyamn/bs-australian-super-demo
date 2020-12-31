package testcases;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.SessionId;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class EmailStatus {
    String username, accesskey;
    String buildName;
    String status;
    public EmailStatus(String username, String accesskey){
        this.username = username;
        this.accesskey = accesskey;
    }

    public void triggerEmail(SessionId sessionId) throws Exception {
        //Gmail: Allow less secured apps for this to work!
        System.out.println("Preparing to send email...");
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        final String myEmailAddress = System.getenv("email_id");
        final String myPassword = System.getenv("password");
        String recepient =  System.getenv("rec_email_id");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmailAddress, myPassword);
            }
        });
        //System.out.println(checkStatus);
        String sessionData = sessionDetails(sessionId);
        Message message = prepareMessage(session, myEmailAddress, recepient, sessionData);


//        if(checkStatus.contains("Status: failed")) {
//            Transport.send(message);
//            System.out.println("Email sent successfully!");
//        }else{
//            System.out.println("Wow! No tests failed. No Email sent.");
//        }

        Transport.send(message);
        System.out.println("Email sent successfully!");
    }

    private Message prepareMessage(Session session, String myEmailAddress, String recepient,String sessionData) {
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmailAddress));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("BrowserStack Build - "+buildName+" - Test session "+status+"!");
            message.setText("Here are some details about your current test run. Please check Dashboard for detailed information. \n"+sessionData);
            //message.setContent(sessionData, "text/html;charset=UTF-8");
            return message;
        }catch(Exception e){

        }
        return null;
    }
    public String sessionDetails(SessionId sessionId) throws URISyntaxException, IOException, ParseException {

        URI uri = new URI("https://"+username+":"+accesskey+"@api.browserstack.com/automate/sessions/"+sessionId+".json"); //App Automate
        String emailData = "";
        HttpGet getRequest = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse httpresponse = httpclient.execute(getRequest);

        String jsonResponseData = EntityUtils.toString(httpresponse.getEntity());
        String trimResposneData = jsonResponseData.substring(22, jsonResponseData.length()-1);

        JSONParser parser = new JSONParser();
        JSONObject bsSessionData = (JSONObject) parser.parse(trimResposneData);
        buildName = (String) bsSessionData.get("build_name");
        status = (String)bsSessionData.get("status");
        emailData = "\nName: "+bsSessionData.get("name")
                    +"\nBuild: "+bsSessionData.get("build_name")
                    +"\nProject: "+bsSessionData.get("project_name")
                    +"\nDevice: "+bsSessionData.get("device")
                    +"\nOS: "+bsSessionData.get("os")
                    +"\nOS Version: "+bsSessionData.get("os_version")
                    +"\nBrowser: "+bsSessionData.get("browser")
                    +"\nBrowser Version: "+bsSessionData.get("browser_version")
                    +"\nStatus: "+bsSessionData.get("status")
                    +"\nReason: "+bsSessionData.get("reason")
                    +"\nPublic Session URL: "+bsSessionData.get("public_url");

        return emailData;
    }
}
