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





public class CASE extends JPanel implements MouseMotionListener {
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
    public Diagram diagram; 
    public boolean movinghead=false;
    
    
    // private static final int recW = 100;
   // private static final int MAX = 10000;
   // private boolean dragging=false;
    private boolean curving=false;
   // private boolean is_inside=true;
    //private int a,b,c,d;
    public int currentCurveIndex = -1;

   private void deleteshape(int index){
	    Shape_CASE foo=diagram.shapeList.get(index);
	    System.out.println("out"+foo.arrowoutList.size());
	    for(int i=foo.arrowoutList.size()-1;i>=0;i--){
		    Arrow arr=foo.arrowoutList.get(i);
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
		    Arrow arr=foo.arrowinList.get(i);
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
	    Shape_CASE head=diagram.arrowList.get(index).getHead();
	    if(head!=null){
		System.out.println("head is there"+head.arrowinList.size());
	    	head.arrowinList.remove(diagram.arrowList.get(index));
		System.out.println("head is there"+head.arrowinList.size());
	    }
	    Shape_CASE tail=diagram.arrowList.get(index).getTail();
	    if(tail!=null){
		 System.out.println("tail is there"+tail.arrowoutList.size());
	   	 tail.arrowoutList.remove(diagram.arrowList.get(index));
		 System.out.println("tail is there"+tail.arrowoutList.size());
	    }
	    diagram.arrowList.remove(index);
	    diagram.lineList.remove(index);
	diagram.updatedataDictionary();

    }
    private Point2D.Double attaching(double x,double y){
	    int index=-1;
	    double distance=10000;
		for(int i=0;i<diagram.shapeList.size();i++){
			if(!(diagram.shapeList.get(i).shapetype.equals(ShapeType.BUBBLE)||diagram.shapeList.get(i).shapetype.equals(ShapeType.ENTITY)||diagram.shapeList.get(i).shapetype.equals(ShapeType.DATASTORE)))
				continue;
			if(!diagram.shapeList.get(i).contains(x, y))
				continue;
			double cx=0;
			double cy=0;
			if(diagram.shapeList.get(i).shapetype.equals(ShapeType.BUBBLE)){
				cx=((Bubble)diagram.shapeList.get(i)).geom.getCenterX();
				cy=((Bubble)diagram.shapeList.get(i)).geom.getCenterY();
			}
			if(diagram.shapeList.get(i).shapetype.equals(ShapeType.ENTITY)){
				cx=((Entity)diagram.shapeList.get(i)).geom.getCenterX();
				cy=((Entity)diagram.shapeList.get(i)).geom.getCenterY();
			}
			if(diagram.shapeList.get(i).shapetype.equals(ShapeType.DATASTORE)){
				cx=((DataStore)diagram.shapeList.get(i)).geom.getCenterX();
				cy=((DataStore)diagram.shapeList.get(i)).geom.getCenterY();
			}

			double dis=Math.sqrt((cx-x)*(cx-x)+(cy-y)*(cy-y));
			if(dis<distance){
				distance=dis;
				index=i;
			}
		}
		attached=false;
		attachedindex=-1;
		if(index!=-1&&diagram.shapeList.get(index).shapetype.equals(ShapeType.BUBBLE)){
			Point p = new Point((int)x,(int)y);             // j2se 1.5
			double foo;
			double bar;
			double height=((Bubble)diagram.shapeList.get(index)).geom.getBounds().height;
			double width=((Bubble)diagram.shapeList.get(index)).geom.getBounds().width;
            		double pointToCenter = p.distance(foo=((Bubble)diagram.shapeList.get(index)).geom.getCenterX(), bar=((Bubble)diagram.shapeList.get(index)).geom.getCenterY());
            		double dy = p.y - bar;
            		double dx = p.x - foo;
			if(height>width){
				double temp=dy;
				dy=dx;
				dx=temp;
			}
            		double theta = Math.atan2(dy, dx);
            		double centerToCurve = distanceCenterToCurve(index,theta);
            		double
               			 x1 = foo,                       // oval center
               			 y1 = bar,                       //   coords
               			 x2 = x1 + centerToCurve * Math.cos(theta),    // curve
               			 y2 = y1 + centerToCurve * Math.sin(theta);    //   coords
	    		if(height>width){
				x2 = x1 + centerToCurve * Math.sin(theta);    // curve
               		 	y2 = y1 + centerToCurve * Math.cos(theta);    //   coords
			}
	    		double xx=(int)x2;
			double yy=(int)y2;
			attached=true;
			attachedindex=index;
			return new Point2D.Double(xx, yy);
		}
		if(index!=-1&&(diagram.shapeList.get(index).shapetype.equals(ShapeType.ENTITY)||diagram.shapeList.get(index).shapetype.equals(ShapeType.DATASTORE))){
			double width=0,height=0,cx=0,cy=0;
			if(diagram.shapeList.get(index).shapetype.equals(ShapeType.ENTITY)){
				width=((Entity)diagram.shapeList.get(index)).geom.width;
				height=((Entity)diagram.shapeList.get(index)).geom.height;
				cx=((Entity)diagram.shapeList.get(index)).geom.x;
				cy=((Entity)diagram.shapeList.get(index)).geom.y;
			}
			if(diagram.shapeList.get(index).shapetype.equals(ShapeType.DATASTORE)){
				width=((DataStore)diagram.shapeList.get(index)).geom.width;
				height=((DataStore)diagram.shapeList.get(index)).geom.height;
				cx=((DataStore)diagram.shapeList.get(index)).geom.x;
				cy=((DataStore)diagram.shapeList.get(index)).geom.y;
			}
			Line2D.Double lines[]=new Line2D.Double[4];
			lines[0]=new Line2D.Double(cx, cy, cx+width, cy);
			lines[1]=new Line2D.Double(cx, cy, cx, cy+height);
			lines[2]=new Line2D.Double(cx+width, cy, cx+width, cy+height);
			lines[3]=new Line2D.Double(cx+width, cy+height, cx, cy+height);
			double dis=100000;
			int ind=-1;
			for(int i=0;i<4;i++){
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
    private double distanceCenterToCurve(int index,double theta)
    {
        Rectangle r = ((Bubble)diagram.shapeList.get(index)).geom.getBounds();
	double height=r.height,width=r.width;
	if(r.height>r.width){
		height=r.width;
		width=r.height;
	}

        double e = Math.sqrt(1.0 - (double)(height*height)/(width*width)); // 22
        double divisor = 1.0 - e*e * Math.cos(theta)*Math.cos(theta);
        double radial = (width/2) * Math.sqrt((1.0 - e*e) / divisor);            // 27
        return radial;
    }
    private int get_pos(int z,int x)
    {
        if(z>x) return z-x;
        return x-z;
    }
    
    public CASE() {
    addMouseListener(new MouseAdapter() {
      
public int isneartoend(double x,double y){
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
 	if(type==3||type==4){
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
 	if(type==3||type==4){
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
		for(int i=0;i<diagram.arrowList.size();i++){
			double dist=diagram.lineList.get(i).ptLineDist(diagram.arrowList.get(i).geom.getCtrlPt());
			if(dist>=5)
				continue;
			
			if(diagram.lineList.get(i).ptSegDist(a, b)<=5){
				currentCurveIndex=i;
				curving=true;
				return;
			}
		}
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
			if(!diagram.shapeList.get(i).shapetype.equals(ShapeType.BUBBLE))
				continue;
			if(!diagram.shapeList.get(i).contains(a, b))
				continue;
			double cx=((Bubble)diagram.shapeList.get(i)).geom.getCenterX();
			double cy=((Bubble)diagram.shapeList.get(i)).geom.getCenterY();

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
    }
}

public void mouseReleased(MouseEvent m)
{
    if(SwingUtilities.isRightMouseButton(m)){
        //if (m.isPopupTrigger())
      //      doPop(m);
    } 
            else{
	if(type==1||type==3||type==4){
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
				diagram.lineList.get(currentCurveIndex).x2=att.x;
				diagram.lineList.get(currentCurveIndex).y2=att.y;
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
            Ellipse2D.Double temp=null;
            Rectangle2D.Double temp1=null;
            Rectangle2D.Double temp2=null;
         if(diagram.shapeList.get(i).shapetype==ShapeType.BUBBLE)
                temp=((Bubble)(diagram.shapeList.get(i))).geom;
         if(diagram.shapeList.get(i).shapetype==ShapeType.ENTITY)
                temp1=((Entity)(diagram.shapeList.get(i))).geom;
	if(diagram.shapeList.get(i).shapetype==ShapeType.DATASTORE)
                temp2=((DataStore)(diagram.shapeList.get(i))).geom;
            
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
                    if(diagram.shapeList.get(i).shapetype==ShapeType.BUBBLE)
                        g2.drawString(diagram.shapeList.get(i).name,(int) temp.getCenterX()-(width_1)/2,(int) temp.getCenterY());
                    if(diagram.shapeList.get(i).shapetype==ShapeType.ENTITY)
                        g2.drawString(diagram.shapeList.get(i).name,(int) temp1.getCenterX()-(width_1)/2,(int) temp1.getCenterY());
			if(diagram.shapeList.get(i).shapetype==ShapeType.DATASTORE)
                        g2.drawString(diagram.shapeList.get(i).name,(int) temp2.getCenterX()-(width_1)/2,(int) temp2.getCenterY());
                    
                    g2.setColor(colour);
                }
        }
        for(int i=0;i<diagram.arrowList.size();i++)
		diagram.arrowList.get(i).drawArrow(g);
    }

   public int getRec(int x, int y) {
        if(type==1){
  		for (int i = 0; i < diagram.shapeList.size(); i++) {
			if (diagram.shapeList.get(i).contains(x, y)&&diagram.shapeList.get(i).shapetype==ShapeType.BUBBLE) {
    				return i;
			}
  		}
  		return -1;
        }
	if(type==3){
  		for (int i = 0; i < diagram.shapeList.size(); i++) {
			if (diagram.shapeList.get(i).contains(x, y) && diagram.shapeList.get(i).shapetype==ShapeType.ENTITY) {
    				return i;
			}
  		}
  		return -1;
        }
	if(type==4){
  		for (int i = 0; i < diagram.shapeList.size(); i++) {
			if (diagram.shapeList.get(i).contains(x, y) && diagram.shapeList.get(i).shapetype==ShapeType.DATASTORE) {
    				return i;
			}
  		}
  		return -1;
        }

        if(type==2)
        {

            for (int i = 0; i <diagram.arrowList.size(); i++) {
		if (diagram.arrowList.get(i).geom.contains(x, y)) {
    			return i;
		}
  	    }
  	    return -1;
        }
        return -1;
    }
    
    
    
    public int getRec_1(int x, int y) {
        	for (int i = 0; i < diagram.arrowList.size(); i++) {
                    double dist=diagram.lineList.get(i).ptLineDist(diagram.arrowList.get(i).geom.getCtrlPt());
			if (dist<5) {
    				if(diagram.lineList.get(i).ptSegDist(x, y)<=5){
				
				return i;
			}
			}
  		}
  		return -1;
        
    }

 public void add_1(int x, int y,int x1,int y1) {
     
     if(type==1){

	((Bubble)diagram.shapeList.get(diagram.shapeList.size()-1)).geom=new Ellipse2D.Double(x, y, get_pos(x1,x), get_pos(y1,y));
	currentSquareIndex = diagram.shapeList.size()-1;
	repaint();
     }
     if(type==3){

	((Entity)diagram.shapeList.get(diagram.shapeList.size()-1)).geom=new Rectangle2D.Double(x, y, get_pos(x1,x), get_pos(y1,y));
	currentSquareIndex = diagram.shapeList.size()-1;
	repaint();
     }
    if(type==4){

	((DataStore)diagram.shapeList.get(diagram.shapeList.size()-1)).geom=new Rectangle2D.Double(x, y, get_pos(x1,x), get_pos(y1,y));
	currentSquareIndex = diagram.shapeList.size()-1;
	repaint();
     }
     if(type==2)
     {

         diagram.lineList.set(diagram.lineList.size()-1,  new Line2D.Double(x, y, x1,y1));
	diagram.arrowList.get(diagram.arrowList.size()-1).geom=new QuadCurve2D.Double(x,y,(x+x1)/2,(y+y1)/2,x1,y1);
	currentCurveIndex = diagram.arrowList.size()-1;
     }
}   

    @Override
      public void remove(int n) {
        if(type==1||type==3||type==4){
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
    }

    @Override
     public void mouseMoved(MouseEvent event) {
        if(type==1){
  		int x = event.getX();
  		int y = event.getY();
                
  		if (getRec(x, y) >= 0) {
                    
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                        Ellipse2D.Double temp=((Bubble)(diagram.shapeList.get(getRec(x,y)))).geom;
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
	if(type==3){
  		int x = event.getX();
  		int y = event.getY();
                
  		if (getRec(x, y) >= 0) {
                    
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                        Rectangle2D.Double temp=((Entity)(diagram.shapeList.get(getRec(x,y)))).geom;
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
	if(type==4){
  		int x = event.getX();
  		int y = event.getY();
                
  		if (getRec(x, y) >= 0) {
                    
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                        Rectangle2D.Double geom=((DataStore)(diagram.shapeList.get(getRec(x,y)))).geom;
                        Graphics2D g=(Graphics2D)getGraphics();
                        g.setStroke(new BasicStroke(4));
			g.draw(new Line2D.Double(geom.x,geom.y,geom.x+geom.width,geom.y));
			g.draw(new Line2D.Double(geom.x,geom.y+geom.height,geom.x+geom.width,geom.y+geom.height));

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
                QuadCurve2D.Double temp=((diagram.arrowList.get(getRec(x,y)))).geom;
                        Graphics2D g=(Graphics2D)getGraphics();
                        g.setStroke(new BasicStroke(4));
                        g.draw(temp);
                        g.dispose();
                        h=false;

  		}
            else if (getRec_1(x, y) >= 0 ) {
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                QuadCurve2D.Double temp=((diagram.arrowList.get(getRec_1(x,y)))).geom;
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
			double prevx=((Bubble)diagram.shapeList.get(currentSquareIndex)).geom.getCenterX();
			double prevy=((Bubble)diagram.shapeList.get(currentSquareIndex)).geom.getCenterY();
			((Bubble)diagram.shapeList.get(currentSquareIndex)).geom.x=x;
			((Bubble)diagram.shapeList.get(currentSquareIndex)).geom.y=y;
			Ellipse2D.Double bar=((Bubble)diagram.shapeList.get(currentSquareIndex)).geom;
			for(int i=0;i<diagram.shapeList.get(currentSquareIndex).arrowoutList.size();i++){
				
				QuadCurve2D.Double arr=diagram.shapeList.get(currentSquareIndex).arrowoutList.get(i).geom;
				Point2D.Double foo=transition((Point2D.Double)arr.getP1(),new Point2D.Double(prevx, prevy),new Point2D.Double(bar.getCenterX(), bar.getCenterY()));
				
				arr.x1=foo.x;
				arr.y1=foo.y;
			}
			for(int i=0;i<diagram.shapeList.get(currentSquareIndex).arrowinList.size();i++){
				
				QuadCurve2D.Double arr=diagram.shapeList.get(currentSquareIndex).arrowinList.get(i).geom;
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
			Bubble foo=new Bubble();
			foo.arrowinList=new ArrayList<>();
			foo.arrowoutList=new ArrayList<>();
			foo.colour=colour;
			foo.shapetype=ShapeType.BUBBLE;
			diagram.shapeList.add(foo);
	    	}
                add_1(a,b,x,y);
                repaint();
            }
        }
	if(type==3){
  		int x = event.getX();
  		int y = event.getY();
  		if (currentSquareIndex >= 0) {
			Graphics graphics = getGraphics();
			graphics.setXORMode(getBackground());
			double prevx=((Entity)diagram.shapeList.get(currentSquareIndex)).geom.getCenterX();
			double prevy=((Entity)diagram.shapeList.get(currentSquareIndex)).geom.getCenterY();
			((Entity)diagram.shapeList.get(currentSquareIndex)).geom.x=x;
			((Entity)diagram.shapeList.get(currentSquareIndex)).geom.y=y;
			Rectangle2D.Double bar=((Entity)diagram.shapeList.get(currentSquareIndex)).geom;
			for(int i=0;i<diagram.shapeList.get(currentSquareIndex).arrowoutList.size();i++){
				
				QuadCurve2D.Double arr=diagram.shapeList.get(currentSquareIndex).arrowoutList.get(i).geom;
				Point2D.Double foo=transition((Point2D.Double)arr.getP1(),new Point2D.Double(prevx, prevy),new Point2D.Double(bar.getCenterX(), bar.getCenterY()));
				
				arr.x1=foo.x;
				arr.y1=foo.y;
			}
			for(int i=0;i<diagram.shapeList.get(currentSquareIndex).arrowinList.size();i++){
				
				QuadCurve2D.Double arr=diagram.shapeList.get(currentSquareIndex).arrowinList.get(i).geom;
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
			Entity foo=new Entity();
			foo.arrowinList=new ArrayList<>();
			foo.arrowoutList=new ArrayList<>();
			foo.colour=colour;
			foo.shapetype=ShapeType.ENTITY;
			diagram.shapeList.add(foo);
	    	}
                add_1(a,b,x,y);
                repaint();
            }
        }
	if(type==4){
  		int x = event.getX();
  		int y = event.getY();
  		if (currentSquareIndex >= 0) {
			Graphics graphics = getGraphics();
			graphics.setXORMode(getBackground());
			double prevx=((DataStore)diagram.shapeList.get(currentSquareIndex)).geom.getCenterX();
			double prevy=((DataStore)diagram.shapeList.get(currentSquareIndex)).geom.getCenterY();
			((DataStore)diagram.shapeList.get(currentSquareIndex)).geom.x=x;
			((DataStore)diagram.shapeList.get(currentSquareIndex)).geom.y=y;
			Rectangle2D.Double bar=((DataStore)diagram.shapeList.get(currentSquareIndex)).geom;
			for(int i=0;i<diagram.shapeList.get(currentSquareIndex).arrowoutList.size();i++){
				
				QuadCurve2D.Double arr=diagram.shapeList.get(currentSquareIndex).arrowoutList.get(i).geom;
				Point2D.Double foo=transition((Point2D.Double)arr.getP1(),new Point2D.Double(prevx, prevy),new Point2D.Double(bar.getCenterX(), bar.getCenterY()));
				
				arr.x1=foo.x;
				arr.y1=foo.y;
			}
			for(int i=0;i<diagram.shapeList.get(currentSquareIndex).arrowinList.size();i++){
				
				QuadCurve2D.Double arr=diagram.shapeList.get(currentSquareIndex).arrowinList.get(i).geom;
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
			DataStore foo=new DataStore();
			foo.arrowinList=new ArrayList<>();
			foo.arrowoutList=new ArrayList<>();
			foo.colour=colour;
			foo.shapetype=ShapeType.DATASTORE;
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
	    if(curving){
		    diagram.arrowList.get(currentCurveIndex).geom.ctrlx=x;
		    diagram.arrowList.get(currentCurveIndex).geom.ctrly=y;
		    repaint();
		    return;
	    }

  	   if (currentCurveIndex >= 0){

		double x1=diagram.lineList.get(currentCurveIndex).x1;
		double y1=diagram.lineList.get(currentCurveIndex).y1;
		diagram.lineList.get(currentCurveIndex).x2=x;
		diagram.lineList.get(currentCurveIndex).y2=y;
		diagram.arrowList.get(currentCurveIndex).geom.x2=x;
		diagram.arrowList.get(currentCurveIndex).geom.y2=y;
		diagram.arrowList.get(currentCurveIndex).geom.ctrlx=(x1+x)/2;
		diagram.arrowList.get(currentCurveIndex).geom.ctrly=(y1+y)/2;
		repaint();
		return;
  	   }
           if(!is_inside)
            {
	    	if(!dragging){
	    		dragging=true;
			Arrow foo=new Arrow();
			if(attached){
				foo.setTail(diagram.shapeList.get(attachedindex));
				diagram.shapeList.get(attachedindex).arrowoutList.add(foo);
				attached=false;
			}
			foo.colour=colour;
			diagram.lineList.add(null);
			diagram.arrowList.add(foo);
	    	}
                add_1(a,b,x,y);
                repaint();
            }
        }

        }
    }
}
