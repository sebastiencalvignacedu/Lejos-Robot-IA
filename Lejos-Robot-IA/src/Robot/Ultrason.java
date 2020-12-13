package Robot;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
/**
 * Classe qui permet d'avoir accès au capteur de distance de type Sonar
 *
 */
public class Ultrason /*extends Capteurs*/ {
	/**
	 * attribut de classe qui définit le port sur lequel est branché le capteur Touche
	 * @see Port
	 */
	Port s2;
	/**
	 * Attribut de classe permettant d'interagir avec le capteur
	 * @see EV3TouchSensor
	 */
	EV3UltrasonicSensor ultraCapteur;
	/**
	 * Attribut de classe permettant d'obtenir un échantillon du capteur
	 * @see SampleProvider
	 */
	SampleProvider recup;
	/**
	 * tableau de float permettant de garder en mémoire la valeur enregistré
	 */
	float[] echantillon;
	/**
	 * Attribut permettant de stocké sla distance mesurée
	 */
	float distance;
	/**
	 * définit les attributs s2, ultraCapteur, recup.
	 * Le tableau echantillon est initialisé avec la taille approprié définit par buteeCapteur
	 * l'attribut distance lui est initiialisé avec la fonction actualise()
	 * @constructor
	 */
	public Ultrason() {
		s2 = LocalEV3.get().getPort("S1");
		ultraCapteur = new EV3UltrasonicSensor(s2);
		recup = ultraCapteur.getDistanceMode();
		echantillon = new float[recup.sampleSize()];
		recup.fetchSample(echantillon, 0);
		actualise();
		//ultraCapteur.close();
	}
	/**
	 * Méthode qui permet de mesurer la distance et de la stocker dans l'attribut distance
	 * @return void
	 * @see SampleProvider#fetchSample(float[] sample, int offset)
	 */
	public void actualise() {
		recup.fetchSample(echantillon, 0);
		this.distance = echantillon[0]; 
		//ultraCapteur.close();
	}
	
	/*public static void main(String[] args) {
		Ultrason u = new Ultrason();
		while (Button.ESCAPE.isUp())  {
			u.actualise();
			System.out.println(u.distance);
		}
	}*/

}
