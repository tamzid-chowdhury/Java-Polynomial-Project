import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DensePolynomialTest {



    @Test
    void testDegree() {
        DensePolynomial testZero = new DensePolynomial("0");
        // zero polynomial should return degree 0
        assertEquals(0,testZero.degree());

        DensePolynomial testNormal = new DensePolynomial("5x^7 + 4x^3 + -2x + 8");
        //testing regular polynomial
        assertEquals(7,testNormal.degree());

        //testing polynomial with large degree
        DensePolynomial testLarge = new DensePolynomial("8x^1234567 + 3x^12345 + 25x^123 + 8x^12");
        assertEquals(1234567, testLarge.degree());

    }

    @Test
    void testGetCoefficient() {
        DensePolynomial testPolynomial = new DensePolynomial("12x^9876543 + -8x^42 + 25x + 2");

        //testing degree 0
        assertEquals(2,testPolynomial.getCoefficient(0));

        //testing degree 1
        assertEquals(25,testPolynomial.getCoefficient(1));

        //testing degree which returns negative exponent
        assertEquals(-8,testPolynomial.getCoefficient(42));

        //testing large degree
        assertEquals(12, testPolynomial.getCoefficient(9876543));

        //testing that negative degree throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class,() -> testPolynomial.getCoefficient(-5));
    }

    @Test
    void testIsZero() {
        DensePolynomial zeroPolynomial = new DensePolynomial("0");
        //zero polynomial should return true
        assertTrue(zeroPolynomial.isZero());

        DensePolynomial notZeroPolynomial = new DensePolynomial("3x^4 + -2x^2 + 8");
        //nonzero polynomial should return false
        assertFalse(notZeroPolynomial.isZero());

    }

    @Test
    void testAdd() {
        DensePolynomial densePolynomial1 = new DensePolynomial("12x^4 + 8x^3 + 5x^2 + 2x + 7");
        DensePolynomial densePolynomial2 = new DensePolynomial("3x^4 + 4x^3 + 6x");
        DensePolynomial zeroPolynomial = new DensePolynomial("0");
        DensePolynomial nullPolynomial = null;
        SparsePolynomial sparsePolynomial = new SparsePolynomial("5x^4 + -x + 8");
        SparsePolynomial sparsePolynomialWithNegatives = new SparsePolynomial("7x^4 + 2x + 5x^-2");

        //testing dense polynomial added to another dense polynomial
        assertEquals(new DensePolynomial("15x^4 + 12x^3 + 5x^2 + 8x + 7"), densePolynomial1.add(densePolynomial2));

        //testing dense polynomial added to a sparse polynomial with positive exponents
        assertEquals(new DensePolynomial("17x^4 + 8x^3 + 5x^2 + x + 15"), densePolynomial1.add(sparsePolynomial));

        //testing that adding dense polynomial too a zero polynomial will return itself e.g. x + 0 = x
        assertEquals(densePolynomial1, densePolynomial1.add(zeroPolynomial));

        //testing that adding a dense polynomial to a null polynomial will throw null pointer exception
        assertThrows(NullPointerException.class, () -> densePolynomial1.add(nullPolynomial));

        //testing that adding a dense polynomial to a sparse polynomial with negatives will throw illegal argument exception
        assertThrows(IllegalArgumentException.class, () -> densePolynomial1.add(sparsePolynomialWithNegatives));
    }

    @Test
    void testMultiply() {
        DensePolynomial densePolynomial1 = new DensePolynomial("4x^2 + 8x + 2");
        DensePolynomial densePolynomial2 = new DensePolynomial("12x^2 + 3x");
        DensePolynomial zeroPolynomial = new DensePolynomial("0");
        DensePolynomial nullPolynomial = null;
        SparsePolynomial sparsePolynomial = new SparsePolynomial("2x + 3");
        SparsePolynomial sparsePolynomialWithNegatives = new SparsePolynomial("7x^4 + 2x + 5x^-2");

        //testing dense polynomial multiplied by another dense polynomial
        assertEquals(new DensePolynomial("48x^4 + 108x^3 + 48x^2 + 6x"), densePolynomial1.multiply(densePolynomial2));

        //testing dense polynomial multiplied by a sparse polynomial with positive exponents
        assertEquals(new DensePolynomial("8x^3 + 28x^2 + 28x + 6"), densePolynomial1.multiply(sparsePolynomial));

        //testing that multiplying dense polynomial by a zero polynomial will return zero polynomial e.g. x * 0 = 0
        assertEquals(zeroPolynomial, densePolynomial1.multiply(zeroPolynomial));

        //testing that multiplying a dense polynomial to a null polynomial will throw null pointer exception
        assertThrows(NullPointerException.class, () -> densePolynomial1.multiply(nullPolynomial));

        //testing that multiplying a dense polynomial to a sparse polynomial with negatives will throw illegal argument exception
        assertThrows(IllegalArgumentException.class, () -> densePolynomial1.multiply(sparsePolynomialWithNegatives));
    }

    @Test
    void testSubtract() {
        DensePolynomial densePolynomial1 = new DensePolynomial("12x^4 + 8x^3 + 5x^2 + 2x + 7");
        DensePolynomial densePolynomial2 = new DensePolynomial("3x^4 + 4x^3 + 6x");
        DensePolynomial zeroPolynomial = new DensePolynomial("0");
        DensePolynomial nullPolynomial = null;
        SparsePolynomial sparsePolynomial = new SparsePolynomial("5x^4 + -x + 8");
        SparsePolynomial sparsePolynomialWithNegatives = new SparsePolynomial("7x^4 + 2x + 5x^-2");

        //testing dense polynomial subtracted by another dense polynomial
        assertEquals(new DensePolynomial("9x^4 + 4x^3 + 5x^2 + -4x + 7"), densePolynomial1.subtract(densePolynomial2));

        //testing dense polynomial subtracted by a sparse polynomial with positive exponents
        assertEquals(new DensePolynomial("7x^4 + 8x^3 + 5x^2 + 3x + -1"), densePolynomial1.subtract(sparsePolynomial));

        //testing that subtracting dense polynomial by a zero polynomial will return itself e.g. x - 0 = x
        assertEquals(densePolynomial1, densePolynomial1.subtract(zeroPolynomial));

        //testing that subtracting a dense polynomial to a null polynomial will throw null pointer exception
        assertThrows(NullPointerException.class, () -> densePolynomial1.subtract(nullPolynomial));

        //testing that subtracting a dense polynomial by a spase polynomial with negatives will throw illegal argument exception
        assertThrows(IllegalArgumentException.class, () -> densePolynomial1.subtract(sparsePolynomialWithNegatives));
    }

    @Test
    void testMinus() {
        DensePolynomial densePolynomial1 = new DensePolynomial("4x^2 + 8x + 2");
        DensePolynomial densePolynomial2 = new DensePolynomial("5");
        DensePolynomial zeroPolynomial = new DensePolynomial("0");

        //test regular dense polynomial
        assertEquals(new DensePolynomial("-4x^2 + -8x + -2"), densePolynomial1.minus());

        //test a single constant polynomial
        assertEquals(new DensePolynomial("-5"), densePolynomial2.minus());

        //test zero polynomial
        assertEquals(zeroPolynomial, zeroPolynomial.minus());
    }

    /*DensePolynomial constructor uses wellFormed() to make sure invariants are true (exponents and
    * coefficients are integers) as well as making sure the string is in canonical form. I will be testing
    * this method by testing if instances can be created*/
    @Test
    void testWellFormed() {
        DensePolynomial densePolynomial1 = new DensePolynomial("4x^2 + 8x + 2");
        //test that densePolynomial1 is well formed -> should return true as invariant is true and its in canonical form
        assertTrue(densePolynomial1.wellFormed());

        DensePolynomial densePolynomial2 = new DensePolynomial("5");
        //test that densePolynomial2 (a single constant) is well formed -> should return true as invariant is true and its in canonical form
        assertTrue(densePolynomial2.wellFormed());

        /*throws IllegalArgumentException because when creating instance, the well formed method returned false
        * because we have non integer coefficients and degrees indicating that the invariant is false*/
        assertThrows(IllegalArgumentException.class, () -> new DensePolynomial("4.5x^2.4 + 8.3x"));

        //throws IllegalArgumentException when trying to create an instance of DensePolynomial with negative exponents
        assertThrows(IllegalArgumentException.class, () -> new DensePolynomial("4x^2 + 8x + 2x^-4"));

    }

    @Test
    void testEquals() {
        DensePolynomial densePolynomial1 = new DensePolynomial("4x^2 + 8x + -2");
        DensePolynomial densePolynomial2 = new DensePolynomial("4x^2 + 8x - 2");
        //test that two densePolynomials with two differing string reps ('+ -' vs '-') are still equal
        assertTrue(densePolynomial1.equals(densePolynomial2));

        DensePolynomial densePolynomialAdd1 = new DensePolynomial("x^2 + 2x - 5");
        DensePolynomial densePolynomialAdd2 = new DensePolynomial("3x^2 + 6x + 3");
        Polynomial sum = densePolynomialAdd1.add(densePolynomialAdd2);
        //test that dense polynomial will equal the sum of two polynomials
        assertTrue(densePolynomial1.equals(sum));

        SparsePolynomial sparsePolynomial1 = new SparsePolynomial("4x^2 + 8x + -2");
        //test that illegal argument exception if we are not given instance of dense polynomial
        assertThrows(IllegalArgumentException.class, () -> densePolynomial1.equals(sparsePolynomial1));


    }

}