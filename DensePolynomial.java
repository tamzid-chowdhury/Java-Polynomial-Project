import java.util.ArrayList;
import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeMap;

public class DensePolynomial implements Polynomial {
    private int[] coefficients;
    private String input;

    /**
     * Creates an instance of a DensePolynomial object from the canonical string representation
     * Precondition: String must not contain any negative numbers; Coefficients and degrees must both be integers
     * Postcondition: Returns instance of DensePolynomial with coefficients stored in an array where indices represent degrees
     * @param s the canonical string representation of a polynomial 
     * @throws IllegalArgumentException if a coefficient or degree are not integers or we are given a string that is not canonical 
     */
    public DensePolynomial(String s) {
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

        this.coefficients = new int[degrees.get(0)+1];

        for(int i = 0; i < degrees.size(); i++) {
            this.coefficients[degrees.get(i)] = coefficients.get(i);
        }

    }
    
    
    /**
     * Creates an instance of a DensePolynomial object from a coefficient array and using the toString method for the input
     * Precondition: None
     * Postcondition: Returns instance of DensePolynomial with coefficients represented in an array with indices representing degrees
     * @param coefficients the integer array 
     *  
     */
    private DensePolynomial(int[] coefficients) {
    	this.coefficients = coefficients;
    	if(this.isZero()){
    	    this.coefficients = new int[]{0};
        }
    	this.input = this.toString();
    }

    /**
     * Returns the degree of the polynomial.
     * Precondition: None
     * Postcondition: Returns the degree of the polynomial as an integer
     * @return the largest exponent with a non-zero coefficient.  If all terms have zero exponents, it returns 0.
     */
    @Override
    public int degree() {
        int degree = 0; 
        for(int i = 0; i < coefficients.length; i++) {
        	if(coefficients[i] != 0) {
        		degree = i; 
        	}
        }
        return degree;
    }
    
    /**
     * Returns the coefficient corresponding to the given exponent.  Returns 0 if there is no term with that exponent
     * in the polynomial. 
     * Precondition: d is not negative
     * Postcondition: Returns the coefficient of the corresponding exponent
     *
     * @param d the exponent whose coefficient is returned.
     * @return the coefficient of the term of whose exponent is d.
     * @throws IllegalArgumentException if exponent is a negative number
     */
    @Override
    public int getCoefficient(int d) {
        int polDegree = degree(); 
        if(d < 0) {
        	throw new IllegalArgumentException("We cannot have negative exponents for a dense polynomial");
        }
        if(d > polDegree) {
            return 0;
        }
        return coefficients[d];
    }

    
    /**
     * Checks the polynomial to see if it represents the zero constant
     * Precondition: None
     * Postcondition: returns true if polynomial represents the zero constant, if not then it returns false
     * @return true if the polynomial represents the zero constant
     */
    @Override
    public boolean isZero() {
        for(int i = 0; i < coefficients.length; i++) {
        	if(coefficients[i] != 0) {
        		return false; 
        	}
        }
        
        return true; 
       
    }

    
    /**
     * Returns a polynomial by adding the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     * Precondition: q is not null && SparsePolynomial cannot have negative exponents
     * Postcondition: returns a polynomial representing the sum of this + q
     * @param q the non-null polynomial to add to <code>this</code>
     * @return <code>this + </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if DensePolynomial + SparsePolynomial with Negative Exponents
     */
    @Override
    public Polynomial add(Polynomial q) {
        if(q == null) {
        	throw new NullPointerException("Argument is null");
        }
        
        DensePolynomial d;
        
        if(q instanceof SparsePolynomial) {
        	SparsePolynomial s = (SparsePolynomial) q;
            TreeMap<Integer,Integer> map = s.getMap();
            NavigableSet<Integer> degrees = map.navigableKeySet();
            for(Integer degree: degrees) {
                if (degree < 0) {
                    throw new IllegalArgumentException("DensePolynomial cannot have negative exponents");
                }
            }

        	d = convertToDense(s);
        }

        else {
        	d = (DensePolynomial) q; 
        }
        
        int[] p1 = (this.degree() > d.degree()) ? new int[this.degree()+ 1] : new int[d.degree()+1];
        for(int i = 0; i < this.coefficients.length; i++) {
        	p1[i] = this.coefficients[i];
        }
        
        int[] p2 = (this.degree() > d.degree()) ? new int[this.degree()+ 1] : new int[d.degree()+1];
        for(int i = 0; i < d.coefficients.length; i++) {
        	p2[i] = d.coefficients[i];
        }
    
        int[] sum = (this.degree() > d.degree()) ? new int[this.degree()+ 1] : new int[d.degree()+1];
        
        for(int i = 0; i < sum.length; i++) {
        	sum[i] = p1[i] + p2[i];	
        }
        
        return new DensePolynomial(sum);
        
    }
    
    /**
     * Returns a polynomial by multiplying the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     *
     * Precondition: q is not null && SparsePolynomial cannot have negative exponents
     * Postcondition: returns a polynomial representing the product of this * q
     *
     * @param q the non-null polynomial to multiply to <code>this</code>
     * @return <code>this * </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if DensePolynomial * SparcePolynomial with Negative Exponents
     */
    @Override
    public Polynomial multiply(Polynomial q) {
        if(q == null) {
        	throw new NullPointerException("Argument is null");
        }
        
        DensePolynomial d;

        if(q instanceof SparsePolynomial) {
            SparsePolynomial s = (SparsePolynomial) q;
            TreeMap<Integer,Integer> map = s.getMap();
            NavigableSet<Integer> degrees = map.navigableKeySet();
            for(Integer degree: degrees) {
                if (degree < 0) {
                    throw new IllegalArgumentException("DensePolynomial cannot have negative exponents");
                }
            }

            d = convertToDense(s);
        }

        else {
        	d = (DensePolynomial) q; 
        }
        	
        int m = this.coefficients.length; 
        int n = d.coefficients.length; 
        
        int[] prod = new int[m + n - 1];
        
        for(int i = 0; i < m; i++) {
        	for(int j = 0; j < n; j++) {
        		prod[i+j] += this.coefficients[i] * d.coefficients[j];
        	}
        }
        
        return new DensePolynomial(prod);
        
    }
    
    /**
     * Returns a polynomial by subtracting the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     *
     * Precondition: q is not null && SparsePolynomial cannot have negative exponents
     * Postcondition: returns a polynomial representing the difference of this - q
     *
     * @param q the non-null polynomial to subtract to <code>this</code>
     * @return <code>this - </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if DensePolynomial - SparcePolynomial with Negative Exponents
     */

    @Override
    public Polynomial subtract(Polynomial q) {
        if(q == null) {
        	throw new NullPointerException("Argument is null");
        }
        
        DensePolynomial d;

        if(q instanceof SparsePolynomial) {
            SparsePolynomial s = (SparsePolynomial) q;
            TreeMap<Integer,Integer> map = s.getMap();
            NavigableSet<Integer> degrees = map.navigableKeySet();
            for(Integer degree: degrees) {
                if (degree < 0) {
                    throw new IllegalArgumentException("DensePolynomial cannot have negative exponents");
                }
            }

            d = convertToDense(s);
        }

        else {
        	d = (DensePolynomial) q; 
        }
        
        int[] p1 = (this.degree() > d.degree()) ? new int[this.degree()+ 1] : new int[d.degree()+1];
        for(int i = 0; i < this.coefficients.length; i++) {
        	p1[i] = this.coefficients[i];
        }
        
        int[] p2 = (this.degree() > d.degree()) ? new int[this.degree()+ 1] : new int[d.degree()+1];
        for(int i = 0; i < d.coefficients.length; i++) {
        	p2[i] = d.coefficients[i];
        }
    
        int[] diff = (this.degree() > d.degree()) ? new int[this.degree()+ 1] : new int[d.degree()+1];
        
        for(int i = 0; i < diff.length; i++) {
        	diff[i] = p1[i] - p2[i];	
        }
        
        return new DensePolynomial(diff);
        
    }
    
    /**
     * Returns a polynomial by negating the current instance. The current instance is not modified.
     * Precondition: None
     * Postcondition: Returns the negated polynomial of this
     * @return -this
     */  
    @Override
    public Polynomial minus() {
        int[] negatedCoeff = this.coefficients; 
        for(int i = 0; i < negatedCoeff.length; i++) {
        	negatedCoeff[i] = -(negatedCoeff[i]);
        }
        
        return new DensePolynomial(negatedCoeff);
    }

    /**
     * Checks the input string to make sure that we are given a string that contains a valid canonical string as well
     * as makes sure that invariant holds true that both the coefficients and degrees are all integers
     * Precondition: None
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

        //check if degrees are negative for dense polynomial
        for(int i = 0; i < degrees.size(); i++) {
            if(degrees.get(i) < 0) {
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
     *
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
    	
    	for(int i=0; i < coefficients.length; i++) {
    		if(coefficients[i] != 0) {
    			String term = "";
    			if(i == 0) {
    				term += coefficients[i];
    			}
    			else if(i == 1) {
    				if(coefficients[i] == 1) {
    					term += "x";
    				}
    				else {
    					term += coefficients[i] + "x";
    				}
    			}
    			else {
    				if(coefficients[i] == 1) {
    					term += "x^" + i;
    				}
    				else {
    					term += coefficients[i] + "x^" + i;
    				}
    			}
    			terms.add(term);
    		}
    			
    	}
    	
    	for(int i=terms.size()-1; i>0; i--) {
    		s += terms.get(i) + " + ";
    	}
    	
    	s += terms.get(0);
    	
    		
    	return s; 
    }
    
    /**
     * Equals method overrode from the object class checks if the polynomials have the same coefficients and exponents
     *
     * Precondition: o must be instance of DensePolynomial
     * Postcondition: return true if this and o represent same polynomial
     *
     * @return true if the two polynomial objects represent the same polynomial 
     * @throws IllegalArgumentException if object is not instance of DensePolynomial
     */ 
    @Override 
    public boolean equals(Object o) {
    	if(!(o instanceof DensePolynomial)) {
    		throw new IllegalArgumentException("Object is not a DensePolynomial");
    	}
    	
    	DensePolynomial other = (DensePolynomial) o;
        
    	return Arrays.equals(this.coefficients, other.coefficients);
    }
    
    /**
     * Getter method which returns the coefficients array of the polynomial object
     *
     * Precondition: None
     * Postcondition: returns coefficients array
     *
     * @return the coefficients array  
     * 
     */  
    public int[] getCoefficients() {
    	return coefficients;
    }
    
    
    /**
     * Converts an instance of a sparsepolynomial object into a densepolynomial object
     *
     * Preconditions: None
     * Postcondition: returns a DensePolynomial which has the same polynomial representation
     * as the original SparsePolynomial
     *
     * @return SparePolynomial representation of our instance 
     *
     */   
    private DensePolynomial convertToDense(SparsePolynomial s) {
    	TreeMap<Integer,Integer> map = s.getMap();
    	NavigableSet<Integer> degrees = map.navigableKeySet();
    	for(Integer degree: degrees) {
    		assert degree >= 0;
    	}
    	int[] coefficients = new int[s.degree()+1];
    	for(Integer degree: degrees) {
    		coefficients[degree] = map.get(degree);
    	}
    	return new DensePolynomial(coefficients);
    	
    }



}