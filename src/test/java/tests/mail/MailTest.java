package tests.mail;

import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import projectpackage.support.mail.MailTool;
import tests.AbstractTest;

/**
 * Created by Lenovo on 10.04.2017.
 */
public class MailTest extends AbstractTest {

//    IF YOUR MAIL IS GMAIL:
//By default, gmail does not allow less secure apps to get authenticated. You need to turn on the option in you gmail account to allow less secure apps to get authenticated.
//
//Follow these steps:
//1.Login to Gmail.
//2.Access the URL as https://www.google.com/settings/security/lesssecureapps
//3.Select "Turn on"
//
//Try running your code again, it should work.

    @Test
    public void doTest(){
        MailTool mailTool = new MailTool();
        mailTool.setJavaMailSender(new JavaMailSenderImpl());
        mailTool.setMailSmtpHost("smtp-host-name-for-your-server(DO NOT ENTER IF YOU'RE ON GMAIL)");
        mailTool.setMailSmtpPort(587);//This port for gmail only
        Boolean setDataResult = mailTool.setMessageData("your-email", "recepient-email", "Test", "Test data text");
        Boolean setLoginAndPass = mailTool.setUserForMailSend("your-email-or-login", "your-password");
        System.out.println("Setting message data="+setDataResult);
        System.out.println("Setting login/password data="+setLoginAndPass);
        System.out.println(mailTool.toString());
        mailTool.sendMail();
    }
}
