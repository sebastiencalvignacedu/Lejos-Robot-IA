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
			 * retourne un long, correspondant au temps total �coul� (approxim� en secondes) depuis l'initialisation de l'attribut topDepart via la cr�ation d'une instance de Agent_Final.
			 */
			return(Math.round((System.currentTimeMillis() - this.topDepart)/1000));
		}
		public int getBut() {
			/*
			 * retourne un int, attribut correspondant virtuellement au nombre de palet(s) marqu�(s) lors de l'�x�cution du main de cette classe. 
			 */
			return but;
		}
		
		public void deplacementAleatoire() {
			/*
			 * permet de se d�placer selon une valeur de temps al�atoirement g�n�r� entre 0 et 10000Ms,
			 * puis d'effectuer une rotation d'une valeur g�n�r�e al�atoirement entre 0 et 360�
			 * 
			 * si le capteur de distance per�oit une valeur inf�rieure � ~20cm ou que le capteur couleur d�tecte une ligne blanche, on effectue une rotation de 180� avant de sortir de la m�thode
			 * si le capteur de pression est activ� durant le d�placement, on met � jour l'attribut paletSaisi et l'on sort de la m�thode
			 * �galement, on met � jour l'attribut derniereLigneTraversee si l'on per�oit une ligne de couleur vert ou bleu via le capteur couleur
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
			/*
			 * 
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
			 * l'on part du principe que l'objet palet a �t� d�tect� avant l'appel de prendrePalet()
			 * la m�thode retourne true si durant un d�placement en ligne droite, le capteur de pression est activ�, l'action d'attraper est r�alis�e
			 * l'on renvoie false si lors du d�placement sont per�us une ligne blanche via le capteur couleur ou une distance inf�rieure � ~20cm sur le capteur distance, apr�s avoir effectu� une rotation de 180�
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
			 * cette m�thode impose un temps maximal d'execution d'environ 60 sec et renvoie false � l'�ch�ance.
			 * renvoie true ssi le robot per�oit une ligne blanche et poss�de l'information de l'attribut derniereLigneTraversee congruente avec la valeur de l'attribut ligneArrivee
			 * m�me fonctionnalit� de s�curit� de distance et de mise � jour de derniereLigneTraversee lors du d�placement que les m�thodes pr�c�dentes
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
			 * permet de mettre � jour la valeur d'attribut rotation de l'instance li�e de Pilote.
			 * en effectuant des rotations successives de 5� sur une amplitude de 90�; on cherche � d�terminer la valeur minimale per�ue par le capteur de distance
			 * lorsque cette valeur est atteinte, l'on consid�re �tre en face du mur de la zone de but
			 * cette m�thode sert � corriger la diff�rence entre la valeur th�orique de rotation et la valeur r�elle; due au cumul des impr�cisions et contacts avec les murs du terrain
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
			 * permet de rechercher la ligne d'enbut adverse lorsque �chec de la m�thode marquerPalet()
			 * par des appels successifs � chercherBut()
			 * retourne true si chercherBut() retourne true avant la fin de l'�ch�ance de temps de 60sec, false sinon. 
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
			 * permet de recherche la ligne d'enbut adverse sur un fonctionnement similaire � deplacementAleatoire()
			 * renvoie true si per�ue via le capteur couleur et l'historique de la derni�re ligne parall�le � la cible via l'attribut derniereLigneTraversee
			 * false avec rotation si distance per�ue trop faible ou mauvais enbut
			 * false sinon
			 * mise � jour de derniereLigneTraversee durant le d�placement
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
			 * appel unique � Homologation()
			 * mise � jour de l'attribut premierPalet pour ne plus retourner cette s�quence apr�s appel de updateEtat() et retouneSequence() dans le main
			 */
			Homologation();
			premierPalet=false;
		}
		public void sequence2() {
			/*
			 * permet de r�aliser autant de d�placements al�atoires et de recherches de palet que n�cessaires
			 * arret de la s�quence lorsque un palet est pris ou detecte, l'attribut correspondant est alors mis � jour pour �tre trait� par updateEtat()
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
			 * si prendrePalet() r�ussit, mise � jour de l'attribut paletSaisi et de paletDetecte
			 * si non, palet consid�r� perdu, mise � jour de paletDetecte
			 */
			if(prendrePalet()) {
				paletSaisi=true;
				paletDetecte=false;
			}
			else paletDetecte=false;
		}
		public void sequence4() {
			/*
			 * si marquerPalet r�ussit, mise � jour de l'attribut int but auquel on ajoute une valeur de 1, paletSaisi mis � jour car plus en possession des pinces
			 * sinon, appel � marquerPalet2() qui repose sur une strat�gie diff�rente. Si r�ussit, m�me cons�quences que pr�cedemment pr�cis�
			 * sinon et finalement, on abandonne la s�quence et l'on ouvre les pinces du robot, mise � jour de paletSaisi
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
			 * m�thode permettant d'avancer jusqu'� r�cup�rer un palet, le saisir, effectuer une l�g�re rotation, avancer juusqu'� l'enbut adverse et d�poser le palet
			 * si �chec, abandon de la m�thode apr�s une rotation de 180�
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
			 * permet de mettre � jour l'�tat courant d'apr�s les attributs de l'instance
			 * l'�tat courant est r�f�renc� comme une String stock�e � l'indice 0 de l'attribut etat qui est un tableau de String
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
			 * permet d'afficher sur la brique du robot la String repr�sentant l'�tat courant stock�e � l'indice 0 de l'attribut etat
			 */
			System.out.println(etat[0]);			
		}	
		public void retourneSequence() {	
			/*
			 * permet de comaprer l'�tat courant avec l'ensemble des �tats possible contenus dans l'attribut etat tableau de String
			 * retourne la s�quence correspondante � la strat�gie d'action souhait�e
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


