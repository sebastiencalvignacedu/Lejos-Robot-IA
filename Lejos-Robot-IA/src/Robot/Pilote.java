package Robot;


import lejos.robotics.chassis.Chassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.motor.*;

/**
 * <b>Pilote represente la generation des d�placements du projet.</b>
 * <p> Classe permettant de g�rer les d�placements du robot.</p>
 * @author Calvignac S�bastien, Simon Dorian, Kamissoko Djoko, Auray C�dric
 * @version 8.0
 *
 */
public class Pilote extends MovePilot{
	/**
	 *  @see EV3MediumRegulatedMotor#pince
	 * Attribut de classe permettant d'interragir avec le moteur de la pince du robot. 
	 */
	 EV3MediumRegulatedMotor pince;
	 /**
	  * @see Chassis#chassis
	  * Attribut de classe permettant de d�finir le chassis du robot. 
	  * Ce chassis permettra de faire tourner le robot
	  */
	 Chassis chassis;
	 /**
	  * attribut de classe d�finissant la rotation totale �ffectu� par le robot
	  */
	 double rotation = 0;	
	 
	 /**
	  * @constructor
	  * @see Chassis#chassis
	  * @see EV3MediumRegulatedMotor#pince
	  * @param chassis
	  * @param pince
	  */
	public Pilote(Chassis chassis, EV3MediumRegulatedMotor pince) {
		super(chassis);
		this.chassis = chassis;
		this.pince = pince;
	}
	
	/**
	 * La methode getAngle represente la rotation du Robot.
	 * @return double;
	 */
	public double getAngle() {
		return rotation % 360;
	}
	/**
	 * La m�thode setAngle est la rotation en fonction. 
	 * Elle est une valeur � rotation en fonction du param�tre donn� en degr�s. 
	 * @return void
	 */
	public void setAngle(double angle) {
		rotation = angle;
	}
	/**
	 * La methode prendre() permet de fermer la pince sur un objet.
	 * Elle ferme la pince de 600 (rotation demand� au moteur). 
	 * @return void
	 */
	
	public void prendre() {
		this.pince.rotate(-600);
	}
	/**
	 * La methode lacher() 
	 * Fais ouvrir la pince de 600 (rotation demand� au moteur) . 
	 * @return void
	 */
	public void lacher() {
		this.pince.rotate(600);
	}
	/**
	 * La methode vitesseMax() deplace le robot aux maximuns.
	 * @return void
	 * @deprecated
	 */
	public void vitesseMax() {
		chassis.getMaxLinearSpeed();
		chassis.getMaxAngularSpeed();
	}
	/**
	 * @see chassis#setSpeed(linear, angular)
	 * La m�thode reglerVitesse pr�cise la vitesse de d�placement.
	 *Elle  D�finit la vitesse de d�placement en fonction de deux param�tres.
	 * @param linear
	 * @param angular
	 * @return void
	 */
	public void reglerVitesse(double linear, double angular) {
		chassis.setSpeed(linear, angular);
	}
	/**
	 * @see MovePilot#rotate(double,boolean)
	 * La methode rotate g�re la rotation des Robots.
	 * Elle permet au robot de tourner d�un angle donn� en param�tre tout en mettant � jour l�attribut rotation.   
	 * @param angle
	 * @return void
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
