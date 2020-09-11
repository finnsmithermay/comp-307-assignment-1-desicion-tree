import java.util.*;


import java.io.*;

public class dTree {

  int numCategories;
  int numAtts;
  List<String> categoryNames;
  List<String> attNames;
  List<Instance> allInstances;
	
	
  
  private void tree(String test, String training) {
		//System.out.println("here");
		this.readDataFile(test);
		this.test(training);
		System.out.println("here");
		System.out.println(allInstances.size());
		
		
	}

  			 

  		//method that checks the instances using the tree and prints the output
		public void test(String training){
  		decisionTreeConstruction dt	 = new decisionTreeConstruction(training);
  			
  			double correct =0;
  				for(int i =0; i < this.allInstances.size(); i++){
  					String output =  dt.getPredictedCategory(allInstances.get(i));
  					System.out.println(output +"		"	+  this.allInstances.get(i).getCategory());
  					if(output.equals(this.allInstances.get(i).getCategory())){
  						correct++;
  					}
  				}
  				double acc = (correct / allInstances.size()) * 100;
  				System.out.println("correct number - "+ correct);

  				System.out.println("correct percentage  -  " + acc);
  		}
  
		
		//reads in the text files and creates instances from each line
		private void readDataFile(String fname){
		    /* format of names file:
		     * names of categories, separated by spaces
		     * names of attributes
		     * category followed by true's and false's for each instance
		     */
		    System.out.println("Reading data from file "+fname);
		    try {
		      Scanner din = new Scanner(new File(fname));
		   
		      categoryNames = new ArrayList<String>();
		      for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) 
		    	  
		    	  categoryNames.add(s.next());
		      
		      // here i am removing the "CLASS" part of the first line 
		      categoryNames.remove(0); 
		     
		      
		      numCategories=categoryNames.size();
		      System.out.println(numCategories +" categories");

//		      attNames = new ArrayList<String>();
//		      for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) attNames.add(s.next());
//		      numAtts = attNames.size();
//		      System.out.println(numAtts +" attributes");
		      
		      allInstances = readInstances(din);
		      din.close();
		    }
		   catch (IOException e) {
		     throw new RuntimeException("Data File caused IO exception");
		   }
		  }

	
	
	  private List<Instance> readInstances(Scanner din){
		    /* instance = classname and space separated attribute values */
		    List<Instance> instances = new ArrayList<Instance>();
		    String ln;
		    int a =0;
		    while (din.hasNextLine()){ 
		      Scanner line = new Scanner(din.nextLine());
		      instances.add(new Instance(line.next(),line));
		      a++;
		    }
		    System.out.println("Read " + instances.size()+" instances");
		    System.out.println(a);
		    return instances;
		  }

	 
	 
	 
	
	
	public static void main(String[] args) {
		new dTree().tree(args[0], args[1]);
		
	}
	
	
	//instance class, represents a line of the text file 
	class Instance {
		
		//variables that makes up the instance
	    private String category;
	    private List<Boolean> vals;
	    private String Class;
	    public Instance(String cat, Scanner s){
	    	
	    	//Variable for the class 
	      category = cat;
	      Class = cat;
	     // Class = className;
	      vals = new ArrayList<Boolean>();
	      while (s.hasNextBoolean()) vals.add(s.nextBoolean());
	    }

	    public boolean getAtt(int index){
	      return vals.get(index);
	    }

	    public String getCategory(){
	      return category;
	    }
	    public String getInstanceClass(){
		      return Class;
		    }
	    public int getClassNumber(){
	    	if(category.equals("die")){
	    		return 0;
	    	}else {
	    		return 1;
	    	}
	    }
	    
	
	}



}

