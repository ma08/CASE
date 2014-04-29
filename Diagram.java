/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sourya
 */
import java.util.*;
import java.awt.Panel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class Diagram implements java.io.Serializable{
	public boolean hassavename;
	public String savename;
	private String name;
	public ArrayList<Shape_CASE> shapeList;
	public ArrayList<Arrow> arrowList;
	public ArrayList<java.awt.geom.Line2D.Double> lineList;
	private boolean isModule;
	public DataDictionary datadictionary;

	public static Diagram createDiagram(String name){
		Diagram x=new Diagram();
		x.setName(name);
		return x;
	}

	public static Diagram openDiagram(String name){

		return null;
	}

	public void saveDiagram(String name){

	}

	public String checkBalanceError(){
		String s="";
		for(int i=0;i<arrowList.size();i++){
			Arrow foo=arrowList.get(i);
			if(foo.getHead()==null){
				s+="\nDataFlowArrow "+foo.datatype.name+" doesn't have Head";
			}
			if(foo.getTail()==null){
				s+="\nDataFlowArrow "+ foo.datatype.name+" doesn't have Tail";
			}
		}
		for(int i=0;i<shapeList.size();i++){
			Shape_CASE foo=shapeList.get(i);
			if(foo.arrowinList.size()==0&&foo.arrowoutList.size()==0)
				s+="\n"+ foo.name+" is disconnected";
			
		}

		if(s=="")
			return null;

		return s;
	}

	public void deleteShape(){

	}

	public DataDictionary updatedataDictionary(){
		/*
		datadictionary=new DataDictionary();
		String s="";
		for(int i=0;i<arrowList.size();i++){
			Arrow foo=arrowList.get(i);
			s=foo.datatype.name+": "+foo.datatype.definition+"\n";
			datadictionary.datatypeList.add(foo.datatype);
		}
		for(int i=0;i<shapeList.size();i++){
			if(shapeList.get(i).shapetype==ShapeType.DATASTORE){
				DataStore foo=((DataStore)shapeList.get(i));
				s=foo.datatype.name+": "+foo.datatype.definition+"\n";
				datadictionary.datatypeList.add(foo.datatype);
			}
		}
		datadictionary.output=s;
		*/
		int len=datadictionary.namesList.size();
	 	int count[]=new int[len];
		int j;
		for(int i=0;i<arrowList.size();i++){
				j=(datadictionary.namesList.indexOf(arrowList.get(i).datatype.name));
				if(j!=-1)
					count[j]++;

		}
		for(int i=0;i<shapeList.size();i++){
			if(!shapeList.get(i).shapetype.equals(ShapeType.DATASTORE))
				continue;
			j=(datadictionary.namesList.indexOf(((DataStore)shapeList.get(i)).datatype.name));
			if(j!=-1)
				count[j]++;

		}
		System.out.println(len);
		for(int i=len-1;i>=0;i--){
			if(count[i]==0){
				datadictionary.datatypeList.remove(i);
				datadictionary.namesList.remove(i);
			}
		}
		return datadictionary;
	}
	public void drawDiagram(Panel canvas){

	}

	public void addShape(Shape_CASE shape){

	}

	public static Diagram get_serialize(String name){
	Diagram e;
	try
      	{
         FileInputStream fileIn = new FileInputStream(name);
         ObjectInputStream in = new ObjectInputStream(fileIn);
         e = (Diagram) in.readObject();
         in.close();
         fileIn.close();
	if(e==null)
		System.out.println("sakldjflkfja");
	return e;
      }catch(IOException i)
      {
         i.printStackTrace();
      }catch(ClassNotFoundException c)
      {
         System.out.println("Employee class not found");
         c.printStackTrace();
      }
	return null;
}
	
	public static void set_serialisable(Diagram e,String s ){
		if(s==null){
			s="diagram_SA.ser";
		}
		try{
		FileOutputStream b= new FileOutputStream(s);
		ObjectOutputStream c=new ObjectOutputStream(b);
		c.writeObject(e);
		c.close();
		b.close();
			
		}
		catch(IOException k){
			k.printStackTrace();
		}
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
	 * @return the shapeList
	 */
	public ArrayList<Shape_CASE> getShapeList() {
		return shapeList;
	}

	/**
	 * @param shapeList the shapeList to set
	 */
	public void setShapeList(ArrayList<Shape_CASE> shapeList) {
		this.shapeList = shapeList;
	}

	/**
	 * @return the arrowList
	 */
	public ArrayList<Arrow> getArrowList() {
		return arrowList;
	}

	/**
	 * @param arrowList the arrowList to set
	 */
	public void setArrowList(ArrayList<Arrow> arrowList) {
		this.arrowList = arrowList;
	}

	/**
	 * @return the isModule
	 */
	public boolean isIsModule() {
		return isModule;
	}

	/**
	 * @param isModule the isModule to set
	 */
	public void setIsModule(boolean isModule) {
		this.isModule = isModule;
	}

	/**
	 * @return the datadictionary
	 */
	public DataDictionary getDatadictionary() {
		return datadictionary;
	}

	/**
	 * @param datadictionary the datadictionary to set
	 */
	public void setDatadictionary(DataDictionary datadictionary) {
		this.datadictionary = datadictionary;
	}



	
}
