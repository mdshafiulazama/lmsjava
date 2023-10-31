import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Book {
    private String category;
    private String bookName;
    private String bookId;
    private String rackNumber;

    public Book(String category, String bookName, String bookId, String rackNumber) {
        this.category = category;
        this.bookName = bookName;
        this.bookId = bookId;
        this.rackNumber = rackNumber;
    }

    public String getCategory() {
        return category;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookId() {
        return bookId;
    }

    public String getRackNumber() {
        return rackNumber;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setRackNumber(String rackNumber) {
        this.rackNumber = rackNumber;
    }

    @Override
    public String toString() {
        return "Category: " + category + "\nBook Name: " + bookName + "\nBook ID: " + bookId + "\nRack Number: " + rackNumber;
    }
}

public class LibraryManagementSystem {
    private static final String booksFile = "BooksDetails.txt";
    private static final String issuedFile = "IssuedBooks.txt";
    private static final String dateFormat = "yyyy-MM-dd";
    private static List<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        loadBooksData();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter User ID: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String enteredPassword = new String(System.console().readPassword());

        if (username.equals("saikot") && enteredPassword.equals("saikot")) {
            System.out.println("Login successful.\n");
            mainMenu();
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private static void loadBooksData() {
        try (Scanner scanner = new Scanner(new File(booksFile))) {
            while (scanner.hasNextLine()) {
                String[] bookData = scanner.nextLine().split(",");
                Book book = new Book(bookData[0], bookData[1], bookData[2], bookData[3]);
                books.add(book);
            }
        } catch (FileNotFoundException e) {
            books = new ArrayList<>();
        }
    }

    private static void saveBooksData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(booksFile))) {
            for (Book book : books) {
                writer.println(book.getCategory() + "," + book.getBookName() + "," + book.getBookId() + "," + book.getRackNumber());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveIssuedBookData(String studentId, String bookId, String returnDate) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(issuedFile, true))) {
            writer.println(studentId + "," + bookId + "," + returnDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nDate and Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss\n\n\n").format(new Date()));
            System.out.println("\t\t\tWelcome To Our Library\n\n\n");
            System.out.println("\t\t\tMain Menu\n");
            System.out.println("\t\t\t1. Add Books");
            System.out.println("\t\t\t2. Delete Books");
            System.out.println("\t\t\t3. Search Books");
            System.out.println("\t\t\t4. Issue Books");
            System.out.println("\t\t\t5. View Books List");
            System.out.println("\t\t\t6. Edit Books Records");
            System.out.println("\t\t\t7. Close Application");
            

            System.out.print("\nEnter your choice: ");
            String choice = scanner.nextLine();
            
            

            switch (choice) {
                case "1":
                    addBooks();
                    break;
                case "2":
                    deleteBooks();
                    break;
                case "3":
                    searchBooks();
                    break;
                case "4":
                    issueBooks();
                    break;
                case "5":
                    viewBooksList();
                    break;
                case "6":
                    editBooksRecords();
                    break;
                case "7":
                    System.out.println("Closing the application...");
                    saveBooksData();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addBooks() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add Books Menu");
        System.out.println("Select category:");
        String[] categoryNames = {"Computer", "Electronics", "Electrical", "Civil", "Mechanical"};
        for (int i = 0; i < categoryNames.length; i++) {
            System.out.println((i + 1) + ". " + categoryNames[i]);
        }
        System.out.println((categoryNames.length + 1) + ". Back to main menu");
        System.out.print("Enter your choice: ");
        String categoryChoice = scanner.nextLine();
        int categoryIndex = Integer.parseInt(categoryChoice);
        if (categoryIndex < 1 || categoryIndex > categoryNames.length + 1) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }
        if (categoryIndex == categoryNames.length + 1) {
            return;
        }

        String selectedCategory = categoryNames[categoryIndex - 1];
        System.out.print("Enter Book Name: ");
        String bookName = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter Rack Number: ");
        String rackNumber = scanner.nextLine();

        Book newBook = new Book(selectedCategory, bookName, bookId, rackNumber);
        books.add(newBook);

        System.out.println("Book added successfully.");
        saveBooksData();
    }

    private static void deleteBooks() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Book ID to delete: ");
        String bookId = scanner.nextLine();

        List<Book> booksToDelete = books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .collect(Collectors.toList());

        if (!booksToDelete.isEmpty()) {
            books.removeAll(booksToDelete);
            System.out.println("Book(s) with ID '" + bookId + "' deleted successfully.");
            saveBooksData();
        } else {
            System.out.println("Book with ID '" + bookId + "' not found in the database.");
        }
    }

    private static void searchBooks() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Book ID or Book Name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<Book> foundBooks = books.stream()
                .filter(book -> book.getBookId().toLowerCase().contains(searchTerm) || book.getBookName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        if (!foundBooks.isEmpty()) {
            System.out.println("Search Results:");
            for (Book book : foundBooks) {
                System.out.println(book);
            }
        } else {
            System.out.println("No books found with the given search term.");
        }
    }

    private static void issueBooks() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Book ID to issue: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter Date to Return (yyyy-MM-dd): ");
        String returnDate = scanner.nextLine();

        saveIssuedBookData(studentId, bookId, returnDate);
        System.out.println("Book issued successfully.");
    }

    private static void viewBooksList() {
        System.out.println("Book Name             Book ID                Rack No.");
        System.out.println("---------------------------------------------------------");
        for (Book book : books) {
            System.out.println(String.format("%-20s %-24s %-8s", book.getBookName(), book.getBookId(), book.getRackNumber()));
        }
    }

    private static void editBooksRecords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Edit Books Records Menu");
        System.out.print("Enter Book ID to edit: ");
        String bookId = scanner.nextLine();

        Optional<Book> bookToEdit = books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst();

        if (bookToEdit.isPresent()) {
            Book selectedBook = bookToEdit.get();
            System.out.println("Selected Book Details:");
            System.out.println(selectedBook);

            // Provide options to edit book attributes
            System.out.println("Choose what to edit:");
            System.out.println("1. Edit Category");
            System.out.println("2. Edit Book Name");
            System.out.println("3. Edit Book ID");
            System.out.println("4. Edit Rack Number");
            System.out.println("5. Back to main menu");

            System.out.print("Enter your choice: ");
            String editChoice = scanner.nextLine();

            switch (editChoice) {
                case "1":
                    System.out.print("Enter new category: ");
                    String newCategory = scanner.nextLine();
                    selectedBook.setCategory(newCategory);
                    break;
                case "2":
                    System.out.print("Enter new book name: ");
                    String newBookName = scanner.nextLine();
                    selectedBook.setBookName(newBookName);
                    break;
                case "3":
                    System.out.print("Enter new book ID: ");
                    String newBookId = scanner.nextLine();
                    selectedBook.setBookId(newBookId);
                    break;
                case "4":
                    System.out.print("Enter new rack number: ");
                    String newRackNumber = scanner.nextLine();
                    selectedBook.setRackNumber(newRackNumber);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("Book details updated successfully.");
            saveBooksData();
        } else {
            System.out.println("Book with ID '" + bookId + "' not found in the database.");
        }
    }
}
