import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.*;
import java.util.Scanner;


public class Agent {
	
	private Port A,B,C,D;
	private EV3LargeRegulatedMotor moteurGauche,moteurDroit;
	private EV3MediumRegulatedMotor pince;
	private Wheel roueGauche, roueDroite;
	private Chassis chassis;
	private Pilote pilote;
	private Capteurs capt;
	private ActionCompose actionComp;
	String couleurDepart,couleurArrivee; 
	private Object [][] cartePalets = new Object[3][2]; 
	private int butPalets = 0;
	private int[] etat = new int[5];
	/* 
	 * l'état au premier indice etat[0] est l'état courant
	 * 
	 * etat n1 ; lié à l'automate en "dur", recupère autant de palets que possible le plus vite possible
	 * etat n2 ; recherche en fonction de cartePalets
	 * etat n3 ; recherche aléatoire
	 * etat n4 ; palet en possession
	 * etat n5 ; 
	 * [...] ;
	 * 
	 */
	
	public Agent() {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Position de départ? vert/bleu");
		couleurDepart = sc.nextLine();
		while( !(couleurDepart.equals("vert") && couleurDepart.equals("bleu")) ) {
			System.out.println("Position de départ? vert/bleu");
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
	
	public void deplacementAleaV1(int temps) {
		// La V1 ne prend pas en compte les obstacles.
		// se deplace de facon aleatoire dans la table de jeu en utilisant 
		// detectionObjet pour eviter les mures et tourner angle pour prendre
		// une nouvelle direction aleatoire (inspiration de bumper car)

		long heureDepart = System.currentTimeMillis();
		long heureDif = 0;
		
		while (heureDif<temps*1000) {
			int distanceAlea = Math.floor(Math.random()*10); // genere une distance entre 0 et 10 cm
			monActionneur.travel(distanceAlea);
			int angleAlea = Math.floor((Math.random()*720)-360); // genere un angle aleatoire entre -360 et 360 degrees
			monActionneur.rotate(angleAlea);
			heureDif = System.currentTimeMillis() - heureDepart;
		}
	}
	
	
	public void updateEtat() {}
	public void retourneSequence() {}
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
		
		Button.ENTER.waitForPressAndRelease();

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		}
	}
