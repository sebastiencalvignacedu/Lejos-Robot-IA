import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Ultrason /*extends Capteurs*/ {

	Port s2;
	EV3UltrasonicSensor ultraCapteur;
	SampleProvider recup;
	float[] echantillon;
	float distance;
	
	public Ultrason() {
		s2 = LocalEV3.get().getPort("S1");
		ultraCapteur = new EV3UltrasonicSensor(s2);
		recup = ultraCapteur.getDistanceMode();
		echantillon = new float[recup.sampleSize()];
		recup.fetchSample(echantillon, 0);
		actualise();
		//ultraCapteur.close();
	}
	
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
