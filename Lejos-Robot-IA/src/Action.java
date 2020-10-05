
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;


public class Action {

	private Port A,B,C,D;
	private EV3LargeRegulatedMotor LeftMotor, RightMotor;
	private EV3MediumRegulatedMotor MiddleMotor;
	
	public Action() {
		
		A = LocalEV3.get().getPort("A");
		B = LocalEV3.get().getPort("B");
		C = LocalEV3.get().getPort("C");

		LeftMotor = new EV3LargeRegulatedMotor(A);
        RightMotor = new EV3LargeRegulatedMotor(B);
        MiddleMotor = new EV3MediumRegulatedMotor(C);
        
		LeftMotor.getMaxSpeed();
        RightMotor.getMaxSpeed();
       
	}
	
	public void forward() {
		LeftMotor.forward();
        RightMotor.forward();
	}
	public void backward() {
		LeftMotor.backward();
        RightMotor.backward();
	}
	public void rotate() {
		LeftMotor.forward();
        RightMotor.backward();
	}
	public void rotate_anti() {
		LeftMotor.backward();
        RightMotor.forward();
	}
	public void pick() {
		MiddleMotor.forward();
	}
	public void let() {
		MiddleMotor.backward();
	}
	public void stop() {
		LeftMotor.stop();
        RightMotor.stop();
	}
	
}
