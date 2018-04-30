package shapes;
import com.sun.j3d.utils.geometry.*;

import javax.media.j3d.*;
import javax.vecmath.*;

public class CylinderObj extends Polygon {
	private int radius;
	private int height;
	public Cylinder cyl;
	
	public CylinderObj(Material m, Color3f c, int rad, int h) {
		
		super ();
		Cylinder cylinder = new Cylinder(radius, height);
		this.setShape(cylinder);
		radius = rad;
		height = h;
		this.setMat(m);
		this.setColor(c);
		this.setAppearances();
		this.cyl = cylinder;

		
		
	}
	public Cylinder getShape() {
		return cyl;
	}
	public String getName() {
		return "cylinder";
	}
	
}
