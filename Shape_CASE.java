/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sourya
 */
import java.awt.*;
import java.util.*;


public abstract class Shape_CASE implements java.io.Serializable {

	public Color colour;
	public ShapeType shapetype; 
	protected ArrayList<Arrow> arrowinList;
	protected ArrayList<Arrow> arrowoutList;
	protected String name="";
	protected String description="";


	public abstract void drawShape(java.awt.Graphics canvas);
	public abstract void deleteShape();
	public abstract void addArrow(boolean in_out,DataType datatype);
	public abstract boolean contains(double a,double b);

	
}
