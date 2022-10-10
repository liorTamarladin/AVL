
 //name: may yona, id: 206564841, user-name: mayyona
 //name: lior ladin, id: 318434503, user-name: liorladin

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {

	private IAVLNode root;
	private IAVLNode min;
	private IAVLNode max;


	public AVLTree() //constructor for empty tree - O(1)
	{
		this.root = new AVLNode(null); //initialize the root to virtual Node
		this.min = this.root;
		this.max = this.root;
	}

	public AVLTree(IAVLNode node) //constructor for avl subtree - O(logn)
	{
		this.root = node;
		this.root.setParent(null);
		UpdateMin();
		UpdateMax();
	}

	/**
   * public boolean empty()
   *
   * Returns true if and only if the tree is empty.
   *
   */
  public boolean empty() //O(1)
  {
	  return this.root.getSize()==0;
  }

 /**
   * public String search(int k)
   *
   * Returns the info of an item with key k if it exists in the tree.
   * otherwise, returns null.
   */
  public String search(int k)  //O(logn)
  {
	  IAVLNode curr = this.root;
	  while (curr.getKey()!=-1)  //until we got to a virtual node
	  {
		  if (k == curr.getKey())
		  {
			  return curr.getValue();
		  }
		  if (k > curr.getKey())
		  {
			  curr = curr.getRight();
		  }
		  else
		  {
			  curr = curr.getLeft();
		  }
	  }
	  return curr.getValue();  // meaning we didn't find k - return null (virtual node value)
  }

  /**
   * public int insert(int k, String i)
   *
   * Inserts an item with key k and info i to the AVL tree.
   * The tree must remain valid, i.e. keep its invariants.
   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
   * Returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) { //O(logn)
	   IAVLNode virtual_son = TreePosition(k); //finding the pos of the virtual node that node needs to be it parent
	   if(virtual_son.getKey()!=-1)
	   {
		   return -1; //the node is already in the tree
	   }
	   IAVLNode new_node = new AVLNode(k,i);
	   if (empty())
	   {
		   this.root = new_node;
	   }
	   else
	   {
		   if (new_node.getKey() > virtual_son.getParent().getKey())
		   {
			   virtual_son.getParent().setRight(new_node);
		   }
		   else
		   {
			   virtual_son.getParent().setLeft(new_node);
		   }
	   }

	   new_node.setParent(virtual_son.getParent());
	   virtual_son.setParent(new_node);
	   new_node.setRight(virtual_son);
	   new_node.setLeft(new AVLNode(new_node));

	   int rebalanced = Rebalanced(new_node.getParent());
	   UpdateMax();
	   UpdateMin();

	   return rebalanced;
   }


	public int BalanceFactor(IAVLNode L,IAVLNode R){ //calculate the BF for a node using it sons - O(1)
		int BF = L.getHeight()-R.getHeight();
		return BF;
	}


   public int rightRotate(IAVLNode node) //making a right rotate on the edge between node to it left son O(1)
   {
	   IAVLNode L = node.getLeft();
	   IAVLNode LR = L.getRight();

	   int prevLHeight = L.getHeight();
	   int prevNodeHeight = node.getHeight();

	   if (node.equals(this.root))
	   {
		   this.root = L;
	   }
	   IAVLNode nodeParent = node.getParent();
	   LR.setParent(node);
	   L.setParent(node.getParent());
	   L.setRight(node);
	   node.setLeft(LR);
	   node.setParent(L);
	   if (nodeParent!=null)
	   {
		   if (nodeParent.getRight()==node)
		   {
			   nodeParent.setRight(L);
		   }
		   else
		   {
			   nodeParent.setLeft(L);
		   }
	   }
	   int node_size = node.getLeft().getSize()+node.getRight().getSize()+1;
	   node.setSize(node_size);
	   int L_size = L.getLeft().getSize()+L.getRight().getSize()+1;
	   L.setSize(L_size);

	   node.setHeight(Math.max(node.getLeft().getHeight(),node.getRight().getHeight())+1);
	   L.setHeight(Math.max(L.getLeft().getHeight(),L.getRight().getHeight())+1);

	   int cnt = Math.abs(prevLHeight-L.getHeight())+Math.abs(prevNodeHeight-node.getHeight());
	   return cnt;
   }

	public int leftRotate(IAVLNode node) //making a left rotate on the edge between node to it right son O(1)
	{
		IAVLNode R = node.getRight();
		IAVLNode RL = R.getLeft();

		int prevLHeight = R.getHeight();
		int prevNodeHeight = node.getHeight();

		if (node.equals(this.root))
		{
			this.root = R;
		}
		IAVLNode nodeParent = node.getParent();
		RL.setParent(node);
		R.setParent(node.getParent());
		R.setLeft(node);
		node.setRight(RL);
		node.setParent(R);
		if (nodeParent!=null)
		{
			if (nodeParent.getRight()==node)
			{
				nodeParent.setRight(R);
			}
			else
			{
				nodeParent.setLeft(R);
			}
		}
		int size = node.getLeft().getSize()+node.getRight().getSize()+1;
		node.setSize(size);
		int R_size = R.getLeft().getSize()+R.getRight().getSize()+1;
		R.setSize(R_size);

		node.setHeight(Math.max(node.getLeft().getHeight(),node.getRight().getHeight())+1);
		R.setHeight(Math.max(R.getLeft().getHeight(),R.getRight().getHeight())+1);


		int cnt = Math.abs(prevLHeight-R.getHeight())+Math.abs(prevNodeHeight-node.getHeight());
		return cnt;
	}

	public IAVLNode TreePosition(int k){ //k is the key we are searching it position - O(logn)
		IAVLNode curr = this.root;
		while (curr.getKey()!=-1)
		{
			if (k == curr.getKey())
			{
				return curr;
			}
			if (k > curr.getKey())
			{
				curr = curr.getRight();
			}
			else
			{
				curr = curr.getLeft();
			}
		}
		return curr; //return the virtual Node that k needs to be it parent.
	}

  /**
   * public int delete(int k)
   *
   * Deletes an item with key k from the binary tree, if it is there.
   * The tree must remain valid, i.e. keep its invariants.
   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
   * Returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k) //O(logn)
   {
	   IAVLNode node = TreePosition(k);
	   if (node.getKey()==-1) // k not found in the tree
	   {
		   return -1;
	   }
	   else if (node.getHeight() == 0)// leaf
	   {
		   deleteLeaf(node);
		   node = node.getParent();
	   }
	   else if (node.getRight().getKey()==-1 || node.getLeft().getKey()==-1) //an unary node
	   {
		   deleteUnary(node);
		   node = node.getParent();
	   }
	   else
	   {
		   IAVLNode successor = successor(node);
		   IAVLNode successor_parent = successor.getParent();
		   deleteBinary(node,successor);
		   if (successor_parent == node) // if the successor parent is the node that we deleted
		   {
			   node = successor;
		   }
		   else
		   {
			   node = successor_parent;
		   }
	   }

	   int rebalanced = Rebalanced(node);
	   UpdateMin();
	   UpdateMax();
	   return rebalanced;
   }

   public int Rebalanced(IAVLNode node) //a func to fix the tree structure in order to maintain a correct AVL tree. O(logn)
   {
	   int cnt = 0;
	   while (node != null) { //the while loop is being executed until the root
		   int size = node.getLeft().getSize() + node.getRight().getSize() + 1;
		   node.setSize(size);
		   int BF = BalanceFactor(node.getLeft(), node.getRight());
		   boolean change_height = node.getHeight() != (Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);
		   if (change_height && (BF > -2 && BF < 2)) {
			   node.setHeight(Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);
			   cnt++;
		   }
		   if (BF == -2)
		   {
			   int BF_rightSon = BalanceFactor(node.getRight().getLeft(),node.getRight().getRight());
			   if (BF_rightSon == -1) //left rotate
			   {
				   cnt+=leftRotate(node);
			   }
			   else if (BF_rightSon == 0) //left rotate
			   {
				   cnt+=leftRotate(node);
			   }
			   else if (BF_rightSon == 1) //right then left rotate
			   {
				   cnt+=rightRotate(node.getRight());
				   cnt+=leftRotate(node);
			   }
		   }
		   else if (BF==2)
		   {
			   int BF_leftSon = BalanceFactor(node.getLeft().getLeft(),node.getLeft().getRight());
			   if (BF_leftSon == 1) //right rotate
			   {
				   cnt+=rightRotate(node);
			   }
			   if (BF_leftSon == 0)
			   {
				   cnt+=rightRotate(node);
			   }
			   else if (BF_leftSon == -1) //left then right rotate
			   {
				   cnt+=leftRotate(node.getLeft());
				   cnt+=rightRotate(node);
			   }
		   }
		node = node.getParent();
	   }
	   return cnt;
   }

   public void deleteBinary(IAVLNode node, IAVLNode successor) //O(logn)
   {
	   if (successor.getHeight()==0)//successor is a leaf
	   {
		   deleteLeaf(successor);
	   }
	   else //successor is an unary node
	   {
		   deleteUnary(successor);
	   }
	   replacePos(node,successor);

   }

   public void replacePos(IAVLNode node, IAVLNode successor) //replace the position of node with its successor- O(logn)
   {
	   if (node == this.root) //node is the root
	   {
		   this.root = successor;
		   successor.setParent(null);
	   }
	   else
	   {
		   successor.setParent(node.getParent());
		   if (node.getParent().getRight() == node) //node is a right son
		   {
			   node.getParent().setRight(successor);
		   }
		   else //node is a left son
		   {
			   node.getParent().setLeft(successor);
		   }
	   }
	   successor.setRight(node.getRight());
	   successor.setLeft(node.getLeft());
	   node.getRight().setParent(successor);
	   node.getLeft().setParent(successor);
   }

   public void deleteUnary(IAVLNode node) //O(1)
   {
	   IAVLNode curr;
	   boolean right = node.getRight().getKey() != -1; //node has a real node right son
	   if (node == this.root)
	   {
		   if (right)
		   {
			   this.root = node.getRight();
			   node.getRight().setParent(null);
		   }
		   else //node has a real node left son
		   {
			   this.root = node.getLeft();
			   node.getLeft().setParent(null);
		   }
	   }
	   else
	   {
		   if (right)
		   {
			   curr = node.getRight();
		   }
		   else
		   {
			   curr = node.getLeft();
		   }
		   curr.setParent(node.getParent());
		   if (node.getParent().getRight()==node)
		   {
			   node.getParent().setRight(curr);
		   }
		   else
		   {
			   node.getParent().setLeft(curr);
		   }
	   }
   }

   public void deleteLeaf(IAVLNode node) //O(1)
   {
	   if (node == this.root) //a tree with one node
	   {
		   this.root = node.getRight();
		   node.getRight().setParent(null);
	   }
	   else
	   {
		   if (node.getParent().getRight() == node) //node is a right son
		   {
			   node.getParent().setRight(node.getRight());
		   }
		   else //node is a left son
		   {
			   node.getParent().setLeft(node.getRight());
		   }
		   node.getRight().setParent(node.getParent());
	   }
   }

   public IAVLNode successor(IAVLNode node) //return the node that is the successor of node- O(logn)
   {
	   if(node.getRight().getKey()!= -1) //if node has a right son
	   {
		  return minSubTree(node.getRight());
	   }
	   IAVLNode node_Parent = node.getParent();
	   while (node_Parent!=null && node==node_Parent.getRight()) //node doesn't a right son- the successor is an ancestor parent
	   {
		   node = node_Parent;
		   node_Parent = node.getParent();
	   }
	   return node_Parent;
   }

   public IAVLNode minSubTree(IAVLNode node) //returns the min node in a subtree node is its "root"- O(logn)
   {
	   while (node.getKey()!=-1)
	   {
		   node = node.getLeft();
	   }
	   return node.getParent();
   }


   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty.
    */

   public String min() //O(1)
   {
	   return this.min.getValue();
   }

	public void UpdateMin() //updates the min node in the tree - O(logn)
	{
		if (this.empty()) //the tree has only virtual node.
		{
			return;
		}
		IAVLNode curr = this.root;
		while (curr.getKey()!=-1)
		{
			curr = curr.getLeft();
		}
		this.min = curr.getParent();
	}

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty.
    */
   public String max() // O(1)
   {
	   return this.max.getValue();
   }

	public void UpdateMax()  //updates the max node in the tree - O(logn)
	{
		if (this.empty()) //the tree has only virtual node.
		{
			return;
		}
		IAVLNode curr = this.root;
		while (curr.getKey()!=-1)
		{
			curr = curr.getRight();
		}
		this.max = curr.getParent();
	}

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */

  public static int i;
  public int[] keysToArray() //O(n)
  {
	  i=0;
	  int[] ordered_keys = new int[this.root.getSize()];
	  ReckeysToArray(ordered_keys,this.root);
	  return ordered_keys;
  }

	public void ReckeysToArray(int[] ordered_keys,IAVLNode curr){ //doing an inorder search on the tree and saving the nodes keys in this order - O(n)
	  if (curr.getKey() == -1){
		  return;
	  }
		ReckeysToArray(ordered_keys,curr.getLeft());
		ordered_keys[i] = curr.getKey();
		i++;
		ReckeysToArray(ordered_keys,curr.getRight());
	}

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */


  public String[] infoToArray() //O(n)
  {
	  i=0;
	  String[] ordered_val = new String[this.root.getSize()];
	  RecinfoToArray(ordered_val,this.root);
	  return ordered_val;
  }

	public void RecinfoToArray(String[] ordered_val,IAVLNode curr){ //doing an inorder search on the tree and saving the nodes values in this order - O(n)
		if (curr.getKey() == -1){
			return;
		}
		RecinfoToArray(ordered_val,curr.getLeft());
		ordered_val[i] = curr.getValue();
		i++;
		RecinfoToArray(ordered_val,curr.getRight());
	}

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    */
   public int size()
   {
	   return this.root.getSize();
   } //O(1)
   
   /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    */
   public IAVLNode getRoot() //O(1)
   {
	   return this.root;
   }
   
   /**
    * public AVLTree[] split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
    * 
	* precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    */   
   public AVLTree[] split(int x) //O(logn)
   {
	   IAVLNode node = this.TreePosition(x); //finding the node in the tree that we need to split according to it.
	   IAVLNode[] Less = Less(this,node);
	   IAVLNode[] Bigger = Bigger(this,node);

	   AVLTree t1 = splitHelper(Less);
	   AVLTree t2 = splitHelper(Bigger);
	   return new AVLTree[] {t1,t2};
   }

	public AVLTree splitHelper(IAVLNode[] array) //doing a series of joins to the elements in the array - O(logn)
	{
		AVLTree T1 = new AVLTree(array[0]);
		int i = 1;
		while (array[i] != null) //array[i] is the divider node in the join operation
		{
			AVLTree T2 = new AVLTree(array[i+1]);
			T1.join(array[i],T2);
			i += 2;
		}
		return T1;
	}

   public IAVLNode[] Less(AVLTree t,IAVLNode node) //returns an array of nodes in t that their keys are smaller than node.key - O(logn)
   {
	   IAVLNode[] Less = new IAVLNode[(t.root.getHeight()+1)*3];
	   Less[0] = node.getLeft();
	   int i = 1;
	   while (node != t.root)
	   {
		 if (node == node.getParent().getRight()) //node is a right child meaning its father.key<node.key
		 {
			 Less[i] = node.getParent();
			 Less[i+1] = node.getParent().getLeft();
			 i += 2;
		 }
		 node = node.getParent();
	   }
	   return Less;
   }

	public IAVLNode[] Bigger(AVLTree t,IAVLNode node) //returns an array of nodes in t that their keys are bigger than node.key - O(logn)
	{
		IAVLNode[] Bigger = new IAVLNode[(t.root.getHeight()+1)*3];
		Bigger[0] = node.getRight();
		int i = 1;
		while (node != t.root)
		{
			if (node == node.getParent().getLeft()) //node is a left child meaning its father.key>node.key
			{
				Bigger[i] = node.getParent();
				Bigger[i+1] = node.getParent().getRight();
				i += 2;
			}
			node = node.getParent();
		}
		return Bigger;
	}


   /**
    * public int join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	*
	* precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    */   
   public int join(IAVLNode x, AVLTree t) // O(logn)
   {
	   int res = Math.abs(t.root.getHeight()-this.root.getHeight())+1;
	   if (empty()) ///this tree is empty()
	   {
		   this.root = t.getRoot();
		   insert(x.getKey(),x.getValue());
	   }
	   else if (t.empty()){ //t root is empty()
		   insert(x.getKey(),x.getValue());
	   }
	   else if (this.root.getKey() > t.root.getKey()) //keys(t) < x < keys()
	   {
		   joinHelper(this,x,t);
	   }
	   else { //keys(t) > x > keys()
		   joinHelper(t, x, this);
	   }
	   return res;
   }

   public void joinHelper(AVLTree T_bigger,IAVLNode x, AVLTree T_smaller) //combines the two trees to this tree with x as a "separator"- O(logn)
   {
		AVLTree T_higher = higherTree(T_bigger,T_smaller);
	    AVLTree T_shorter = shorterTree(T_bigger,T_smaller);
	    IAVLNode b = similarRankNode(T_higher, T_shorter.root.getHeight(), T_higher == T_bigger); // b is the first vertex on the higher tree with rank<= t_shorter.height
		T_shorter.root.setParent(x);
		x.setParent(b.getParent());
	    boolean right = false;
		if (b!=T_higher.root)
		{
			right = b.getParent().getRight() == b; //b is a right son
		}
	    b.setParent(x);
		if(T_smaller==T_shorter)
		{
			x.setLeft(T_smaller.root);
			x.setRight(b);
		}
		else //T_smaller==T_higher
		{
			x.setLeft(b);
			x.setRight(T_bigger.root);
		}
		if (x.getParent()==null) //x needs to be the root of this
		{
			this.root = x;
		}
		else
		{
			this.root = T_higher.root;
			if (right){ //x needs to be a right son (instead of b)
				x.getParent().setRight(x);
			}
			else //x needs to be a left son (instead of b)
			{
				x.getParent().setLeft(x);
			}
		}

	    Rebalanced(x);
		UpdateMax();
		UpdateMin();
		return;
   }

   public IAVLNode similarRankNode(AVLTree higherTree, int height, boolean left)
   // returns the first node on the right (if left==false)/left branch of the tree with rank<= height - O(logn)
   {
	   IAVLNode curr_node = higherTree.root;
	   while (curr_node.getHeight() > height)
	   {
		   if (left) //needs to go throw the most left branch
		   {
			   curr_node = curr_node.getLeft();
		   }
		   else //needs to go throw the most right branch
		   {
			   curr_node = curr_node.getRight();
		   }
	   }
	   return curr_node;
   }

   public AVLTree higherTree(AVLTree T1, AVLTree T2) //returns the higher tree - O(1)
   {
	   if (T1.getRoot().getHeight() >= T2.getRoot().getHeight())
	   {
		   return T1;
	   }
	   return T2;
   }

	public AVLTree shorterTree(AVLTree T1, AVLTree T2) //returns the shorter tree - O(1)
	{
		if (T1.getRoot().getHeight() < T2.getRoot().getHeight())
		{
			return T1;
		}
		return T2;
	}
		/**
         * public interface IAVLNode
         * ! Do not delete or modify this - otherwise all tests will fail !
         */
	public interface IAVLNode{	
		public int getKey(); // Returns node's key (for virtual node return -1).
		public String getValue(); // Returns node's value [info], for virtual node returns null.
		public void setLeft(IAVLNode node); // Sets left child.
		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
		public void setRight(IAVLNode node); // Sets right child.
		public IAVLNode getRight(); // Returns right child, if there is no right child return null.
		public void setParent(IAVLNode node); // Sets parent.
		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
    	public void setHeight(int height); // Sets the height of the node.
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
		public int getSize(); // Returns the size of the subtree of this node - we added
		public void setSize(int size); // set the size of the subtree of this node - we added
	}



   /** 
    * public class AVLNode
    *
    * If you wish to implement classes other than AVLTree
    * (for example AVLNode), do it in this file, not in another file. 
    * 
    * This class can and MUST be modified (It must implement IAVLNode).
    */
  public static class AVLNode implements IAVLNode{ //added static

	  private int key;
	  private int height;
	  private IAVLNode left;
	  private IAVLNode right;
	  private String val;
	  private IAVLNode parent;
	  private int size;


	   public AVLNode(int key, String val) // constructor for real node - O(1)
	   {
		   this.key = key;
		   this.val = val;
		   this.size = 1;
	   }
	   public AVLNode(IAVLNode parent) // constructor for virtual node, sets the virtual node parent to be parent - O(1)
	   {
		   this.height = -1;
		   this.key = -1;
		   this.size = 0;
		   this.parent = parent;
	   }
		public int getKey() //O(1)
		{
			return this.key;
		}
		public String getValue() //O(1)
		{
			return this.val;
		}
		public void setLeft(IAVLNode node) //O(1)
		{
			this.left = node;
			return;
		}
		public IAVLNode getLeft() //O(1)
		{
			return this.left;
		}
		public void setRight(IAVLNode node) //O(1)
		{
			this.right = node;
			return;
		}
		public IAVLNode getRight() //O(1)
		{
			return this.right;
		}
		public void setParent(IAVLNode node) //O(1)
		{
			this.parent = node;
			return;
		}
		public IAVLNode getParent() //O(1)
		{
			return this.parent;
		}
		public boolean isRealNode() //O(1)
		{
			return this.height!=-1;
		}
	    public void setHeight(int height) //O(1)
	    {
			this.height = height;
	        return;
	    }
	    public int getHeight() //O(1)
	    {
	      return this.height;
	    }
	   public int getSize() //O(1)
	   {
		   return this.size;
	   }
	   public void setSize(int size) //O(1)
	   {
		   this.size = size;
		   return;
	   }

   }
}
  
