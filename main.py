import getpass
import datetime
import os

# Sample user and password
user_id = "saikot"
password = "saikot"

# Files for storing book details and issued books
books_file = "BooksDetails.txt"
issued_file = "IssuedBooks.txt"

# Initialize book database
books = []

# Function to load book data from file
def load_books_data():
    if os.path.isfile(books_file):
        with open(books_file, "r") as f:
            for line in f:
                book_data = line.strip().split(",")
                category, book_name, book_id, rack_number = book_data
                books.append({
                    "Category": category,
                    "Book Name": book_name,
                    "Book ID": book_id,
                    "Rack Number": rack_number
                })

# Function to save book data to file
def save_books_data():
    with open(books_file, "w") as f:
        for book in books:
            f.write(f"{book['Category']},{book['Book Name']},{book['Book ID']},{book['Rack Number']}\n")

# Create the file if it doesn't exist
if not os.path.isfile(books_file):
    with open(books_file, "w"):
        pass

# Function to load issued book data from file
def load_issued_books_data():
    issued_books = []
    if os.path.isfile(issued_file):
        with open(issued_file, "r") as f:
            for line in f:
                issued_book_data = line.strip().split(",")
                student_id, book_id, return_date = issued_book_data
                issued_books.append({
                    "Student ID": student_id,
                    "Book ID": book_id,
                    "Return Date": return_date
                })
    return issued_books

# Function to save issued book data to file
def save_issued_books_data(issued_books):
    with open(issued_file, "a") as f:
        for issued_book in issued_books:
            f.write(f"{issued_book['Student ID']},{issued_book['Book ID']},{issued_book['Return Date']}\n")

# Create the file if it doesn't exist
if not os.path.isfile(issued_file):
    with open(issued_file, "w"):
        pass

# Load book data and issued book data when the program starts
load_books_data()
issued_books = load_issued_books_data()

def main_menu():
    while True:
        print("Main Menu")
        print("1. Add Books")
        print("2. Delete Books")
        print("3. Search Books")
        print("4. Issue Books")
        print("5. View Books List")
        print("6. Edit Books Records")
        print("7. Close Application")
        print("Date and Time:", datetime.datetime.now())

        choice = input("Enter your choice: ")

        if choice == "1":
            add_books()
        elif choice == "2":
            delete_books()
        elif choice == "3":
            search_books()
        elif choice == "4":
            issue_books()
        elif choice == "5":
            view_books_list()
        elif choice == "6":
            edit_books_records()
        elif choice == "7":
            print("Closing the application...")
            save_books_data()  # Save book data before exiting
            break
        else:
            print("Invalid choice. Please try again.")

def add_books():
    print("Add Books Menu")
    print("Select category:")
    print("1. Computer")
    print("2. Electronics")
    print("3. Electrical")
    print("4. Civil")
    print("5. Mechanical")
    print("6. Back to main menu")

    category_choice = input("Enter your choice: ")

    if category_choice == "6":
        return  # Return to the main menu

    if category_choice not in ["1", "2", "3", "4", "5"]:
        print("Invalid category choice. Please try again.")
        return

    category_names = ["Computer", "Electronics", "Electrical", "Civil", "Mechanical"]
    category = category_names[int(category_choice) - 1]

    book_name = input("Enter book name: ")
    book_id = input("Enter book ID: ")
    rack_number = input("Enter rack number: ")

    books.append({
        "Category": category,
        "Book Name": book_name,
        "Book ID": book_id,
        "Rack Number": rack_number
    })

    print("Book added successfully!")

def delete_books():
    print("Delete Books Menu")
    print("Select a book to delete:")

    for i, book in enumerate(books):
        print(f"{i + 1}. {book['Book Name']} ({book['Category']})")

    delete_choice = input("Enter the number of the book to delete, or '0' to go back: ")
    delete_choice = int(delete_choice)

    if delete_choice == 0:
        return  # Return to the main menu

    if delete_choice < 1 or delete_choice > len(books):
        print("Invalid choice. Please try again.")
        return

    deleted_book = books.pop(delete_choice - 1)
    print(f"Deleted book: {deleted_book['Book Name']} ({deleted_book['Category']})")

def search_books():
    print("Search Books Menu")
    print("1. Search by Book ID")
    print("2. Search by Book Name")
    print("3. Back to main menu")

    search_choice = input("Enter your choice: ")

    if search_choice == "3":
        return  # Return to the main menu

    if search_choice not in ["1", "2"]:
        print("Invalid choice. Please try again.")
        return

    search_term = input("Enter the search term: ").strip().lower()

    if search_choice == "1":
        found_books = [book for book in books if search_term in book["Book ID"].lower()]
    elif search_choice == "2":
        found_books = [book for book in books if search_term in book["Book Name"].lower()]

    if found_books:
        print("Search results:")
        for book in found_books:
            print(f"Category: {book['Category']}")
            print(f"Book Name: {book['Book Name']}")
            print(f"Book ID: {book['Book ID']}")
            print(f"Rack Number: {book['Rack Number']}")
    else:
        print("No books found with the specified criteria.")

def issue_books():
    print("Issue Books Menu")
    student_id = input("Enter Student ID: ")
    book_id = input("Enter Book ID: ")
    return_date = input("Enter the return date (yyyy-mm-dd): ")

    book_to_issue = None
    for book in books:
        if book["Book ID"] == book_id:
            book_to_issue = book
            break

    if book_to_issue:
        print(f"Book '{book_to_issue['Book Name']}' has been issued to Student ID {student_id}.")
        print(f"Return Date: {return_date}")
        save_issued_books_data([{
            "Student ID": student_id,
            "Book ID": book_id,
            "Return Date": return_date
        }])
    else:
        print(f"Book with ID '{book_id}' not found in the database.")

def view_books_list():
    print("View Books List Menu")
    print("{:<30} {:<15} {:<15}".format("Book Name", "Book ID", "Rack No"))
    print("-" * 60)

    for book in books:
        print("{:<30} {:<15} {:<15}".format(book["Book Name"], book["Book ID"], book["Rack Number"]))

def edit_books_records():
    print("Edit Books Records Menu")
    print("1. Edit Book Data")
    print("2. Delete Student Records")
    print("3. Back to main menu")

    edit_choice = input("Enter your choice: ")

    if edit_choice == "3":
        return  # Return to the main menu

    if edit_choice not in ["1", "2"]:
        print("Invalid choice. Please try again.")
        return

    if edit_choice == "1":
        book_id = input("Enter the Book ID to edit: ")
        for book in books:
            if book["Book ID"] == book_id:
                print(f"Editing book: {book['Book Name']} ({book['Category']})")
                new_book_name = input("Enter new book name: ")
                new_rack_number = input("Enter new rack number: ")
                book["Book Name"] = new_book_name
                book["Rack Number"] = new_rack_number
                print("Book data updated successfully.")
                break
        else:
            print(f"Book with ID '{book_id}' not found in the database.")
    elif edit_choice == "2":
        # Implement the logic to delete student records here (if needed)
        pass

if __name__ == "__main__":
    while True:
        username = input("Enter User ID: ")
        entered_password = getpass.getpass("Enter Password: ")

        if username == user_id and entered_password == password:
            print("Login successful.")
            main_menu()
        else:
            print("Invalid credentials. Please try again.")
