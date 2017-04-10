package projectpackage.support.mail;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Lenovo on 10.04.2017.
 */
@Log4j
public class MailTool {

    @Autowired
    private JavaMailSender javaMailSender;

    private String username = null;
    private String password = null;
    private Boolean mailSmtpAuth = true;
    private Boolean mailSmtpStarttlsEnable = true;
    private String mailSmtpHost = "smtp.gmail.com";
    private Integer mailSmtpPort = 587;
    private String mailTransportProtocol = "smtp";
    private String mailFromAttribute = null;
    private String attributeSendFrom = null;
    private String attributeSendTo = null;
    private String attributeSubject = null;
    private String attributeText = null;
    private File attributeFile = null;

    public void setMailSmtpAuth(Boolean mailSmtpAuth) {
        if (null != mailSmtpAuth) this.mailSmtpAuth = mailSmtpAuth;
    }

    public void setMailSmtpStarttlsEnable(Boolean mailSmtpStarttlsEnable) {
        if (null != mailSmtpStarttlsEnable) this.mailSmtpStarttlsEnable = mailSmtpStarttlsEnable;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        if (null != mailSmtpHost) this.mailSmtpHost = mailSmtpHost;
    }

    public void setMailSmtpPort(Integer mailSmtpPort) {
        if (null != mailSmtpPort) this.mailSmtpPort = mailSmtpPort;
    }

    public void setMailTransportProtocol(String mailTransportProtocol) {
        this.mailTransportProtocol = mailTransportProtocol;
    }

    public void setMailFromAttribute(String mailFromAttribute) {
        this.mailFromAttribute = mailFromAttribute;
    }

    public boolean setUserForMailSend(String username, String password) {
        if (null != username && null != password) {
            this.username = username;
            this.password = password;
            if (null == mailFromAttribute) mailFromAttribute = username;
            return true;
        } else return false;
    }

    public boolean setMessageData(String from, String to, String subject, String messageData) {
        if (null != from && null != to && null != subject && null != messageData) {
            this.attributeSendFrom = from;
            this.attributeSendTo = to;
            this.attributeSubject = subject;
            this.attributeText = messageData;
            return true;
        } else return false;
    }

    public void setAttributeFile(File attributeFile) {
        this.attributeFile = attributeFile;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendMail() {
        synchronized (MailTool.class) {
            prepareSender();
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                prepareMail(message);
            } catch (MessagingException e) {
                log.error(message, e);
                e.printStackTrace();
            }
            try {
                javaMailSender.send(message);
            } catch (MailException e){
                log.error("Unknown message error",e);
                return false;
            };
            return true;
        }
    }

    private void prepareSender() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.transport.protocol", mailTransportProtocol);
        props.put("mail.from.email", mailFromAttribute);
        JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl) javaMailSender;
        javaMailSenderImpl.setUsername(username);
        javaMailSenderImpl.setPassword(password);
        javaMailSenderImpl.setJavaMailProperties(props);
    }

    private void prepareMail(MimeMessage message) throws MessagingException {
        MimeMessageHelper messageHelper;
        if (null != attributeFile) {
            messageHelper = new MimeMessageHelper(message, true);
        } else messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(attributeSendFrom);
        messageHelper.setTo(attributeSendTo);
        messageHelper.setSubject(attributeSubject);
        messageHelper.setSentDate(new Date(System.currentTimeMillis()));
        messageHelper.setText(attributeText);
        if (messageHelper.isMultipart()) {
            messageHelper.addAttachment(attributeFile.getName(), attributeFile);
        }
    }

    @Override
    public String toString() {
        return "MailTool{" +
                "javaMailSender=" + javaMailSender +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mailSmtpAuth=" + mailSmtpAuth +
                ", mailSmtpStarttlsEnable=" + mailSmtpStarttlsEnable +
                ", mailSmtpHost='" + mailSmtpHost + '\'' +
                ", mailSmtpPort=" + mailSmtpPort +
                ", mailTransportProtocol='" + mailTransportProtocol + '\'' +
                ", mailFromAttribute='" + mailFromAttribute + '\'' +
                ", attributeSendFrom='" + attributeSendFrom + '\'' +
                ", attributeSendTo='" + attributeSendTo + '\'' +
                ", attributeSubject='" + attributeSubject + '\'' +
                ", attributeText='" + attributeText + '\'' +
                ", attributeFile=" + attributeFile +
                '}';
    }
}