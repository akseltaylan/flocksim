package shapes;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class ConeObj extends Polygon {
	private int radius;
	private int height;
	public Cone co;
	
	public ConeObj(Material m, Color3f c, int rad, int h) {
		
		super();
		Cone cone = new Cone(radius, height);
		this.setShape(cone);
		this.setMat(m);
		this.setColor(c);
		this.setAppearances();
		radius = rad;
		height = h;
		this.co = cone;
		
		
	}
	
	protected int getRadius () {
		return radius;
	}
	
	protected int getHeight () {
		return height;
	}
	public Node getShape() {
		return co.getParent();
	}
	public String getName() {
		return "cone";
	}
}
