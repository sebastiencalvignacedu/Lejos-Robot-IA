
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
/**
 * <b> La classe Touche represente ......</b>
 * <p>
 * La classe Touche hérite de la Capteurs
 * description de la classe ????????
 * </p>
 * @author membre du Groupe
 * @version 8.0
 * 
 */
public class Touche extends Capteurs {
	/**
	 * 	@see Port#s4
	 * Le port s4 est le numéro de port de la touche.
	 */
	Port s4;
	
	/**
	 * @see EV3TouchSensor
	 */
	EV3TouchSensor buteeCapteur;
	
	/**
	 * @see SampleProvider
	 */
	SampleProvider recup;
	
	/**
	 * Le "echantillon" de la touche. Est un tableau qui contient la valeur des echantillon.
	 */
	float[] echantillon;
	
	/**
	 * La "touche" de la Touche. Detecte la presence d'un objet.
	 */
	boolean touche;
	
	/**
	 * Constructeur Touche Vide.
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
	 * actualise de la Touche. Permet d'actualiser la "touche".
	 */
	public void actualise() {
		recup.fetchSample(echantillon,0);
		//touchSensor.close();
		touche = ((int)echantillon[0]==1);	
	}

}