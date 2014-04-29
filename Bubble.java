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
public class Bubble extends Shape_CASE implements java.io.Serializable{
	Diagram module;
	boolean hasModule;
	public java.awt.geom.Ellipse2D.Double geom;


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

	public boolean checkBalanceError(){

		return true;
	}

	public Diagram createModule_nextLevel(){
		module=new Diagram();
		return module;
	}
	
}
