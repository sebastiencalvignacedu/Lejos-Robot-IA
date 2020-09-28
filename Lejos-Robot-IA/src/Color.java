import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class Color extends Perception {

	
 Port s3;
 EV3ColorSensor colorSensor;
 SampleProvider colorProvider;
 float[] colorSample;
  
 public Color() {
  s3 = LocalEV3.get().getPort("S3");
  colorSensor = new EV3ColorSensor(s3);
  colorProvider = colorSensor.getRGBMode();
  colorSample = new float[colorProvider.sampleSize()];
  colorProvider.fetchSample(colorSample, 0);
  //Delay.msDelay(250);
  colorSensor.close();
 }
 
	public void refresh() {
		colorProvider.fetchSample(colorSample, 0);
		colorSensor.close();
	}

}
