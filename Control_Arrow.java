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
public class Control_Arrow implements java.io.Serializable {
	private Module Head;
	private Module Tail;
	public DataType datatype;
	public java.awt.Color colour;
	private String name="";
	private String description;
	public Line2D.Double geom;
	public String in_name;
	public String out_name;
	public Line2D.Double in ;
	public Line2D.Double out ;
	public java.util.ArrayList<DataFlow> dataflows;
	public java.util.ArrayList<Double> percentagestart_dataflow;
	public java.util.ArrayList<Double> percentageend_dataflow;
	


	public void drawArrow(Graphics canvas) {
		Graphics g1=(Graphics2D)canvas;
		Graphics2D g = (Graphics2D) g1.create();
		g.draw(geom);
		

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
