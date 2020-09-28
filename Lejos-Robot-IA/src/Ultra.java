import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Ultra extends Perception {

		Port s2;
		EV3UltrasonicSensor ultraSensor;
		SampleProvider ultraProvider;
		float[] ultraSample;
		
		public Ultra() {
			s2 = LocalEV3.get().getPort("S2");
			ultraSensor = new EV3UltrasonicSensor(s2);
			ultraProvider = ultraSensor.getDistanceMode();
			ultraSample = new float[ultraProvider.sampleSize()];
			ultraProvider.fetchSample(ultraSample, 0);
			ultraSensor.close();
		}
		
		public void refresh() {
			ultraProvider.fetchSample(ultraSample, 0);
			ultraSensor.close();
		}


	}

