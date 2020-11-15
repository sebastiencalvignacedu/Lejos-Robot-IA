
import lejos.hardware.Button;
/**
 * <b> La classe Capteurs represente ......</b>
 * <p>
 * description de la classe ????????
 * </p>
 * @author membre du Groupe
 * @version 8.0
 * 
 */
public class Capteurs {
	/**
	 * La "touche" du Capteurs.
	 * Cette touche a une valeur boolean.
	 * Elle change en fonction si la touche est touché
	 * Ou pas .  
	 * 
	 */
	boolean touche;
	
	/**
	 * La "distance" du Capteurs. 
	 * Cette distance peut changer en fonction des objets.
	 * 
	 */
	float distance;
	
	/**
	 * La "couleur" du Capteurs.
	 * couleur représente les differentes couleur du terrain.
	 */
	String couleur;
	
	/**
	 * @see Touche
	 */
	private Touche t = new Touche();
	
	/**
	 * @see Couleur
	 */
	private Couleur c = new Couleur();
	
	/**
	 * @see Ultrason
	 */
	private Ultrason u = new Ultrason();
	
	/**
	 * Constructeur Capteurs Vide.
	 * <p>
	 * A la construction la distance de ultraSon est à 0,
	 * La touche si touche à un objet ou pas 
	 * et la couleur par une couleur du debut ex: blanche.
	 * </p>
	 */
	public Capteurs() {
		this.distance = u.distance;
		this.touche = t.touche;
		this.couleur = c.couleur;
		
	}
	
	/**
	 * Actualise permet d'actualiser :
	 * les touches ,La couleur ,Ultrason
	 */
	public void actualise() {
		c.actualise();
		t.actualise();
		u.actualise();
		this.distance = u.distance;
		this.touche = t.touche;
		this.couleur = c.couleur;
	}
	public static void main(String[] args) {
		Capteurs capt = new Capteurs();
		while (Button.ESCAPE.isUp())  {
			capt.actualise();
			System.out.println("distance? -- "+capt.distance);
			System.out.println("touch? -- "+capt.touche);
			System.out.println("color? --"+capt.couleur);
			System.out.println("**********");
		}

	}

}
