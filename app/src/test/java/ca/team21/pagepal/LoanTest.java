package ca.team21.pagepal;

import org.junit.Test;

import ca.team21.pagepal.models.Loan;

import static org.junit.Assert.assertEquals;

public class LoanTest {

    private Loan loan = new Loan();

    @Test
    public void testSetBook() {
        String isbn = "12345";
        loan.setBook(isbn);

        assertEquals(isbn, loan.getBook());
    }

    @Test
    public void testSetOwner() {
        String owner = "Mark Messier";
        loan.setOwner(owner);

        assertEquals(owner, loan.getOwner());
    }

    @Test
    public void testSetBorrower() {
        String borrower = "Don Cherry";
        loan.setBorrower(borrower);

        assertEquals(borrower, loan.getBorrower());
    }
}
