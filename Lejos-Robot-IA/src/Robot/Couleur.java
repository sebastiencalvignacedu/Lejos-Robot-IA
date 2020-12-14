package Robot;

import java.util.List;
import java.util.Arrays;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color; //changer nom de classe, variables, attributs et m�thodes
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

/**
 *<b>Couleur  donne acc�s au capteur de couleur.</b>
 *<p> Elle permet d'avoir acc�s au capteur de Couleur.</p>
 * @author Calvignac S�bastien, Simon Dorian, Kamissoko Djoko, Auray C�dric
 *@version 8.0
 */
public class Couleur /*extends Capteurs*/ {

	/**
	 * attribut de classe qui d�finit le port sur lequel est branch� le capteur Couleur
	 * @see Port#s3
	 */	
 Port s3;
 /**
   * Attribut de classe permettant d'interagir avec le capteur
  * @see EV3ColorSensor#capteurCouleur
   */
 EV3ColorSensor capteurCouleur;
 /**
  * Attribut de classe permettant d'obtenir un �chantillon du capteur 
  * @see SampleProvider#recup
  */
 SampleProvider recup;
 /**
  * tableau de trois float correspondant aux valeurs RGB de l'�chantillon.
  */
 float[] echantillon;
 /**
  * tableau de trois float correspondant aux valeurs RGB du rouge
  */
 float[] rouge;
 /**
  * tableau de trois float correspondant aux valeurs RGB du bleu
  */
 float[] bleu;
 /**
  * tableau de trois float correspondant aux valeurs RGB du vert
  */
 float[] vert;
 /**
  * tableau de trois float correspondant aux valeurs RGB du noir callibre
  */
 float[] noir;
 /**
  * tableau de trois float correspondant aux valeurs RGB du blanc callibre
  */
 float[] blanc;
 /**
  * tableau de trois float correspondant aux valeurs RGB du jaune callibre
  */
 float[] jaune;
 /**
  * tableau de trois float correspondant aux valeurs RGB du gris callibre
  */
 float[] gris;
 /**
  * attribut de classe stockant le nom de la couleur rencontr�e
  */
 String couleur ="";
 
 /**
  * d�finit les attributs s3, capteurCouleur et recup.
  * Le constructeur permet d'instancier les tableaux de couleur en utilisant le capteurCouleur. 
  * Et enfin le constructeur appel la m�thode actualise().
  * @constructeur
  */
 public Couleur() {
	 
	// try {
	 
  s3 = LocalEV3.get().getPort("S3");
  capteurCouleur = new EV3ColorSensor(s3);
  recup = new MeanFilter(capteurCouleur.getRGBMode(),1);
  capteurCouleur.setFloodlight(Color.WHITE);
  
  System.out.println("Calibrez le bleu.");
	Button.ENTER.waitForPressAndRelease();
	bleu = new float[recup.sampleSize()];
	recup.fetchSample(bleu, 0);
	System.out.println("Bleu calibr�.");
	
	System.out.println("Calibrez le rouge.");
	Button.ENTER.waitForPressAndRelease();
	rouge = new float[recup.sampleSize()];
	recup.fetchSample(rouge, 0);
	System.out.println("Rouge calibr�.");

	System.out.println("Calibrez le vert.");
	Button.ENTER.waitForPressAndRelease();
	vert = new float[recup.sampleSize()];
	recup.fetchSample(vert, 0);
	System.out.println("Vert calibr�.");

	System.out.println("Calibrez le noir.");
	Button.ENTER.waitForPressAndRelease();
	noir = new float[recup.sampleSize()];
	recup.fetchSample(noir, 0);
	System.out.println("Noir calibr�.");
	
	System.out.println("Calibrez le jaune.");
	Button.ENTER.waitForPressAndRelease();
	jaune = new float[recup.sampleSize()];
	recup.fetchSample(jaune, 0);
	System.out.println("Jaune calibr�.");
	
	System.out.println("Calibrez le blanc.");
	Button.ENTER.waitForPressAndRelease();
	blanc = new float[recup.sampleSize()];
	recup.fetchSample(blanc, 0);
	System.out.println("Blanc calibr�.");
	
	System.out.println("Calibrez le gris.");
	Button.ENTER.waitForPressAndRelease();
	gris = new float[recup.sampleSize()];
	recup.fetchSample(gris, 0);
	System.out.println("Gris calibr�.");
	
	actualise();
	// }
	 /*catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);*/
		}
  //colorProvider = colorSensor.getRGBMode();
  //colorSample = new float[colorProvider.sampleSize()];
  //colorProvider.fetchSample(colorSample, 0);
  //Delay.msDelay(250);
  //colorSensor.close();
 //}
 
 /**
  * La methode scalaire correspond aux valeurs RGB, 
  * cette m�thode prend en param�tres des deux tableaux 
  * de 3 float chacun contenant les valeurs correspondant 
  * virtuellement aux valeurs RGB �chantillonnales via le 
  * capteur de couleurs.
  * @param v1
  * @param v2
  * @return le scalaire des deux tableaux
  */
 public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}
 
 /**
  * La m�thode onPath
  * m�thode qui permet de comparer une String en parametre � l'attribut couleur actualiser
  * @param chemin
  * @return true si la couleur en parametre est la meme que celle percue. la valeur retourn est False si la couleur en parametre
  * n'est pas la meme que celle percue
  */
 public boolean onPath(String chemin) {
	 actualise();
	 return(chemin==this.couleur);
	 }
 /**
  * m�thode qui permet de savoir si une couleur donn� en parametre correspond a une couleur presente sur la table
  * @param chemin
  * @return true si la valeur d�tect� correspond � une couleur de la table
  */
 public boolean onPath(String []chemin) {
	 actualise();
     List<String> liste = Arrays.asList(chemin);
     return(liste.contains(this.couleur));
 }
/**
 * Permet d'actualiser l'attribut this.couleur pour representer sous forme d'une String la couleur per�ue par le capteur couleur.
 */
 	public void actualise() {
		//colorProvider.fetchSample(colorSample, 0);
		//colorSensor.close();
 		//try {
 			
 			echantillon = new float[recup.sampleSize()];
 			recup.fetchSample(echantillon, 0);
 			double minscal = Double.MAX_VALUE;
		
 			double scalaire = scalaire(echantillon, bleu);	
 			if (scalaire < minscal) {
 				minscal = scalaire;
 				this.couleur = "bleu";
 			}
		
 			scalaire = scalaire(echantillon, rouge);
 			if (scalaire < minscal) {
 				minscal = scalaire;
 				this.couleur = "rouge";
 			}
		
 			scalaire = scalaire(echantillon, vert);
 			if (scalaire < minscal) {
 				minscal = scalaire;
 				this.couleur = "vert";
 			}
		
 			scalaire = scalaire(echantillon, noir);
 			if (scalaire < minscal) {
 				minscal = scalaire;
 				this.couleur = "noir";
 			}
		
 			scalaire = scalaire(echantillon, blanc);
 			if (scalaire < minscal) {
 				minscal = scalaire;
 				this.couleur = "blanc";
 			}
 			
 			scalaire = scalaire(echantillon, jaune);
 			if (scalaire < minscal) {
 				minscal = scalaire;
 				this.couleur = "jaune";
 			}
 			
 			scalaire = scalaire(echantillon, gris);
 			if (scalaire < minscal) {
 				minscal = scalaire;
 				this.couleur = "gris";
 			}
 		}
 		
 		/*catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);
		}*/
	//}
 	

 	/*public static void main(String[] args) {
		Couleur c = new Couleur();
		while (Button.ESCAPE.isUp())  {
			c.actualise();
			System.out.println(c.couleur);
		}
	}*/
	
}



