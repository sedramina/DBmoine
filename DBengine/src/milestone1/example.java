package milestone1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class example {

	    public static void main(String[] args) throws IOException {
	    	 Hashtable htblColNameValue = new Hashtable <String,String>();
	     	htblColNameValue.put("ssn", "18");
	     	htblColNameValue.put("name", "sameh");
	     	htblColNameValue.put("age", "21");
	     	insertIntoTable("Employee", htblColNameValue);
	     	System.out.println(getPrimaryIndex("Employee"));
	    }
	
	
	
	public static void insertIntoTable(String strTableName, Hashtable <String,String> htblColNameValue)throws  IOException{
		int enteredSoFar=6;
    	BufferedReader bfr;
        String line2;    
        String fileName="/home/sameh/workspace/DBengine/MetaData.txt";
        File file=new File(fileName);
       try{
	        String result="";
	        bfr=new BufferedReader(new FileReader(file));
            line2=bfr.readLine();
            while((line2)!=null){
            	String[] current = line2.split(",");
            	
            	if(current[0].equals(strTableName)){
            		System.out.println("file entered");
            		if(current[3].equals("True")){
//System.out.print(current[3]);
                		System.out.println("is primary key");
            			if(htblColNameValue.get(current[1])==null){
            				
            			System.out.println("Primary Key missing!");

            			return;
            			}

                		System.out.println("from method: "+ alreadyInsertedPrimaryKey(htblColNameValue.get(current[1]),strTableName));
            			if(alreadyInsertedPrimaryKey(htblColNameValue.get(current[1]),strTableName)){
                    		System.out.println("Primary Key already exists!");

                    		return;	            	
                    		}
            		}
            		result = result+htblColNameValue.get(current[1])+",";
            	
            	}
            		System.out.println("in in metadata "+(current[1])+" ");
            	
	            line2=bfr.readLine();
            
            }
            System.out.println("sameh");
            result=result.substring(0, result.length()-1);
            int index=0;
            if(enteredSoFar%3==0){
           	 //insert   

        		FileWriter out = new FileWriter("/home/sameh/workspace/DBengine/"
            +strTableName+"_"+enteredSoFar/3+".txt");
	    	    out.write(result+"\n");
	    	    out.close();

	    	    }
            else{

    	 //insert   
    	PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter
    			("/home/sameh/workspace/DBengine/"+strTableName+"_"+enteredSoFar/3+".txt", true)));
    	    out.println(result);
    	    out.close();
    	}
        }
       
    	 catch(FileNotFoundException fex){
	            fex.printStackTrace();
	        }
        catch (IOException e) {
    	}
	}
       private static int getPrimaryIndex(String tableName) throws IOException{
    	   int counter=0;   
    	   String fileName="/home/sameh/workspace/DBengine/MetaData.txt";
		        File file=new File(fileName);
		      BufferedReader  bfr=new BufferedReader(new FileReader(file));
	            String line2=bfr.readLine();
	            while((line2)!=null){
	            	String[] current = line2.split(",");
	            	if(current[0].equals(tableName)){
	            		if(current[3].equals("True")){
	            			return counter;
	            		}
	            		counter++;
	            	}
	            	line2=bfr.readLine();
       }
	            return -1;
	          
	            
		
}

	private static boolean alreadyInsertedPrimaryKey(String toBeInserted,String tableName) throws IOException{
		boolean result = false;
		int insertedSoFar = 6;
		
		if(insertedSoFar>0){
			double numOfFiles = Math.ceil(insertedSoFar/3)+1;
			System.out.println("number of files:"+numOfFiles);
			System.out.println(getPrimaryIndex(tableName));
if(insertedSoFar%3==0){
			for(int i=0;i<numOfFiles-1;i++){
		        String fileName="/home/sameh/workspace/DBengine/"+tableName+"_"+i+".txt";
		        File file=new File(fileName);
		      BufferedReader  bfr=new BufferedReader(new FileReader(file));
	            String line2=bfr.readLine();
	            while((line2)!=null){
	            	String[] current = line2.split(",");
	            	System.out.println("in method comaparing:"+toBeInserted+" , "+current[getPrimaryIndex(tableName)]);
	            	if(toBeInserted.equals(current[getPrimaryIndex(tableName)])){
	            		result=true;
	            	}
	            line2=bfr.readLine();
	            }
	            }
		}
else{
	for(int i=0;i<numOfFiles;i++){
        String fileName="/home/sameh/workspace/DBengine/"+tableName+"_"+i+".txt";
        File file=new File(fileName);
      BufferedReader  bfr=new BufferedReader(new FileReader(file));
        String line2=bfr.readLine();
        while((line2)!=null){
        	String[] current = line2.split(",");
        	System.out.println("in method comaparing:"+toBeInserted+" , "+current[getPrimaryIndex(tableName)]);
        	if(toBeInserted.equals(current[getPrimaryIndex(tableName)])){
        		result=true;
        	}
        line2=bfr.readLine();
        }
        }
}
			}
		return result;
		}
		
	}
