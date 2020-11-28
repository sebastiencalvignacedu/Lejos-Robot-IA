//premier commit
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.*;

import java.lang.reflect.Field;
import java.util.Scanner;

// TODO : gérer variations de vitesse
// TODO : vérifier les stops
// TODO : vérifier récupérations via getters
// TODO : code en dur
// TODO : assurer ramassage palets et vérifier valeurs de la pince
// TODO : assurer les prises random et changement de procédure if(capt.isTouche()){prisePalet=true;return;}


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
	private int bleu=3;
	private int noir=3;
	private int vert=3;
	//private Object [][] cartePalets = new Object[3][2]; 
	private long topDepart;
	private int butPalets = 0;
	private boolean detectPalet;
	private boolean prisePalet = false;
	private String[] etat = new String[6];
	/*  
	 * etat n0 ; etat courant parmi n1,n2,n3,n4,n5. mise à jour du tableau par appel de updateEtat(). 
	 * etat n1 ; lié à l'automate en "dur", recupère autant de palets que possible le plus vite possible.
	 * etat n2 ; recherche en fonction de cartePalets. 
	 * etat n3 ; recherche aléatoire.
	 * etat n4 ; palet détecté, objectif de le saisir.
	 * etat n5 ; palet en possession.
	 */	
	public Agent() {
		couleurDepart = "bleu";
		couleurArrivee = "vert";
		/*
		cartePalets[0][0]= couleurDepart;
		cartePalets[1][0]= "noir";
		cartePalets[2][0]= couleurArrivee;
		cartePalets[0][1]= 3;
		cartePalets[1][1]= 3;
		cartePalets[2][1]= 3;
		// ~~~ {{couleurDepart,3},{"noir",3},{couleurArrivee,3}};
		 */	
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
		Wheel wheel1 = WheeledChassis.modelWheel(moteurGauche, 56).offset(-61);
		Wheel wheel2 = WheeledChassis.modelWheel(moteurDroit, 56).offset(61);
		// PENSER A DETERMINER LES VALEURS POUR UN TEST
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilote = new Pilote(chassis, pince);
		capt = new Capteurs();
	}
	public void setTopDepart(long topDepart) {
		this.topDepart=topDepart;
	}
	public long tempsEcouleSec() {
		return(Math.round((System.currentTimeMillis() - this.topDepart)/1000));
	}
	public long tempsEcouleSec(long debut) {
		return(Math.round((System.currentTimeMillis() - debut)/1000));
	}
	public void sequence1(){}
	public void sequence2(){
	// cherche la ligne qui a le plus de palets
	// l'atteindre, chercher un palet, saisir un palet, mettre à jour carte
	// si recherche nulle, suivreLigne, mise à jour carte		
		if(chercherLigneV2(ligneCible())) {
			this.detectPalet=chercherPalet();
			if(this.detectPalet) {
				ligneCible(1);
			}
			else {
				ligneCible(3);
			}
		}
		else {
			ligneCible(3);
		}
	}
	public String ligneCible() {
		if(this.bleu >= this.noir) {
			if(this.bleu >= this.vert) {
				return "bleu";
			}
			else return "vert";
		}
		else {
			if(this.noir >= this.vert) {
				return "noir";
			}
			else return "vert";
			}
	} 
	public void ligneCible(int minus) {
		if(this.bleu >= this.noir) {
			if(this.bleu >= this.vert) {
				this.bleu =- minus;
			}
			else this.vert =- minus;
		}
		else {
			if(this.noir >= this.vert) {
				this.noir =- minus;
			}
			else this.vert =- minus;
			}
	} 
	public void sequence3(){
		deplacementAleaV2();
		this.detectPalet = chercherPalet();
		while(this.detectPalet==false || capt.isTouche()==false) {
			deplacementAleaV2();
			this.detectPalet = chercherPalet();
		}
	}
	public void sequence4(){
		this.prisePalet = prendrePalet();
	}	
	public void sequence5(){
		allerBaseAdverse();
	}
	public int getBut() {
		return this.butPalets;
	}
	public boolean prendrePalet(){
		pilote.reglerVitesse(300, 300);
		pilote.forward();
		capt.actualise();
		while(pilote.isMoving()) {
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				this.detectPalet=false;
				this.prisePalet=true;
				return true;
			}
			if(capt.getDistance() < 0.15 || capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.rotate(180);
				pilote.travel(200);
				this.detectPalet=false;
				return false;
			}
			capt.actualise();
		}
		pilote.stop();
		this.detectPalet=false;
		return false;
	}	    
	public boolean chercherPalet(){		
		capt.actualise();
		while (capt.getDistance() > 0.6)  {
			pilote.rotate(10);
			Delay.msDelay(1000);
			capt.actualise();
			}
		pilote.rotate(20);
		if(detectionObjet()) {
			return true;
		}
		else return false;
	}
	public boolean detectionObjet(){
		pilote.reglerVitesse(100, 100);
		capt.actualise();
		if(capt.getDistance() > 0.2) {
			pilote.travel((capt.getDistance()*1000)-200);
		}
		Delay.msDelay(5000);
		capt.actualise();
		if(capt.getDistance() < 0.22) {
			return false;
		}
		else return true; 
	}
	public void retourneSequence() {		
		if(etat[0].equals(etat[1])) {
			sequence1();
			return;
		}
		if(etat[0].equals(etat[2])) {
			sequence2();
			return;
		}
		if(etat[0].equals(etat[3])) {
			sequence3();
			return;
		}
		if(etat[0].equals(etat[4])) {
			sequence4();
			return;
		}
		if(etat[0].equals(etat[5])) {
			sequence5();
			return;
		}	
	}
	public String recupNomVariable(Object o) throws java.lang.IllegalAccessException
	  {
	    Field[] f = this.getClass().getFields();
	    for(int i=0;i<f.length;i++)
	    {
	      if(o.equals(f[i].get(this))) 
	      {
	        return f[i].getName();
	      }
	    }
	    return "";
	  }
	public void updateEtat() {		
		capt.actualise();		
		if(capt.isTouche() || this.prisePalet) {
			etat[0] = etat[5];
			return;
		}
		if(tempsEcouleSec() < 120) {
			etat[0] = etat[1];
			return;
		}
		if(this.detectPalet) {
			etat[0] = etat[4];
			return;
		}
		if(this.bleu > 0 || this.vert > 0 || this.noir > 0) {
			etat[0] = etat[2];
			return;
		}
		else etat[0] = etat[3];
	}
	public void deplacementAleaV2() {
		// Prise en compte des obstacles. 
		// Déplacement aléatoire ; en ligne droite (entre 0 et x cm) puis rotation (entre -360 et 360 degrés)  
		// si un object est détecté à moins de 15cm ou si le robot perçoit une ligne blanche, il effectue une rotation (entre -180 et 180 degrés) et l'on quitte l'éxécution de la méthode

	    double tempsDebut = System.currentTimeMillis();
	    double tempsFin = tempsDebut + (Math.random()*15000)+3000;
	    double angleAlea = Math.floor((Math.random()*720)-360);
	    
		pilote.forward();	
		while(System.currentTimeMillis() < tempsFin && pilote.isMoving()) {
			capt.actualise();
			if(capt.getDistance() < 0.15 || capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.rotate(180);
				return;
			}
		}
		pilote.stop();
		pilote.rotate(angleAlea);
	}
	public boolean deplacementAleaV2(String couleurStop) {
		// retourne true si une couleur cible est atteinte. celle-ci est une String en paramètre.
		// Prise en compte des obstacles. 
		// Déplacement aléatoire ; en ligne droite (entre 0 et x cm) puis rotation (entre -360 et 360 degrés)  
		// si un object est détecté à moins de 15cm ou si le robot perçoit une ligne blanche, il effectue une rotation (entre -180 et 180 degrés) et l'on quitte l'éxécution de la méthode

		
		
		double tempsDebut = System.currentTimeMillis();
	    double tempsFin = tempsDebut + (Math.random()*15000)+3000;
	    double angleAlea = Math.floor((Math.random()*720)-360);
	    
		pilote.forward();	
		while(System.currentTimeMillis() < tempsFin && pilote.isMoving()) {
			capt.actualise();
			if(capt.couleur.equals(couleurStop)) {
				pilote.stop();
				return true;
			}
			if(capt.getDistance() < 0.15 || capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.rotate(180);
				return false;
			}
		}
		pilote.stop();
		pilote.rotate(angleAlea);
		return false;
	}
	public void allerBaseAdverse(){	
		chercherLigneV2(couleurArrivee);
		pilote.rotate(0 - pilote.rotation);
		pilote.forward();
		capt.actualise();
		while (!capt.getCouleur().equals("blanc"))  {
			capt.actualise();
		}
		Delay.msDelay(500);
		pilote.stop();
		calibrerAngle();
		pilote.lacher();
		this.prisePalet=false;
		this.butPalets++;
		Delay.msDelay(3000);
		pilote.travel(-100); // TODO : déterminer une valeur
		pilote.rotate(180);
		}
	public void calibrerAngle(){
		float min;
		pilote.rotate(0 - pilote.rotation);
		pilote.reglerVitesse(100, 100);
		pilote.rotate(-45);
		Delay.msDelay(1000);
		capt.actualise();
		min = capt.getDistance();
		pilote.rotate(10);
		Delay.msDelay(1000);
		capt.actualise();

		while (capt.distance < min)  {
			min = capt.getDistance();
			pilote.rotation = 0;
			pilote.rotate(10);
			Delay.msDelay(1000);
			capt.actualise();
			}
		pilote.rotate(0 - pilote.rotation);
		pilote.vitesseMax();
		}
	@Deprecated
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
	@Deprecated
	public void chercherLigne(String couleur){
		
		deplacementAleaV2();
		capt.actualise();
		while (!capt.couleur.equals(couleur))  {
			capt.actualise();
			Delay.msDelay(50);
		}
		pilote.stop();
	}
	public boolean chercherLigneV2(String couleurCible){		
		boolean trouve = false;	
		long debut = System.currentTimeMillis();
		if(couleurCible.equals("bleu") || couleurCible.equals("noir") || couleurCible.equals("vert")) {	    		
    		pilote.rotate(0 - pilote.rotation);
    		pilote.forward();
    		capt.actualise();		
    		while(pilote.isMoving() && tempsEcouleSec(debut) < 90) {
    			if(capt.getCouleur().equals(couleurCible)) {
    				pilote.stop();
    				return true;
    			}
    			if(capt.getDistance() < 0.15 || capt.getCouleur().equals("blanc")){
    				pilote.stop();
    				pilote.rotate(180);
    				pilote.forward();
    			}
    			capt.actualise();
    		}	
    	}
    	else {
    		pilote.rotate(90 - pilote.rotation);
    		pilote.forward();
    		capt.actualise();		
    		while(pilote.isMoving() && tempsEcouleSec(debut) < 90) {
    			if(capt.getCouleur().equals(couleurCible)) {
    				pilote.stop();
    				return true; 
    			}
    			if(capt.getDistance() < 0.15 || capt.getCouleur().equals("blanc")){
    				pilote.stop();
    				pilote.rotate(180);
    				pilote.forward();
    			}
    			capt.actualise();
    		}	
    	}
	    debut = System.currentTimeMillis();
	    while(!trouve && tempsEcouleSec(debut) < 90) {
	    	trouve = deplacementAleaV2(couleurCible);
	    }
	    return trouve;
	}
	@Deprecated
	public void testHomologation(){}
	public static void main(String[] args) {
		Agent agent = new Agent();
		Button.ENTER.waitForPressAndRelease();
		agent.setTopDepart(System.currentTimeMillis());
		while(agent.getBut() < 9 || agent.tempsEcouleSec() < 600) {
			agent.updateEtat();
			agent.retourneSequence();
		}		
	}
}
