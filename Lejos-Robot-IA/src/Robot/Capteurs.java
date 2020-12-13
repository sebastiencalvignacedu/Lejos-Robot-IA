package Robot;
import lejos.hardware.Button;
import lejos.utility.Delay;


public class Capteurs {

	boolean touche;
	float distance;
	String couleur;
	private Touche t = new Touche();
	private Couleur c = new Couleur();
	private Ultrason u = new Ultrason();
	
	public Capteurs() {
		this.distance = u.distance;
		this.touche = t.touche;
		this.couleur = c.couleur;
		
	}
	
	public boolean isTouche() {
		return touche;
	}

	public float getDistance() {
		return distance;
	}

	public String getCouleur() {
		return couleur;
	}
	
	public void actualise() {
		c.actualise();
		t.actualise();
		u.actualise();
		this.distance = u.distance;
		this.touche = t.touche;
		this.couleur = c.couleur;
	}

	/*
	 * public static void main(String[] args) {
		Capteurs capt = new Capteurs();
		while (Button.ESCAPE.isUp())  {
			capt.actualise();
			//System.out.println(capt.distance);
			//System.out.println(capt.touche);
			System.out.println(capt.couleur);
		}

	}
	 */
}
