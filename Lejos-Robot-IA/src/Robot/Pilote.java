package Robot;


import lejos.robotics.chassis.Chassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.motor.*;

/**
 * Class permettant de g�rer les d�placements du robot.
 * 
 *
 */

public class Pilote extends MovePilot {
	/**
	 * Attribut de classe permettant d'interragir avec le moteur de la pince du robot
	 * @see EV3MediumRegulatedMotor
	 * 
	 */
	 EV3MediumRegulatedMotor pince;
	 /**
	  * Attribut de classe permettant de d�finir le chassis du robot. 
	  * Ce chassis permettra de faire tourner le robot
	  * @see Chassis
	  * 
	  */
	 Chassis chassis;
	 /**
	  * attribut de classe d�finissant la rotation totale �ffectu� par le robot
	  */
	 double rotation = 0;	
	 
	 /**
	  * @constructor
	  * Ce constructeur permet de d�finir le chassis et la pince
	  * 
	  * @param chassis
	  * @param pince
	  */
	public Pilote(Chassis chassis, EV3MediumRegulatedMotor pince) {
		super(chassis);
		this.chassis = chassis;
		this.pince = pince;
	}
	
	/**
		 * M�thode qui permet le retour de l�attribut rotation.
		 * @return double, repr�sentant la rotation du robot
		 */
	public double getAngle() {
		
		return rotation % 360;
	}
	/**
		 * D�fini une valeur � rotation en fonction du param�tre donn� en degr�s. 
		 * @return void
		 */
	public void setAngle(double angle) {
		
		rotation = angle;
	}
	/**
	 * Fais fermer la pince de 600 (rotation demand� au moteur) . 
	 * @return void
	 */
	
	public void prendre() {
		
		this.pince.rotate(-600);
	}
	
	/**
	 * Fais ouvrir la pince de 600 (rotation demand� au moteur) . 
	 * @return void
	 */
	
	public void lacher() {
		this.pince.rotate(600);
	}
	/**
	 * m�thode faisant ce d�plac� le robot au maximum de sa vitesse
	 * @return void
	 * @deprecated
	 */
	public void vitesseMax() {
		chassis.getMaxLinearSpeed();
		chassis.getMaxAngularSpeed();
	}
	/**
	 * D�fini la vitesse de d�placement en fonction de deux param�tres.
	 * @param linear
	 * @param angular
	 * @return void
	 * @see chassis#setSpeed(linear, angular)
	 */
	public void reglerVitesse(double linear, double angular) {
		chassis.setSpeed(linear, angular);
	}
	/**
	 * Permet au robots de tourner d�un angle donn� en param�tre tout en mettant � jour l�attribut rotation. 
	 * @see MovePilot#rotate(double,boolean)  
	 * @param angle
	 */
	public void rotate(double angle) {
		if(angle > 180) {
			rotate(angle - 360,false);
			rotation=rotation+(angle - 360);
		}
		else {	    
			rotate(angle, false);
			rotation=rotation+angle;
		}
	  }
}
