package ca.team21.pagepal;

import org.junit.Test;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.Loan;

import static org.junit.Assert.assertEquals;

public class LoanTest {

    private Loan loan = new Loan();

    @Test
    public void testSetBook() {
        Book book = new Book();
        loan.setBook(book);

        assertEquals(book, loan.getBook());
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
