import java.util.*;
import java.io.*;

public class LibraryManagementSystem {
    private static final int RETURNTIME = 15;
    private static final String[] categories = { "Computer", "Electronics", "Electrical", "Civil", "Mechanical",
            "Architecture" };
    private static Scanner scanner = new Scanner(System.in);
    private static List<Book> books = new ArrayList<>();
    private static List<IssueRecord> issueRecords = new ArrayList<>();
    private static int currentBookId = 1;
    private static int currentIssueId = 1;
    private static final String BOOKS_RECORD_FILE = "booksrecoder.txt"; // File to store book records

    private static class Date {
        int day, month, year;

        Date(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }
    }

    private static class Book {
        int id;
        String stname;
        String name;
        String Author;
        int quantity;
        float Price;
        int count;
        int rackno;
        String cat;
        Date issued;
        Date duedate;

        Book(int id, String stname, String name, String Author, int quantity, float Price, int count, int rackno,
                String cat, Date issued, Date duedate) {
            this.id = id;
            this.stname = stname;
            this.name = name;
            this.Author = Author;
            this.quantity = quantity;
            this.Price = Price;
            this.count = count;
            this.rackno = rackno;
            this.cat = cat;
            this.issued = issued;
            this.duedate = duedate;
        }
    }

    private static class IssueRecord {
        int id;
        String studentName;
        int bookId;
        Date issuedDate;
        Date dueDate;

        IssueRecord(int id, String studentName, int bookId, Date issuedDate, Date dueDate) {
            this.id = id;
            this.studentName = studentName;
            this.bookId = bookId;
            this.issuedDate = issuedDate;
            this.dueDate = dueDate;
        }
    }

    public static void main(String[] args) {
        loadBookRecords(); // Load book records from the file
        password();
    }

    private static void password() {
        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();
        String password = "saikot"; // Set your password here

        if (inputPassword.equals(password)) {
            mainMenu();
        } else {
            System.out.println("Incorrect password. Exiting.");
        }
    }

    private static void mainMenu() {
        int choice;
        do {
            System.out.println("\n+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
            System.out.println("\tMAIN MENU");
            System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
            System.out.println("1. Add Books");
            System.out.println("2. Delete Books");
            System.out.println("3. Search Books");
            System.out.println("4. Issue Books");
            System.out.println("5. View Book List");
            System.out.println("6. Edit Book's Record");
            System.out.println("7. Close Application");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addBooks();
                    break;
                case 2:
                    deleteBooks();
                    break;
                case 3:
                    searchBooks();
                    break;
                case 4:
                    issueBooks();
                    break;
                case 5:
                    viewBooks();
                    break;
                case 6:
                    editBooks();
                    break;
                case 7:
                    closeApplication();
                    break;
                default:
                    System.out.println("\nWrong Entry!! Please re-enter the correct option.");
            }
        } while (choice != 7);
    }

    private static void addBooks() {
        System.out.println("\nAdding a new book:");

        System.out.print("Enter book name: ");
        String name = scanner.next();

        System.out.print("Enter author name: ");
        String author = scanner.next();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter price: ");
        float price = scanner.nextFloat();

        System.out.print("Enter rack number: ");
        int rackNo = scanner.nextInt();

        System.out.println("Select category (1-6):");
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int categoryIndex = scanner.nextInt() - 1;
        String category = categories[categoryIndex];

        Date issuedDate = getCurrentDate();
        Date dueDate = calculateDueDate(issuedDate);

        Book book = new Book(currentBookId, "", name, author, quantity, price, 0, rackNo, category, issuedDate,
                dueDate);
        currentBookId++;
        books.add(book);

        // Save the updated book records to the file
        saveBookRecords();

        System.out.println("Book added successfully!");
    }

    private static void deleteBooks() {
        System.out.println("\nDeleting a book:");
        System.out.print("Enter the book ID to delete: ");
        int bookIdToDelete = scanner.nextInt();

        boolean bookFound = false;

        for (Book book : books) {
            if (book.id == bookIdToDelete) {
                books.remove(book);
                bookFound = true;
                System.out.println("Book with ID " + bookIdToDelete + " deleted successfully.");
                break;
            }
        }

        if (!bookFound) {
            System.out.println("Book with ID " + bookIdToDelete + " not found in the library.");
        }

        // Save the updated book records to the file
        saveBookRecords();
    }

    private static void searchBooks() {
        System.out.println("\nSearch Books:");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Author");
        System.out.print("Enter your choice (1/2): ");
        int choice = scanner.nextInt();

        scanner.nextLine(); // Consume newline character

        if (choice == 1) {
            System.out.print("Enter book name to search: ");
            String searchTerm = scanner.nextLine();

            boolean found = false;
            System.out.println("\nSearch Results by Name:");
            for (Book book : books) {
                if (book.name.contains(searchTerm)) {
                    displayBookDetails(book);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No books found with the given name.");
            }
        } else if (choice == 2) {
            System.out.print("Enter author name to search: ");
            String searchTerm = scanner.nextLine();

            boolean found = false;
            System.out.println("\nSearch Results by Author:");
            for (Book book : books) {
                if (book.Author.contains(searchTerm)) {
                    displayBookDetails(book);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No books found by the given author.");
            }
        } else {
            System.out.println("Invalid choice. Please choose 1 or 2.");
        }
    }

    private static void displayBookDetails(Book book) {
        System.out.println("Book ID: " + book.id);
        System.out.println("Name: " + book.name);
        System.out.println("Author: " + book.Author);
        System.out.println("Quantity: " + book.quantity);
        System.out.println("Price: " + book.Price);
        System.out.println("Rack No: " + book.rackno);
        System.out.println("Category: " + book.cat);
        System.out.println();
    }

    private static void issueBooks() {
        System.out.println("\nIssue Books:");
        System.out.println("1. Issue a Book");
        System.out.println("2. View Issued Books");
        System.out.print("Enter your choice (1/2): ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            issueBook();
        } else if (choice == 2) {
            viewIssuedBooks();
        } else {
            System.out.println("Invalid choice. Please choose 1 or 2.");
        }
    }

    private static void issueBook() {
        System.out.println("\nIssue a Book:");
        System.out.print("Enter the Book ID to issue: ");
        int bookId = scanner.nextInt();

        // Check if the book exists in the library
        Book selectedBook = findBookById(bookId);

        if (selectedBook != null) {
            if (selectedBook.quantity > 0) {
                System.out.print("Enter student name: ");
                String studentName = scanner.next();

                // Get the current date
                Date issuedDate = getCurrentDate();

                // Calculate the due date
                Date dueDate = calculateDueDate(issuedDate);

                // Create an issue record
                IssueRecord issueRecord = new IssueRecord(currentIssueId, studentName, bookId, issuedDate, dueDate);
                currentIssueId++;

                // Decrease the book quantity
                selectedBook.quantity--;

                // Add the issue record to the list
                issueRecords.add(issueRecord);

                System.out.println("Book issued successfully.");
            } else {
                System.out.println("Sorry, this book is currently out of stock.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " not found in the library.");
        }
    }

    private static void viewIssuedBooks() {
        System.out.println("\nIssued Books:");
        if (issueRecords.isEmpty()) {
            System.out.println("No books have been issued yet.");
        } else {
            System.out.println("ID\tStudent Name\tBook Name\tIssued Date\tDue Date");
            for (IssueRecord record : issueRecords) {
                Book issuedBook = findBookById(record.bookId);
                if (issuedBook != null) {
                    System.out.println(record.id + "\t" + record.studentName + "\t" + issuedBook.name + "\t" +
                            formatDate(record.issuedDate) + "\t" + formatDate(record.dueDate));
                }
            }
        }
    }

    private static String formatDate(Date date) {
        return date.day + "-" + date.month + "-" + date.year;
    }

    // Helper function to find a book by its ID
    private static Book findBookById(int bookId) {
        for (Book book : books) {
            if (book.id == bookId) {
                return book;
            }
        }
        return null;
    }

    // Helper function to get the current date
    private static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return new Date(day, month, year);
    }

    // Helper function to calculate the due date
    private static Date calculateDueDate(Date issuedDate) {
        int day = issuedDate.day + RETURNTIME;
        int month = issuedDate.month;
        int year = issuedDate.year;

        if (day > 30) {
            month += day / 30;
            day %= 30;
        }
        if (month > 12) {
            year += month / 12;
            month %= 12;
        }

        return new Date(day, month, year);
    }

    private static void viewBooks() {
        System.out.println("\n+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        System.out.println("\tBOOK LIST");
        System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");

        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            System.out.println("ID\tName\tAuthor\tQuantity\tPrice\tRack No\tCategory");
            for (Book book : books) {
                System.out.println(book.id + "\t" + book.name + "\t" + book.Author + "\t" + book.quantity + "\t" +
                        book.Price + "\t" + book.rackno + "\t" + book.cat);
            }
        }
    }

    private static void editBooks() {
        System.out.println("\nEdit Books:");
        System.out.print("Enter the Book ID to edit: ");
        int bookIdToEdit = scanner.nextInt();

        // Check if the book exists in the library
        Book bookToEdit = findBookById(bookIdToEdit);

        if (bookToEdit != null) {
            displayBookDetails(bookToEdit);

            System.out.println("\nSelect the field to edit:");
            System.out.println("1. Name");
            System.out.println("2. Author");
            System.out.println("3. Quantity");
            System.out.println("4. Price");
            System.out.println("5. Rack Number");
            System.out.println("6. Cancel (Don't edit)");

            int editChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (editChoice) {
                case 1:
                    System.out.print("Enter the new name: ");
                    String newName = scanner.nextLine();
                    bookToEdit.name = newName;
                    break;
                case 2:
                    System.out.print("Enter the new author: ");
                    String newAuthor = scanner.nextLine();
                    bookToEdit.Author = newAuthor;
                    break;
                case 3:
                    System.out.print("Enter the new quantity: ");
                    int newQuantity = scanner.nextInt();
                    bookToEdit.quantity = newQuantity;
                    break;
                case 4:
                    System.out.print("Enter the new price: ");
                    float newPrice = scanner.nextFloat();
                    bookToEdit.Price = newPrice;
                    break;
                case 5:
                    System.out.print("Enter the new rack number: ");
                    int newRackNo = scanner.nextInt();
                    bookToEdit.rackno = newRackNo;
                    break;
                case 6:
                    System.out.println("Edit canceled.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            System.out.println("Book details updated successfully.");
            displayBookDetails(bookToEdit);

            // Save the updated book records to the file
            saveBookRecords();
        } else {
            System.out.println("Book with ID " + bookIdToEdit + " not found in the library.");
        }
    }

    private static void closeApplication() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }

    private static void loadBookRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_RECORD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 11) {
                    int id = Integer.parseInt(parts[0]);
                    String stname = parts[1];
                    String name = parts[2];
                    String author = parts[3];
                    int quantity = Integer.parseInt(parts[4]);
                    float price = Float.parseFloat(parts[5]);
                    int count = Integer.parseInt(parts[6]);
                    int rackno = Integer.parseInt(parts[7]);
                    String cat = parts[8];
                    int day = Integer.parseInt(parts[9]);
                    int month = Integer.parseInt(parts[10]);
                    int year = Integer.parseInt(parts[11]);
                    Date issuedDate = new Date(day, month, year);
                    Date dueDate = calculateDueDate(issuedDate);

                    Book book = new Book(id, stname, name, author, quantity, price, count, rackno, cat, issuedDate,
                            dueDate);
                    if (id >= currentBookId) {
                        currentBookId = id + 1;
                    }
                    books.add(book);
                }
            }
            System.out.println("Book records loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading book records: " + e.getMessage());
        }
    }

    private static void saveBookRecords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_RECORD_FILE))) {
            for (Book book : books) {
                String line = book.id + "," + book.stname + "," + book.name + "," + book.Author + "," + book.quantity
                        + "," +
                        book.Price + "," + book.count + "," + book.rackno + "," + book.cat + "," +
                        book.issued.day + "," + book.issued.month + "," + book.issued.year;
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Book records saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving book records: " + e.getMessage());
        }
    }
}
