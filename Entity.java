import java.awt.Panel;
import java.awt.*;
public class Entity extends Shape_CASE implements java.io.Serializable {
	public java.awt.geom.Rectangle2D.Double geom;


	@Override
	public boolean contains(double a,double b){
		return geom.contains(a, b);
	}

	@Override
	public void drawShape(java.awt.Graphics canvas){
		Graphics2D g2=(Graphics2D)canvas;
		g2.setColor(colour);
		//System.out.println("colooooooor"+colour);
		g2.draw(geom);

	}

	@Override
	public void deleteShape(){

	}

	@Override
	public void addArrow(boolean in_out,DataType datatype){

	}

	
}
