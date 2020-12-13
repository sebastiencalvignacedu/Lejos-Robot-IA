package Robot;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

/**
 * Classe qui permet d'avoir acc�s au capteur de but�e ( plus commod�ment appel�s bouton touche)
 * 
 *
 */

public class Touche /*extends Capteurs*/ {
	/**
	 * attribut de classe qui d�finit le port sur lequel est branch� le capteur Touche
	 * @see Port
	 */
	Port s4;
	/**
	 * Attribut de classe permettant d'interagir avec le capteur
	 * @see EV3TouchSensor
	 */
	EV3TouchSensor buteeCapteur;
	/**
	 * Attribut de classe permettant d'obtenir un �chantillon du capteur
	 * @see SampleProvider
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
 * d�finit les attributs s4, buteeCapteur, recup.
 * Le tableau echantillon est initialis� avec la taille appropri� d�finit par buteeCapteur
 * l'attribut touche lui est initiialis� avec la fonction 
 * @constructor
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
	 * Methode qui permet de prendre un �chantillon puis de savoir si la valeur correspond � la pression du bouton.
	 * Cette m�thode actualise la valeur de l'attribut touche
	 * @return void
	 * @see SampleProvider#fetchSample(float[] sample, int offset)
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
