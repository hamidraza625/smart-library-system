package LMS;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;

public class Loan 
{
    private Borrower borrower;      
    private Book book;
    
    private Staff issuer;
    private Date issuedDate;
    
    private Date dateReturned;
    private Staff receiver;
    
    private boolean finePaid;
       
    public Loan(Borrower bor, Book b, Staff i, Staff r, Date iDate, Date rDate, boolean fPaid)
    {
        borrower = bor;
        book = b;
        issuer = i;
        receiver = r;
        issuedDate = iDate;
        dateReturned = rDate;
        finePaid = fPaid;
    }
    
    public Book getBook()
    {
        return book;
    }
    
    public Staff getIssuer()
    {
        return issuer;
    }
    
    public Staff getReceiver()
    {
        return receiver;
    }
    
    public Date getIssuedDate()
    {
        return issuedDate;
    } 

    public Date getReturnDate()
    {
        return dateReturned;
    }
    
    public Borrower getBorrower()
    {
        return borrower;
    }
    
    public boolean getFineStatus()
    {
        return finePaid;
    }
    
    public void setReturnedDate(Date dReturned)
    {
        dateReturned = dReturned;
    }
    
    public void setFineStatus(boolean fStatus)
    {
        finePaid = fStatus;
    }    
    
    public void setReceiver(Staff r)
    {
        receiver = r;
    }

    // Calculate fine
    public double computeFine1()
    {
        double totalFine = 0;
        
        if (!finePaid)
        {    
            Date currentDate = new Date();                

            long days = ChronoUnit.DAYS.between(issuedDate.toInstant(), currentDate.toInstant());

            days = days - Library.getInstance().book_return_deadline;

            if(days > 0)
                totalFine = days * Library.getInstance().per_day_fine;
        }

        return totalFine;
    }
    
    
    public void payFine()
    {
        double totalFine = computeFine1();
                
        if (totalFine > 0)
        {
            System.out.println("\nTotal Fine: Rs " + totalFine);
            System.out.println("Pay now? (y/n)");
            
            Scanner input = new Scanner(System.in); 
            String choice = input.next();
            
            if(choice.equalsIgnoreCase("y"))
                finePaid = true;
            else
                finePaid = false;
        }
        else
        {
            System.out.println("\nNo fine.");
            finePaid = true;
        }        
    }

    public void renewIssuedBook(Date newDate)
    {        
        issuedDate = newDate;
        
        System.out.println("\nBook renewed successfully.");
    }
}