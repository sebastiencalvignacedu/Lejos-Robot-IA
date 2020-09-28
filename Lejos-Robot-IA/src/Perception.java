import lejos.hardware.Button;


public class Perception {

	private int touch;
	private float [] rgb = new float [3];
	private float dist;
	private Touch b = new Touch();
	private Color c = new Color();
	private Ultra u = new Ultra();
	
	public Perception() {
		this.dist = u.ultraSample[0];
		this.touch = (int) b.touchSample[0];
		rgb[0] = c.colorSample[0];
		rgb[1] = c.colorSample[1];
		rgb[2] = c.colorSample[2];
		//System.out.println("distance? -- "+p.dist);
		//System.out.println("touch? -- "+p.touch);
		//System.out.println("red? --"+p.rgb[0]);
		//System.out.println("green? --"+p.rgb[1]);
		//System.out.println("blue? --"+p.rgb[2]);
		//System.out.println("**********");
	}
	
	public void refresh() {
		c.refresh();
		b.refresh();
		u.refresh();
	}

	public static void main(String[] args) {
		Perception p = new Perception();
		while (Button.ESCAPE.isUp())  {
			p.refresh();
			System.out.println("distance? -- "+p.dist);
			System.out.println("touch? -- "+p.touch);
			System.out.println("red? --"+p.rgb[0]);
			System.out.println("green? --"+p.rgb[1]);
			System.out.println("blue? --"+p.rgb[2]);
			System.out.println("**********");
		}

	}

}
