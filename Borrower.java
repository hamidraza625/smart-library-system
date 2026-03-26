package LMS;

import java.io.*;
import java.util.*;

public class Borrower extends Person 
{    
    private ArrayList<Loan> borrowedBooks;
    private ArrayList<HoldRequest> onHoldBooks;

    public Borrower(int id, String name, String address, int phoneNum)
    {
        super(id, name, address, phoneNum);
        
        borrowedBooks = new ArrayList<>();
        onHoldBooks = new ArrayList<>();
    }

    @Override
    public void printInfo()
    {
        super.printInfo();
        printBorrowedBooks();
        printOnHoldBooks();
    }
   
    public void printBorrowedBooks()
    {
        if (!borrowedBooks.isEmpty())
        { 
            System.out.println("\nBorrowed Books:");

            for (int i = 0; i < borrowedBooks.size(); i++)
            {                      
                System.out.print(i + "- ");
                borrowedBooks.get(i).getBook().printInfo();
            }
        }
        else
            System.out.println("\nNo borrowed books.");                
    }
    
    public void printOnHoldBooks()
    {
        if (!onHoldBooks.isEmpty())
        { 
            System.out.println("\nOn Hold Books:");

            for (int i = 0; i < onHoldBooks.size(); i++)
            {                      
                System.out.print(i + "- ");
                onHoldBooks.get(i).getBook().printInfo();
            }
        }
        else
            System.out.println("\nNo on hold books.");                
    }
   
    public void updateBorrowerInfo() throws IOException
    {
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nUpdate Name? (y/n)");
        if(sc.next().equals("y"))
        {
            System.out.println("Enter new name:");
            setName(reader.readLine());
        }

        System.out.println("Update Address? (y/n)");
        if(sc.next().equals("y"))
        {
            System.out.println("Enter new address:");
            setAddress(reader.readLine());
        }

        System.out.println("Update Phone Number? (y/n)");
        if(sc.next().equals("y"))
        {
            System.out.println("Enter new phone number:");
            setPhone(sc.nextInt());
        }

        System.out.println("\nDetails updated.");
    }

    public void addBorrowedBook(Loan book)
    {
        borrowedBooks.add(book);
    }
    
    public void removeBorrowedBook(Loan book)
    {
        borrowedBooks.remove(book);
    }    
    
    public void addHoldRequest(HoldRequest hr)
    {
        onHoldBooks.add(hr);
    }
    
    public void removeHoldRequest(HoldRequest hr)
    {
        onHoldBooks.remove(hr);
    }
    
    public ArrayList<Loan> getBorrowedBooks()
    {
        return borrowedBooks;
    }
    
    public ArrayList<HoldRequest> getOnHoldBooks()
    {
        return onHoldBooks;
    }
}