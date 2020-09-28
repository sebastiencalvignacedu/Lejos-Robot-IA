import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

public class Touch extends Perception {
	
	Port s4;
	EV3TouchSensor touchSensor;
	SampleProvider touchProvider;
	float[] touchSample;

	public Touch() {
		s4 = LocalEV3.get().getPort("S4");
		touchSensor = new EV3TouchSensor(s4);
		touchProvider = touchSensor.getTouchMode();
		touchSample = new float[touchProvider.sampleSize()];
		touchProvider.fetchSample(touchSample,0);
		//Delay.msDelay(250);
		touchSensor.close();
	}
	
	public void refresh() {
		touchProvider.fetchSample(touchSample,0);
		touchSensor.close();
	}

}
