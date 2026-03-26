package LMS;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Library {
    
    private String name;
    public static Librarian librarian;
    public static ArrayList<Person> persons;
    private ArrayList<Book> booksInLibrary;
    private ArrayList<Loan> loans;

    public int book_return_deadline;
    public double per_day_fine;
    public int hold_request_expiry;

    private HoldRequestOperations holdRequestsOperations = new HoldRequestOperations();

    private static Library instance;

    public static Library getInstance()
    {
        if(instance == null)
        {
            instance = new Library();
        }
        return instance;
    }

    private Library()
    {
        name = null;
        librarian = null;
        persons = new ArrayList<>();
        booksInLibrary = new ArrayList<>();
        loans = new ArrayList<>();
    }

    public void setReturnDeadline(int deadline)
    {
        book_return_deadline = deadline;
    }

    public void setFine(double perDayFine)
    {
        per_day_fine = perDayFine;
    }

    public void setRequestExpiry(int expiry)
    {
        hold_request_expiry = expiry;
    }

    public void setName(String n)
    {
        name = n;
    }

    public String getLibraryName()
    {
        return name;
    }

    public ArrayList<Person> getPersons()
    {
        return persons;
    }

    public Librarian getLibrarian()
    {
        return librarian;
    }

    public ArrayList<Book> getBooks()
    {
        return booksInLibrary;
    }

    public int getHoldRequestExpiry()
    {
        return hold_request_expiry;
    }

    public void addClerk(Clerk c)
    {
        persons.add(c);
    }

    public void addBorrower(Borrower b)
    {
        persons.add(b);
    }

    public void addLoan(Loan l)
    {
        loans.add(l);
    }

    public Borrower findBorrower()
    {
        System.out.println("\nEnter Borrower ID: ");
        Scanner scanner = new Scanner(System.in);

        try {
            int id = scanner.nextInt();

            for (Person p : persons)
            {
                if (p.getID() == id && p instanceof Borrower)
                    return (Borrower) p;
            }
        } catch (Exception e) {
            System.out.println("\nInvalid input");
        }

        System.out.println("\nBorrower not found.");
        return null;
    }

    public Clerk findClerk()
    {
        System.out.println("\nEnter Clerk ID: ");
        Scanner scanner = new Scanner(System.in);

        try {
            int id = scanner.nextInt();

            for (Person p : persons)
            {
                if (p.getID() == id && p instanceof Clerk)
                    return (Clerk) p;
            }
        } catch (Exception e) {
            System.out.println("\nInvalid input");
        }

        System.out.println("\nClerk not found.");
        return null;
    }

    public void addBookinLibrary(Book b)
    {
        booksInLibrary.add(b);
    }

    public void removeBookfromLibrary(Book b)
    {
        boolean canDelete = true;

        for (Person p : persons)
        {
            if (p instanceof Borrower)
            {
                ArrayList<Loan> borBooks = ((Borrower)p).getBorrowedBooks();

                for (Loan l : borBooks)
                {
                    if (l.getBook() == b)
                    {
                        canDelete = false;
                        System.out.println("Book is currently issued.");
                    }
                }
            }
        }

        if (canDelete)
        {
            booksInLibrary.remove(b);
            System.out.println("Book removed successfully.");
        }
        else
        {
            System.out.println("Delete unsuccessful.");
        }
    }

    public ArrayList<Book> searchForBooks() throws IOException
    {
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nSearch by: 1-Title  2-Subject  3-Author");
        String choice = sc.next();

        String value = "";
        System.out.println("Enter value: ");
        value = reader.readLine();

        ArrayList<Book> result = new ArrayList<>();

        for (Book b : booksInLibrary)
        {
            if ((choice.equals("1") && b.getTitle().equals(value)) ||
                (choice.equals("2") && b.getSubject().equals(value)) ||
                (choice.equals("3") && b.getAuthor().equals(value)))
            {
                result.add(b);
            }
        }

        if (result.isEmpty())
        {
            System.out.println("\nNo books found.");
            return null;
        }

        for (int i = 0; i < result.size(); i++)
        {
            System.out.print(i + "- ");
            result.get(i).printInfo();
        }

        return result;
    }

    public void viewAllBooks()
    {
        if (booksInLibrary.isEmpty())
        {
            System.out.println("\nNo books available.");
            return;
        }

        for (int i = 0; i < booksInLibrary.size(); i++)
        {
            System.out.print(i + "- ");
            booksInLibrary.get(i).printInfo();
        }
    }

    public double computeFine2(Borrower borrower)
    {
        double totalFine = 0;

        for (Loan l : loans)
        {
            if (l.getBorrower() == borrower)
            {
                totalFine += l.computeFine1();
            }
        }

        return totalFine;
    }

    public void createBook(String title, String subject, String author)
    {
        Book b = new Book(-1, title, subject, author, false);
        addBookinLibrary(b);
        System.out.println("\nBook added.");
    }

    public Person login()
    {
        Scanner input = new Scanner(System.in);

        try {
            System.out.println("\nEnter ID:");
            int id = input.nextInt();

            System.out.println("Enter Password:");
            String password = input.next();

            for (Person p : persons)
            {
                if (p.getID() == id && p.getPassword().equals(password))
                    return p;
            }

            if (librarian != null && librarian.getID() == id && librarian.getPassword().equals(password))
                return librarian;

        } catch (Exception e) {
            System.out.println("Invalid input");
        }

        System.out.println("Login failed.");
        return null;
    }

    public Connection makeConnection()
    {
        try
        {
            String url = "jdbc:derby://localhost:1527/LMS";
            return DriverManager.getConnection(url, "haris", "123");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
}