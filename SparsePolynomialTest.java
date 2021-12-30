import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SparsePolynomialTest {

    @Test
    void testDegree() {
        SparsePolynomial testZero = new SparsePolynomial("0");
        // zero polynomial should return degree 0
        assertEquals(0,testZero.degree());

        SparsePolynomial testNormal = new SparsePolynomial("5x^7 + 4x^3 + -2x + 8");
        //testing regular polynomial
        assertEquals(7,testNormal.degree());

        //testing polynomial with large degree
        SparsePolynomial testLarge = new SparsePolynomial("8x^1234567 + 3x^12345 + 25x^123 + 8x^12");
        assertEquals(1234567, testLarge.degree());
    }

    @Test
    void testGetCoefficient() {
        SparsePolynomial testPolynomial = new SparsePolynomial("12x^9876543 + -8x^42 + 25x + 2 + 4x^-3");

        //testing degree 0
        assertEquals(2,testPolynomial.getCoefficient(0));

        //testing degree 1
        assertEquals(25,testPolynomial.getCoefficient(1));

        //testing degree which returns negative exponent
        assertEquals(-8,testPolynomial.getCoefficient(42));

        //testing large degree
        assertEquals(12, testPolynomial.getCoefficient(9876543));

        //testing negative degree
        assertEquals(4,testPolynomial.getCoefficient(-3));
    }

    @Test
    void testIsZero() {
        SparsePolynomial zeroPolynomial = new SparsePolynomial("0");
        //zero polynomial should return true
        assertTrue(zeroPolynomial.isZero());

        SparsePolynomial notZeroPolynomial = new SparsePolynomial("3x^4 + -2x^2 + 8 + 3x^-4");
        //nonzero polynomial should return false
        assertFalse(notZeroPolynomial.isZero());

    }

    @Test
    void testAdd() {
        SparsePolynomial sparsePolynomial1 = new SparsePolynomial("12x^4 + 8x^3 + 5x^2 + 2x + 7 + 4x^-3");
        SparsePolynomial sparsePolynomial2 = new SparsePolynomial("3x^4 + 4x^3 + 6x + 2x^-3");
        SparsePolynomial zeroPolynomial = new SparsePolynomial("0");
        SparsePolynomial nullPolynomial = null;
        DensePolynomial densePolynomial = new DensePolynomial("5x^4 + -x + 8");

        //testing sparse polynomial added to another sparse polynomial
        assertEquals(new SparsePolynomial("15x^4 + 12x^3 + 5x^2 + 8x + 7 + 6x^-3"), sparsePolynomial1.add(sparsePolynomial2));

        //testing sparse polynomial added to a dense polynomial
        assertEquals(new SparsePolynomial("17x^4 + 8x^3 + 5x^2 + x + 15 + 4x^-3"), sparsePolynomial1.add(densePolynomial));

        //testing that adding sparse polynomial to a zero polynomial will return itself e.g. x + 0 = x
        assertEquals(sparsePolynomial1, sparsePolynomial1.add(zeroPolynomial));

        //testing that adding a sparse polynomial to a null polynomial will throw null pointer exception
        assertThrows(NullPointerException.class, () -> sparsePolynomial1.add(nullPolynomial));
    }

    @Test
    void testMultiply() {
        SparsePolynomial sparsePolynomial1 = new SparsePolynomial("4x^2 + 8x + 2");
        SparsePolynomial sparsePolynomial2 = new SparsePolynomial("12x^2 + 3x^-2");
        SparsePolynomial zeroPolynomial = new SparsePolynomial("0");
        SparsePolynomial nullPolynomial = null;
        DensePolynomial densePolynomial = new DensePolynomial("2x + 3");

        //testing sparse polynomial multiplied by another sparse polynomial
        assertEquals(new SparsePolynomial("48x^4 + 96x^3 + 24x^2 + 12 + 24x^-1 + 6x^-2"), sparsePolynomial1.multiply(sparsePolynomial2));

        //testing sparse polynomial multiplied by a dense polynomial
        assertEquals(new SparsePolynomial("8x^3 + 28x^2 + 28x + 6"), sparsePolynomial1.multiply(densePolynomial));

        //testing that multiplying sparse polynomial by a zero polynomial will return zero polynomial e.g. x * 0 = 0
        assertEquals(zeroPolynomial, sparsePolynomial1.multiply(zeroPolynomial));

        //testing that multiplying a sparse polynomial to a null polynomial will throw null pointer exception
        assertThrows(NullPointerException.class, () -> sparsePolynomial1.multiply(nullPolynomial));
    }

    @Test
    void testSubtract() {
        SparsePolynomial sparsePolynomial1 = new SparsePolynomial("12x^4 + 8x^3 + 5x^2 + 2x + 7 + 4x^-3");
        SparsePolynomial sparsePolynomial2 = new SparsePolynomial("3x^4 + 4x^3 + 6x + 2x^-3");
        SparsePolynomial zeroPolynomial = new SparsePolynomial("0");
        SparsePolynomial nullPolynomial = null;
        DensePolynomial densePolynomial = new DensePolynomial("5x^4 + -x + 8");

        //testing sparse polynomial subtracted by another sparse polynomial
        assertEquals(new SparsePolynomial("9x^4 + 4x^3 + 5x^2 + -4x + 7 + 2x^-3"), sparsePolynomial1.subtract(sparsePolynomial2));

        //testing sparse polynomial subtracted by a dense polynomial
        assertEquals(new SparsePolynomial("7x^4 + 8x^3 + 5x^2 + 3x + -1 + 4x^-3"), sparsePolynomial1.subtract(densePolynomial));

        //testing that subtracting sparse polynomial by a zero polynomial will return itself e.g. x - 0 = x
        assertEquals(sparsePolynomial1, sparsePolynomial1.subtract(zeroPolynomial));

        //testing that subtracting a sparse polynomial by a null polynomial will throw null pointer exception
        assertThrows(NullPointerException.class, () -> sparsePolynomial1.subtract(nullPolynomial));
    }

    @Test
    void testMinus() {
        SparsePolynomial sparsePolynomial1 = new SparsePolynomial("4x^2 + 8x + 2 + 4x^-5");
        SparsePolynomial sparsePolynomial2 = new SparsePolynomial("34");
        SparsePolynomial zeroPolynomial = new SparsePolynomial("0");

        //test regular sparse polynomial
        assertEquals(new SparsePolynomial("-4x^2 + -8x + -2 + -4x^-5"), sparsePolynomial1.minus());

        //test a single constant polynomial
        assertEquals(new SparsePolynomial("-34"), sparsePolynomial2.minus());

        //test zero polynomial
        assertEquals(zeroPolynomial, zeroPolynomial.minus());
    }

    /* SparsePolynomial constructor uses wellFormed() to make sure invariants are true (exponents and
     * coefficients are integers) as well as making sure the string is in canonical form. I will be testing
     * this method by testing if instances can be created*/
    @Test
    void testWellFormed() {
        SparsePolynomial sparsePolynomial1 = new SparsePolynomial("4x^2 + 8x + 2 + 3x^-4");
        //test that sparsePolynomial1 is well formed -> should return true as invariant is true and its in canonical form
        assertTrue(sparsePolynomial1.wellFormed());

        SparsePolynomial sparsePolynomial2 = new SparsePolynomial("8");
        //test that sparsePolynomial2 (a single constant) is well formed -> should return true as invariant is true and its in canonical form
        assertTrue(sparsePolynomial2.wellFormed());

        /*throws IllegalArgumentException because when creating instance, the well formed method returned false
         * because we have non integer coefficients and degrees indicating that the invariant is false*/
        assertThrows(IllegalArgumentException.class, () -> new SparsePolynomial("4.5x^2.4 + 8.3x"));
    }


    @Test
    void testEquals() {
        SparsePolynomial sparsePolynomial1 = new SparsePolynomial("4x^2 + 8x + -2 + -8x^-3");
        SparsePolynomial sparsePolynomial2 = new SparsePolynomial("4x^2 + 8x - 2 - 8x^-3");
        //test that two sparsePolynomials with two differing string reps ('+ -' vs '-') are still equal
        assertTrue(sparsePolynomial1.equals(sparsePolynomial2));

        SparsePolynomial sparsePolynomialAdd1 = new SparsePolynomial("x^2 + 2x - 5 + -9x^-3");
        SparsePolynomial sparsePolynomialAdd2 = new SparsePolynomial("3x^2 + 6x + 3 + x^-3");
        Polynomial sum = sparsePolynomialAdd1.add(sparsePolynomialAdd2);
        //test that sparse polynomial will equal the sum of two sparse polynomials
        assertTrue(sparsePolynomial1.equals(sum));

        DensePolynomial densePolynomial1 = new DensePolynomial("4x^2 + 8x + -2");
        //test that illegal argument exception if we are not given instance of sparse polynomial
        assertThrows(IllegalArgumentException.class, () -> sparsePolynomial1.equals(densePolynomial1));
    }

}