package Robot;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

/**
 * <b>Touche represente le bouton touche du projet.</b>
 * <p> Classe qui permet d'avoir acc�s au capteur de but�e ( plus commod�ment appel�s bouton touche).</p>
 * @author Calvignac S�bastien, Simon Dorian, Kamissoko Djoko, Auray C�dric
 * @version 8.0
 *
 */
public class Touche /*extends Capteurs*/ {
	/**
	 *  @see Port#s4
	 * attribut de classe qui d�finit le port sur lequel est branch� le capteur Touche
	 */
	Port s4;
	/**
	 * @see EV3TouchSensor
	 * Attribut de classe permettant d'interagir avec le capteur
	 */
	EV3TouchSensor buteeCapteur;
	/**
	 * @see SampleProvider
	 * Attribut de classe permettant d'obtenir un �chantillon du capteur
	 */
	SampleProvider recup;
	/**
	 * tableau de float permettant de garder en m�moire la valeur enregistr�
	 */
	float[] echantillon;
	/**
	 * Attribut permettant de stock� si le bouton est activ� ou non
	 */
	boolean touche;
/**
 * @constructor
 * d�finit les attributs s4, buteeCapteur, recup.
 * Le tableau echantillon est initialis� avec la taille appropri� d�finit par buteeCapteur
 * l'attribut touche lui est initiialis� avec la fonction .
 */
	public Touche() {
		s4 = LocalEV3.get().getPort("S4");
		buteeCapteur = new EV3TouchSensor(s4);
		recup = buteeCapteur.getTouchMode();
		echantillon = new float[recup.sampleSize()];
		actualise();
		//touchProvider.fetchSample(touchSample,0);
		//Delay.msDelay(250);
		//touchSensor.close();
	}
	/**
	 * @see SampleProvider#fetchSample(float[] sample, int offset)
	 * Methode qui permet de prendre un �chantillon puis de savoir si la valeur correspond � la pression du bouton.
	 * Cette m�thode actualise la valeur de l'attribut touche
	 * @return void
	 */
	public void actualise() {
		recup.fetchSample(echantillon,0);
		//touchSensor.close();
		touche = echantillon[0]==1;	
		}
	
	/*public static void main(String[] args) {
		Touche t = new Touche();
		while (Button.ESCAPE.isUp())  {
			t.actualise();
			System.out.println(t.touche);
		}
	}*/

}
