package milestone1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.btree.BTree;
import jdbm.helper.StringComparator;
import jdbm.helper.Tuple;
import jdbm.helper.TupleBrowser;
import jdbm.btree.*;
import jdbm.recman.*;
import jdbm.helper.*;
import jdbm.htree.*;
import jdbm.*;


public class DBApp {
final	String DBname = "xxx" ;
	File metadata;
	BufferedWriter bfw;
	BufferedReader bfr;   
//ArrayList <BTree> currentTrees ;
 Properties    props = new Properties();
RecordManager recman= RecordManagerFactory.createRecordManager( DBname, props ); ;
static Tuple         tuple = new Tuple();
static TupleBrowser  browser;
	

	
	public static void main(String []args) throws DBAppException, IOException{
		DBApp x =new DBApp();
		x.init();
		//System.out.println("Array size: " +x.currentTrees.size());
	Hashtable <String,String> htblColNameType = new Hashtable<String,String>();
		htblColNameType.put("Name", "String");
		htblColNameType.put("ID", "Integer");
		htblColNameType.put("Age", "Integer");
		
		Hashtable <String,String> htblColNameRefs = new Hashtable<String,String>();
		htblColNameRefs.put("Name", "null");
		htblColNameRefs.put("ID", "null");
		htblColNameRefs.put("Age", "null");
		x.createTable("Employee2", htblColNameType,htblColNameRefs,"ID");
		
		 Hashtable htblColNameValue1 = new Hashtable <String,String>();
	     	htblColNameValue1.put("ID", "1");
	     	htblColNameValue1.put("Name", "sameh");
	     	htblColNameValue1.put("Age", "21");
	    	
	     	 Hashtable htblColNameValue2 = new Hashtable <String,String>();
		     	htblColNameValue2.put("ID", "2");
		     	htblColNameValue2.put("Name", "sameh");
		     	htblColNameValue2.put("Age", "21");
		     	
		     	 Hashtable htblColNameValue3 = new Hashtable <String,String>();
			     	htblColNameValue3.put("ID", "3");
			     	htblColNameValue3.put("Name", "sameh");
			     	htblColNameValue3.put("Age", "21");
	    
	    
			     	 Hashtable htblColNameValue5 = new Hashtable <String,String>();
				     	htblColNameValue5.put("ID", "6");
				     	htblColNameValue5.put("Name", "sameh");
				     	htblColNameValue5.put("Age", "21");
	   
		x.insertIntoTable("Employee2", htblColNameValue1);
    	// System.out.println( x.currentTree.size());

		x.insertIntoTable("Employee2", htblColNameValue2);
    	 //System.out.println( x.currentTree.size());

x.insertIntoTable("Employee2", htblColNameValue3);
 //System.out.println( x.currentTree.size());

//x.insertIntoTable("Employee2", htblColNameValue5);
				     	// System.out.println( x.currentTree.size());
	     	
	}
	
	public DBApp() throws IOException {
		// TODO Auto-generated constructor stub
	this.metadata = new File("metadata.txt");
	if (!metadata.exists()){	
	metadata.createNewFile();
		FileWriter temp = new FileWriter(metadata);
        bfw = new BufferedWriter(temp);
        bfw.write("TableName");
		bfw.write(",");
		bfw.write("ColoumnName");
		bfw.write(",");
		bfw.write("ColoumnType");
		bfw.write(",");
		bfw.write("Key");
		bfw.write(",");
		bfw.write("Indexed");
		bfw.write(",");
		bfw.write("Refrenced");
		bfw.newLine();
		bfw.flush();
		bfw.close();
		//currentTrees = new ArrayList <BTree>();
	}
	}
	
	
	public void createTable(String strTableName,
			Hashtable<String,String> htblColNameType,
			Hashtable<String,String>htblColNameRefs,
			String strKeyColName)
			throws DBAppException, IOException{
		
		if(tableNameExistsInDB(strTableName)){
			System.out.println("Tablename already exists");
			return;
		}
		if(strKeyColName==null /*|| !htblColNameType.contains(strKeyColName)*/){
			System.out.println("Primary Key needed!");
			return;
		}
		
		this.bfw = new BufferedWriter(new FileWriter(metadata, true));
		
		Enumeration<String> htb1 = htblColNameType.keys();
		
		String temp;
		
		
		while(htb1.hasMoreElements()){
			temp = (String) htb1.nextElement();
			if(strKeyColName.equals(temp)) {
				bfw.write(strTableName);
				bfw.write(",");
				bfw.write(temp);
				bfw.write(",");
				bfw.write(htblColNameType.get(temp));
				bfw.write(",");
				bfw.write("True");
				bfw.write(",");
				bfw.write("True");
				bfw.write(",");
				bfw.write(htblColNameRefs.get(temp));
				bfw.newLine();
			}
			else {
				bfw.write(strTableName);
				bfw.write(",");
				bfw.write(temp);
				bfw.write(",");
				bfw.write(htblColNameType.get(temp));
				bfw.write(",");
				bfw.write("False");
				bfw.write(",");
				bfw.write("False");
				bfw.write(",");
				bfw.write(htblColNameRefs.get(temp));
				bfw.newLine();
				
			}			
		}
		bfw.flush();
		bfw.close();
		
		// create a BTree for the new table
        BTree currentTree = BTree.createInstance( recman, new StringComparator() );
        recman.setNamedObject( strTableName+"_"+strKeyColName, currentTree.getRecid() );
        System.out.println( "Created a new empty BTree" );
       //this.currentTrees.add(currentTree);
        recman.commit();
       		System.out.println(recman.getNamedObject(strTableName+"_"+strKeyColName));
	}
	
	public void createIndex(String strTableName,String strColName)throws DBAppException{
		
	}

	
	
	public  void insertIntoTable(String strTableName, Hashtable <String,String> htblColNameValue)throws  IOException{
		  System.out.println(getPrimaryKey(strTableName));
		//  BTree currentTree=currentTrees.get(0);
	
    	BufferedReader bfr;
        String line2;    
        String fileName="metadata.txt";
        File file=new File(fileName);
       try{
	        String result="";
	        bfr=new BufferedReader(new FileReader(file));
            line2=bfr.readLine();
            while((line2)!=null){
            	String[] current = line2.split(",");
            	if(!tableNameExistsInDB(strTableName)){
            		System.out.println("Table does not exist");
            		return;
            	}
            	if(current[0].equals(strTableName)){
            		System.out.println("file entered");
            		if(current[3].equals("True")){
                		System.out.println("is primary key");
            			if(htblColNameValue.get(current[1])=="null"){
            				
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
            BTree currentTree=getCurrentTree(strTableName, getPrimaryKey(strTableName));
    		int enteredSoFar=currentTree.size();
    		//System.out.println("___________________________");
    	      // browser = currentTree.browse();
    	       //while ( browser.getNext( tuple ) ) {
    	         //  print( tuple );
    	          // System.out.println("___________________________");
    	       //}
            System.out.println("sameh");
            result=result.substring(0, result.length()-1);
            if(enteredSoFar%3==0){
           	 //insert  new text file 
        		FileWriter out = new FileWriter("/home/sameh/workspace1/DBengine/"
            +strTableName+"_"+enteredSoFar/3+".txt");
	    	    out.write(result+"\n");
	    	    out.close();

	    	    }
            else{

    	 //insert   existing text file 
            	
    	BufferedWriter out = new BufferedWriter(new FileWriter
    			("/home/sameh/workspace1/DBengine/"+strTableName+"_"+enteredSoFar/3+".txt", true));
    	    out.write(result+"\n");
    		out.flush();
    		out.close();    	
    		}
    
           int temp = currentTree.size();
           int row_num;
		int filenum ;
		
		filenum = temp/3;
        row_num = temp%3;	   
       currentTree.insert(htblColNameValue.get(getPrimaryKey(strTableName)), filenum+","+row_num, false);
       recman.commit();
       

       }
    	 catch(FileNotFoundException fex){
	            fex.printStackTrace();
	        }
        catch (IOException e) {
    	}
       

	}
	
	private static int getPrimaryIndex(String tableName) throws IOException{
    	   int counter=0;   
    	   String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
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

	private  boolean alreadyInsertedPrimaryKey(String toBeInserted,String tableName) throws IOException{
		boolean result = false;
		
		int insertedSoFar = this.getCurrentTree(tableName, getPrimaryKey(tableName)).size();
		
		if(insertedSoFar>0){
			double numOfFiles = Math.ceil(insertedSoFar/3);
			System.out.println("number of files:"+numOfFiles);
			System.out.println(getPrimaryIndex(tableName));
if(insertedSoFar%3==0){
			for(int i=0;i<numOfFiles;i++){
		        String fileName="/home/sameh/workspace1/DBengine/"+tableName+"_"+i+".txt";
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
	for(int i=0;i<=numOfFiles;i++){
        String fileName="/home/sameh/workspace1/DBengine/"+tableName+"_"+i+".txt";
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
		
	private static boolean isIndexed (String tableName, String colName) throws IOException{
		   String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
	        File file=new File(fileName);
	      BufferedReader  bfr=new BufferedReader(new FileReader(file));
           String line2=bfr.readLine();
           while((line2)!=null){
           	String[] current = line2.split(",");
           	if(current[0].equals(tableName)){
           		if(current[4].equals("True") && current[1].equals(colName)){
           			return true;
           		}
           	}
           	line2=bfr.readLine();
  }
           return false;
	}
	private static boolean colExistsInTable (String tableName, String colName) throws IOException{
		   String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
	        File file=new File(fileName);
	      BufferedReader  bfr=new BufferedReader(new FileReader(file));
        String line2=bfr.readLine();
        while((line2)!=null){
        	String[] current = line2.split(",");
        	if(current[0].equals(tableName) && colExistsInTable(tableName, colName)){
        		if(current[1].equals(colName)){
        			return true;
        		}
        	}
        	line2=bfr.readLine();
}
        return false;
	}

	private static boolean tableNameExistsInDB (String tableName) throws IOException, FileNotFoundException{
		  String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
	        File file=new File(fileName);
	      BufferedReader  bfr=new BufferedReader(new FileReader(file));
    String line2=bfr.readLine();
    while((line2)!=null){
    	String[] current = line2.split(",");
    	if(current[0].equals(tableName)) {
    	
    			return true;
    		
    	}
    	line2=bfr.readLine();
}
    return false;
	}
	

	private static String getPrimaryKey(String tableName) throws IOException{
		  String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
	        File file=new File(fileName);
	      BufferedReader  bfr=new BufferedReader(new FileReader(file));
  String line2=bfr.readLine();
  while((line2)!=null){
  	String[] current = line2.split(",");
  	if(current[0].equals(tableName)&&current[3].equals("True")) {
  	
  			return current[1];
  		
  	}
  	line2=bfr.readLine();
}
  return "no primary key found";
	
	}

	   static void print( Tuple tuple ) {
	        String person = (String) tuple.getKey();
	        String occupation = (String) tuple.getValue();
	        System.out.println( pad( person, 25) + occupation );
	    }
	   static String pad( String str, int width ) {
	        StringBuffer buf = new StringBuffer( str );
	        int space = width-buf.length();
	        while ( space-- > 0 ) {
	            buf.append( ' ' );
	        }
	        return buf.toString();
	    }
	   
	   private  void init() throws IOException{
			  String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
		        File file=new File(fileName);
		      BufferedReader  bfr=new BufferedReader(new FileReader(file));
		      String line2=bfr.readLine();
	  while((line2)!=null){
	  	String[] current = line2.split(",");
	  	if(current[4].equals("True")) {
	  	
	  	
	  		long  recid = recman.getNamedObject( current[0]+"_"+current[1] );
			   System.out.println("loaded recid: "+recman.getNamedObject(current[0]+"_"+current[1]));

	  		System.out.println("looking for tree: "+current[0]+"_"+current[1]);
	  		System.out.println();
	  		if(BTree.load( recman, recid)!=null){
	  			System.out.println("___________________________");
	  	       browser = BTree.load( recman, recid).browse();
	  	       while ( browser.getNext( tuple ) ) {
	  	           print( tuple );
	  	           System.out.println("___________________________");
	  	       }
	  		}
	  		else{
	  			System.out.println("null tree");
	  		}

	  		//this.currentTrees.add(BTree.load( recman, recid));
	  		
	  	}
	  	line2=bfr.readLine();
	}
		
	   }
	   
	   private BTree getCurrentTree(String tableName, String colName) throws IOException{
			 // System.out.println("Array size: "+currentTrees.size());

		   System.out.println("loaded recid: "+recman.getNamedObject(tableName+"_"+colName));
		   return BTree.load(recman,recman.getNamedObject(tableName+"_"+colName));
	   }

}

