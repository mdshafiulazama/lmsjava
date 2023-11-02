import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Book {
    private String category;
    private String bookName;
    private String bookId;
    private String rackNumber;
    private int quantity;

    public Book(String category, String bookName, String bookId, String rackNumber, int quantity) {
        this.category = category;
        this.bookName = bookName;
        this.bookId = bookId;
        this.rackNumber = rackNumber;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
        return "Category: " + category + "\nBook Name: " + bookName + "\nBook ID: " + bookId + "\nRack Number: " + rackNumber + "\nQuantity: " + quantity;
    }
}

public class LibraryManagementSystem {
    private static final String booksFile = "BooksDetails.txt";
    private static final String issuedFile = "IssuedBooks.txt";
    private static final String usersFile = "Users.txt";
    private static final String dateFormat = "yyyy-MM-dd";
    private static List<Book> books = new ArrayList<>();
    private static List<User> users = new ArrayList<>();

    static class User {
        private String name;
        private String contact;
        private String username;
        private String password;

        public User(String name, String contact, String username, String password) {
            this.name = name;
            this.contact = contact;
            this.username = username;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getContact() {
            return contact;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public static void main(String[] args) {
        loadBooksData();
        loadUsersData();

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\t\t\t\t--------------------------------------");
        System.out.print("\t\t\t\t+ \tWelcome to our Library\t     +\n");
        System.out.println("\t\t\t\t--------------------------------------");
        System.out.print("\n\t\t\t\t\tLogin (Admin or User)\n");
        System.out.print("\n\t\t\t\t\t   Username: ");
        String username = scanner.nextLine();
        System.out.print("\n\t\t\t\t\t   Password: ");
        String enteredPassword = new String(System.console().readPassword());

        if (username.equals("admin") && enteredPassword.equals("admin")) {
            System.out.println("\n\t\t\t\t\tAdmin Login successful.\n");
            adminMenu();
        } else {
            User currentUser = validateUser(username, enteredPassword);
            if (currentUser != null) {
                System.out.println("\n\t\t\t\tUser Login successful. Welcome, " + currentUser.getName() + "\n");
                mainMenu(currentUser);
            } else {
                System.out.println("Invalid credentials!!!  Please try again!!!");
            }
        }
    }

    private static User validateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private static void loadUsersData() {
        try (Scanner scanner = new Scanner(new File(usersFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userData = line.split(",");
                
                // Check if the line contains the expected number of values (4 in this case)
                if (userData.length == 4) {
                    User user = new User(userData[0], userData[1], userData[2], userData[3]);
                    users.add(user);
                } else {
                    //System.out.println("Skipping invalid user data: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            users = new ArrayList<>();
        }
    }
    

    private static void saveUsersData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(usersFile))) {
            for (User user : users) {
                writer.println(user.getName() + "," + user.getContact() + "," + user.getUsername() + "," + user.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--------------------------------------");
            System.out.print("+\t      Admin Menu\t     +\n");
            System.out.println("--------------------------------------\n");
            System.out.println("1. Add Books");
            System.out.println("2. Delete Books");
            System.out.println("3. Search Books");
            System.out.println("4. Issue Books");
            System.out.println("5. View Books List");
            System.out.println("6. Edit Books Records");
            System.out.println("7. Add New User");
            System.out.println("8. Edit Issued");
            System.out.println("9. Back to Main Menu");
    
            System.out.print("Enter your choice: ");
            String adminChoice = scanner.nextLine();
    
            switch (adminChoice) {
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
                    issueBook();
                    break;
                case "5":
                    viewBookList();
                    break;
                case "6":
                    editBooksRecords();
                    break;
                case "7":
                    addNewUser();
                    break;
                case "8":
                    editIssuedMenu();
                    break;
                case "9":
                    System.out.println("Closing the application...");
                    saveBooksData();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void addNewUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add New User");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact: ");
        String contact = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = new String(System.console().readPassword());

        User newUser = new User(name, contact, username, password);
        users.add(newUser);

        System.out.println("New user added successfully.");
        saveUsersData();
    }

    private static void loadBooksData() {
        try (Scanner scanner = new Scanner(new File(booksFile))) {
            while (scanner.hasNextLine()) {
                String[] bookData = scanner.nextLine().split(",");
                Book book = new Book(bookData[0], bookData[1], bookData[2], bookData[3], Integer.parseInt(bookData[4]));
                books.add(book);
            }
        } catch (FileNotFoundException e) {
            books = new ArrayList<>();
        }
    }

    private static void saveBooksData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(booksFile))) {
            for (Book book : books) {
                writer.println(book.getCategory() + "," + book.getBookName() + "," + book.getBookId() + "," + book.getRackNumber() + "," + book.getQuantity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveIssuedBookData(String studentId, String bookId, String issueDate, String returnDate) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(issuedFile, true))) {
            writer.println(studentId + "," + bookId + "," + issueDate + "," + returnDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static void mainMenu(User currentUser) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nDate and Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss\n\n\n").format(new Date()));
            System.out.println("\n----------------------------------------------------------");
            System.out.println("         Welcome to Our Library, " + currentUser.getName() + "       ");
            System.out.println("----------------------------------------------------------\n\n\n");
            
            System.out.println("\n--------------------------------------");
            System.out.print("+\t      User Menu\t             +\n");
            System.out.println("--------------------------------------\n");
            
            System.out.println("1. Search Books");
            System.out.println("2. Issue Books");
            System.out.println("3. View Books List");          
            System.out.println("4. Close Application");

            System.out.print("\nEnter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                
                case "1":
                    searchBooks();
                    break;
                case "2":
                    issueBook();
                    break;
                case "3":
                    viewBookList();
                    break;
                
                case "4":
                    System.out.println("Closing the application...");
                    saveBooksData();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addBooks() {
        // ... (same as before)
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--------------------------------------");
        System.out.print("+\t    Add Books Menu\t     +\n");
        System.out.println("--------------------------------------\n");
        
        System.out.println("\t\t\t\t\tSelect category:");
        String[] categoryNames = {"Computer", "Electronics", "English", "Civil", "Mechanical"};
        for (int i = 0; i < categoryNames.length; i++) {
            System.out.println((i + 1) + ". " + categoryNames[i]);
        }
        System.out.println((categoryNames.length + 1) + ".\t\t\t\t\t Back to main menu");
        System.out.print("Enter your choice: ");
        String categoryChoice = scanner.nextLine();
        int categoryIndex = Integer.parseInt(categoryChoice);
        if (categoryIndex < 1 || categoryIndex > categoryNames.length + 1) {
            System.out.println("Invalid choice !!! Please try again!!!");
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
        System.out.print("Enter Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        Book newBook = new Book(selectedCategory, bookName, bookId, rackNumber, quantity);
        books.add(newBook);

        System.out.println("Book added successfully.");
        saveBooksData();
    }

    private static void deleteBooks() {
        // ... (same as before)
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
    
        System.out.println("Search Books:");
        System.out.println("1. Search by Category");
        System.out.println("2. Search by Book ID");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
    
        switch (choice) {
            case 1:
                displayCategoryMenu(); // Show category menu
                int categoryChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                searchBooksByCategory(categoryChoice);
                break;
            case 2:
                System.out.print("Enter Book ID to search: ");
                String bookIdSearchTerm = scanner.nextLine().toLowerCase();
                searchBooksByBookId(bookIdSearchTerm);
                break;
            default:
                System.out.println("Invalid choice. Please select 1 or 2.");
        }
    }
    
    private static void displayCategoryMenu() {
        System.out.println("Select category:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        System.out.print("Enter the category number to search: ");
    }
    
    private static void searchBooksByCategory(int categoryChoice) {
        if (categoryChoice < 1 || categoryChoice > categories.size()) {
            System.out.println("Invalid category number. Please select a valid category number.");
            return;
        }
        String categorySearchTerm = categories.get(categoryChoice - 1).toLowerCase();
        List<Book> foundBooks = books.stream()
                .filter(book -> book.getCategory().toLowerCase().contains(categorySearchTerm))
                .collect(Collectors.toList());
    
        displayBooks(foundBooks);
    }
    
    
    private static void displayBooks(List<Book> foundBooks) {
        if (!foundBooks.isEmpty()) {
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println("Category          Book Name             Book ID              Rack No.              Quantity");
            System.out.println("-------------------------------------------------------------------------------------------");
            for (Book book : foundBooks) {
                System.out.println(String.format("%-18s %-20s %-24s %-12s %-18s", book.getCategory(), book.getBookName(), book.getBookId(), book.getRackNumber(), book.getQuantity()));
            }
        } else {
            System.out.println("No books found with the given search term.");
        }
    }
    

    private static void issueBook() {
        // ... (existing code)
    
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Book ID to issue: ");
        String bookId = scanner.nextLine();
    
        // Get the current date as the issue date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String issueDate = dateFormat.format(new Date());
    
        System.out.print("Enter Return Date (yyyy-MM-dd): ");
        String returnDate = scanner.nextLine();
    
        Book bookToIssue = null;
    
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                bookToIssue = book;
                break;
            }
        }
    
        if (bookToIssue != null && bookToIssue.getQuantity() > 0) {
            saveIssuedBookData(studentId, bookId, issueDate, returnDate);
            bookToIssue.setQuantity(bookToIssue.getQuantity() - 1);
            System.out.println("Book issued successfully.");
            saveBooksData();
        } else {
            System.out.println("Book with ID '" + bookId + "' is not available for issuing.");
        }
    }
    
    private static void viewBookList() {
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("Category          Book Name             Book ID              Rack No.              Quantity");
        System.out.println("-------------------------------------------------------------------------------------------");
        for (Book book : books) {
            System.out.println(String.format("%-18s %-20s %-24s %-12s %-18s", book.getCategory(), book.getBookName(), book.getBookId(), book.getRackNumber(), book.getQuantity()));
        }
    }
    private static void editIssuedMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--------------------------------------");
            System.out.print("+\t   Edit Issued Menu\t     +\n");
            System.out.println("--------------------------------------\n");
            System.out.println("1. Show All Issued Books");
            System.out.println("2. Delete Issued Book");
            System.out.println("3. Back to Admin Menu");
    
            System.out.print("Enter your choice: ");
            String editIssuedChoice = scanner.nextLine();
    
            switch (editIssuedChoice) {
                case "1":
                    showAllIssuedBooks();
                    break;
                case "2":
                    deleteIssuedBook();
                    break;
                case "3":
                    return; // Back to the Admin Menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void showAllIssuedBooks() {
        try (Scanner scanner = new Scanner(new File(issuedFile))) {
            System.out.println("Issued Books:");
            System.out.println("Student ID\tBook ID\tIssued Date\tReturn Date");
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] issuedData = line.split(",");
                if (issuedData.length == 4) { // Expecting 4 elements: Student ID, Book ID, Issued Date, Return Date
                    System.out.println(issuedData[0] + "\t" + issuedData[1] + "\t" + issuedData[2] + "\t" + issuedData[3]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No issued books found.");
        }
    }
    
    
    private static void deleteIssuedBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Student ID of the issued book to delete: ");
        String studentId = scanner.nextLine();
    
        System.out.print("Enter Book ID of the issued book to delete: ");
        String bookId = scanner.nextLine();
    
        List<String> updatedIssuedBooks = new ArrayList<>();
    
        try (Scanner fileScanner = new Scanner(new File(issuedFile))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] issuedData = line.split(",");
                if (issuedData.length == 3 && !issuedData[0].equals(studentId) && !issuedData[1].equals(bookId)) {
                    updatedIssuedBooks.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No issued books found.");
            return;
        }
    
        try (PrintWriter writer = new PrintWriter(new FileWriter(issuedFile))) {
            for (String line : updatedIssuedBooks) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                book.setQuantity(book.getQuantity() + 1);
                break;
            }
        }
    
        System.out.println("Issued book with Student ID '" + studentId + "' and Book ID '" + bookId + "' has been deleted.");
        saveBooksData();
    }
    

    private static void editBooksRecords() {
        // ... (same as before)
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--------------------------------------");
            System.out.print("+\tEdit Books Records Menu\t      +\n");
            System.out.println("--------------------------------------\n");
        //System.out.println("Edit Books Records Menu");
        System.out.print("Enter Book ID to edit: ");
        String bookId = scanner.nextLine();

        Optional<Book> bookToEdit = books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst();

        if (bookToEdit.isPresent()) {
            Book selectedBook = bookToEdit.get();
            System.out.println("Selected Book Details.");
            System.out.println(selectedBook);

            // Provide options to edit book attributes
            System.out.println("\n--------------------------------------");
            System.out.print("+\t  Choose what to edit\t     +\n");
            System.out.println("--------------------------------------\n");
            //System.out.println("Choose what to edit:");
            System.out.println("1. Edit Category");
            System.out.println("2. Edit Book Name");
            System.out.println("3. Edit Book ID");
            System.out.println("4. Edit Rack Number");
            System.out.println("5. Edit Quantity");
            System.out.println("6. Back to main menu");

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
