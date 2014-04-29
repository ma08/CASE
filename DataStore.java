/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sourya
 */
import java.awt.Panel;
import java.awt.*;
import java.awt.geom.*;
public class DataStore extends Shape_CASE implements java.io.Serializable {
		public java.awt.geom.Rectangle2D.Double geom;
        public DataType datatype=new DataType();
@Override
public boolean contains(double a,double b){
return geom.contains(a, b);
}

@Override
public void drawShape(java.awt.Graphics canvas){
                if(!"".equals(datatype.name))
                {
                    name=datatype.name;
                }
                if(!"".equals(datatype.definition))
                {
                    description=datatype.definition;
                }
Graphics2D g2=(Graphics2D)canvas;
g2.setColor(colour);
g2.draw(new Line2D.Double(geom.x,geom.y,geom.x+geom.width,geom.y));
g2.draw(new Line2D.Double(geom.x,geom.y+geom.height,geom.x+geom.width,geom.y+geom.height));

}
	@Override
	public void deleteShape(){

	}

	@Override
	public void addArrow(boolean in_out,DataType datatype){

	}

	
}
