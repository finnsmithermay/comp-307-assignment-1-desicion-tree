import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

//import dTree.Instance;

public class decisionTreeConstruction {

	
		
	//variables and lists 
	  int numCategories;
	  int numAtts;
	  List<String> categoryNames;
	  List<String> attNames;
	  List<Instance> allInstances;	
	  private node root;
	  private node base;
	  private double baselineProb;
	  private String baselineCategory;
	
	//reads the training file and calls the build tree and print tree methods
	  public decisionTreeConstruction(String fName) {
		  this.readDataFile(fName);
		  List<Instance> instancesSet= new ArrayList<>(allInstances);
		  ArrayList<String> attributeList = new ArrayList<>(categoryNames);
		   root = buildTree(allInstances, categoryNames);
		   System.out.println("----------  Tree structure  --------");
		   System.out.println("");
		   System.out.println("");
		   this.printTree(" ", root);
		   System.out.println("----------  end of Tree structure  --------");

			System.out.println("");
			System.out.println("");
			base = baseLinePredictor();
			System.out.println("");
			System.out.println( "base line class	" +base.getAttribute() +"	"+ base.getProb() + "	base line accuracy");
			System.out.println("");
			node nn = this.leafNodeMajority(allInstances );
			System.out.println(nn.getProb());
			
		
	  }
	  
	//Traverses the tree starting at the root node printing out the attributes and the probabilitys
	  private void printTree(String in, node n) {
		// TODO Auto-generated method stub
	
		  //if the left node is not null print the node and recursively call the method 
		  if(n.getLeft() != null) {
		  System.out.format("%s%s = True:\n", in,n.getAttribute());
		  printTree(in + " ", n.getLeft());
		  }
		  //if the right node is not null print the node and recursively call the method 
		  if(n.getRight() != null) {
			  System.out.format("%s%s = False:\n", in,n.getAttribute());
			  printTree(in + " ", n.getRight());
			  }
		
		  //if there is node children nodes are null (leaf node) then print the probability and the attribute
		  if(n.getLeft() == null && n.getLeft() == null){
				System.out.format("%sClass %s, prob = " + n.getProb() * 100 + "%%\n", in, n.getAttribute());

		  }
	}

	  		//method that builds the decision tree from the instances list passed to it
	private node buildTree(List<Instance> instances, List<String> attributes) {
			 String bestAttribute = "";
			 List<Instance> bestInstsTrue = new ArrayList<Instance>();
			 List<Instance> bestInstsFalse = new ArrayList<Instance>();
		
			 //if instances is empty
			  if(instances.isEmpty()){
				  //get the baseline predictor (probability of the majority class) and the class
				  return baseLinePredictor();
			  }
			  
			  //if instances are pure (i.e., all in the same class)
			  if(isPure(instances)){
				  //return a node with the pure class and the probability 1
				  return pureNode(instances);
			  }
			  
			  //if attributes is empty 
			  if(attributes.isEmpty()){
				  //returns a node with the majority class and the probability of that class
				  return leafNodeMajority(instances);
			  }else {
			 
				  //find best attribute
				  double best = 1000;
				  for(String attribute : attributes){
				  int indexofAttribute = categoryNames.indexOf(attribute);
				 
				 //create the temp lists of true and false instances for this attribute
				 List<Instance> instancesTrue = new ArrayList<Instance>();
				 List<Instance> instancesFalse = new ArrayList<Instance>();
				 
				 
				 for(Instance instance : instances) {
					 //true instances
					 if(instance.getAtt(indexofAttribute)){
						 instancesTrue.add(instance);
					 }else {
						 //false instances
						 instancesFalse.add(instance);
					 }
				 }
				 //get the average purity of the attribute
				 double averagePurity = calculateWeightedPurity(instancesTrue, instancesFalse);
				 //update the best attribute and attribute purity 
				 if(averagePurity < best) {
					 best = averagePurity;
					 bestAttribute = attribute;
					 bestInstsTrue = instancesTrue;
					 bestInstsFalse = instancesFalse;
				 }
			}
				 //remove the best attribute 
			 attributes.remove(bestAttribute);
			 //build the subtrees by calling the build tree passing the best lists and the list of attributes with the best Attribute removed
			 node left = buildTree(bestInstsTrue, attributes);
			 node right = buildTree(bestInstsFalse, attributes);
			 return new node(bestAttribute, left, right);
		 }
		
	}


	//method to calculate the weighted purity of the two lists 
	private double calculateWeightedPurity(List<Instance> instancesTrue, List<Instance> instancesFalse) {
		// TODO Auto-generated method stub
		double total = instancesTrue.size() + instancesFalse.size();
		double truePurity = purity(instancesTrue) * (instancesTrue.size() / total);
		double falsePurity = purity(instancesFalse) * (instancesFalse.size() / total);
		double weightedImpurity = truePurity + falsePurity;
		//System.out.println(weightedImpurity); 
 
		return weightedImpurity;
	}




	//method to calculate the purity of the instances list it is passed
	private double purity(List<Instance> instances) {
		// TODO Auto-generated method stub
		
		int numLive = 0;
		int numDie = 0;
		for(Instance i : instances){
			if(i.getClassNumber() == 0){
				numDie++;
			}else{
				numLive++;
			}
		}
		double total = instances.size();
		double trueImpurity = ((float) numLive / total);
		double falseImpurity = ((float) numDie / total);
		double impurity = trueImpurity * falseImpurity;
		return impurity;
	}



	//method to return a leaf node by calculating the majority node and the probability of that nodes class
	private node leafNodeMajority(List<Instance> instances) {
		//System.out.println("leafNodeMajority here");

		int numLive = 0;
		int numDie = 0;
		for(Instance i : instances){
			if(i.getClassNumber() == 0){
				numDie++;
			}else{
				numLive++;
			}
		}
		if(numLive > numDie){
			node n = new node("live", null, null);
			double prob = ((float)numLive / (numLive + numDie));
			n.setProb(prob);
			//System.out.println("------------------------  live + leaf" + instances.size());
			return n;
		}else {
			node n = new node("die", null, null);
			double prob =((float)numLive / (numLive + numDie));
		//	System.out.println("------------------------  die + leaf" + prob);

			n.setProb(prob);
			
			return n;
		}
		
		
		
		
	}

	//returns a node with the class of the pure list and the probability of 1
	private node pureNode(List<Instance> instances) {
		int numLive = 0;
		int numDie = 0;
		for(Instance i : instances){
			if(i.getClassNumber() == 0){
				numDie++;
			}else{
				numLive++;
			}
		}
		
		String Class;
		if(numLive > numDie){
			Class = "live";
		}else {
			Class = "die";
		}
		
		node n = new node(Class, null, null);
		n.setProb(1);
		return n;
	}




	//takes a list and returns true if the list is pure and false if it is not
	private boolean isPure(List<Instance> instances) {
		// TODO Auto-generated method stub
		int numLive = 0;
		int numDie = 0;
		for(Instance i : instances){
			if(i.getClassNumber() == 0){
				numDie++;
			}else{
				numLive++;
			}
		}
		if(numLive == 0 || numDie == 0){
			return true;
		}
		
		
		return false;
	}


	//returns a node with the majority call of all the instances and the probability of that class occurring
	private node baseLinePredictor() {
		// TODO Auto-generated method stub
		int numLive = 0;
		int numDie = 0;
		for(Instance i : allInstances){
			if(i.getClassNumber() == 0){
				numDie++;
			}else{
				numLive++;
			}
		}
		if(numLive > numDie){
			node n = new node("live", null, null);
			double prob = ((float)numLive / (numLive + numDie));
			this.baselineProb = prob;
			n.setProb(prob);

			return n;
		}else {
			node n = new node("die", null, null);
			double prob =((float)numLive / (numLive + numDie));
			n.setProb(prob);
			//System.out.println("die" + prob);
			this.baselineProb = prob;

			return n;
		}
		
	}

	//method to get the predicted class by traversing the tree using the passed instance to traverse the correct branch
	public String getPredictedCategory(dTree.Instance instance) {
		// TODO Auto-generated method stub
		node n = root;
		while(!(n.getLeft() == null & n.getRight() == null)){
			String s = n.getAttribute();
			int i;
			for(i =0; i < categoryNames.size(); i++){
				if(categoryNames.get(i).equals(s)){
					break;
				}
			}
			if(instance.getAtt(i)){
				n = n.getLeft();
			}else {
				n = n.getRight();
			}
		}
		
		
		return n.getAttribute();
	}


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
		   ///   System.out.println(numCategories +" categories");

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


		 //instance class, represents a line of the text file 

		private class Instance {

	    private String category;
	    private List<Boolean> vals;
	    private String Class;
	    public Instance(String cat, Scanner s){
	      category = cat;
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
