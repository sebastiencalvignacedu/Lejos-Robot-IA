
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.*;
import java.util.Scanner;
/**
 * <b> La classe Agent represente ......</b>
 * <p>
 * description de la classe ????????
 * </p>
 * @author membre du Groupe
 * @version 8.0
 * 
 */

public class Agent {
	/**
	 * @see Port
	 * le " A,B,C,D " de l'agent sont les port des differents moteurs et peut-être modifié</br>
	 * <p>
	 * 
	 * </p>
	 */
	private Port A,B,C,D;
	/**
	 * @see EV3LargeRegulatedMotor
	 * <p></p>
	 */
	private EV3LargeRegulatedMotor moteurGauche,moteurDroit;
	/**
	 * @see EV3MediumRegulatedMotor
	 * 
	 */
	private EV3MediumRegulatedMotor pince;
	/**
	 * @see Wheel
	 * <p></p>
	 */
	private Wheel roueGauche, roueDroite;
	/**
	 * @see Wheel
	 * <p></p>
	 */
	private Chassis chassis;
	/**
	 * @see Wheel
	 * <p></p>
	 */
	private Pilote pilote;
	/**
	 * @see Wheel
	 * <p></p>
	 */
	private Capteurs capt;
	/**
	 * @see Wheel
	 * <p></p>
	 */
	private ActionCompose actionComp;
	/**
	 * 
	 */
	String couleurDepart,couleurArrivee; 
	/**
	 * 
	 */
	private Object [][] cartePalets = new Object[3][2]; 
	/**
	 * 
	 */
	private int butPalets = 0;
	/**
	 * 
	 */
	private int[] etat = new int[6];
	/**
	 * 
	 */
	/* 
	 * l'état au premier indice etat[0] est l'état courant
	 * 
	 * etat n1 ; liÃ© Ã  l'automate en "dur", recupère autant de palets que possible le plus vite possible
	 * etat n2 ; recherche en fonction de cartePalets
	 * etat n3 ; recherche alÃ©atoire
	 * etat n4 ; palet en possession
	 * etat n5 ; palet dÃ©tectÃ©
	 * [...] ;
	 * 
	 */
	/**
	 * Constructeur Agent.
	 * <P></p>
	 * 
	 */
	public Agent() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Position de dÃ©part? vert/bleu");
		couleurDepart = sc.nextLine();
		while( !(couleurDepart.equals("vert") && couleurDepart.equals("bleu")) ) {
			System.out.println("Position de dÃ©part? vert/bleu");
			couleurDepart = sc.nextLine();
		}
		sc.close();
		if(couleurDepart.equals("vert")) {
			couleurArrivee = "bleu";
		}
		else couleurArrivee = "vert";

		cartePalets[0][0]= couleurDepart;
		cartePalets[1][0]= "noir";
		cartePalets[2][0]= couleurArrivee;
		cartePalets[0][1]= 3;
		cartePalets[1][1]= 3;
		cartePalets[2][1]= 3;
		// ~~~ {{couleurDepart,3},{"noir",3},{couleurArrivee,3}};

		A = LocalEV3.get().getPort("A");
		B = LocalEV3.get().getPort("B");
		D = LocalEV3.get().getPort("D");
		moteurGauche = new EV3LargeRegulatedMotor(A);
		moteurDroit = new EV3LargeRegulatedMotor(B);
		pince = new EV3MediumRegulatedMotor(D);
		Wheel wheel1 = WheeledChassis.modelWheel(moteurGauche, 81.6).offset(-70);
		Wheel wheel2 = WheeledChassis.modelWheel(moteurDroit, 81.6).offset(70);
		// PENSER A DETERMINER LES VALEURS POUR UN TEST
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilote = new Pilote(chassis, pince);
		capt = new Capteurs();
		actionComp = new ActionCompose();

	}
	/**
	 * deplacementAleaV1 permet de.......
	 * <p>
	 * V1 est la forme la plus simple de deplacementAlea
	 * V1 ne prend pas en compte les obstacles
	 * se deplace de facon aleatoire dans la table de jeu en utilisant 
	 * ne prend PAS en compte detectionObjet (voir deplacementAleaV2)
	 * une nouvelle direction aleatoire (inspiration de bumper car)
	 * </p> 
	 */
	public void deplacementAleaV1() {
		double distanceAlea = Math.floor(Math.random()*10); // genere une distance entre 0 et 10 cm
		pilote.travel(distanceAlea);
		double angleAlea = Math.floor((Math.random()*720)-360); // genere un angle aleatoire entre -360 et 360 degrees
		pilote.rotate(angleAlea);
	}
	/**
	 * deplacementAleaV2 permet de.......
	 * <p>
	 * La V1 ne prend pas en compte les obstacles.
	 * se deplace de facon aleatoire dans la table de jeu en utilisant 
	 * detectionObjet pour eviter les mures et tourner angle pour prendre
	 * une nouvelle direction aleatoire (inspiration de bumper car)
	 * </p>
	 * 
	 */
	public void deplacementAleaV2() {
		double distanceAlea = Math.floor(Math.random()*10); // genere une distance entre 0 et 10 cm		
		pilote.travel(distanceAlea);
		double angleAlea = Math.floor((Math.random()*720)-360); // genere un angle aleatoire entre -360 et 360 degrees
		pilote.rotate(angleAlea);
	}
	/**
	 * sequence1 permet de....
	 * <p> </p>
	 */
	public void sequence1(){}
	/**
	 * sequence2 permet de....
	 * <p></p>
	 */
	public void sequence2(){}
	/**
	 * sequence3 permet de....
	 * <p></p>
	 * 
	 */
	public void sequence3(){}
	/**
	 * sequence4 permet de....
	 * <p></p>
	 */
	public void sequence4(){}
	/**
	 * sequence5 permet de....
	 * <p></p>
	 * 
	 */
	public void sequence5(){}

	/**
	 * detectionObjet de l'Agent permet de detecter un objet à une certaines distances.
	 * 
	 */
	// il semblerait interessant qu'il y ait un retour boolean ; true --> palet , false --> mur
	public void detectionObjet(){
		Action act = new Action();
		capt.actualise();
		float distanceObj = (float) capt.distance;

		if(distanceObj < (float)25) {
			return false;
		}
		act.avancerDistance(distanceObj - 25);
		capt.actualise();
		if(capt.distance < 25){
			return false;
		}
		return true;
	}
	/**
	 * allerBaseAdverse de l'Agent permet de....
	 * 
	 */
	public void allerBaseAdverse(){
		chercherLigne(); // le robot ce dirige sur une ligne
		if(capt.couleur.equals("bleu")) || capt.couleur.equals("noir")) || capt.couleur;equals("vert")){
			rotate((float)180);
			while(!capt.couleur.equals("white")) {
				forward();
			}
			// est ce que ça marche ? 
			pilote.lacher();
			// rotate((float) 180);
		}
		if(capt.couleur.equals("rouge") || capt.couleur.equals("jaune") || capt.couleur.equals("noir"){
			rotate((float)90);
			while(!capt.couleur.equals("white")) {
				forward();
			}
			// est ce que Ã§a marche ? 
			pilote.lacher();
			// rotate((float) 180);
		}
	}

	/**
	 *prendrePalet
	 */
	public void prendrePalet(){}
	/**
	 * calibrerAngle
	 */
	public void calibrerAngle(){}
	/**
	 * updateEtat
	 */
	public void updateEtat() {}
	/**
	 * chercherLigne
	 */
	public void chercherLigne(){}
	/**
	 * chercherPalet
	 */
	public void chercherPalet(){}
	/**
	 * suivreLigne
	 */
	public void suivreLigne(){}
	/**
	 * retourneSequence
	 */
	public void retourneSequence(){}
	/**
	 * testHomologation de l'Agent permet de ....
	 * <p></p>
	 */
	public void testHomologation(){

		/*Capteurs capt = new Capteurs();
		Action act = new Action();
		int cpt = 0;


		Button.ENTER.waitForPressAndRelease();

		act.RightMotor.setSpeed(200);
		act.LeftMotor.setSpeed(200);
		act.let();
		act.forward();

		while (!capt.couleur.equals("noir"))  {

			capt.actualise();
			System.out.println(capt.couleur +" - "+capt.distance);
			Delay.msDelay(50);


			}
		act.stop();


		while(capt.distance>0.4) {

			capt.actualise();
			System.out.println(capt.couleur +" - "+capt.distance);
			Delay.msDelay(50);
			act.rotate();
			cpt++;

				}

		act.stop();

		while(!capt.touche){
			capt.actualise();
			System.out.println(capt.touche);
			Delay.msDelay(50);
			capt.actualise();
			act.forward();
		}

		act.stop();
		act.pick();

		while(cpt>1) {
			act.rotate_anti();
			cpt--;
		}

		act.stop();
		act.forward();
		capt.actualise();

		while(!capt.couleur.equals("white")) {
			capt.actualise();
			System.out.println(capt.couleur);
			Delay.msDelay(50);
			}

			act.let();
			act.stop();
		 */

	}

	public static void main(String[] args) {

		//Button.ENTER.waitForPressAndRelease();
		// Tester la partie suivante pour deplacementAleaV1
		try {
			Agent agent = new Agent();
			agent.deplacementAleaV1();

			if(Button.ESCAPE.isDown()) {
				pilote.stop();
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Une");
			System.out.println("Erreur de Seb");
		}
		// fin de test 

	}
}
