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
public class DataDictionary implements java.io.Serializable{
	ArrayList<DataType> datatypeList;
	ArrayList<String> namesList;
	String output;

	public String display(){
		String s="";
		for(int i=0;i<datatypeList.size();i++){
			s=s+datatypeList.get(i).name+": "+datatypeList.get(i).definition+"\n";
		}
		return s;
	}

	
}
