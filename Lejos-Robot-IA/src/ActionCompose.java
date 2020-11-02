
public class ActionCompose {
	public void suvreLigne(String couleur) {
		// voir exemple de code
	}
	
	public void rechercheLigne(String couleur) {
		// deplacemenetAlea ** ulitsé ici * jusqu’a trouver la couleur voulu

	}
	
	public void deplacementAlea(int temps) {
		// se deplace de facon aleatoire dans la table de jeu en utilisant 
		// detectionObjet pour eviter les mures et tourner angle pour prendre
		// une nouvelle direction aleatoire (inspiration de bumper car)
	}
	
	public void recherchePalet() {
		// effectuer une rotation une fois stoppé sur une ligne pour essayer
		// de détecter un palet
	}
	
	public void allerBaseAdverse() {
		// sens et valeur de rotation à effectuer; avancer jusqu'à une ligne; 
		// détecter ligne + lâcher palet
	}
	
	public void detectionObjet(){
		// déterminer si l’objet est un mur, palet ou robot
		Action a = new Action();
			Capteurs p = new Capteurs();
			a.avancerDist((float)25.0);
			p.actualise();
			if(p.distance < (float)25.0) {
				deplacementAlea(10);
			}
			else {
				while(p.touche == false) {
					a.avancerDist((float)5.0);
					p.actualise();
				}
				a.pick();
				allerBaseAdverse();
			}
	}
	
	public void avancerDistance(int d) {
		
	}
	
	public void reculerDistance(int d) {
		
	}
	
	public void tournerAngle(int a) {
		// si positif sens horaire, si négatif antihoraire NB: il faudra peut-être
		// régler la vitesse et le delay 

	}
	
	public void Alignement() {
		// aligne la tête du robot sur l’objet perçu s’il est reconnu comme un palet
	}
}
