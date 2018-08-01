package hauptsteuerung;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.bind.DatatypeConverter;

import model.AusbildungsfestTermine;
/**
 * Zentrale Klasse
 * enth�lt statische Methoden 
 * @author haug_heinrich
 *
 */
public class ZentraleDienste {
	private final static String schluessel = "GFI M�nchen";
	
	/**
	 * Methode zur Erstellung der Anrede (Herr / Frau)
	 * @param nachname
	 * @param vorname
	 * @param geschlecht
	 * @return
	 */
	public static String anredefeststellen(String nachname,String vorname,char geschlecht)
	{
		String anrede = "";
		switch (geschlecht) {
		case 'm':
			anrede = "Herr ";
			break;
		case 'w':
			anrede = "Frau ";
			break;
			
		default:
			break;
		}
		
		return anrede+vorname.trim()+" "+nachname.trim();
	}
	
	/**
	 * Erstellen der 128 Bit Verschl�sselung f�r den Secretkey
	 * @return
	 * @throws Exception
	 */
	private static Key getSecretKey() throws Exception {
		// TODO Auto-generated method stub
		
		
		byte[] key = schluessel.getBytes("UTF-8"); 
		MessageDigest sha = MessageDigest.getInstance("SHA");
		key = sha.digest(key); 
		key = Arrays.copyOf(key, 16);  // nur die ersten 128 bit nutzen testen
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES"); 
		// Advanced Encryption Standard (AES)
		// secure hash algorithm (SHA)
		return secretKeySpec;
	}
	
	/**
	 * Verschl�sselung des Passwortes
	 * @param passwort
	 * @return
	 * @throws Exception
	 */
	public static String verschluesselung(String passwort) throws Exception {
		// TODO Auto-generated method stub
		
		
	
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE,getSecretKey()); 
		byte[] encrypted = cipher.doFinal(passwort.getBytes());  
		
	    
		return DatatypeConverter.printBase64Binary(encrypted);
		
		
	
		

		
		
	}
	/**
	 * Neues Passwort generieren
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String passwortGenerierung() throws NoSuchAlgorithmException
	{
		 final String allowedChars = "0123456789abcdefghijklmnopqrstuvwABCDEFGHIJKLMNOP!�$%&?*+#";
		 SecureRandom random = new SecureRandom();
		 StringBuilder pass = new StringBuilder(8);
		    for (int i = 0; i < 8; i++) {
		        pass.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
		    }
       
		return pass.toString();
	}
	/**
	 * Umwandlung eines Datums String in java.util.Date
	 * @param datum
	 * @return
	 */
	  public static Date stringToDate(String datum)
	    {
	    	SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	    	try {
				Date d = df.parse(datum);
				return d;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return null;
			}
	    }
	    
	    /**
	     *  Umwandlung eines Datums java.util.Date in formatierten String
	     * @param datum
	     * @return
	     */
	    public static String dateToString(Date datum)
	    {
	    	SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	    	String dat = df.format(datum);
	    	return dat;
	    }
	    
	    public static LocalDate dateToLocalDate(Date datum)
	    {
	    	return datum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    }
	    
	    
	    /**
	     *  Eine Methode f�r Text der Abschlussart
	     * @param abschluss
	     * @return
	     */
	    private static List<Abschlussart> abschlussart()
	    {
	    	
	    	
	    	List<Abschlussart> li = new ArrayList<Abschlussart>();
	    	li.add(new Abschlussart(1, "IHK"));
	    	li.add(new Abschlussart(2, "BVB"));
	    	li.add(new Abschlussart(3, "Praktikant"));
	    	
	    	return li;
	    		    	
	    }
	    
	   /**
	    * Feiertage erstellen
	    * @param jahr
	    * @return
	    */
	    public static ArrayList<Feiertage> feiertage(int jahr)
	    {
	    	
	    	EntityManagerFactory emf = 
					 Persistence.createEntityManagerFactory("Seminarplaner");
			 EntityManager em = emf.createEntityManager();
			
			 Query query = em.createQuery("Select a from AusbildungsfestTermine a "+
			 " where a.jahr = "+jahr+" and a.arttermin = 3");
			
			 if (query.getResultList().size() != 0)
				 return null;
			
	    	ArrayList<Feiertage> liFeiertage = new ArrayList<>();
	    	
	    	// Feste Feiertage
	    	
	    	
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,1, 1), "Neujahr"));
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,1, 6), "3 Könige"));
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,5, 1), "1.Mai"));
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,8, 15), "Maria Himmelfahrt"));
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,11, 1), "Allerheiligen"));
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,12, 24), "Hl.Abend"));
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,12, 25), "1.Weihnachtsfeiertag"));
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,12, 26), "2.WeihnachtsfeierTag"));
	    	liFeiertage.add(new Feiertage(LocalDate.of(jahr,12, 31), "Sylvester"));
	    	
	    	LocalDate os=ostern(jahr);
	    	
	    	// Bewegliche Feiertage
	    	liFeiertage.add(new Feiertage(os, "Ostersonntag"));
	    	liFeiertage.add(new Feiertage(os.minusDays(2), "Karfreitag"));
	    	liFeiertage.add(new Feiertage(os.plusDays(1), "Ostermontag"));
	    	
	    	liFeiertage.add(new Feiertage(os.plusDays(49), "Pfingstsontag"));
	    	liFeiertage.add(new Feiertage(os.plusDays(50), "Pfingstmntag"));
	    	liFeiertage.add(new Feiertage(os.plusDays(39), "Christi Himmelfahrt"));
	    	liFeiertage.add(new Feiertage(os.plusDays(60), "Fronleichnam"));
	    	liFeiertage.sort(new Comparator<Feiertage>() {

				@Override
				public int compare(Feiertage o1, Feiertage o2) {
					// TODO Auto-generated method stub
					return o1.getDatum().compareTo(o2.getDatum());
				}
			});
	    	
	    
	    	// Auf der Datenbank speichern
	    	
	    	
	    	for(Feiertage ft : liFeiertage)
	    	{
	    		AusbildungsfestTermine ftd = new AusbildungsfestTermine();
	    		ftd.setArttermin(3);
	    		ftd.setBezeichnung(ft.getBezeichnung());
	    		ftd.setDatumvon(stringToDate(ft.getDatum().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
	    		ftd.setDatumbis(stringToDate(ft.getDatum().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
	    		ftd.setJahr(ft.getDatum().getYear());
	    		em.getTransaction().begin();
	    		em.persist(ftd);
	    		em.getTransaction().commit();
	    	}
	    	
	    	return liFeiertage;
	    	
	    }
	    
	    /**
	     * Gausche Osterformel für die Berechnung von Ostern
	     * @param jahr
	     * @return
	     */
	    private static LocalDate ostern(int jahr)
		{
			     int a = jahr % 19; 
				 int b = jahr % 4 ;  
				 int c = jahr % 7;
				 int k = jahr / 100;
				 int p = k / 3 ;
				 int q = k / 4;
				 int m = (15+k-p-q) % 30;
				 int d = (19*a + m) % 30;
				 int n = (4+k-q) % 7;
				 int e = (2*b + 4*c + 6*d + n) % 7;
				 
				 int ostern = (22 + d + e);
				 
				 if (ostern > 31)
					return LocalDate.of(jahr, 4, ostern-31);
				 else
					 return LocalDate.of(jahr, 3, ostern);
				 

				 
	 
		} 
	    
	   

	    
	   

	    
	

}
