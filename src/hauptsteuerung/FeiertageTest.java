package hauptsteuerung;

import java.util.ArrayList;



public class FeiertageTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<Feiertage> li = ZentraleDienste.feiertage(2018);
		for(Feiertage f : li)
			System.out.println(f.getDatum()+" "+f.getBezeichnung());
		

	}

}
