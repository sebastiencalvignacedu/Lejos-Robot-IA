package Robot;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.utility.Delay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;

/**
 * <b> La classe Action represente ......</b>
 * <p>
 * description de la classe ????????
 * </p>
 * @author membre du Groupe
 * @version 8.0
 * 
 */
public class Action {
	/**
	 * 
	 */
	private float vitesseMax = (float) 17.45;
	/**
	 * 
	 */
	private Port A,B,C,D;
	/**
	 * 
	 */
	private EV3LargeRegulatedMotor LeftMotor, RightMotor;
	/**
	 * 
	 */
	private EV3MediumRegulatedMotor MiddleMotor;
	
	/**
	 * Constructeur Vide de l'Action.
	 */
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
	/**
	 * avancer
	 */
	public void forward() {
		LeftMotor.forward();
        RightMotor.forward();
	}
	/**
	 * retourner 
	 */
	public void backward() {
		LeftMotor.backward();
        RightMotor.backward();
	}
	/**
	 * rotation
	 */
	public void rotate() {
		LeftMotor.forward();
        RightMotor.backward();
	}
	/**
	 * anti-rotation
	 */
	public void rotate_anti() {
		LeftMotor.backward();
        RightMotor.forward();
	}
	/**
	 * 
	 */
	public void pick() {
		MiddleMotor.forward();
	}
	/**
	 * 
	 */
	public void let() {
		MiddleMotor.backward();
	}
	/**
	 * 
	 */
	public void stop() {
		LeftMotor.stop();
        RightMotor.stop();
	}
	/**
	 * 
	 * @param temps est le temps � laquelle le robot
	 * avaancement des des ch 
	 */
	public void avancerTempsMS(int temps) {
		this.forward();
		Delay.msDelay(temps);
		
	}
	/**
	 * 
	 * @param temps
	 */
	public void avancerDistance(int temps) {
		
	}
	/**
	 * 
	 * @param temps
	 */
	public void reculerTempsMS(int temps) {
		this.backward();
		Delay.msDelay(temps);
	}
	/**
	 * 
	 * @param d
	 */
	public void avancerDist(float d) {
		float t = d/vitesseMax;
		this.forward();
		Delay.msDelay((long) t*1000);
	}
}
