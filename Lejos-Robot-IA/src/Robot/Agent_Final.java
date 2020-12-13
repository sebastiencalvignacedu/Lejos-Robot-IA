package Robot;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.utility.Delay;
public class Agent_Final {
	private Port A,B,C,D;
	private EV3LargeRegulatedMotor moteurGauche,moteurDroit;
	private EV3MediumRegulatedMotor pince;
	private Wheel roueGauche, roueDroite;
	private Chassis chassis;
	private Pilote pilote;
	private Capteurs capt;
	private String couleurDepart,couleurArrivee;
	private long topDepart;
	private int but;
	private String derniereLigneTraversee;
	private boolean paletDetecte;
	private boolean paletSaisi;
	private boolean premierPalet;
	private String[] etat = new String[5];
	private final static int MIN_ROT = 5;
    private final static int MED_ROT = 10;
    private final static int MAX_ROT = 15;
	
    public Agent_Final() {
		but = 0;
		couleurDepart = "bleu";
		couleurArrivee = "vert";
		derniereLigneTraversee ="";
		etat[0]="";
		etat[1]="premierPalet";
		etat[2]="rechercheAleatoire";
		etat[3]="paletDetecte";
		etat[4]="paletSaisi";	
		premierPalet = true;
		paletDetecte = false;
		paletSaisi = false;
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
		topDepart=System.currentTimeMillis();
		}
	
	public long tempsTotalSec() {
		/*
		 * @return long, correspondant au temps total écoulé (approximé en secondes) depuis l'initialisation de l'attribut topDepart via la création d'une instance de Agent_Final.
		 */
		return(Math.round((System.currentTimeMillis() - this.topDepart)/1000));
	}
	public int getBut() {
		/*
		 * @return int, attribut correspondant virtuellement au nombre de palet(s) marqué(s) lors de l'éxécution du main de cette classe. 
		 */
		return but;
	}
	
	public void deplacementAleatoire() {
		/*
		 * permet de se déplacer selon une valeur de temps aléatoirement généré entre 0 et 10000Ms,
		 * puis d'effectuer une rotation d'une valeur générée aléatoirement entre 0 et 360°
		 * 
		 * si le capteur de distance perçoit une valeur inférieure à ~20cm ou que le capteur couleur détecte une ligne blanche, on effectue une rotation de 180° avant de sortir de la méthode
		 * si le capteur de pression est activé durant le déplacement, on met à jour l'attribut paletSaisi et l'on sort de la méthode
		 * également, on met à jour l'attribut derniereLigneTraversee si l'on perçoit une ligne de couleur vert ou bleu via le capteur couleur
		 */
		double tempsFin = System.currentTimeMillis() + Math.floor(Math.random()*10000);
	    double angleAlea = Math.floor(Math.random()*360);
		pilote.reglerVitesse(400, 400);
		pilote.forward();	
		while(System.currentTimeMillis() < tempsFin && pilote.isMoving()) {
			capt.actualise();
			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.rotate(180);
				return;
			}
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				paletSaisi=true;
				return;
			}
			if(capt.getCouleur().equals("vert") || capt.getCouleur().equals("bleu")) {
				derniereLigneTraversee=capt.getCouleur();
			}
		}
		pilote.stop();
		pilote.rotate(angleAlea);
	}
	public boolean chercherPalet() {
		/*Méthode qui permet de détecter un objet avant de tester si l’objet est un palet ou non. 
		 * Si on effectue 36 rotations on quitte la méthode car il y aurait trop d'imprécision
		 * @return un boolean qui indique si le robot a trouvé un palet. true si on a un palet. false dans les autres cas
		 */
		float dist;
		int i=0;
		pilote.reglerVitesse(200, 200);
		capt.actualise();
		dist=capt.getDistance();
		while (dist > 0.8 && i<36)  {
			pilote.rotate(5);
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
	public boolean prendrePalet() {
		/*
		 * l'on part du principe que l'objet palet a été détecté avant l'appel de  @see prendrePalet()
		 * @return boolean, true si durant un déplacement en ligne droite, le capteur de pression est activé, 
		 * l'action d'attraper est réalisée
		 * false si lors du déplacement sont perçus une ligne blanche via le capteur couleur 
		 * ou une distance inférieure à ~20cm sur le capteur distance, après avoir effectué une rotation de 180°
		 */
		pilote.reglerVitesse(200, 200);
		pilote.forward();
		while(pilote.isMoving()) {
			capt.actualise();
			if(capt.isTouche()) {
				pilote.stop();
				pilote.prendre();
				return true;
			}
			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.rotate(180);
				return false;
			}
		}
		return false;
	}
	public boolean marquerPalet() {
		/*
		 * cette méthode impose un temps maximal d'execution d'environ 60 sec et renvoie false à l'échéant.
		 * @return boolean, true ssi le robot perçoit une ligne blanche et possède l'information de l'attribut derniereLigneTraversee congruente avec la valeur de l'attribut ligneArrivee
		 * même fonctionnalité de sécurité de distance et de mise à jour de derniereLigneTraversee lors du déplacement que les méthodes précédentes
		 */
		long tempsFin = System.currentTimeMillis() + 60000;
		pilote.rotate(0 - pilote.getAngle());
		pilote.reglerVitesse(400, 400);
		pilote.forward();
		while(pilote.isMoving() && System.currentTimeMillis() < tempsFin) {
			
			capt.actualise();
			
			if(capt.getCouleur().equals("blanc")){
				pilote.stop();
				if(derniereLigneTraversee.equals(couleurArrivee) || !derniereLigneTraversee.equals(couleurDepart)) {
					calibrer();
					pilote.lacher();
					pilote.travel(-200);  
					pilote.rotate(180);
					return true;
				}
				else if(derniereLigneTraversee.equals(couleurDepart)) {
					pilote.rotate(180);
					pilote.forward();
					capt.actualise();
				}				
			}
			if(capt.getDistance() < 0.2) {
				pilote.stop();
				pilote.rotate(180);
				return false;
			}
			if(capt.getCouleur().equals("vert") || capt.getCouleur().equals("bleu")) {
				derniereLigneTraversee=capt.getCouleur();
			}
		}
	return false;
	}	
	public void calibrer(){
		/*
		 * permet de mettre à jour la valeur d'attribut rotation de l'instance liée de Pilote.
		 * en effectuant des rotations successives de 5° sur une amplitude de 90°; 
		 * on cherche à déterminer la valeur minimale perçue par le capteur de distance
		 * lorsque cette valeur est atteinte, l'on considère être en face du mur de la zone de but
		 * cette méthode sert à corriger la différence entre la valeur théorique de rotation et la valeur réelle;
		 *  due au cumul des imprécisions et contacts avec les murs du terrain
		 *  @return void 
		 */
		float min;
		pilote.reglerVitesse(200, 200);
		pilote.rotate(0 - pilote.getAngle());
		pilote.rotate(-45);
		capt.actualise();
		min = capt.getDistance();
		for(int i=0;i<18;i++) {
			pilote.rotate(MIN_ROT);
			capt.actualise();
			if(capt.getDistance() < min) {
				min = capt.getDistance();
				pilote.setAngle(0);
			}
		}
		pilote.rotate(0 - pilote.getAngle());
		}
	public boolean marquerPalet2() {
		/*
		 * permet de rechercher la ligne d'enbut adverse lorsque échec de la méthode marquerPalet()
		 * par des appels successifs à chercherBut()
		 * @return boolean, true si chercherBut() retourne true avant la fin de l'échéance de temps de 60sec,
		 *  false sinon. 
		 */
		boolean trouve;
		long tempsFin = System.currentTimeMillis() + 60000;
		pilote.reglerVitesse(400, 400);
		trouve=chercherBut();
		while(System.currentTimeMillis() < tempsFin && !trouve) {
			trouve=chercherBut();
		}
		if(trouve) {
			calibrer();
			pilote.lacher();
			pilote.travel(-200);  
			pilote.rotate(180);
			return true;
		}
		else return false;
	}
	public boolean chercherBut() {
		/*
		 * permet de recherche la ligne d'enbut adverse sur un fonctionnement similaire à deplacementAleatoire()
		 * @return boolean 
		 * true si perçue via le capteur couleur et l'historique de la dernière ligne parallèle à la cible via l'attribut derniereLigneTraversee
		 * false avec rotation si distance perçue trop faible ou mauvais enbut
		 * mise à jour durant le déplacement @see derniereLigneTraversee 
		 */
		double tempsFin = System.currentTimeMillis() + Math.floor(Math.random()*10000);
	    double angleAlea = Math.floor(Math.random()*360);
		pilote.reglerVitesse(400, 400);
		pilote.forward();	
		while(System.currentTimeMillis() < tempsFin && pilote.isMoving()) {
			capt.actualise();
			if(capt.getDistance() < 0.2) {
				pilote.stop();
				pilote.rotate(180);
				return false;
			}
			if(capt.getCouleur().equals("blanc")){
				pilote.stop();
				if(derniereLigneTraversee.equals(couleurArrivee) || !derniereLigneTraversee.equals(couleurDepart)) {
					return true;
				}
				else if(derniereLigneTraversee.equals(couleurDepart)) {
					pilote.rotate(180);
					return false;
				}				
			}
			if(capt.getCouleur().equals("vert") || capt.getCouleur().equals("bleu")) {
				derniereLigneTraversee=capt.getCouleur();
			}
		}
		pilote.stop();
		pilote.rotate(angleAlea);
		return false;
	}
	
	public void sequence1() {
		/*
		 * appel unique à Homologation()
		 * @see Homologation()
		 * mise à jour de l'attribut premierPalet pour ne plus retourner cette séquence après appel de updateEtat()
		 *  et retouneSequence() dans le main
		 *  @return void
		 */
		Homologation();
		premierPalet=false;
	}
	public void sequence2() {
		/*
		 * permet de réaliser autant de déplacements aléatoires et de recherches de palet que nécessaires
		 * arret de la séquence lorsque un palet est pris ou detecte,
		 *  l'attribut correspondant est alors mis à jour pour être traité par @see updateEtat()
		 *  return void
		 */
		deplacementAleatoire();
		paletDetecte = chercherPalet();
		while(!paletDetecte) {
			deplacementAleatoire();
			if(paletSaisi) {
				return;
			}
			paletDetecte = chercherPalet();
		}	
	}
	public void sequence3() {
		/*
		 * permet de se saisir d'un palet
		 * si prendrePalet() réussit, mise à jour de l'attribut paletSaisi et de paletDetecte
		 * si non, palet considéré perdu, mise à jour de paletDetecte
		 * @return void
		 */
		if(prendrePalet()) {
			paletSaisi=true;
			paletDetecte=false;
		}
		else paletDetecte=false;
	}
	public void sequence4() {
		/*
		 * si marquerPalet réussit, mise à jour de l'attribut int but auquel on ajoute une valeur de 1,
		 *  paletSaisi mis à jour car plus en possession des pinces
		 * sinon, appel à marquerPalet2() qui repose sur une stratégie différente. 
		 * Si réussit, même conséquences que précedemment précisé
		 * sinon et finalement, on abandonne la séquence et l'on ouvre les pinces du robot, mise à jour de paletSaisi
		 * @return void
		 */
		if(marquerPalet()) {
			but++;
			paletSaisi=false;
		}
		else if(marquerPalet2()){
			but++;
			paletSaisi=false;
			return;
		}
		else {
			pilote.lacher();
			paletSaisi=false;
		}
	}
	public void Homologation() {
		/*
		 * méthode permettant d'avancer jusqu'à récupérer un palet, le saisir, effectuer une légère rotation,
		 *  avancer juusqu'à l'enbut adverse et déposer le palet
		 * si échec, abandon de la méthode après une rotation de 180°
		 * @return void
		 */
		pilote.reglerVitesse(400, 400);
		pilote.forward();
		while(pilote.isMoving()) {
			capt.actualise();
			if(capt.getDistance() < 0.2 || capt.getCouleur().equals("blanc")) {
				pilote.stop();
				pilote.rotate(180);
				return;
			}
			if(capt.isTouche()) {
				pilote.stop();
			}
			if(capt.getCouleur().equals("vert") || capt.getCouleur().equals("bleu")) {
				derniereLigneTraversee=capt.getCouleur();
			}
		}
		pilote.prendre();
		pilote.rotate(MAX_ROT);
		pilote.forward();
		while(!capt.getCouleur().equals("blanc")) {
			capt.actualise();
			if(capt.getDistance() < 0.2) {
				pilote.stop();
				pilote.lacher();
				pilote.rotate(180);
				return;
			}
			if(capt.getCouleur().equals("vert") || capt.getCouleur().equals("bleu")) {
				derniereLigneTraversee=capt.getCouleur();
			}
		}
		pilote.stop();
		pilote.lacher();
		pilote.rotate(180);
	}


	public void updateEtat() {		
		/*
		 * permet de mettre à jour l'état courant d'après les attributs de l'instance
		 * l'état courant est référencé comme une String stockée à l'indice 0 de l'attribut etat qui est un tableau de String
		 * @return void
		 */
		if(premierPalet) { 
			etat[0] = etat[1];
			return;
		}
		else if(paletDetecte) {
			etat[0] = etat[3];
			return;
		}
		else if(paletSaisi) {
			etat[0] = etat[4];
			return;
		}
		else if (paletDetecte==false && paletSaisi==false) { 
			etat[0] = etat[2];
		}
	}
	public void afficheEtat() {	
		/*
		 * permet d'afficher sur la brique du robot la String représentant l'état courant stockée à l'indice 0 de l'attribut etat
		 * @return void
		 */
		System.out.println(etat[0]);			
	}	
	public void retourneSequence() {	
		/*
		 * permet de comaprer l'état courant avec l'ensemble des états possible contenus dans l'attribut etat tableau de String
		 * retourne la séquence correspondante à la stratégie d'action souhaitée
		 * @return void
		 */
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
	
	public static void main(String[] args) {
		
		Agent_Final agent = new Agent_Final();
		Button.ENTER.waitForPressAndRelease();
		while(agent.getBut() < 9 || agent.tempsTotalSec() < 600) {
			agent.updateEtat();
			agent.afficheEtat();
			agent.retourneSequence();
		}
		
	}

}
