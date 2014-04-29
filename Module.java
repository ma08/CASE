import java.awt.Panel;
import java.util.*;
import java.awt.*;
public class Module  implements java.io.Serializable {
	public boolean hasModule;
	public java.awt.Color colour;
	public String name;
	public Structure_Diagram child;
	public java.awt.geom.Rectangle2D.Double geom;
	public java.awt.geom.Rectangle2D.Double geom1=new java.awt.geom.Rectangle2D.Double();
	public java.awt.geom.Rectangle2D.Double geom2=new java.awt.geom.Rectangle2D.Double();
        public boolean am_i_lib=false;
        
	protected ArrayList<Control_Arrow> arrowinList;
	protected ArrayList<Control_Arrow> arrowoutList;
    String description;
	public boolean contains(double a,double b){
		return geom.contains(a, b);
	}

	public void drawShape(java.awt.Graphics canvas){
                
		Graphics2D g2=(Graphics2D)canvas;
		g2.setColor(colour);
		//System.out.println("colooooooor"+colour);
		g2.draw(geom);
		//geom1.x=;
               // geom1.y=;
               // geom1.height=;
               // geom1.width=(double)10;
                if(am_i_lib){
                geom1.setRect(geom.x-30, geom.y, 30, geom.height);
                geom2.setRect(geom.x+geom.width, geom.y, 30, geom.height);
                g2.draw(geom1);
                g2.draw(geom2);}
	}

	public void deleteShape(){

	}

	public Structure_Diagram create_nextLevel(){
		child=new Structure_Diagram();
		return child;
	}

	public void addArrow(boolean in_out,DataType datatype){

	}

}
