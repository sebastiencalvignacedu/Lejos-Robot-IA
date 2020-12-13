package Robot;


import lejos.robotics.chassis.Chassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.motor.*;

/**
 * Class permettant de gérer les déplacements du robot.
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
	  * Attribut de classe permettant de définir le chassis du robot. 
	  * Ce chassis permettra de faire tourner le robot
	  * @see Chassis
	  * 
	  */
	 Chassis chassis;
	 /**
	  * attribut de classe définissant la rotation totale éffectué par le robot
	  */
	 double rotation = 0;	
	 
	 /**
	  * @constructor
	  * Ce constructeur permet de définir le chassis et la pince
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
		 * Méthode qui permet le retour de l’attribut rotation.
		 * @return double, représentant la rotation du robot
		 */
	public double getAngle() {
		
		return rotation % 360;
	}
	/**
		 * Défini une valeur à rotation en fonction du paramètre donné en degrés. 
		 * @return void
		 */
	public void setAngle(double angle) {
		
		rotation = angle;
	}
	/**
	 * Fais fermer la pince de 600 (rotation demandé au moteur) . 
	 * @return void
	 */
	
	public void prendre() {
		
		this.pince.rotate(-600);
	}
	
	/**
	 * Fais ouvrir la pince de 600 (rotation demandé au moteur) . 
	 * @return void
	 */
	
	public void lacher() {
		this.pince.rotate(600);
	}
	/**
	 * méthode faisant ce déplacé le robot au maximum de sa vitesse
	 * @return void
	 * @deprecated
	 */
	public void vitesseMax() {
		chassis.getMaxLinearSpeed();
		chassis.getMaxAngularSpeed();
	}
	/**
	 * Défini la vitesse de déplacement en fonction de deux paramètres.
	 * @param linear
	 * @param angular
	 * @return void
	 * @see chassis#setSpeed(linear, angular)
	 */
	public void reglerVitesse(double linear, double angular) {
		chassis.setSpeed(linear, angular);
	}
	/**
	 * Permet au robots de tourner d’un angle donné en paramètre tout en mettant à jour l’attribut rotation. 
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
