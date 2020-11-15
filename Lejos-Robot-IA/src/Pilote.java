
import lejos.robotics.chassis.Chassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.motor.*;
/**
 * <b> La classe Pilote represente ......</b>
 * <p>
 * La classe hérite de la classe MovePilot
 * description de la classe ????????
 * </p>
 * @author membre du Groupe
 * @version 8.0
 * 
 */

public class Pilote extends MovePilot {
	/**
	 * @see EV3MediumRegulatedMotor
	 * 
	 */
	EV3MediumRegulatedMotor pince;
	/**
	 * @see Chassis
	 */
	Chassis chassis;
	/**
	 * La rotation du pilote, cette rotation change de variable.
	 */
	double rotation = 0;
	/**
	 * Constructeur Pilote. 
	 * @param chassis
	 * 				le chassis de Pilote.
	 * @param pince
	 * 			   La pince de Pilote.
	 * @see Chassis
	 * @see EV3MediumRegulatedMotor
	 * 
	 */
	public Pilote(Chassis chassis, EV3MediumRegulatedMotor pince) {
		super(chassis);
		this.chassis = chassis;
		this.pince = pince;
	}
	/**
	 *"prendre" pilote, permet de prendre un palet.
	 * 
	 */
	public void prendre() {
		pince.rotate(-1300);
	}
	/**
	 * lacher permet de lacher ou deposer l'objet
	 */
	public void lacher() {
		pince.rotate(1300);
	}
	/**
	 * vitesseMax permet d'obtenir la vitesse maximale.
	 * 
	 */
	public void vitesseMax() {
		chassis.getMaxLinearSpeed();
		chassis.getMaxAngularSpeed();
	}
	/**
	 *reglerVitesse Met à jour la vitesse quand le robot est en marche.
	 * @param linear est la valeur de la vitesse linaire
	 * @param angular est la valeur de la vitesse angulaire
	 */
	public void reglerVitesse(double linear, double angular) {
		chassis.setSpeed(linear, angular);
	}
	/**
	 * rotate permet de tourner le robot à un ange précie
	 * @param angle est l'angle de la rotation.
	 */
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
