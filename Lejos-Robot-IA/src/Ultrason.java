
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
/**
 * <b> La classe Ultrason represente ......</b>
 * <p>
 * description de la classe ????????
 * </p>
 * @author membre du Groupe
 * @version 8.0 
 */
public class Ultrason extends Capteurs {
	/**
	 * @see Port
	 * Le s2 est le numero de port de Ultrason.
	 */
	Port s2;
	
	/**
	 * @see EV3UltrasonicSensor
	 * Le "ultraCapteur" est le capteur de ultraSon
	 */
	EV3UltrasonicSensor ultraCapteur;
	
	/**
	 * @see SampleProvider
	 * 
	 */
	SampleProvider recup;
	
	/**
	 * Le "echantillon" est un tableau qui contient la valeur des echantillons.
	 */
	float[] echantillon;
	
	/**
	 * La "distance" est la valeur des distances que le robot doit parcourir. 
	 */
	float distance;
	
	/**
	 * Constructeur Vide de Ultason
	 */
	public Ultrason() {
		s2 = LocalEV3.get().getPort("S2");
		ultraCapteur = new EV3UltrasonicSensor(s2);
		recup = ultraCapteur.getDistanceMode();
		echantillon = new float[recup.sampleSize()];
		recup.fetchSample(echantillon, 0);
		actualise();
		//ultraCapteur.close();
	}
	/**
	 * actualise de UltraSon.
	 */
	public void actualise() {
		recup.fetchSample(echantillon, 0);
		this.distance = echantillon[0]; 
		//ultraCapteur.close();
	}

}
