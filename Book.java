package LMS;

import java.io.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Book {

    private int bookID;
    private String title;
    private String subject;
    private String author;
    private boolean isIssued;

    private HoldRequestOperations holdRequestsOperations = new HoldRequestOperations();

    static int currentIdNumber = 0;

    public Book(int id, String t, String s, String a, boolean issued)
    {
        currentIdNumber++;

        if(id == -1)
            bookID = currentIdNumber;
        else
            bookID = id;

        title = t;
        subject = s;
        author = a;
        isIssued = issued;
    }

    public void printHoldRequests()
    {
        if (!holdRequestsOperations.holdRequests.isEmpty())
        {
            System.out.println("\nHold Requests:");

            for (int i = 0; i < holdRequestsOperations.holdRequests.size(); i++)
            {
                System.out.print(i + "- ");
                holdRequestsOperations.holdRequests.get(i).print();
            }
        }
        else
            System.out.println("\nNo hold requests.");
    }

    public void printInfo()
    {
        System.out.println(title + "\t\t" + author + "\t\t" + subject);
    }

    public void changeBookInfo() throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nChange Author? (y/n)");
        if(scanner.next().equals("y"))
        {
            System.out.println("Enter new Author:");
            author = reader.readLine();
        }

        System.out.println("Change Subject? (y/n)");
        if(scanner.next().equals("y"))
        {
            System.out.println("Enter new Subject:");
            subject = reader.readLine();
        }

        System.out.println("Change Title? (y/n)");
        if(scanner.next().equals("y"))
        {
            System.out.println("Enter new Title:");
            title = reader.readLine();
        }

        System.out.println("\nBook updated.");
    }

    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public String getAuthor() { return author; }
    public boolean getIssuedStatus() { return isIssued; }

    public void setIssuedStatus(boolean s)
    {
        isIssued = s;
    }

    public int getID()
    {
        return bookID;
    }

    public ArrayList<HoldRequest> getHoldRequests()
    {
        return holdRequestsOperations.holdRequests;
    }

    public static void setIDCount(int n)
    {
        currentIdNumber = n;
    }

    public void placeBookOnHold(Borrower bor)
    {
        HoldRequest hr = new HoldRequest(bor, this, new Date());

        holdRequestsOperations.addHoldRequest(hr);
        bor.addHoldRequest(hr);

        System.out.println("\nBook placed on hold.");
    }

    public void makeHoldRequest(Borrower borrower)
    {
        for (Loan l : borrower.getBorrowedBooks())
        {
            if(l.getBook() == this)
            {
                System.out.println("\nYou already borrowed this book.");
                return;
            }
        }

        for (HoldRequest hr : holdRequestsOperations.holdRequests)
        {
            if (hr.getBorrower() == borrower)
            {
                System.out.println("\nAlready requested.");
                return;
            }
        }

        placeBookOnHold(borrower);
    }

    public void serviceHoldRequest(HoldRequest hr)
    {
        holdRequestsOperations.removeHoldRequest();
        hr.getBorrower().removeHoldRequest(hr);
    }

    public void issueBook(Borrower borrower, Staff staff)
    {
        Date today = new Date();

        ArrayList<HoldRequest> hRequests = holdRequestsOperations.holdRequests;

        for (HoldRequest hr : hRequests)
        {
            long days = ChronoUnit.DAYS.between(today.toInstant(), hr.getRequestDate().toInstant());
            days = -days;

            if(days > Library.getInstance().getHoldRequestExpiry())
            {
                holdRequestsOperations.removeHoldRequest();
                hr.getBorrower().removeHoldRequest(hr);
            }
        }

        if (isIssued)
        {
            System.out.println("\nBook already issued.");
            return;
        }

        setIssuedStatus(true);

        Loan loan = new Loan(borrower, this, staff, null, new Date(), null, false);

        Library.getInstance().addLoan(loan);
        borrower.addBorrowedBook(loan);

        System.out.println("\nBook issued successfully.");
    }

    public void returnBook(Borrower borrower, Loan l, Staff staff)
    {
        setIssuedStatus(false);
        l.setReturnedDate(new Date());
        l.setReceiver(staff);

        borrower.removeBorrowedBook(l);
        l.payFine();

        System.out.println("\nBook returned successfully.");
    }
}