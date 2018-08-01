package hauptsteuerung;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Versenden an den Benutzer vor allem des Passwortes
 * Bis zur Produktion wird der Account
 * mautseminar@gmail.com ( Passwort gfimaut1)
 * verwendet.
 * ToDo : bei Produktionseinführung muss dieser Accout durch
 * das Mailsystem der gfi geändert weden.
 * @author haug_heinrich
 *
 */
public class SmtpMail {
	
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_HOST_PORT = "465";
	private static final String SMTP_AUTH_USER = "mautseminar@gmail.com";
	private static final String SMTP_AUTH_PWD = "gfimaut1";
	private String emailadresse = "mautseminar@gmail.com";
	private String body = "TestMail";
	private String subject = "TestMail";
	
	/**
	 * Senden des Emails via der Standardklasse SMTPMail
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void emailsenden() throws AddressException, MessagingException
	{
		Properties props = new Properties();
		
		props.put("mail.transport.protocol","smtp");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.port", SMTP_HOST_PORT);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.socketFactory.port", SMTP_HOST_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

	    
	    Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getDefaultInstance(props, auth);

        Transport transport = mailSession.getTransport();
        
        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject(this.subject);
        message.setContent(this.body, "text/plain");
        message.setFrom(new InternetAddress("mautseminar@gmail.com"));
        message.addRecipient(Message.RecipientType.TO,
             new InternetAddress(this.emailadresse));

        transport.connect();
        transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
        transport.close();
        System.out.println("Email gesendet");



				
	}
	
	/**
	 * parameterloser Konstruktor 
	 * ruft die Methode emailsenden auf.
	 * Die Daten müssen über die getter/setter eingegeben werden
	 * 
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public SmtpMail() throws AddressException, MessagingException
	{
		this.emailsenden();
	}
	/**
	 * überladener Konstruktur mit den Daten für das EMail und 
	 * Aufruf der Methode EMailsenden
	 * @param emailadresse
	 * @param subject
	 * @param body
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public SmtpMail(String emailadresse,String subject, String body) throws AddressException, MessagingException
	{
		this.emailadresse = emailadresse;
		this.subject = subject;
		this.body = body;
		this.emailsenden();
	}
	/**
	 * Interne Klasse für die Authentifiezierung
	 * Ist bei allen Accounten erfolderlich, die beim Senden
	 * eine Authentifizerung verlangen
	 * @author haug_heinrich
	 *
	 */
	private class SMTPAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
           String username = SMTP_AUTH_USER;
           String password = SMTP_AUTH_PWD;
           return new PasswordAuthentication(username, password);
        }
    }

	

}
