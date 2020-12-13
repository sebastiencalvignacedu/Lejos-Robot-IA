package Robot;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.motor.*;

public class Pilote extends MovePilot {

	 EV3MediumRegulatedMotor pince;
	 Chassis chassis;
	 double rotation = 0;
	 
	 public double getAngle() {
		 return rotation;
	 }

	 public void setAngle(double angle) {
		 this.rotation = angle;
	 }
	
	public Pilote(Chassis chassis, EV3MediumRegulatedMotor pince) {
		super(chassis);
		this.chassis = chassis;
		this.pince = pince;
		// TODO Auto-generated constructor stub
	}
	
	public void prendre() {
		pince.rotate(-600);
	}
	public void lacher() {
		pince.rotate(600);
	}
	public void vitesseMax() {
		chassis.getMaxLinearSpeed();
		chassis.getMaxAngularSpeed();
	}
	public void reglerVitesse(double linear, double angular) {
		chassis.setSpeed(linear, angular);
	}
	public void rotate(double angle) {
	    rotate(angle, false);
	    rotation = rotation + angle;
	  }
	
	/*
	 * public void forward()
	 * public void backward()
	 * public void travel(double distance)
	 * public void rotate(double angle)
	 * public void rotateLeft()
	 * public void rotateRight()
	 * public void stop()
	 * 
	 */
}
