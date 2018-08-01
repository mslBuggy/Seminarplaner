package hauptsteuerung;

public class Abschlussart {
	private int key;
	private String text;
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public Abschlussart(){}
	
	public Abschlussart(int key, String text) {
		
		this.key = key;
		this.text = text;
	}


}
