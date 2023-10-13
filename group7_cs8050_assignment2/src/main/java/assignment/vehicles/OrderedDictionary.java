package assignment.vehicles;

public class OrderedDictionary implements OrderedDictionaryADT {

    Node root;

    OrderedDictionary() {
        root = new Node();
    }

    /**
     * Returns the Record object with key k, or it returns null if such a record
     * is not in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment/vehicles/DictionaryException.java
     */
    @Override
    public VehicleRecord find(DataKey k) throws DictionaryException {
        Node current = root;
        int comparison;
        if (root.isEmpty()) {         
            throw new DictionaryException("There is no record matches the given key");
        }
        
        if(k.getVehicleName().isEmpty()) {
        	throw new DictionaryException("Input cannot be empty, Please enter the search query");
        }

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0 || (current.getData().getDataKey().getVehicleName().toLowerCase().contains(k.getVehicleName().toLowerCase()) && current.getData().getDataKey().getVehicleSize() == k.getVehicleSize())) { // key found
                return current.getData();
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getRightChild();
            }
        }

    }

    /**
     * Inserts r into the ordered dictionary. It throws a DictionaryException if
     * a record with the same key as r is already in the dictionary.
     *
     * @param r
     * @throws vehicles.DictionaryException
     */
    @Override
    public void insert(VehicleRecord r) throws DictionaryException {
        DataKey k = r.getDataKey();
        root = insertRec(root, r, k);
    }

    // Helper method for inserting a record
    private Node insertRec(Node root, VehicleRecord r, DataKey k) throws DictionaryException {
        if (root == null || root.getData().getDataKey() == null) {
            return new Node(r);
        }

        int comparison = root.getData().getDataKey().compareTo(k);
        if (comparison == 0) {
            throw new DictionaryException("Duplicate record with key " + k.toString());
        }

        if (comparison > 0) {
            root.setLeftChild(insertRec(root.getLeftChild(), r, k));
        } else {
            root.setRightChild(insertRec(root.getRightChild(), r, k));
        }

        return root;
    }
    
    /**
     * Removes the record with Key k from the dictionary. It throws a
     * DictionaryException if the record is not in the dictionary.
     *
     * @param k
     * @throws vehicles.DictionaryException
     */
    @Override
    public void remove(DataKey k) throws DictionaryException {
    	root = removeRec(root, k);
    }
    
    // Helper method for removing a record
    private Node removeRec(Node root, DataKey k) throws DictionaryException {
        if (root == null) {
            throw new DictionaryException("Record not found");
        }

        int comparison = root.getData().getDataKey().compareTo(k);
        if (comparison > 0) {
            root.setLeftChild(removeRec(root.getLeftChild(), k));
        } else if (comparison < 0) {
            root.setRightChild(removeRec(root.getRightChild(), k));
        } else {
            // Node with only one child or no child
            if (root.getLeftChild() == null) {
                return root.getRightChild();
            } else if (root.getRightChild() == null) {
                return root.getLeftChild();
            }

            // Node with two children: Get the inorder successor
            root.setData(findSmallest(root.getRightChild()).getData());
            // Delete the inorder successor
            root.setRightChild(removeRec(root.getRightChild(), root.getData().getDataKey()));
        }

        return root;
    }

    // Helper method to find the node with the smallest key in a subtree
    private Node findSmallest(Node root) {
        while (root.getLeftChild() != null) {
            root = root.getLeftChild();
        }
        return root;
    }
    
    /**
     * Returns the successor of k (the record from the ordered dictionary with
     * smallest key larger than k); it returns null if the given key has no
     * successor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws vehicles.DictionaryException
     */
    @Override
    public VehicleRecord successor(DataKey k) throws DictionaryException{
        Node successor = findSuccessor(root, k);
        if (successor == null) {
            throw new DictionaryException("No successor found");
        }
        return successor.getData();
    }
    
    // Helper method to find the successor of a given key
    private Node findSuccessor(Node root, DataKey k) {
        if (root == null) {
            return null;
        }

        int comparison = root.getData().getDataKey().compareTo(k);
        if (comparison <= 0) {
            return findSuccessor(root.getRightChild(), k);
        }

        Node leftSuccessor = findSuccessor(root.getLeftChild(), k);
        return leftSuccessor != null ? leftSuccessor : root;
    }

   
    /**
     * Returns the predecessor of k (the record from the ordered dictionary with
     * largest key smaller than k; it returns null if the given key has no
     * predecessor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws vehicles.DictionaryException
     */
    @Override
    public VehicleRecord predecessor(DataKey k) throws DictionaryException{
    	Node predecessor = findPredecessor(root, k);
        if (predecessor == null) {
            throw new DictionaryException("No predecessor found");
        }
        return predecessor.getData();
    }

    // Helper method to find the predecessor of a given key
    private Node findPredecessor(Node root, DataKey k) {
        if (root == null) {
            return null;
        }

        int comparison = root.getData().getDataKey().compareTo(k);
        if (comparison >= 0) {
            return findPredecessor(root.getLeftChild(), k);
        }

        Node rightPredecessor = findPredecessor(root.getRightChild(), k);
        return rightPredecessor != null ? rightPredecessor : root;
    }
    
    /**
     * Returns the record with smallest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     *
     * @return
     */
    @Override
    public VehicleRecord smallest() throws DictionaryException{
    	if (root == null) {
            throw new DictionaryException("Dictionary is empty");
        }
        return findSmallest(root).getData();
    }

    /*
	 * Returns the record with largest key in the ordered dictionary. Returns
	 * null if the dictionary is empty.
     */
    @Override
    public VehicleRecord largest() throws DictionaryException{
    	if (root == null) {
            throw new DictionaryException("Dictionary is empty");
        }
        return findLargest(root).getData();
    }
    
    // Helper method to find the node with the largest key in a subtree
    private Node findLargest(Node root) {
        while (root.getRightChild() != null) {
            root = root.getRightChild();
        }
        return root;
    }
      
    /* Returns true if the dictionary is empty, and true otherwise. */
    @Override
    public boolean isEmpty (){
        return root == null || root.isEmpty();
    }
}
