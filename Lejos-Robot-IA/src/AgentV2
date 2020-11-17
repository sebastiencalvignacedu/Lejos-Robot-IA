//premier commit
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
	Pilote pilote;
	Capteurs capt;
	private ActionCompose actionComp;
	String couleurDepart,couleurArrivee; 
	private Object [][] cartePalets = new Object[3][2]; 
	private int butPalets = 0;
	private String[] etat = new String[6];
	/* 
	 * 
	 * etat n0 ; etat courant parmi n1,n2,n3,n4,n5. mise à jour du tableau par appel de updateEtat(). 
	 * etat n1 ; lié à l'automate en "dur", recupère autant de palets que possible le plus vite possible.
	 * etat n2 ; recherche en fonction de cartePalets. 
	 * etat n3 ; recherche aléatoire.
	 * etat n4 ; palet détecté, objectif de le saisir.
	 * etat n5 ; palet en possession.
	 * 
	 */
	
	public Agent() {
		
		//Scanner sc = new Scanner(System.in);
		//System.out.println("Position de départ? vert: GAUCHE - bleu: DROIT");
		//couleurDepart = sc.nextLine();
		
		// A MODIFIER, BESOIN POUR LES TEST
		
		/*while( !(couleurDepart.equals("vert") && couleurDepart.equals("bleu")) ) {
			System.out.println("Position de départ? vert/bleu");
			couleurDepart = sc.nextLine();
		}
		sc.close();
		if(couleurDepart.equals("vert")) {
			couleurArrivee = "bleu";
		}
		else couleurArrivee = "vert"; */
		couleurDepart = "bleu";
		couleurArrivee = "vert";
		cartePalets[0][0]= couleurDepart;
		cartePalets[1][0]= "noir";
		cartePalets[2][0]= couleurArrivee;
		cartePalets[0][1]= 3;
		cartePalets[1][1]= 3;
		cartePalets[2][1]= 3;
		// ~~~ {{couleurDepart,3},{"noir",3},{couleurArrivee,3}};
		
		etat[0]="";
		etat[1]="rush";
		etat[2]="carte";
		etat[3]="aleatoire";
		etat[4]="detection";
		etat[5]="saisi";
		
		A = LocalEV3.get().getPort("A");
		B = LocalEV3.get().getPort("B");
		D = LocalEV3.get().getPort("D");
		moteurGauche = new EV3LargeRegulatedMotor(A);
	    moteurDroit = new EV3LargeRegulatedMotor(B);
	    pince = new EV3MediumRegulatedMotor(D);
		Wheel wheel1 = WheeledChassis.modelWheel(moteurGauche, 29).offset(-70);
		Wheel wheel2 = WheeledChassis.modelWheel(moteurDroit, 29).offset(70);
		// PENSER A DETERMINER LES VALEURS POUR UN TEST
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilote = new Pilote(chassis, pince);
		capt = new Capteurs();
		actionComp = new ActionCompose();
		
	}
	
	public void testHomologationV2(){}
	public void prendrePalet(){}
	
	public void chercherLigne(String couleurCible){
		
		boolean trouve = false;
	    long tempsDebut = System.currentTimeMillis();
	    
	    while(System.currentTimeMillis() - tempsDebut < 180000) {
	    	
	    	if(couleurCible.equals("bleu") || couleurCible.equals("noir") || couleurCible.equals("vert")) {
	    		
	    		pilote.rotate(0 - pilote.rotation);
	    		pilote.forward();
	    		capt.actualise();		
	    		while(pilote.isMoving()) {
	    			if(capt.couleur.equals(couleurCible)) {
	    				pilote.stop();
	    				trouve = true;
	    			}
	    			if(capt.distance < 15 || capt.couleur.equals("blanc")){
	    				pilote.stop();
	    				pilote.rotate(180);
	    				pilote.forward();
	    			}
	    			capt.actualise();
	    			Delay.msDelay(50);
	    		}	
	    	}
	    	else {
	    		pilote.rotate(0 - pilote.rotation);
	    		pilote.rotate(90);
	    		pilote.forward();
	    		capt.actualise();		
	    		while(pilote.isMoving()) {
	    			if(capt.couleur.equals(couleurCible)) {
	    				pilote.stop();
	    				trouve = true ; 
	    			}
	    			if(capt.distance < 15 || capt.couleur.equals("blanc")){
	    				pilote.stop();
	    				pilote.rotate(180);
	    				pilote.forward();
	    			}
	    			capt.actualise();
	    			Delay.msDelay(50);
	    		}	
	    	}
	    		
	    }
	    
	    while(!trouve) {
	    	trouve = deplacementAleaV2(couleurCible);
	    }	
	}
	    
	public void chercherPalet(){}
	public void suivreLigne(){}
	public void sequence1(){}
	public void sequence2(){
		
		// cherche la ligne qui a le plus de palets
		// l'atteindre, chercher un palet, saisir un palet, mettre à jour carte
		// si recherche nulle, suivreLigne, mise à jour carte
		String couleurCible;
		if((int)cartePalets[0][1] >= (int)cartePalets[1][1]) {
			couleurCible = (String)cartePalets[0][0];
			if((int)cartePalets[0][1] >= (int)cartePalets[1][1]) {}
		}
		
		
	}
	public void sequence3(){}
	public void sequence4(){}
	public void sequence5(){}	
	public boolean detectionObjet(){return true;}
	
	public void retourneSequence() {
		
		if(etat[0]==etat[1]) {
			sequence1();
			return;
		}
		if(etat[0]==etat[2]) {
			sequence2();
			return;
		}
		if(etat[0]==etat[3]) {
			sequence3();
			return;
		}
		if(etat[0]==etat[4]) {
			sequence4();
			return;
		}
		if(etat[0]==etat[5]) {
			sequence5();
			return;
		}

		
	}
	
	public void updateEtat() {
		
		capt.actualise();
		
		if(capt.touche) {
			etat[0] = etat[5];
			return;
		}
		if(System.currentTimeMillis()<180000) {
			etat[0] = etat[1];
			return;
		}
		if(detectionObjet()) {
			etat[0] = etat[4];
			return;
		}
		if((int)cartePalets[0][1] < 0 || (int)cartePalets[1][1] < 0 || (int)cartePalets[2][1] < 0) {
			etat[0] = etat[2];
			return;
		}
		else etat[0] = etat[3];
	}
	
	public void deplacementAleaV2() {
		// Prise en compte des obstacles. 
		// Déplacement aléatoire ; en ligne droite (entre 0 et x cm) puis rotation (entre -360 et 360 degrés)  
		// si un object est détecté à moins de 15cm ou si le robot perçoit une ligne blanche, il effectue une rotation (entre -180 et 180 degrés) et l'on quitte l'éxécution de la méthode

		double distanceAlea = Math.floor(Math.random()*60); // genere une distance entre 0 et 10 cm		
		pilote.travel(distanceAlea);		
		capt.actualise();		
		while(pilote.isMoving()) {
			if(capt.distance < 15 || capt.couleur.equals("blanc")){
				pilote.stop();
				pilote.rotate(Math.floor((Math.random()*360)-180));
				return;
			}
			capt.actualise();
			Delay.msDelay(50);
		}	
		double angleAlea = Math.floor((Math.random()*720)-360); // genere un angle aleatoire entre -360 et 360 degrees
		pilote.rotate(angleAlea);
	}
	
	public boolean deplacementAleaV2(String couleurStop) {
		// retourne true si une couleur cible est atteinte. celle-ci est une String en paramètre.
		// Prise en compte des obstacles. 
		// Déplacement aléatoire ; en ligne droite (entre 0 et x cm) puis rotation (entre -360 et 360 degrés)  
		// si un object est détecté à moins de 15cm ou si le robot perçoit une ligne blanche, il effectue une rotation (entre -180 et 180 degrés) et l'on quitte l'éxécution de la méthode

		double distanceAlea = Math.floor(Math.random()*60); // genere une distance entre 0 et 10 cm		
		pilote.travel(distanceAlea);		
		capt.actualise();		
		while(pilote.isMoving()) {
			if(capt.couleur.equals(couleurStop)) {
				pilote.stop();
				return true;
			}
			if(capt.distance < 15 || capt.couleur.equals("blanc")){
				pilote.stop();
				pilote.rotate(Math.floor((Math.random()*360)-180));
				return false;
			}
			capt.actualise();
			Delay.msDelay(50);
		}	
		double angleAlea = Math.floor((Math.random()*720)-360); // genere un angle aleatoire entre -360 et 360 degrees
		pilote.rotate(angleAlea);
		return false;
	}
	
	public void allerBaseAdverse(){
		
		chercheLigne(couleurArrivee);
		pilote.rotate(0 - pilote.rotation);
		pilote.forward();
		capt.actualise();
		while (!capt.couleur.equals("blanc"))  {
			capt.actualise();
			Delay.msDelay(50);
		}
		Delay.msDelay(1000);
		pilote.stop();
		calibrerAngle();
		pilote.lacher();
		Delay.msDelay(3000);
		pilote.travel(-30);
		pilote.backward();
		capt.actualise();
		while (!capt.couleur.equals("blanc"))  {
			capt.actualise();
			Delay.msDelay(50);
		}
		pilote.rotate(180);
		}
	
	public void calibrerAngle(){
		
		float min;
		pilote.rotate(0 - pilote.rotation);
		pilote.reglerVitesse(100, 100);
		pilote.rotate(-60);
		min = capt.distance;
		
		pilote.rotate(5);
		capt.actualise();
		
		while (capt.distance < min)  {
			min = capt.distance;
			pilote.rotation = 0;
			pilote.rotate(5);
			capt.actualise();
			Delay.msDelay(50);
			}
		pilote.vitesseMax();
		}
	
public void calibrerAngleV2(){
		
		float min;
		pilote.rotate(0 - pilote.rotation);
		pilote.reglerVitesse(100, 100);
		pilote.rotate(-60);
		min = capt.distance;
		
		pilote.rotate(120);
		capt.actualise();
		while (pilote.isMoving())  {
			if(capt.distance < min) {
				min = capt.distance;
				pilote.rotation = 0;
			}
			capt.actualise();
			Delay.msDelay(50);
			}
		pilote.vitesseMax();
		}
	
	public void chercheLigne(String couleur){
		
		deplacementAleaV2();
		capt.actualise();
		while (!capt.couleur.equals(couleur))  {
			capt.actualise();
			Delay.msDelay(50);
		}
		pilote.stop();
	}
	
	public void testHomologation(){
		
		/*
		Button.ENTER.waitForPressAndRelease();
		
		pilote.reglerVitesse(200,200);
		pilote.forward();
		capt.actualise;
		
		while ((!capt.couleur.equals("vert")) || (!capt.couleur.equals("noir")) || '!capt.couleur.equals("bleu")))  {
			
			capt.actualise();
			System.out.println(capt.couleur +" - "+capt.distance);
			Delay.msDelay(50);
			}
			
		pilote.stop();
		
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
		
		
	}
}
