/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sourya
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
public class DataFlow implements java.io.Serializable {
	private Module Head;
	private Module Tail;
	public DataType datatype;
	public java.awt.Color colour;
	private String name="";
	private String description;
	public Line2D.Double geom;
	public int parentindex=-1;
	


	public void drawArrow(Graphics canvas) {
		Graphics2D g1 = (Graphics2D)canvas;
		Graphics2D g = (Graphics2D) g1.create();
		g.draw(geom);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                AffineTransform at = AffineTransform.getTranslateInstance(geom.x1, geom.y1);
		double y2=geom.y2;
		double x2=geom.x2;
		double cy=geom.y1;
		double cx=geom.x1;
                double angle = Math.atan2(y2-cy, x2-cx);
		int len=(int) Math.sqrt((y2-cy)*(y2-cy)+(x2-cx)*(x2-cx));
                at.concatenate(AffineTransform.getRotateInstance(angle));
                g.transform(at);
		g.setColor(Color.black);
                if(datatype!=null){
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                   
                    g.drawString(datatype.name, (int)geom.getP1().distance(geom.getP2())/3, 0);
                }
		g.setColor(colour);
		int ARR_SIZE=10;
                g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                              new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
		g.dispose();
		

	}
 
	public void deleteArrow(){

	}

	/**
	 * @return the Head
	 */
	public Module getHead() {
		return Head;
	}

	/**
	 * @param Head the Head to set
	 */
	public void setHead(Module Head) {
		this.Head = Head;
	}

	/**
	 * @return the Tail
	 */
	public Module getTail() {
		return Tail;
	}

	/**
	 * @param Tail the Tail to set
	 */
	public void setTail(Module Tail) {
		this.Tail = Tail;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the geom
	 */
	public Line2D.Double getGeom() {
		return geom;
	}

	/**
	 * @param geom the geom to set
	 */
	public void setGeom(Line2D.Double geom) {
		this.geom = geom;
	}
	
	
}
