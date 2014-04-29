import java.awt.*;
import java.awt.print.*;
import javax.swing.*;

public class Printing
extends Object
implements Printable
{

private Component component;                              // the component to print

public Printing(Component com) {
    component=com;
    }

public void print() throws PrinterException {
    PrinterJob                          printJob=PrinterJob.getPrinterJob();

    printJob.setPrintable(this);
    if(printJob.printDialog()) {
        printJob.print();
        }
    }

public int print(Graphics gc, PageFormat pageFormat, int pageIndex) {
    if(pageIndex>0) {
        return NO_SUCH_PAGE;
        }

    RepaintManager  mgr=RepaintManager.currentManager(component);
    Graphics2D      g2d=(Graphics2D)gc;

    g2d.translate(pageFormat.getImageableX(),pageFormat.getImageableY());
    mgr.setDoubleBufferingEnabled(false);                                       // only for swing components
    component.paint(g2d);
    mgr.setDoubleBufferingEnabled(true);                                        // only for swing components
    return PAGE_EXISTS;
    }

static public void printComponent(Component com) throws PrinterException {
    new Printing(com).print();
    }

} 