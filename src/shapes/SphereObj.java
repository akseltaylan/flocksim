package shapes;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class SphereObj extends Polygon {
	private int radius;
	public Sphere sph;
	
public SphereObj(Material m, Color3f c, int rad) {
	
	super();
	Sphere sphere = new Sphere(radius);
	this.setShape(sphere);
	this.setMat(m);
	this.setColor(c);
	this.setAppearances();
	radius = rad;
	this.sph = sphere;
	
	
}
public Sphere getShape() {
	return sph;
}
public String getName() {
	return "sphere";
}
}