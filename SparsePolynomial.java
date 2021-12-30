import java.util.ArrayList;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeMap;


public class SparsePolynomial implements Polynomial {
    private TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();
    private String input;


    /**
     * Creates an instance of a SparsePolynomial object from the canonical string representation
     *
     * Precondition: Coefficients and degrees in String s must be integers
     * Postcondition: Creates instance of SparsePolynomial
     *
     * @param s the canonical string representation of a polynomial
     * @throws IllegalArgumentException if a coefficient or degree are not integers or we are given a string that is not canonical
     */
    public SparsePolynomial(String s) {
        this.input = s;
        if(wellFormed() == false) {
            throw new IllegalArgumentException("The input string given is invalid");
        }

        input = input.replace(" - ", " + -");
        String[] terms = input.split("\\+");

        ArrayList<Integer> coefficients = new ArrayList<Integer>();
        ArrayList<Integer> degrees = new ArrayList<Integer>();

        for(String term: terms) {
            term = term.strip();
            if(term.contains("x^")) {
                if(term.charAt(0) == 'x') {
                    term = term.replace("x^", "");
                    int degree = Integer.parseInt(term);
                    coefficients.add(1);
                    degrees.add(degree);

                }
                else {
                    String[] nums = term.split("x\\^");
                    int coefficient = Integer.parseInt(nums[0]);
                    int degree = Integer.parseInt(nums[1]);
                    coefficients.add(coefficient);
                    degrees.add(degree);
                }

            }

            else if(term.contains("x")) {
                if(term.equals("x")) {
                    coefficients.add(1);
                    degrees.add(1);
                }
                else if(term.equals("-x")){
                    coefficients.add(-1);
                    degrees.add(1);
                }
                else {
                    String[] nums = term.split("x");
                    int coefficient = Integer.parseInt(nums[0]);
                    coefficients.add(coefficient);
                    degrees.add(1);
                }
            }
            else {
                int coefficient = Integer.parseInt(term);
                coefficients.add(coefficient);
                degrees.add(0);
            }
        }

        for(int i = 0; i < coefficients.size(); i++) {
            map.put(degrees.get(i), coefficients.get(i));
        }
    }

    
    /**
     * Creates an instance of a SparsePolynomial object from map and using the toString method for the input
     * Precondition: None
     * Postcondition: Creates instance of SparsePolynomial from map and using toString for input
     * @param map the treemap that holds degrees and coefficients  
     *  
     */
    private SparsePolynomial(TreeMap<Integer,Integer> map) {
    	this.map = map;
        if(this.isZero()){
            this.map = new TreeMap<Integer,Integer>();
            this.map.put(0,0);
        }
    	this.input = this.toString();
    }
    /**
     * Returns the degree of the polynomial.
     * Precondition: None
     * Postcondition: Returns degree of polynomial
     * @return the largest exponent with a non-zero coefficient.  If all terms have zero exponents, it returns 0.
     */
    @Override
    public int degree() {
        NavigableSet<Integer> s = map.descendingKeySet();
        return s.first();
    }
    
    /**
     * Returns the coefficient corresponding to the given exponent.  Returns 0 if there is no term with that exponent
     * in the polynomial. 
     *
     * Precondition: None
     * Postcondition: returns the coefficient of d
     *
     * @param d the exponent whose coefficient is returned.
     * @return the coefficient of the term of whose exponent is d.
     */
    @Override
    public int getCoefficient(int d) {
        if(map.containsKey(d)) {
        	return map.get(d);
        }
        else {
        	return 0; 
        }
    }

    /**
     * Checks the polynomial to see if it represents the zero constant
     * Precondition: None
     * Postcondition: returns true if polynomial represents the zero constant, if not then it returns false
     * @return true if the polynomial represents the zero constant
     */
    @Override
    public boolean isZero() {
        Collection<Integer> c = map.values();
        for(Integer x: c) {
        	if(x != 0) {
        		return false; 
        	}
        }
        return true;
    }
    
    /**
     * Returns a polynomial by adding the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     *
     * Precondition: q is not null && SparsePolynomial cannot have negative exponents
     * Postcondition: returns a polynomial representing the sum of this + q
     *
     * @param q the non-null polynomial to add to <code>this</code>
     * @return <code>this + </code>q
     * @throws NullPointerException if q is null
     */
	@Override
	public Polynomial add(Polynomial q) {
		if (q == null) {
			throw new NullPointerException("Argument is null");
		}

		SparsePolynomial s;

		if (q instanceof DensePolynomial) {
			DensePolynomial d = (DensePolynomial) q;
			s = convertToSparse(d);
			
		} 
		else {
			s = (SparsePolynomial) q;
		}
		
		@SuppressWarnings("unchecked")
		TreeMap<Integer, Integer> map1 = (TreeMap<Integer, Integer>) this.map.clone();
		@SuppressWarnings("unchecked")
		TreeMap<Integer, Integer> map2 = (TreeMap<Integer, Integer>) s.map.clone(); 
		
		NavigableSet<Integer> keyset = map2.navigableKeySet();
		
		for(int key: keyset) {
			map1.put(key, map1.getOrDefault(key,0)+map2.get(key));
		}
		
		return new SparsePolynomial(map1);
	}

    /**
     * Returns a polynomial by multiplying the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     *
     * Precondition: q is not null
     * Postcondition: returns a polynomial representing the product of this * q
     *
     * @param q the non-null polynomial to multiply to <code>this</code>
     * @return <code>this * </code>q
     * @throws NullPointerException if q is null
     */
    @Override
    public Polynomial multiply(Polynomial q) {
		if (q == null) {
			throw new NullPointerException("Argument is null");
		}

		SparsePolynomial s;

		if (q instanceof DensePolynomial) {
			DensePolynomial d = (DensePolynomial) q;
			s = convertToSparse(d);
			
		} 
		else {
			s = (SparsePolynomial) q;
		}
		
		
		TreeMap<Integer, Integer> map1 = this.map;
		TreeMap<Integer, Integer> map2 = s.map; 
		
		NavigableSet<Integer> map1Keys = map1.navigableKeySet();
		NavigableSet<Integer> map2Keys = map2.navigableKeySet();
		
		
		TreeMap<Integer, Integer> newMap = new TreeMap<Integer,Integer>();
		
		for(int i: map1Keys) {
			for(int j: map2Keys) {
				newMap.put(i+j, newMap.getOrDefault(i+j, 0)+(map1.get(i)*map2.get(j)));
			}
		}
		
		return new SparsePolynomial(newMap);
    }

    /**
     * Returns a polynomial by subtracting the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     *
     * Precondition: q is not null
     * Postcondition: returns a polynomial representing the difference of this - q
     *
     * @param q the non-null polynomial to subtract to <code>this</code>
     * @return <code>this - </code>q
     * @throws NullPointerException if q is null
     * 
     */
    @Override
    public Polynomial subtract(Polynomial q) {
		if (q == null) {
			throw new NullPointerException("Argument is null");
		}

		SparsePolynomial s;

		if (q instanceof DensePolynomial) {
			DensePolynomial d = (DensePolynomial) q;
			s = convertToSparse(d);
			
		} 
		else {
			s = (SparsePolynomial) q;
		}
		
		@SuppressWarnings("unchecked")
		TreeMap<Integer, Integer> map1 = (TreeMap<Integer, Integer>) this.map.clone();
		@SuppressWarnings("unchecked")
		TreeMap<Integer, Integer> map2 = (TreeMap<Integer, Integer>) s.map.clone();  
		
		NavigableSet<Integer> keyset = map2.navigableKeySet();
		
		for(int key: keyset) {
			map1.put(key, map1.getOrDefault(key,0)-map2.get(key));
		}
		
		return new SparsePolynomial(map1);
    }
    
    /**
     * Returns a polynomial by negating the current instance. The current instance is not modified.
     *
     * Precondition: None
     * Postcondition: Returns the negated polynomial of this
     *
     * @return -this
     */
    @Override
    public Polynomial minus() {
    	TreeMap<Integer,Integer> newMap = new TreeMap<Integer,Integer>();
    	
    	NavigableSet<Integer> degrees = map.navigableKeySet();
    	for(Integer degree: degrees) {  
    		int value = -(map.get(degree));
    		newMap.put(degree, value);
    	}
    	
    	return new SparsePolynomial(newMap);
    }

    /**
     * Checks the input string to make sure that we are given a string that contains a valid canonical string as well
     * as makes sure that invariant holds true that both the coefficients and degrees are all integers
     * Precondition: None
     *
     * PostCondition: returns true if class invariant holds and canonical string can be formed else returns false
     *
     * @return {@literal true} if the class invariant holds and we are given a canonical string or {@literal false} if the class invarient is not
     * true and we are not given a canonical string
     */
    @Override
    public boolean wellFormed() {
        if(isValid(input) == false)
            return false;
        
        if(input.equals("0")) {
        	return true; 
        }

        input = input.replace(" - ", " + -");
        String[] terms = input.split("\\+");

        ArrayList<Integer> coefficients = new ArrayList<Integer>();
        ArrayList<Integer> degrees = new ArrayList<Integer>();

        for(String term: terms) {
            term = term.strip();
            if(term.contains("x^")) {
                if(term.charAt(0) == 'x') {
                    term = term.replace("x^", "");
                    int degree = Integer.parseInt(term);
                    coefficients.add(1);
                    degrees.add(degree);

                }
                else {
                    String[] nums = term.split("x\\^");
                    int coefficient = Integer.parseInt(nums[0]);
                    int degree = Integer.parseInt(nums[1]);
                    coefficients.add(coefficient);
                    degrees.add(degree);
                }

            }

            else if(term.contains("x")) {
                if(term.equals("x")) {
                    coefficients.add(1);
                    degrees.add(1);
                }
                else if(term.equals("-x")){
                    coefficients.add(-1);
                    degrees.add(1);
                }
                else {
                    String[] nums = term.split("x");
                    int coefficient = Integer.parseInt(nums[0]);
                    coefficients.add(coefficient);
                    degrees.add(1);
                }
            }
            else {
                int coefficient = Integer.parseInt(term);
                coefficients.add(coefficient);
                degrees.add(0);
            }
        }


        //if coefficients contains a 0 then we are not in canonical form
        if(coefficients.contains(0)) {
            return false;
        }

        //check if the degrees are in descending order
        for(int i = 0; i < degrees.size()-1; i++) {
            if(degrees.get(i) < degrees.get(i+1)) {
                return false;
            }
        }


        return true;

    }

    /**
     * Checks the input string to make sure that we are not given a string that contains any characters that would not result
     * in a valid polynomial expression. Any characters besides digits, plus, minus, the letter x, ^, or a space would result in
     * an invalid expression
     * Precondition: None
     * Postcondition: Returns false if String would not represent a valid polynomial expression else returns true
     * @param s the canonical string representation of a polynomial
     */
    private boolean isValid(String s) {
        char[] chSearch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '^', 'x', ' '};
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            boolean isPresent = false;
            for (int j = 0; j < chSearch.length; j++) {
                if (chSearch[j] == ch) {
                    isPresent = true;
                }
            }
            if(isPresent == false) {
                return false;
            }

        }
        return true;
    }

    /**
     * Method takes the polynomial object and returns a string representation in canonical form
     * Precondition: None
     * Postcondition: Returns string representation of polynomial
     * @return String representation of the polynomial in canonical form
     */
    @Override
    public String toString() {
    	String s = "";
    	ArrayList<String> terms = new ArrayList<String>();

        if(this.isZero()){
            return "0";
        }
    	
    	NavigableSet<Integer> degrees = map.navigableKeySet();
    	for(Integer degree: degrees) {
    		String term = "";
    		if(degree == 0) {
    			term += map.get(degree);
    		}
    		else if(degree == 1) {
    			if(map.get(degree) == 1) {
    				term += "x";
    			}
    			else {
    				term += map.get(degree) + "x";
    			}
    		}
    		else {
    			if(map.get(degree) == 1) {
    				term += "x^" + degree;
    			}
    			else {
    				term += map.get(degree) + "x^" + degree;
    			}
    		}
    		terms.add(term);
    	}
    	
    	for(int i=terms.size()-1; i>0; i--) {
    		s += terms.get(i) + " + ";
    	}
    	
    	s += terms.get(0);
    	
    		
    	return s; 
    }
    
    /**
     * Equals method overrode from the object class checks if the polynomials have the same coefficients and exponents
     *Precondition: o must be instance of SparsePolynomial
     *Postcondition: return true if this and o represent same polynomial
     * @return true if the two polynomial objects represent the same polynomial 
     * @throws IllegalArgumentException if object is not instance of SparsePolynomial
     */ 
    
    @Override 
    public boolean equals(Object o) {
    	if(!(o instanceof SparsePolynomial)) {
    		throw new IllegalArgumentException("Object is not a SparsePolynomial");
    	}
    	
    	SparsePolynomial other = (SparsePolynomial) o;
    	
    	return this.map.equals(other.map);
    	
    	
    }
    
    /**
     * Getter method which returns the map of the polynomial object
     * Precondition: None
     * Postcondition: Returns map of polynomial object
     * @return the map with coefficients and degrees as key value pairs  
     * 
     */  
    public TreeMap<Integer,Integer> getMap(){
    	return map; 
    }
    

    
    /**
     * Converts an instance of a densepolynomial object into a sparsepolynomial object
     * Precondition: None
     * Postcondition: returns instance of a sparsepolynomial object representing the
     * same polynomial as the original densepolynomial
     * @return SparePolynomial representation of our instance 
     * 
     */
    private SparsePolynomial convertToSparse(DensePolynomial d) {
    	int[] coefficients = d.getCoefficients();  
    	TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();
    	for(int i = 0; i < coefficients.length; i++) {
    		if(coefficients[i] != 0)
    			map.put(i, coefficients[i]);
    	}
    	return new SparsePolynomial(map);
    }
    
}
