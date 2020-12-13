package Robot;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

public class Touche /*extends Capteurs*/ {
	
	Port s4;
	EV3TouchSensor buteeCapteur;
	SampleProvider recup;
	float[] echantillon;
	boolean touche;

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
