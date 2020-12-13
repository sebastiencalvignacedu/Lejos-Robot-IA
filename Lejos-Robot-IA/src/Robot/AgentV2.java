package Robot;
//OLD agent5

import lejos.hardware.Button;
import lejos.utility.Delay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.*;


//régler allerbaseadverse
//rotation?
// sequence rush?
// limites rouge-jaune
// recalibrage en fonction couleur

public class AgentV2 {
	
	private Port A,B,C,D;
	private EV3LargeRegulatedMotor moteurGauche,moteurDroit;
	private EV3MediumRegulatedMotor pince;
	private Wheel roueGauche, roueDroite;
	private Chassis chassis;
	Pilote pilote;
	Capteurs capt;
	String couleurDepart,couleurArrivee; 
	private long topDepart;
	private int butPalets;
	private boolean detectPalet;
	private boolean prisePalet;
	private boolean rush; 
	private String[] etat = new String[5];
	private final static int MIN_ROT = 5;
    private final static int MED_ROT = 10;
    private final static int MAX_ROT = 15;

	public AgentV2() {
		butPalets = 0;
		couleurDepart = "bleu";
		couleurArrivee = "vert";
		etat[0]="";
		etat[1]="paletPremier";
		etat[2]="rechercheAlea";
		etat[3]="paletDetecte";
		etat[4]="paletSaisi";	
		rush = true;
		detectPalet = false;
		prisePalet = false;
		A = LocalEV3.get().getPort("A");
		B = LocalEV3.get().getPort("B");
		D = LocalEV3.get().getPort("D");
		moteurGauche = new EV3LargeRegulatedMotor(A);
	    moteurDroit = new EV3LargeRegulatedMotor(B);
	    pince = new EV3MediumRegulatedMotor(D);
		Wheel wheel1 = WheeledChassis.modelWheel(moteurGauche, 56).offset(-62.5);
		Wheel wheel2 = WheeledChassis.modelWheel(moteurDroit, 56).offset(62.5);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilote = new Pilote(chassis, pince);
		capt = new Capteurs();
	}
	
	public void setTopDepart(long topDepart) {
		this.topDepart=topDepart;	
	}
	public long tempsTotalSec() {
		return(Math.round((System.currentTimeMillis() - this.topDepart)/1000));
	}
	public long tempsEcouleSec(long debut) {
		return(Math.round((System.currentTimeMillis() - debut)/1000));
	}
	public int getBut() {
		return this.butPalets;
	}
	
	public boolean prendrePalet(){
		pilote.reglerVitesse(200, 200);
		pilote.forward();
		capt.actualise();
		while(pilote.isMoving()) {
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				this.detectPalet=false;
				return true;
			}
			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.rotate(180);
				pilote.travel(400);
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
		float dist;
		int i=0;
		capt.actualise();
		dist=capt.getDistance();
		while (dist > 0.8 && i<36)  {
			pilote.rotate(10);
			capt.actualise();
			dist=capt.getDistance();
			i++;
		}
		if(i==36) {
			return false;
		}
		else if(dist < 0.4) {
			pilote.rotate(MAX_ROT);
			return true;	
		}
		else if(dist < 0.6) {
			pilote.rotate(MED_ROT);
			return true;
		}
		else {
			pilote.rotate(MIN_ROT);
			return true;	
		}
	}
	public boolean detectionObjet(){
		
		/*
		 * capt.actualise();
		if(capt.getDistance() > 0.2) {
			pilote.travel((capt.getDistance()*1000)-200);
			while(pilote.isMoving()) {
				if(capt.getCouleur().equals("blanc") || capt.getDistance() < 0.15) {
					pilote.stop();
					pilote.rotate(180);
					return false;
				}
				capt.actualise();
			}
		}
		capt.actualise();
		if(capt.getDistance() < 0.22 || capt.getDistance() > 0.4) {
			return false;
		}
		else return true;
		 */
		return true;
	}
	public void testInitialisationV3() {
		capt.actualise();
		pilote.forward();
		while (!capt.isTouche()) {
			capt.actualise();
		}
		pilote.stop();
		pilote.prendre();
		pilote.rotate(45);
		pilote.travel(500);
		pilote.rotate(-45);
		pilote.forward();
		while(capt.getCouleur()!="blanc") {
			capt.actualise();
		}
		pilote.stop();
		pilote.lacher();
	}

	public void secours() {
		boolean trouve=false;
		boolean attrape=false;
		boolean but=false;
		if(this.rush==true) {
			testInitialisationV3();
			pilote.rotate(180);
			pilote.travel(300);
			this.rush=false;
		}
		deplacementAleaV2();
		trouve = chercherPalet();
		while(trouve==false) {
			deplacementAleaV2();
			if(this.prisePalet) {
				return;
			}
			trouve = chercherPalet();
		}
		if(trouve && this.prisePalet==false) {
			attrape=prendrePalet();
		}
		if(attrape || this.prisePalet) {
			//but=allerBaseAdverse();
			pilote.rotate(0 - pilote.getAngle());
			pilote.forward();
			capt.actualise();
			while(pilote.isMoving()) {
				if(capt.getCouleur().equals("blanc")) {
					pilote.stop();
					calibrerAngle();
					pilote.lacher();
					pilote.travel(-300);
					pilote.rotate(180);
					this.butPalets++;
					return;
				}
				if(capt.getDistance()<0.2) {
					pilote.lacher();
					pilote.rotate(180);
					pilote.travel(300);
					return;
				}
			}
			
		}
		if(but) {
			this.butPalets++;
		}
	}
	public void deplacementAleaV2() {
		// Prise en compte des obstacles. 
		// Déplacement aléatoire ; en ligne droite (entre 0 et x cm) puis rotation (entre -360 et 360 degrés)  
		// si un object est détecté à moins de 15cm ou si le robot perçoit une ligne blanche, il effectue une rotation (entre -180 et 180 degrés) et l'on quitte l'éxécution de la méthode

	    double tempsFin = System.currentTimeMillis() + (Math.random()*10000)+2000;
	    double angleAlea = Math.floor((Math.random()*720)-360);
	    pilote.reglerVitesse(300,300);
		pilote.forward();
		capt.actualise();
		while(System.currentTimeMillis() < tempsFin && pilote.isMoving()) {
			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc") || capt.getCouleur().equals("jaune") || capt.getCouleur().equals("rouge")) {
				pilote.stop();
				pilote.rotate(180);
				return;
			}
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				this.prisePalet=true;
				return;
			}
			capt.actualise();
		}
		pilote.stop();
		pilote.rotate(angleAlea);
	}
	public void deplacementAleaV2Bis() {
		// Prise en compte des obstacles. 
		// Déplacement aléatoire ; en ligne droite (entre 0 et x cm) puis rotation (entre -360 et 360 degrés)  
		// si un object est détecté à moins de 15cm ou si le robot perçoit une ligne blanche, il effectue une rotation (entre -180 et 180 degrés) et l'on quitte l'éxécution de la méthode

	    double tempsFin = System.currentTimeMillis() + (Math.random()*10000)+2000;
	    double angleAlea = Math.floor((Math.random()*720)-360);
	    pilote.reglerVitesse(300,300);
		pilote.forward();	
		while(System.currentTimeMillis() < tempsFin && pilote.isMoving()) {
			capt.actualise();
			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc") || capt.getCouleur().equals("jaune") || capt.getCouleur().equals("rouge")) {
				pilote.stop();
				pilote.rotate(180);
				pilote.travel(200);
				return;
			}
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				this.prisePalet=true;
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
	    pilote.vitesseMax();
		pilote.forward();	
		while(System.currentTimeMillis() < tempsFin && pilote.isMoving()) {
			capt.actualise();
			if(capt.getCouleur().equals(couleurStop)) {
				pilote.stop();
				return true;
			}
			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.rotate(180);
				return false;
			}
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				this.prisePalet=true;
				return false;
			}
		}
		pilote.stop();
		pilote.rotate(angleAlea);
		return false;
	}
	public boolean allerBaseAdverse(){	
		//double tempsFin = System.currentTimeMillis() + 45000;
		//while(System.currentTimeMillis() < tempsFin) {
			chercherLigneV2(couleurArrivee);
			pilote.rotate(0 - pilote.getAngle());
			pilote.forward();
			capt.actualise();
			while(pilote.isMoving()) {
				if(capt.getCouleur().equals("blanc")) {
					pilote.stop();
					calibrerAngle();
					pilote.lacher();
					pilote.travel(-200); // 
					pilote.rotate(180);
					return true;
				}
				if(capt.getDistance() < 0.2) {
					pilote.stop();
					pilote.rotate(180);
					return false;
				}
				capt.actualise();
			}
		//}
		return false;
	}	
	public void calibrerAngle(){
		float min;
		pilote.rotate(0 - pilote.getAngle());
		pilote.rotate(-45);
		capt.actualise();
		min = capt.getDistance();
		pilote.rotate(10);
		capt.actualise();
		while (capt.getDistance() < min)  {
			min = capt.getDistance();
			pilote.setAngle(0);
			pilote.rotate(5);
			Delay.msDelay(200);
			capt.actualise();
			}
		pilote.rotate(0 - pilote.getAngle());
		}
	public void calibrerAngleBis() {
	    double tempsFin = System.currentTimeMillis() + 60000;
	    double angleAlea = Math.floor((Math.random()*720)-360);
		boolean cal = false;
		float min;
		String couleur="";
		pilote.forward();
		capt.actualise();
		while(pilote.isMoving() && System.currentTimeMillis() < tempsFin) {
			if(capt.getCouleur().equals("rouge") || capt.getCouleur().equals("jaune")) {
				pilote.stop();
				cal=true;
			}
			if(capt.getCouleur().equals("blanc") || capt.getDistance()<0.2) {
				pilote.stop();
				pilote.rotate(180);
				pilote.travel(200);
				pilote.rotate(angleAlea);
				pilote.forward();
			}
			couleur=capt.getCouleur();
			capt.actualise();
		}
		if(cal) {
			capt.actualise();
			min = capt.getDistance();
			pilote.rotate(10);
			capt.actualise();
			for(int i=0;i<36;i++) {
				if(capt.getDistance() < min) {
					min = capt.getDistance();
					pilote.setAngle(0);
				}
				pilote.rotate(10);
				capt.actualise();
			}
			pilote.rotate(0 - pilote.getAngle());
			if(couleur.equals("jaune")) {
				pilote.setAngle(-90);
			}
			else {
				pilote.setAngle(90);
			}
			pilote.travel(-300);
			pilote.rotate(0 - pilote.getAngle());
		}
	}
	public void calibrerAngle(String couleurCible) {
		// TODO : penser angles suivant ligne depart etc
		deplacementAleaV2(couleurCible);
		float min;
		capt.actualise();
		min = capt.getDistance();
		pilote.rotate(10);
		capt.actualise();
		for(int i=0;i<36;i++) {
			if(capt.getDistance() < min) {
				min = capt.getDistance();
				pilote.setAngle(0);
			}
			pilote.rotate(10);
			capt.actualise();
		}
		pilote.rotate(0 - pilote.getAngle());
		pilote.travel(-200);
		pilote.rotate(180);
	}
	public boolean chercherLigneV2(String couleurCible){		
		boolean trouve = false;	
		long debut = System.currentTimeMillis();
		if(couleurCible.equals("bleu") || couleurCible.equals("noir") || couleurCible.equals("vert")) {	    		
    		pilote.rotate(0 - pilote.getAngle());
    		pilote.forward();
    		capt.actualise();		
    		while(pilote.isMoving() && tempsEcouleSec(debut) < 60) {
    			if(capt.getCouleur().equals(couleurCible)) {
    				pilote.stop();
    				return true;
    			}
    			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc")){
    				pilote.stop();
    				pilote.rotate(180);
    				pilote.forward();
    			}
    			capt.actualise();
    		}	
    	}
    	else {
    		pilote.rotate(90 - pilote.getAngle());
    		pilote.forward();
    		capt.actualise();		
    		while(pilote.isMoving() && tempsEcouleSec(debut) < 60) {
    			if(capt.getCouleur().equals(couleurCible)) {
    				pilote.stop();
    				return true; 
    			}
    			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc")){
    				pilote.stop();
    				pilote.rotate(180);
    				pilote.forward();
    			}
    			capt.actualise();
    		}	
    	}
	    debut = System.currentTimeMillis();
	    while(!trouve && tempsEcouleSec(debut) < 60) {
	    	trouve = deplacementAleaV2(couleurCible);
	    }
	    return trouve;
	}
	
	public void sequence1() {
		Homologation();
		this.rush=false;
	}
	
	/*
	 * 		boolean pris=false;
		boolean but=false;
		boolean ligne=false;
		pilote.reglerVitesse(300, 300);
		pilote.forward();
		capt.actualise();
		while(pilote.isMoving()) {
			if(capt.isTouche()) {
				pilote.stop();
				pris=true;
			}
			if(capt.getCouleur().equals("bleu") || capt.getCouleur().equals("noir") || capt.getCouleur().equals("vert")) {
				pilote.stop();
				ligne=true;
			}
			if(capt.getCouleur().equals("blanc") || capt.getDistance()<0.2) {
				pilote.stop();
				pilote.rotate(180);
				this.rush=false;
				return;
			}
			capt.actualise();
		}
		if(pris) {
			pilote.rotate(22.5);
			pilote.forward();
			capt.actualise();
			while(pilote.isMoving() && tempsTotalSec()<60) {
				if(capt.getCouleur().equals("blanc")) {
					pilote.stop();
					pilote.lacher();
					pilote.travel(-200);
					pilote.rotate(180);
					this.butPalets++;
					this.rush=false;
					return;
				}
				capt.actualise();
			}
		}
		if(ligne) {
			while(!pris && tempsTotalSec()<60) {
				chercherPalet();
				pris=prendrePalet();
			}
			but=allerBaseAdverse();
			if(but) {
				this.butPalets++;
				this.rush=false;
				return;
			}
		}
		this.rush=false;
		return;
	}
	 */
		/*
		 * while(pilote.isMoving()) {
			if(capt.getCouleur().equals("noir")) {
				pilote.stop();
				while(!pris) {
					chercherPalet();
					pris=prendrePalet();
				}
				but=allerBaseAdverse();
				if(but) {
					this.butPalets++;
					this.rush=false;
					return;
				}
			}
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				pilote.rotate(angle);
				pilote.forward();
				capt.actualise();
				while(pilote.isMoving()) {
					if(capt.getCouleur().equals(couleurArrivee)) {
						pilote.stop();
						pilote.rotate(0-angle);
						pilote.forward();
						capt.actualise();
						while(pilote.isMoving()) {
							if(capt.getCouleur().equals("blanc")) {
								pilote.stop();
								pilote.lacher();
								pilote.travel(-200);
								pilote.rotate(180);
								this.butPalets++;
								this.rush=false;
								return;
							}
							capt.actualise();
						}
					}
					if(capt.getCouleur().equals("blanc")) {
						pilote.stop();
						pilote.lacher();
						pilote.travel(-200);
						pilote.rotate(0 - pilote.getAngle() + 180);
						this.butPalets++;
						this.rush=false;
						return;
					}
					capt.actualise();
				}
			}
			capt.actualise();
		}
		Delay.msDelay(3000);	
	}
		 */
		
	public void sequence2(){
		deplacementAleaV2();
		this.detectPalet = chercherPalet();
		while(this.detectPalet==false) {
			deplacementAleaV2();
			if(this.prisePalet) {
				return;
			}
			this.detectPalet = chercherPalet();
		}
	}
	public void sequence3(){
		this.prisePalet = prendrePalet();
		if(this.prisePalet) {
			this.detectPalet=false;
		}
	}	
	public void sequence4(){
		boolean but=false;
		int essai = 0;
		while(essai<2) {
			but=allerBaseAdverse();
			if(but) {
				this.prisePalet=false;
				this.butPalets++;
				return;
			}
			essai++;
		}
		pilote.lacher();
		this.prisePalet=false;
		calibrerAngleBis();
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
	}
	public void updateEtat() {		
		capt.actualise();
		if(this.rush) { // TODO : enlever 2eme+3eme condition
			etat[0] = etat[1];
			return;
		}
		else if(this.detectPalet) {
			etat[0] = etat[3];
			return;
		}
		else if(this.prisePalet) {
			etat[0] = etat[4];
			return;
		}
		else if (this.detectPalet==false && this.prisePalet==false) { 
			etat[0] = etat[2];
		}
	}
	public void afficheEtat() {
		//System.out.println(etat[0]+"-D"+this.detectPalet+"-P"+ this.prisePalet);
		//System.out.println("temps:"+ (boolean)(tempsTotalSec() < 60));
		System.out.println(etat[0]);
	}
	
	public void Homologation(){
		boolean pris=false;
		pilote.reglerVitesse(200, 200);
		pilote.forward();
		capt.actualise();
		while(pilote.isMoving()) {
			if(capt.getCouleur().equals("bleu") || capt.getCouleur().equals("noir") || capt.getCouleur().equals("vert")) {
				pilote.stop();
			}
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				pris=true;
			}
			capt.actualise();
		}
		Delay.msDelay(3000);
		while(!pris && tempsTotalSec() < 60) {
			chercherPalet();
			pris=prendrePalet();
		}
		if(!pris) {
			this.rush=false;
			return;
			}
		pilote.rotate(0 - pilote.getAngle());
		pilote.forward();
		capt.actualise();
		while(pilote.isMoving()) {
			if(capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.lacher();
				pilote.travel(-200);
				pilote.rotate(180);
				this.butPalets++;
				this.rush=false;
				return;
			}
			if(capt.getDistance() <0.2) {
				pilote.rotate(180);
				pilote.stop();
				this.rush=false;
				return;

			}
			capt.actualise();
		}
		//allerBaseAdverse();
		this.rush=false;
	}
	
	public static void main(String[] args) {
		AgentV2 agent = new AgentV2();	

		/*
		 * Button.ENTER.waitForPressAndRelease();
		agent.pilote.rotate(90);
		agent.pilote.rotate(90);
		agent.pilote.rotate(90);
		agent.pilote.rotate(-270);
		agent.pilote.rotate(-45);
		agent.pilote.rotate(0 - agent.pilote.getAngle());
		 */
		Button.ENTER.waitForPressAndRelease();
		agent.setTopDepart(System.currentTimeMillis());
		while(agent.getBut() < 9 || agent.tempsTotalSec() < 600) {
			agent.secours();
		}
		
		Button.ENTER.waitForPressAndRelease();
		agent.setTopDepart(System.currentTimeMillis());
		while(agent.getBut() < 9 || agent.tempsTotalSec() < 600) {
			agent.updateEtat();
			agent.afficheEtat();
			agent.retourneSequence();
		}
	}
}