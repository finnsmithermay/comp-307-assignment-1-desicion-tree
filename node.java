//node class 
public class node {

	//node variables for the attribute this node represents 
	private	String atribute;
	
	//left and right children nodes
	private node left;
	private node right;
	
	//probability of the node
	private Double prob;
	private	String category;

	//Constructor for a node
	public  node(String atr, node L, node R) {
		this.atribute = atr;
		this.left =L;
		this.right = R;
	}
	
	//get the right child node of this node
	public node getRight(){
		return right;
	}
	//get the left child node of this node

	public node getLeft(){
		return left;
	}
	
	//set the right child node of this node
	public void setRight(node n){
		this.right = n;
	}
	
	//set the left child node of this node
	public void setLeft(node n){
		this.left = n;	
	}
	
	//set the probability of this node
	public void setProb(double d){
		this.prob = d;
	}
	
	//get the probability of this node
	public double getProb() {
		return this.prob;
	}
	
	//get the attribute of this node 
	public String getAttribute() {
		return this.atribute;
	}
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
