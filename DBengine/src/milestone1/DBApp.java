package milestone1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		//x.createTable("Employee", htblColNameType,htblColNameRefs,"ID");

		//x.createTable("Employee2", htblColNameType,htblColNameRefs,"ID");
		
		 Hashtable htblColNameValue1 = new Hashtable <String,String>();
	     	htblColNameValue1.put("ID", "11");
	     	htblColNameValue1.put("Name", "sameh");
	     	htblColNameValue1.put("Age", "21");
	    	
	     	 Hashtable htblColNameValue2 = new Hashtable <String,String>();
		     	htblColNameValue2.put("ID", "12");
		     	htblColNameValue2.put("Name", "sameh");
		     	htblColNameValue2.put("Age", "21");
		     	
		     	 Hashtable htblColNameValue3 = new Hashtable <String,String>();
			     	htblColNameValue3.put("ID", "13");
			     	htblColNameValue3.put("Name", "sameh");
			     	htblColNameValue3.put("Age", "21");
	    
	    
			     	 Hashtable htblColNameValue5 = new Hashtable <String,String>();
				     	htblColNameValue5.put("ID", "17");
				     	htblColNameValue5.put("Name", "mina");
				     	htblColNameValue5.put("Age", "21");
				     	
				     	Hashtable htblColNameValue6 = new Hashtable <String,String>();
				     	htblColNameValue6.put("ID", "8");
				     	htblColNameValue6.put("Name", "mina");
				     	htblColNameValue6.put("Age", "21");
				     	
				     	Hashtable htblColNameValue7 = new Hashtable <String,String>();
				     	htblColNameValue7.put("ID", "10");
				     	htblColNameValue7.put("Name", "mina");
				     	htblColNameValue7.put("Age", "21");
	   
//x.insertIntoTable("Employee", htblColNameValue1);
//x.insertIntoTable("Employee", htblColNameValue2);
//x.insertIntoTable("Employee", htblColNameValue3);
//x.insertIntoTable("Employee", htblColNameValue5);
				     
				     	x.createIndex("Employee", "Age");
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
		if(strKeyColName==null ){
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
	
	public void createIndex(String strTableName,String strColName)throws DBAppException, IOException{
		
		if(isIndexed(strTableName, strColName)){
			System.out.println("Index alredy exists for table: "+strTableName+" and column "+strColName);
			return;
		}
		if(!tableNameExistsInDB(strTableName)){
			System.out.println("Table does not exist");
			return;
		}
		if(!colExistsInTable(strTableName, strColName)){
			System.out.println("Column does not exist");
			return;
		}
		// create a BTree for the new table
        BTree currentTree = BTree.createInstance( recman, new StringComparator() );
        recman.setNamedObject( strTableName+"_"+strColName, currentTree.getRecid() );
        System.out.println( "Created a new empty BTree" );
       //this.currentTrees.add(currentTree);
       		System.out.println(recman.getNamedObject(strTableName+"_"+strColName));
		
BTree currentPrimeTree = getCurrentTree(strTableName, getPrimaryKey(strTableName));
System.out.println("Primary Tree: ");
browser = currentPrimeTree.browse();
while ( browser.getNext( tuple ) ) {
    print( tuple );
}
int numOfEntries= currentPrimeTree.size();
System.out.println("num of entries so far:"+ numOfEntries);
if(numOfEntries==0){
			System.out.println("Tree created, table has no entries");
			return;
		}
		else{
			
			int colIndex = getColumnIndex(strTableName, strColName);
			for(int i=0;i<=numOfEntries/3;i++){
				int row=0;
				String fileName="/home/sameh/workspace1/DBengine/"+strTableName+"_"+i+".txt";
		        File file=new File(fileName);
		      BufferedReader  bfr=new BufferedReader(new FileReader(file));
	  String line2=bfr.readLine();
	  while((line2)!=null){
	  	String[] current = line2.split(",");
	  	
	  	//inserting a key for the first time
	  	if(currentTree.find(current[colIndex])==null) {
	  		System.out.println("inserting a new key");
	  		currentTree.insert(current[colIndex], i+","+row, false);	
	  	}
	  	// insert a key that has been already inserted
	  	else{
	  		System.out.println("inserting in an existing key");
	  		String x = (String) currentTree.find(current[colIndex]);
	  		x+="/"+i+","+row;
	  		currentTree.insert(current[colIndex], x, true);
	  	}
	  	
	  	line2=bfr.readLine();
	  	row++;
	}
		}
			System.out.println(strColName+" Tree: ");
			 browser = currentTree.browse();
	            while ( browser.getNext( tuple ) ) {
	                print( tuple );
	            }
							}
		
	recman.commit();
	// update the metadata file
	 String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
     File file=new File(fileName);
   BufferedReader  bfr=new BufferedReader(new FileReader(file));
 String line2=bfr.readLine();
 String newInput="";
 while((line2)!=null){
 	String[] current = line2.split(",");
 	if(!current[0].equals(strTableName)||!current[1].equals(strColName)){
 		newInput+=line2+ '\n';
 	}
 	else{
 		newInput+=current[0]+","+current[1]+","+current[2]+","+current[3]+","+"True"+","+current[5]+'\n';
 	}
 	
 	line2=bfr.readLine();
 	FileOutputStream File = new FileOutputStream("metadata.txt");
    File.write(newInput.getBytes());
}
 
	
	}

	
	
	public  void insertIntoTable(String strTableName, Hashtable <String,String> htblColNameValue)throws  IOException{
		  System.out.println(getPrimaryKey(strTableName));
	
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
            	//check if table exists
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
    // update primary key tree
           int temp = currentTree.size();
           int row_num;
		int filenum ;
		filenum = temp/3;
        row_num = temp%3;	   
       currentTree.insert(htblColNameValue.get(getPrimaryKey(strTableName)), filenum+","+row_num, false);
       
       //update all trees of all indexed columns
       ArrayList<String> treesToBeUpdated = getIndexedColums(strTableName);
       System.out.println("Num of trees to be updated: "+treesToBeUpdated.size());
       System.out.println("trees to be updated "+treesToBeUpdated.toString());
       
       for(int i=0;i<treesToBeUpdated.size();i++){
    	insertIntoIndexedTree(strTableName, treesToBeUpdated.get(i), 
    			htblColNameValue.get(treesToBeUpdated.get(i)), filenum+","+row_num) ;  
       }
       
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
	private static int getColumnIndex(String tableName,String colName) throws IOException{
 	   int counter=0;   
 	   String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
		        File file=new File(fileName);
		      BufferedReader  bfr=new BufferedReader(new FileReader(file));
	            String line2=bfr.readLine();
	            while((line2)!=null){
	            	String[] current = line2.split(",");
	            	if(current[0].equals(tableName)){
	            		if(current[1].equals(colName)){
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
        	if(current[0].equals(tableName)){
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
	
	private static boolean columnNameExistsInDB (String tableName, String colName) throws IOException, FileNotFoundException{
		  String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
	        File file=new File(fileName);
	      BufferedReader  bfr=new BufferedReader(new FileReader(file));
  String line2=bfr.readLine();
  while((line2)!=null){
  	String[] current = line2.split(",");
  	if(current[0].equals(tableName)&&current[1].equals(colName)) {
  	
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
			   System.out.println("loaded recid: "+recman.getNamedObject(current[0]+"_"+current[1])
					   +" This is tree: "+current[0]+"_"+current[1]);

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
	
	   
	   private static ArrayList<String> getIndexedColums(String tableName) throws IOException{
		   ArrayList<String> result = new ArrayList<String>();
		   String fileName="/home/sameh/workspace1/DBengine/metadata.txt";
	        File file=new File(fileName);
	      BufferedReader  bfr=new BufferedReader(new FileReader(file));
       String line2=bfr.readLine();
       while((line2)!=null){
       	String[] current = line2.split(",");
       	if(current[0].equals(tableName)&&current[4].equals("True")&&!current[3].equals("True")){
       		result.add(current[1]);
       	}
       	line2=bfr.readLine();

	   }
       return result;
	   }
	   
	   
	   private BTree getCurrentTree(String tableName, String colName) throws IOException{
			 // System.out.println("Array size: "+currentTrees.size());

		   System.out.println("loaded recid: "+recman.getNamedObject(tableName+"_"+colName));
		   return BTree.load(recman,recman.getNamedObject(tableName+"_"+colName));
	   }
	   
	   private void insertIntoIndexedTree (String tableName, String colName, String key, String value ) throws IOException{
		   BTree currentTree = BTree.load(recman, recman.getNamedObject(tableName+"_"+colName));
		  System.out.println("inserting into tree: "+tableName+"_"+colName );
	        if(currentTree.find(key)==null) {
		  		System.out.println("inserting a new key: "+key+" and value: "+ value);
		  		currentTree.insert(key,value, false);
		  		System.out.println(colName+" Tree: ");
				 browser = currentTree.browse();
		            while ( browser.getNext( tuple ) ) {
		                print( tuple );
		            }
		  	}
		  	// insert a key that has been already inserted
		  	else{
		  		System.out.println("inserting in an existing key");
		  		String x = (String) currentTree.find(key);
		  		x+="/"+value;
		  		currentTree.insert(key, x, true);
		  		System.out.println(colName+" Tree: ");
				 browser = currentTree.browse();
		            while ( browser.getNext( tuple ) ) {
		                print( tuple );
		            }
		  	}
	   }
public Set<String> select_i(String strTable, String c, String v)
			throws DBAppException, IOException {
		HashSet<String> RESULT = new HashSet<String>();
		String x = strTable + "_" + c;
		long recid = recman.getNamedObject(x);
		BTree tree = BTree.load(recman, recid);
		if(tree.find(v)==null){
			System.out.println("No entries found");
			return RESULT;}
		String tuple = (String) tree.find(v);
		String[] n = tuple.split("/");

		if (n.length > 1) {

			// not primary

			for (int i = 0; i < n.length; i++) {
				// System.out.println("*");

				String fileName = strTable + "_" + n[i].split(",")[0] + ".txt";

				File file = new File(fileName);
				BufferedReader bfr = new BufferedReader(new FileReader(file));
				int j = Integer.parseInt(n[i].split(",")[1]);
				// System.out.println(j);

				String line2 = bfr.readLine();
				while (j > 0) {
					line2 = bfr.readLine();
					j--;
				} // System.out.println(line2);

				RESULT.add(line2);

			}
		} else {

			String fileName = strTable + "_" + n[0].split(",")[0] + ".txt";
			File file = new File(fileName);
			BufferedReader bfr = new BufferedReader(new FileReader(file));
			int j = Integer.parseInt(n[0].split(",")[1]);
			String line2 = bfr.readLine();
			while (j > 0) {
				line2 = bfr.readLine();
				j--;
			}
			RESULT.add(line2);

		}
		return RESULT;
	}
public HashSet<String> select_not_Indexed(String strTable, String c,
			String v) throws DBAppException, IOException {
		int j = this.getColumnIndex(strTable, c);
		System.out.println(j);
		int i = 0;
		String line2 = null;
		HashSet<String> RESULT = new HashSet<String>();

		String fileName = strTable + "_" + i + ".txt";
		File file = new File(fileName);
		while (file.exists()) {
			bfr = new BufferedReader(new FileReader(file));
			line2 = bfr.readLine();

			while ((line2) != null) {
				// System.out.println(line2);
				String[] tuple = line2.split(",");
				if (!(line2.substring(line2.length() - 1, line2.length())
						.equals("$")) && (tuple[j].equals(v)))

					RESULT.add(line2);

				line2 = bfr.readLine();
				// System.out.println(line2);
			}

			i++;
			fileName = strTable + "_" + i + ".txt";
			file = new File(fileName);

		}
		return RESULT;

	}

	public Set<String> selectFromTable(String strTable,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException, IOException, DBAppException {
		BufferedReader bfr2;
		String line;
		String fileName2 = "/home/minaa/workspace/DBapp/metadata.txt";
		File file2 = new File(fileName2);
		LinkedList<String> cnames = new LinkedList<String>();
		bfr2 = new BufferedReader(new FileReader(file2));
		line = bfr2.readLine();
		line = bfr2.readLine();
		int j = 0;
		while ((line) != null) {
			String[] current = line.split(",");

			if (current[0].equals(strTable)) {
				cnames.add(j, current[1]);
				j++;
			}
			line = bfr2.readLine();
		}
		int i = 0;
		if (strOperator.equals("and") && htblColNameValue.size() == 2) {
			Set<String> keys = htblColNameValue.keySet();
			Object[] x = keys.toArray();
			if (!cnames.contains(x[0])) {
				System.out.println("there is no column named by :"
						+ (String) x[0] + " in" + strTable);
				return null;
			} else {
				Set<String> S1 = null;
				Set<String> S2 = null;
				if (isIndexed(strTable, (String) x[0])) {
					S1 = this.select_i(strTable, (String) x[0],
							htblColNameValue.get((String) x[0]));

				} else {
					S1 = this.select_not_Indexed(strTable, (String) x[0],
							htblColNameValue.get((String) x[0]));
				}
				System.out.println("hola");
				if (isIndexed(strTable, (String) x[1])) {
					System.out.println("hola1");
					S2 = this.select_i(strTable, (String) x[1],
							htblColNameValue.get((String) x[1]));
				} else {
					System.out.println("hola2");
					S2 = this.select_not_Indexed(strTable, (String) x[1],
							htblColNameValue.get((String) x[1]));

				}System.out.println("hola3");

				Set intersect = new TreeSet(S1);
				intersect.retainAll(S2);
				return  intersect;
			}

		}
		if (strOperator.equals("or") && htblColNameValue.size() == 2) {
			Set<String> keys = htblColNameValue.keySet();
			Object[] x = keys.toArray();
			if (!cnames.contains(x[0])) {
				System.out.println("there is no column named by :" + x[0]
						+ " in" + strTable);
				return null;
			} else {
				Set<String> S1 = null;
				Set<String> S2 = null;
				if (isIndexed(strTable, (String) x[0])) {
					S1 = this.select_i(strTable, (String) x[0],
							htblColNameValue.get((String) x[0]));
				} else {
					S1 = this.select_not_Indexed(strTable, (String) x[0],
							htblColNameValue.get((String) x[0]));
								
				}
				if (isIndexed(strTable, (String) x[1])) {
					S2 = this.select_i(strTable, (String) x[1],htblColNameValue.get((String) x[1]));
				} else {
					S2 = this.select_not_Indexed(strTable, (String) x[1],
							htblColNameValue.get((String) x[1]));
				}				


				Set union = new TreeSet(S1);
				union.addAll(S2);

				return  union;

			}
		}
		if (htblColNameValue.size() == 1) {
			if (!cnames.contains(htblColNameValue.keySet().toArray()[0])) {
				System.out.println("there is no column named by :"
						+ htblColNameValue.keySet().toArray()[0] + " in"
						+ strTable);
				return null;

			} else {
				Set<String> S1 = null;
				if (isIndexed(strTable, (String) htblColNameValue.keySet()
						.toArray()[0])) {
					S1 = this.select_i(strTable, (String) htblColNameValue
							.keySet().toArray()[0],
							htblColNameValue.get((String) htblColNameValue
									.keySet().toArray()[0]));
				} else {
					S1 = this.select_not_Indexed(strTable,
							(String) htblColNameValue.keySet().toArray()[0],
							htblColNameValue.get((String) htblColNameValue
									.keySet().toArray()[0]));
				}
				return  S1;
			}
		}
		// while(htblColNameValue.containsKey(cnames.get(i))){
		// if(isIndexed(strTable, htblColNameValue.get(cnames.get(i)))){
		// // call
		// }
		// else {
		// //call not i
		// }
		// if (strOperator.equals("and")){
		// // intersection
		// }
		// else{ if(strOperator.equals("or")){
		// // unioin
		// }
		// else {
		// // result
		// }
		// }}
		return null;
	}

}
