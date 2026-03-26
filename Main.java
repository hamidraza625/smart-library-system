package LMS;

import java.io.*;
import java.util.*;
import java.sql.*;

public class Main 
{
    public static void clearScreen()
    {
        for (int i = 0; i < 20; i++)
            System.out.println();
    }

    public static int takeInput(int min, int max)
    {    
        String choice;
        Scanner input = new Scanner(System.in);        
        
        while(true)
        {
            System.out.println("\nEnter choice: ");

            choice = input.next();

            if((!choice.matches(".*[a-zA-Z]+.*")) && (Integer.parseInt(choice) > min && Integer.parseInt(choice) < max))
            {
                return Integer.parseInt(choice);
            }
            else
                System.out.println("\nInvalid input. Try again.");
        }
    }

    public static void allFunctionalities(Person person, int choice) throws IOException
    {
        Library lib = Library.getInstance();
        Scanner scanner = new Scanner(System.in);
        int input = 0;

        if (choice == 1)
        {
            lib.searchForBooks();
        }

        else if (choice == 4)
        {
            double totalFine;

            if(person instanceof Borrower)
                totalFine = lib.computeFine2((Borrower)person);
            else
            {
                Borrower bor = lib.findBorrower();
                if(bor == null) return;
                totalFine = lib.computeFine2(bor);
            }

            System.out.println("\nTotal Fine: Rs " + totalFine);
        }

        System.out.println("\nPress any key to continue...");
        scanner.next();
    }

    public static void main(String[] args)
    {
        Scanner admin = new Scanner(System.in);
        Library lib = Library.getInstance();

        lib.setFine(20);
        lib.setRequestExpiry(7);
        lib.setReturnDeadline(5);
        lib.setName("Smart Library");

        Connection con = lib.makeConnection();

        if (con == null)
        {
            System.out.println("\nError connecting to database.");
            return;
        }

        try {
            lib.populateLibrary(con);

            boolean stop = false;

            while(!stop)
            {   
                clearScreen();

                System.out.println("--------------------------------------------------------");
                System.out.println("Welcome to Smart Library");
                System.out.println("--------------------------------------------------------");

                System.out.println("1- Login");
                System.out.println("2- Exit");
                System.out.println("3- Admin Panel");

                System.out.println("-----------------------------------------");

                int choice = takeInput(0,4);

                if (choice == 3)
                {
                    System.out.println("\nEnter Password: ");
                    String aPass = admin.next();

                    if(aPass.equals("lib"))
                    {
                        while (true)
                        {
                            clearScreen();

                            System.out.println("Admin Panel");
                            System.out.println("1- Add Clerk");
                            System.out.println("2- Add Librarian"); 
                            System.out.println("3- View Issued Books History");  
                            System.out.println("4- View All Books"); 
                            System.out.println("5- Logout");

                            choice = takeInput(0,6);

                            if (choice == 5)
                                break;

                            if (choice == 1)
                                lib.createPerson('c');
                            else if (choice == 2)
                                lib.createPerson('l');
                            else if (choice == 3)
                                lib.viewHistory();
                            else if (choice == 4)
                                lib.viewAllBooks();

                            admin.next();
                        }
                    }
                    else
                        System.out.println("\nWrong Password.");
                }

                else if (choice == 1)
                {
                    Person person = lib.login();

                    if (person == null)
                    {
                        System.out.println("\nLogin failed.");
                    }
                    else
                    {
                        System.out.println("\nLogin successful.");
                    }
                }

                else
                    stop = true;

                new Scanner(System.in).next();
            }

            lib.fillItBack(con);
        }
        catch(Exception e)
        {
            System.out.println("\nSystem error.");
        }
    }
}