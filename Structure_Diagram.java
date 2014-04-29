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
public class Structure_Diagram implements java.io.Serializable{
	public boolean hassavename;
	public String savename;
	private String name;
	public ArrayList<Module> shapeList;
	public ArrayList<Control_Arrow> arrowList;
	public ArrayList<DataFlow> dataList;
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

	public boolean checkBalanceError(){
		return false;
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
		for(int i=0;i<dataList.size();i++){
				j=(datadictionary.namesList.indexOf(dataList.get(i).datatype.name));
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

	public static Structure_Diagram get_serialize(String name){
	Structure_Diagram e;
	try
      	{
         FileInputStream fileIn = new FileInputStream(name);
         ObjectInputStream in = new ObjectInputStream(fileIn);
         e = (Structure_Diagram) in.readObject();
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
	
	public static void set_serialisable(Structure_Diagram e,String s ){
		if(s==null){
			s="diagram_SD.ser";
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
	public ArrayList<Module> getShapeList() {
		return shapeList;
	}

	/**
	 * @param shapeList the shapeList to set
	 */
	public void setShapeList(ArrayList<Module> shapeList) {
		this.shapeList = shapeList;
	}

	/**
	 * @return the arrowList
	 */
	public ArrayList<Control_Arrow> getArrowList() {
		return arrowList;
	}

	/**
	 * @param arrowList the arrowList to set
	 */
	public void setArrowList(ArrayList<Control_Arrow> arrowList) {
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
