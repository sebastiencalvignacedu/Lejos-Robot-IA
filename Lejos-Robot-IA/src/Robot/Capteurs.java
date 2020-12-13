package Robot;

 import lejos.hardware.Button;
import lejos.utility.Delay;

/**
 * 
 * Classe permettant de centralis�e les informations concernant les diff�rents 
 * capteurs.
 *
 */
public class Capteurs {

	/**
	 * Attribut de classe d�finissant si le capteur touche est activ� ou non
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
	 * d�finit les attributs touche, distance et couleur avec les objet t, u, c
	 * @constructor 
	 * 
	 */
	public Capteurs() {
		this.distance = u.distance;
		this.touche = t.touche;
		this.couleur = c.couleur;
		
	}
	/**
	 * M�thode qui retourne l'attribut touche
	 * @return  true si le bouton est enfonc� et false si le bouton n'est pas appuy� 
	 */
	public boolean isTouche() {
		return touche;
	}
	/**
	 * M�thode qui retourne l'attribut disttance
	 * @return un float correspondant � la distance mesur�e
	 */

	public float getDistance() {
		return distance;
	}
/**
 * M�thode qui retourne l'attribut Couleur
 * @return une String donnant le nom de la couleur rencontr�e
 */
	public String getCouleur() {
		return couleur;
	}
	/**
	 * M�thode qui actualise les donn�es des capteurs
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

