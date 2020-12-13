package Robot;

 import lejos.hardware.Button;
import lejos.utility.Delay;

/**
 * 
 * Classe permettant de centralisée les informations concernant les différents 
 * capteurs.
 *
 */
public class Capteurs {

	/**
	 * Attribut de classe définissant si le capteur touche est activé ou non
	 */
	boolean touche;
	
	/*
	 * Attribut de classe permettant de connaitre la distance de l'objet 
	 */
	
	float distance;
	
	/*
	 *Attribut de classe permettant de savoir sur quel couleur on ce trouve 
	 */
	String couleur;
	/**
	 * Objet de la classe Touche
	 * @see Touche
	 */
	private Touche t = new Touche();
	/**
	 * Objet de la Classe Touche
	 * @see Couleur
	 */
	private Couleur c = new Couleur();
	
	/**
	 * Objet de la Classe Ultrason
	 * @see Ultrason
	 */
	private Ultrason u = new Ultrason();
	/**
	 * définit les attributs touche, distance et couleur avec les objet t, u, c
	 * @constructor 
	 * 
	 */
	public Capteurs() {
		this.distance = u.distance;
		this.touche = t.touche;
		this.couleur = c.couleur;
		
	}
	/**
	 * Méthode qui retourne l'attribut touche
	 * @return  true si le bouton est enfoncé et false si le bouton n'est pas appuyé 
	 */
	public boolean isTouche() {
		return touche;
	}
	/**
	 * Méthode qui retourne l'attribut disttance
	 * @return un float correspondant à la distance mesurée
	 */

	public float getDistance() {
		return distance;
	}
/**
 * Méthode qui retourne l'attribut Couleur
 * @return une String donnant le nom de la couleur rencontrée
 */
	public String getCouleur() {
		return couleur;
	}
	/**
	 * Méthode qui actualise les données des capteurs
	 * @return void
	 * @see Touche#actualise()
	 * @see Couleur#actualise()
	 * @see Ultrason#actualise()
	 */
	public void actualise() {
		//c.actualise();
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

