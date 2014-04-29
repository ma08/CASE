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
public class Arrow implements java.io.Serializable {
	private Shape_CASE Head;
	private Shape_CASE Tail;
	public DataType datatype;
	public java.awt.Color colour;
	private String name="";
	private String description;
	public QuadCurve2D.Double geom;



	GeneralPath path;
    QuadCurve2D quad;
    String text = "Hello World";
    double[] tokenWidths;
    Point2D.Double[] points;
    int offset = 175;

	public void drawArrow(Graphics canvas) {
		text=name;
		int ARR_SIZE=10;
		double x1=getGeom().x1;
		double x2=getGeom().x2;
		double y2=getGeom().y2;
		double y1=getGeom().y1;
		boolean isline=false;
		double cx=getGeom().ctrlx;
		double cy=getGeom().ctrly;
		if((new Line2D.Double(x1, y1, x2, y2)).ptLineDist(cx, cy)<=5)
			isline=true;

		Graphics g1=(Graphics2D)canvas;
		Graphics2D g = (Graphics2D) g1.create();
		g.setColor(colour);
		//System.out.println("arrow"+colour);

                double dx = x2 - x1, dy = y2 - y1;
                int len = (int) Math.sqrt(dx*dx + dy*dy);
                                           
        	double L= Math.sqrt( Math.pow((x1-x2),2) + Math.pow((y1-y2),2)); 
		double r=((y1-cy)*(y1-y2)-(x1-cx)*(x2-x1))/Math.pow(L, 2);

		double Px = x1 + r*(x2-x1);
        	double Py = y1 + r*(y2-y1);
		//System.out.println("relative"+relative);

                // Draw horizontal arrow starting in (0, 0)
                //g.drawLine(0, 0, len, 0);
		g.draw(getGeom());
		if(!isline&&datatype!=null){
			g.setColor(Color.black);
			draw(g,new GeneralPath(geom),datatype.name,75);
			g.setColor(colour);
		}
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                AffineTransform at = AffineTransform.getTranslateInstance(cx, cy);
                double angle = Math.atan2(y2-cy, x2-cx);
		len=(int) Math.sqrt((y2-cy)*(y2-cy)+(x2-cx)*(x2-cx));
                at.concatenate(AffineTransform.getRotateInstance(angle));
                g.transform(at);
		if(isline&&datatype!=null)
		{
			g.setColor(Color.black);
			g.drawString(datatype.name, 0, 0);
			g.setColor(colour);
		}
                g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                              new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
		g.dispose();

	}
	protected void draw(Graphics2D g2, GeneralPath path, String text, int offset) {
        this.path = path;
        this.text = text;
        this.offset = offset;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = g2.getFont().deriveFont(16f);
        g2.setFont(font);
        FontRenderContext frc = g2.getFontRenderContext();
        tokenWidths = getTokenWidths(font, frc);
        // collect path points
        collectLayoutPoints(getPathPoints());
        //g2.draw(path);
        // layout text beginning at offset along curve
        String[] tokens = text.split("(?<=[\\w\\s])");
        for(int j = 0; j < points.length-1; j++) {
            double theta = getAngle(j);
            AffineTransform at =
                AffineTransform.getTranslateInstance(points[j].x, points[j].y);
            at.rotate(theta);
            g2.setFont(font.deriveFont(at));
            g2.drawString(tokens[j], 0, 0);
        }
    }
 
    private double getAngle(int index) {
        double dy = points[index+1].y - points[index].y;
        double dx = points[index+1].x - points[index].x;
        return Math.atan2(dy, dx);
    }
 
    private Point2D.Double[] getPathPoints() {
        double flatness = 0.01;
        PathIterator pit = path.getPathIterator(null, flatness);
        int count = 0;
        while(!pit.isDone()) {
            count++;
            pit.next();
        }
        Point2D.Double[] points = new Point2D.Double[count];
        pit = path.getPathIterator(null, flatness);
        double[] coords = new double[6];
        count = 0;
        while(!pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    points[count++] = new Point2D.Double(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_CLOSE:
                    break;
                default:
                    System.out.println("unexpected type: " + type);
            }
            pit.next();
        }
        return points;
    }
 
    private double[] getTokenWidths(Font font, FontRenderContext frc) {
        String[] tokens = text.split("(?<=[\\w\\s])");
        double[] widths = new double[tokens.length];
        for(int j = 0; j < tokens.length; j++) {
            float width = (float)font.getStringBounds(tokens[j], frc).getWidth();
            widths[j] = width;
        }
        return widths;
    }
 
    private void collectLayoutPoints(Point2D.Double[] p) {
        int index = 0;
        int n = tokenWidths.length;
        double distance = offset;
        points = new Point2D.Double[n+1];
        for(int j = 0; j < tokenWidths.length; j++) {
            index = getNextPointIndex(p, index, distance);
            points[j] = p[index];
            distance = tokenWidths[j];
        }
        index = getNextPointIndex(p, index, tokenWidths[n-1]);
        points[points.length-1] = p[index];
    }
 
    private int getNextPointIndex(Point2D.Double[] p,
                                  int start, double targetDist) {
        for(int j = start; j < p.length; j++) {
            double distance = p[j].distance(p[start]);
            if(distance > targetDist) {
               return j;
            }
        }
        return start;
    }
	public void deleteArrow(){

	}

	/**
	 * @return the Head
	 */
	public Shape_CASE getHead() {
		return Head;
	}

	/**
	 * @param Head the Head to set
	 */
	public void setHead(Shape_CASE Head) {
		this.Head = Head;
	}

	/**
	 * @return the Tail
	 */
	public Shape_CASE getTail() {
		return Tail;
	}

	/**
	 * @param Tail the Tail to set
	 */
	public void setTail(Shape_CASE Tail) {
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
	public QuadCurve2D.Double getGeom() {
		return geom;
	}

	/**
	 * @param geom the geom to set
	 */
	public void setGeom(QuadCurve2D.Double geom) {
		this.geom = geom;
	}
	
	
}
