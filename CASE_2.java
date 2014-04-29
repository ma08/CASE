import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.*;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.AffineTransform;
import static java.awt.geom.AffineTransform.*;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;





public class CASE_2 extends JPanel implements MouseMotionListener {
     public int type=1;
    protected boolean dragging=false;
    protected boolean is_inside=false;
    protected String p_nam=null;
    protected String p_dcr=null;
    private int a,b,c,d;
    public int currentSquareIndex = -1;
    public boolean drawingarrows=false;
    public Color colour=Color.BLUE;
    public boolean h=true;
    protected boolean change_descr=false;
    public boolean attached=false;
    public int attachedindex=-1; 
    public Structure_Diagram diagram; 
    public boolean movinghead=false;
    public boolean lib=false;
    
    // private static final int recW = 100;
   // private static final int MAX = 10000;
   // private boolean dragging=false;
    private boolean curving=false;
   // private boolean is_inside=true;
    //private int a,b,c,d;
    public int currentCurveIndex = -1;

   private void deleteshape(int index){
	    Module foo=diagram.shapeList.get(index);
	    System.out.println("out"+foo.arrowoutList.size());
	    for(int i=foo.arrowoutList.size()-1;i>=0;i--){
		    Control_Arrow arr=foo.arrowoutList.get(i);
		    System.out.println("head"+arr.getHead());
		    if(arr.getHead()!=null){
			System.out.println("head is there"+arr.getHead().arrowinList.size());
			    arr.getHead().arrowinList.remove(arr);
			System.out.println("head is there"+arr.getHead().arrowinList.size());
		    }
		    //foo.arrowoutList.remove(arr);
		    int arrind=diagram.arrowList.indexOf(arr);
		    /*
		    diagram.arrowList.remove(arr);
		    diagram.lineList.remove(arrind);
		    */
		    deletearrow(arrind);
	    }
	    System.out.println("in"+foo.arrowinList.size());
	  for(int i=foo.arrowinList.size()-1;i>=0;i--){
		    Control_Arrow arr=foo.arrowinList.get(i);
		    System.out.println("tail"+arr.getTail());
		    if(arr.getTail()!=null){
			System.out.println("tail is there"+arr.getTail().arrowoutList.size());
			    arr.getTail().arrowoutList.remove(arr);
			System.out.println("tail is there"+arr.getTail().arrowoutList.size());
		    }
		    //foo.arrowinList.remove(arr);
		    int arrind=diagram.arrowList.indexOf(arr);
		    /*
		    diagram.arrowList.remove(arr);
		    diagram.lineList.remove(arrind);
		    */
			deletearrow(arrind);
	    }
	  //if(diagram.shapeList.contains(foo))
	  	diagram.shapeList.remove(index);
	diagram.updatedataDictionary();
	 
    }
    private void deletearrow(int index){
	    if(index<0||index>=diagram.arrowList.size()){
		    System.out.println("dafuuq");
		    return;
	    }
	    /*
	    Arrow arr=diagram.arrowList.get(index);
	    Shape_CASE foo=arr.getHead();
	    if(foo!=null){
		if(foo.arrowinList.size()==1){
			int ind=diagram.shapeList.indexOf(foo);
			deleteshape(ind);
		}
	    }
	    if(diagram.arrowList.contains(arr))
	    */
	    Module head=diagram.arrowList.get(index).getHead();
	    if(head!=null){
		System.out.println("head is there"+head.arrowinList.size());
	    	head.arrowinList.remove(diagram.arrowList.get(index));
		System.out.println("head is there"+head.arrowinList.size());
	    }
	    Module tail=diagram.arrowList.get(index).getTail();
	    if(tail!=null){
		 System.out.println("tail is there"+tail.arrowoutList.size());
	   	 tail.arrowoutList.remove(diagram.arrowList.get(index));
		 System.out.println("tail is there"+tail.arrowoutList.size());
	    }
	    diagram.arrowList.remove(index);
	diagram.updatedataDictionary();

    }
    private Point2D.Double attaching(double x,double y){
	    if(type==3){
		    attachedindex=-1;
		    attached=false;
		    return null;
		    /*
		    int index=-1;
		    double distance=100000;
		    double dist;
		    for(int i=0;i<diagram.arrowList.size();i++){
			    dist=diagram.arrowList.get(i).geom.ptLineDist(x, y);
			    if(dist<20){
				    attached=true;
				    attachedindex=i;
				    index=i;
				    break;
			    }
		    }
		    if(index==-1){
			    attached=false;
			    attachedindex=-1;
			    return null;
		    }
		    double side=diagram.arrowList.get(index).geom.ptLineDist(x, y);
		    double hyp=diagram.arrowList.get(index).geom.getP1().distance(x, y);
		    double r=Math.sqrt(hyp*hyp-side*side);
		    Line2D.Double geom=diagram.arrowList.get(index).geom;
		    double x1=geom.x1;
		    double y1=geom.y1;
		    double x2=geom.x2;
		    double y2=geom.y2;
		    double theta=Math.atan2(y2-y1, x2-x1);
		    double r1=x1+r*Math.cos(theta);
		    double r2=y1+r*Math.sin(theta);
		    int mul;
		    if(theta>0)
			    mul=1;
		    else mul=-1;
		    int relativpos=mul*geom.relativeCCW(x, y);

		    if(relativpos>=0)
			    relativpos=-1;
		    else
			    relativpos=1;
		    double smalltheta=relativpos*0.5;
		    double fx=r1+-1*relativpos*side*Math.cos(smalltheta);
		    double fy=r2-side*Math.sin(smalltheta);
		    System.out.println("attaaaaaaaaaacheeeeeeeed mothaldfj"+relativpos);
		    Control_Arrow bar=diagram.arrowList.get(attachedindex);
		    return new Point2D.Double(fx, fy);

		    */
	    }


	    
	    int index=-1;
	    double distance=10000;
			double cx=0;
			double cy=0;
			int i;
		for(i=0;i<diagram.shapeList.size();i++){
			if(diagram.shapeList.get(i).contains(x, y)){
				cx=(diagram.shapeList.get(i)).geom.getCenterX();
				cy=(diagram.shapeList.get(i)).geom.getCenterY();
				index=i;
				break;

			}
		}
		attached=false;
		attachedindex=-1;
		if(i==diagram.shapeList.size()){
			return null;
		}
			double width=0,height=0;
				width=diagram.shapeList.get(index).geom.width;
				height=diagram.shapeList.get(index).geom.height;
				cx=diagram.shapeList.get(index).geom.x;
				cy=diagram.shapeList.get(index).geom.y;
			Line2D.Double lines[]=new Line2D.Double[4];
			lines[0]=new Line2D.Double(cx, cy, cx+width, cy);
			lines[1]=new Line2D.Double(cx+width, cy+height, cx, cy+height);
			double dis=100000;
			int ind=-1;
			for( i=0;i<2;i++){
				double foo=lines[i].ptLineDist(x, y);
				if(foo<dis){
					ind=i;
					dis=foo;
				}
			}
			if(ind!=-1){
				if(lines[ind].x1==lines[ind].x2){
					attached=true;
					attachedindex=index;
					return new Point2D.Double(lines[ind].x1, y);
				}
			if(lines[ind].y1==lines[ind].y2){
					attached=true;
					attachedindex=index;
					return new Point2D.Double(x,lines[ind] .y1);
				}

			}
		return null;
    }
    private Point2D.Double transition(Point2D.Double p,Point2D.Double t1,Point2D.Double t2){
	    double dist=t1.distance(t2);
	    double theta=Math.atan2(t2.y-t1.y, t2.x-t1.x);
	    double x=p.x+dist*Math.cos(theta);
	    double y=p.y+dist*Math.sin(theta);
	    return new Point2D.Double(x,y);
    }
    private int get_pos(int z,int x)
    {
        if(z>x) return z-x;
        return x-z;
    }
    
    public CASE_2() {
    addMouseListener(new MouseAdapter() {
      
public int isneartoend(double x,double y){

	if(type==3){
	for(int i=0;i<diagram.dataList.size();i++){
		double x1=diagram.dataList.get(i).geom.x2;
		double y1=diagram.dataList.get(i).geom.y2;
		if(Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y))<=20)
			return i;
		}
	return -1;
	}

	for(int i=0;i<diagram.arrowList.size();i++){
		double x1=diagram.arrowList.get(i).geom.x2;
		double y1=diagram.arrowList.get(i).geom.y2;
		if(Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y))<=20)
			return i;
	}
	return -1;
}
@Override
public void mousePressed(MouseEvent evt) {
    if(SwingUtilities.isRightMouseButton(evt)){
       if(type==1){
    		a = evt.getX();
    		b = evt.getY();
    		currentSquareIndex = getRec(a, b);
		
    	}
        if(type==2)
        {
        	a = evt.getX();
    		b = evt.getY();
		currentCurveIndex=getRec(a, b);
		if(currentCurveIndex==-1)
			currentCurveIndex=getRec_1(a, b);
        }
	if(type==3)
        {
        	a = evt.getX();
    		b = evt.getY();
		currentCurveIndex=getRec(a, b);
                
		if(currentCurveIndex==-1)
			currentCurveIndex=getRec_2(a, b);
        }        
                        
    } 
    else{
    
    
    if(type==1){
    		a = evt.getX();
    		b = evt.getY();
    		currentSquareIndex = getRec(a, b);
		is_inside=true;
    		if (currentSquareIndex < 0) // not inside a square
    		{
        		is_inside=false;
        		//add(x, y);
    		}
    	}
	if(type==2){

        	a = evt.getX();
    		b = evt.getY();
		currentCurveIndex=-1;
		int x=isneartoend(a, b);
		System.out.print("preeeeeeeeeeeeeeeeeeeeeeeeee");
		movinghead=false;
		if(x!=-1){
			System.out.print("isnear"+x);
			currentCurveIndex=x;
			curving=false;
			movinghead=true;
			if(diagram.arrowList.get(currentCurveIndex).getHead()!=null){
				int ind=diagram.shapeList.indexOf(diagram.arrowList.get(currentCurveIndex).getHead());
				diagram.shapeList.get(ind).arrowinList.remove(diagram.arrowList.get(currentCurveIndex));
				diagram.arrowList.get(currentCurveIndex).setHead(null);

			}
			return;
		}
		currentCurveIndex=-1;
			
		if(currentCurveIndex==-1){
			int ind=getRec(a, b);
			if(ind!=-1){
				currentCurveIndex=ind;
				curving=true;
				return;
			}
		}
		
		double distance=100000;
		int index=-1;
		for(int i=0;i<diagram.shapeList.size();i++){
			double cx=(diagram.shapeList.get(i)).geom.getCenterX();
			double cy=(diagram.shapeList.get(i)).geom.getCenterY();

			double dis=Math.sqrt((cx-a)*(cx-a)+(cy-b)*(cy-b));
			if(dis<distance){
				distance=dis;
				index=i;
			}

		}
		Point2D.Double att=attaching(a, b);
		if(attached){
			a=(int)att.x;
			b=(int)att.y;


		}
    		if (currentCurveIndex < 0){ // not inside a square
                        is_inside=false;
			curving=false;
                }
    	}
	if(type==3){
        	a = evt.getX();
    		b = evt.getY();
		currentCurveIndex=-1;
		int x=isneartoend(a, b);
		System.out.print("preeeeeeeeeeeeeeeeeeeeeeeeee");
		movinghead=false;
		if(x!=-1){
			System.out.print("isnear"+x);
			currentCurveIndex=x;
			curving=false;
			movinghead=true;
			/*
			if(diagram.arrowList.get(currentCurveIndex).getHead()!=null){
				int ind=diagram.shapeList.indexOf(diagram.arrowList.get(currentCurveIndex).getHead());
				diagram.shapeList.get(ind).arrowinList.remove(diagram.arrowList.get(currentCurveIndex));
				diagram.arrowList.get(currentCurveIndex).setHead(null);

			}
			*/
			return;
		}
		
		double distance=100000;
		Point2D.Double att=attaching(a, b);
		if(attached){
			a=(int)att.x;
			b=(int)att.y;

		}
    		if (currentCurveIndex < 0){ // not inside a square
                        is_inside=false;
			curving=false;
                }
    	}
    }
}

public void mouseReleased(MouseEvent m)
{
    if(SwingUtilities.isRightMouseButton(m)){
        //if (m.isPopupTrigger())
      //      doPop(m);
    } 
            else{
	if(type==1){
                int c = m.getX();
                int d = m.getY();
		dragging=false;
        }
	if(type==2)
        {
		if(dragging||movinghead){
			Point2D.Double att=attaching(m.getX(), m.getY());
			if(attached){
				System.out.println("iiiiiiiiiiiiiiiiiin"+attachedindex);
				diagram.arrowList.get(currentCurveIndex).geom.x2=att.x;
				diagram.arrowList.get(currentCurveIndex).geom.y2=att.y;
				diagram.arrowList.get(currentCurveIndex).setHead(diagram.shapeList.get(attachedindex));
				diagram.shapeList.get(attachedindex).arrowinList.add(diagram.arrowList.get(currentCurveIndex));
				repaint();
				attached=false;
				attachedindex=-1;
				movinghead=false;
				dragging=false;
			}

		}
       		dragging=false;
		curving=false;
               int c = m.getX();
               int d = m.getY();
        }if(type==3)
        {
		if(dragging||movinghead){
			
			if(diagram.dataList.get(currentCurveIndex).parentindex!=-1){
				int index;
			Line2D.Double geom=diagram.arrowList.get(index=diagram.dataList.get(currentCurveIndex).parentindex).geom;
		    int parentindex=diagram.dataList.get(currentCurveIndex).parentindex;
		    Control_Arrow baar=diagram.arrowList.get(parentindex);
		    double x=m.getX();
		    double y=m.getY();

		    double side=diagram.arrowList.get(index).geom.ptLineDist(x, y);
		    double hyp=diagram.arrowList.get(index).geom.getP1().distance(x, y);
		    double r=Math.sqrt(hyp*hyp-side*side);
		    double x1=geom.x1;
		    double y1=geom.y1;
		    double x2=geom.x2;
		    double y2=geom.y2;
		    double theta=Math.atan2(y2-y1, x2-x1);
		    double r1=x1+r*Math.cos(theta);
		    double r2=y1+r*Math.sin(theta);
		    int mul;
		    if(theta>0)
			    mul=1;
		    else mul=-1;
		    double xx=diagram.dataList.get(currentCurveIndex).geom.x1;
		    double yy=diagram.dataList.get(currentCurveIndex).geom.y1;
		    int relativpos=mul*geom.relativeCCW(xx, yy);

		    if(relativpos>=0)
			    relativpos=-1;
		    else
			    relativpos=1;
		    double smalltheta=relativpos*0.5;
		    double fx=r1+-1*relativpos*side*Math.cos(smalltheta);
		    double fy=r2-side*Math.sin(smalltheta);
				diagram.dataList.get(currentCurveIndex).geom.x2=fx;
				diagram.dataList.get(currentCurveIndex).geom.y2=fy;
				//diagram.arrowList.get(currentCurveIndex).setHead(diagram.shapeList.get(attachedindex));
				//diagram.shapeList.get(attachedindex).arrowinList.add(diagram.arrowList.get(currentCurveIndex));
				repaint();
				attached=false;
				attachedindex=-1;
				movinghead=false;
				dragging=false;
			}

		}
       		dragging=false;
		curving=false;
		//currentCurveIndex=-1;
               int c = m.getX();
               int d = m.getY();
        }
    }
}
@Override
public void mouseClicked(MouseEvent evt) {
    if(SwingUtilities.isRightMouseButton(evt)){
       // if (evt.isPopupTrigger())
        //    doPop(evt);
    } 
            else{

	    /*
    if(type==1||type==3||type==4){
    	int x = evt.getX();
    	int y = evt.getY();
    	if (evt.getClickCount() >= 2) {
  		remove(getRec(x,y));
    	}
    }
    if(type==2){
    	int x = evt.getX();
    	int y = evt.getY();
    	if (evt.getClickCount() >= 2) {
		System.out.println("doubleclick");
  		remove(currentCurveIndex);
    	}
    }
	    */
    }
}

  });

  addMouseMotionListener(this);
    }
    @Override
    public void paintComponent(Graphics g) {
  	super.paintComponent(g);
	Graphics2D g2=(Graphics2D)g;
	g2.setStroke(new BasicStroke(3));
  	for(int i=0;i<diagram.shapeList.size();i++)
        {
            Rectangle2D.Double temp=null;

                temp=((diagram.shapeList.get(i))).geom;
            
                diagram.shapeList.get(i).drawShape(g);
                if(diagram.shapeList.get(i).name==null)
                {System.out.println("noooads");}
                else
                {
                   // g2.setStroke(new BasicStroke(6));
                   g2.setColor(Color.BLACK);
                    g2.setFont(new Font("default", Font.BOLD, 16));
                    FontMetrics fm = g.getFontMetrics(new Font("default", Font.BOLD, 16));
                    int width_1 = fm.stringWidth(diagram.shapeList.get(i).name);
                        g2.drawString(diagram.shapeList.get(i).name,(int) temp.getCenterX()-(width_1)/2,(int) temp.getCenterY());
                    
                    g2.setColor(colour);
                }
        }
        for(int i=0;i<diagram.arrowList.size();i++)
		diagram.arrowList.get(i).drawArrow(g);
	for(int i=0;i<diagram.dataList.size();i++)
		diagram.dataList.get(i).drawArrow(g);

    }

   public int getRec(int x, int y) {
        if(type==1){
  		for (int i = 0; i < diagram.shapeList.size(); i++) {
			if (diagram.shapeList.get(i).contains(x, y)) {
    				return i;
			}
  		}
  		return -1;
        }

        if(type==2)
        {
		double dis;
            for (int i = 0; i <diagram.arrowList.size(); i++) {
		    dis=diagram.arrowList.get(i).geom.ptLineDist(x, y);
		    if(dis<=20){
			    return i;
		    }
  	    }
  	    return -1;
        }
	if(type==3)
        {
		double dis;
            for (int i = 0; i <diagram.dataList.size(); i++) {
		    dis=diagram.dataList.get(i).geom.ptLineDist(x, y);
		    if(dis<=20){
			    return i;
		    }
  	    }
  	    return -1;
        }
        return -1;
    }
    
    
    
    public int getRec_1(int x, int y) {
        	for (int i = 0; i < diagram.arrowList.size(); i++) {
    				if(diagram.arrowList.get(i).geom.ptSegDist(x, y)<=5){
					return i;
			}
  		}
  		return -1;
        
    }
    
       public int getRec_2(int x, int y) {
        	for (int i = 0; i < diagram.dataList.size(); i++) {
    				if(diagram.dataList.get(i).geom.ptSegDist(x, y)<=5){
					return i;
			}
  		}
  		return -1;
        
    }

 public void add_1(int x, int y,int x1,int y1) {
     
     if(type==1){

	(diagram.shapeList.get(diagram.shapeList.size()-1)).geom=new Rectangle2D.Double(x, y, get_pos(x1,x), get_pos(y1,y));
	currentSquareIndex = diagram.shapeList.size()-1;
	repaint();
     }
     if(type==2)
     {

	diagram.arrowList.get(diagram.arrowList.size()-1).geom=new Line2D.Double(x,y,x1,y1);
	currentCurveIndex = diagram.arrowList.size()-1;
     }
	if(type==3)
     {

	diagram.dataList.get(diagram.dataList.size()-1).geom=new Line2D.Double(x,y,x1,y1);
	System.out.println("add"+(diagram.dataList.size()-1));
	currentCurveIndex = diagram.dataList.size()-1;
        System.out.println("sdfgsdfgsdfgdfg:"+currentCurveIndex);
        System.out.println("sdfgsdfgsdfgdfg:"+diagram.dataList.size());
     }

}   

    @Override
      public void remove(int n) {
        if(type==1){
  		if (n < 0 || n >= diagram.shapeList.size()) {
			return;
  		}
	//	diagram.shapeList.remove(n);
		deleteshape(n);
  		if (currentSquareIndex == n) {
			currentSquareIndex = -1;
  		}
  		repaint();
        }
        if(type==2)
        {
            if (n < 0 || n >= diagram.arrowList.size()) {
			return;
  		}
	    	
	    /*
		diagram.arrowList.remove(n);
		diagram.lineList.remove(n);
		    */
	    System.out.println(n+" "+diagram.arrowList.size());
	    deletearrow(n);
  		if (currentCurveIndex == n) {
			currentCurveIndex = -1;
  		}
  		repaint();
        }
	if(type==3)
        {
            if (n < 0 || n >= diagram.dataList.size()) {
			return;
  		}
	    	
	    /*
		diagram.arrowList.remove(n);
		diagram.lineList.remove(n);
		    */
	    System.out.println(n+" "+diagram.arrowList.size());
	    //deletedata(n);
            diagram.dataList.remove(n);
  		if (currentCurveIndex == n) {
			currentCurveIndex = -1;
  		}
  		repaint();
        }
    }

    @Override
     public void mouseMoved(MouseEvent event) {
        if(type==1){
  		int x = event.getX();
  		int y = event.getY();
                
  		if (getRec(x, y) >= 0) {
                    
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                        Rectangle2D.Double temp=((diagram.shapeList.get(getRec(x,y)))).geom;
                        Graphics2D g=(Graphics2D)getGraphics();
                        g.setStroke(new BasicStroke(4));
                        g.draw(temp);
                        g.dispose();
                        h=false;

  		}else if(!h)
                {
                    repaint();
                    h=true;
                }
                
                else {
			setCursor(Cursor.getDefaultCursor());
  		}
        }
        if(type==2)
        {
            int x = event.getX();
  	    int y = event.getY();
  	    if (getRec(x, y) >= 0 ) {
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                Line2D.Double temp=((diagram.arrowList.get(getRec(x,y)))).geom;
                        Graphics2D g=(Graphics2D)getGraphics();
                        g.setStroke(new BasicStroke(4));
                        g.draw(temp);
                        g.dispose();
                        h=false;

  		}
  		else if(!h)
                {
                    repaint();
                    h=true;
                }
  	     else {
		setCursor(Cursor.getDefaultCursor());
  		} 
        }
	if(type==3)
        {
            int x = event.getX();
  	    int y = event.getY();
  	    if (getRec(x, y) >= 0 ) {
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                Line2D.Double temp=((diagram.dataList.get(getRec(x,y)))).geom;
                        Graphics2D g=(Graphics2D)getGraphics();
                        g.setStroke(new BasicStroke(4));
                        g.draw(temp);
                        g.dispose();
                        h=false;

  		}
  		else if(!h)
                {
                    repaint();
                    h=true;
                }
  	     else {
		setCursor(Cursor.getDefaultCursor());
  		} 
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if(SwingUtilities.isRightMouseButton(event)){
        //if (event.isPopupTrigger())
            
            //doPop(event);
    } 
        else{
         if(type==1){
  		int x = event.getX();
  		int y = event.getY();
  		if (currentSquareIndex >= 0) {
			Graphics graphics = getGraphics();
			graphics.setXORMode(getBackground());
			double prevx=(diagram.shapeList.get(currentSquareIndex)).geom.getCenterX();
			double prevy=(diagram.shapeList.get(currentSquareIndex)).geom.getCenterY();
			(diagram.shapeList.get(currentSquareIndex)).geom.x=x;
			(diagram.shapeList.get(currentSquareIndex)).geom.y=y;
			Rectangle2D.Double bar=(diagram.shapeList.get(currentSquareIndex)).geom;
			for(int i=0;i<diagram.shapeList.get(currentSquareIndex).arrowoutList.size();i++){
				
				Line2D.Double arr=diagram.shapeList.get(currentSquareIndex).arrowoutList.get(i).geom;
				Point2D.Double foo=transition((Point2D.Double)arr.getP1(),new Point2D.Double(prevx, prevy),new Point2D.Double(bar.getCenterX(), bar.getCenterY()));
				
				arr.x1=foo.x;
				arr.y1=foo.y;
			}
			for(int i=0;i<diagram.shapeList.get(currentSquareIndex).arrowinList.size();i++){
				
				Line2D.Double arr=diagram.shapeList.get(currentSquareIndex).arrowinList.get(i).geom;
				Point2D.Double foo=transition((Point2D.Double)arr.getP2(),new Point2D.Double(prevx, prevy),new Point2D.Double(bar.getCenterX(), bar.getCenterY()));
				
				arr.x2=foo.x;
				arr.y2=foo.y;
			}

			repaint();
			graphics.dispose();
  		}
            if(!is_inside)
            {
	    	if(!dragging){
	    		dragging=true;
			Module foo=new Module();
			foo.arrowinList=new ArrayList<>();
			foo.arrowoutList=new ArrayList<>();
			foo.colour=colour;
                        foo.am_i_lib=lib;
			diagram.shapeList.add(foo);
	    	}
                add_1(a,b,x,y);
                repaint();
            }
        }
        if(type==2)
        {

            int x = event.getX();
  	    int y = event.getY();

  	   if (currentCurveIndex >= 0){

		diagram.arrowList.get(currentCurveIndex).geom.x2=x;
		diagram.arrowList.get(currentCurveIndex).geom.y2=y;
		repaint();
		return;
  	   }
           if(!is_inside)
            {
	    	if(!dragging){
	    		dragging=true;
			Control_Arrow foo=new Control_Arrow();
			foo.dataflows=new ArrayList<>();
			if(attached){
				foo.setTail(diagram.shapeList.get(attachedindex));
				diagram.shapeList.get(attachedindex).arrowoutList.add(foo);
				attached=false;
			}
			foo.colour=colour;
			diagram.arrowList.add(foo);
	    	}
                add_1(a,b,x,y);
                repaint();
            }
        }
	if(type==3)
        {

            int x = event.getX();
  	    int y = event.getY();

  	   if (currentCurveIndex >= 0){

		  System.out.println("cur"+currentCurveIndex);
		diagram.dataList.get(currentCurveIndex).geom.x2=x;
		diagram.dataList.get(currentCurveIndex).geom.y2=y;
		repaint();
		return;
  	   }
           if(!is_inside)
            {
	    	if(!dragging){
	    		dragging=true;
			DataFlow foo=new DataFlow();
			if(attached){
				//foo.setTail(diagram.shapeList.get(attachedindex));
				//diagram.shapeList.get(attachedindex).arrowoutList.add(foo);
				foo.parentindex=attachedindex;
				diagram.arrowList.get(attachedindex).dataflows.add(foo);
				
				attached=false;
			}
			foo.colour=colour;
			diagram.dataList.add(foo);
	    	}
                add_1(a,b,x,y);
                repaint();
            }
        }

        }
    }
}

