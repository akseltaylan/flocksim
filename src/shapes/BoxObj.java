package shapes;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class BoxObj extends Polygon {
	private int x;
	private int y;
	private int z;
	public Box bx;
	
	public BoxObj (Material m, Color3f c, int xx, int yy, int zz) {
		
		super ();
		Box box = new Box(x, y, z, this.getAppearance());
		this.setShape(box);
		this.setMat(m);
		this.setColor(c);
		this.setAppearances();
		this.bx = box;
		
		x = xx;
		y = yy;
		z = zz;
		
	}
	public Box getShape() {
		return bx;
	}
	public String getName() {
		return "box";
	}
}
