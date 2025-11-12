### 📘 City Library Digital Management System

A Java-based console application for managing books and members of a city library.
It demonstrates **file handling**, **collections**, **exception handling**, and **object-oriented programming (OOP)** principles.

---

### 🧠 Features

* Add, issue, return, and search books
* Register and manage library members
* Auto-generate unique IDs for books and members
* Save and load data using text files (`books.txt`, `members.txt`)
* Sort books by title, author, or category
* Exception handling using `try-catch-finally`, `throw`, and `throws`
* Uses Java Collections (`Map`, `List`, `Set`) for data management

---

### 🧩 Project Structure

```
C:\Users\praty\.vscode\Java\University assignments\City Library Digital Management System
│
├── Book.java              # Book class with Comparable interface
├── Member.java            # Member class with issued book list
├── LibraryManager.java    # Main menu & logic controller
├── books.txt              # Stores book data
├── members.txt            # Stores member data
└── Screenshots/           # Output screenshots (optional)
```

---

### ⚙️ How to Compile and Run

#### Using Command Prompt or VS Code Terminal

```bash
cd "C:\Users\praty\.vscode\Java\University assignments\City Library Digital Management System"
javac *.java
java LibraryManager
```

The program will automatically create `books.txt` and `members.txt` files in the same folder if they don't exist.

---

### 🧾 Sample Interaction

```
===== City Library Digital Management System =====
1. Add Book
2. Add Member
3. Issue Book
4. Return Book
5. Search Books
6. Sort Books
7. Display All Books
8. Display All Members
9. Exit
Enter your choice: 1
Enter Book Title: Atomic Habits
Enter Author: James Clear
Enter Category: Self Help
Book added with ID: 101
```

```
Enter your choice: 2
Enter Member Name: Alice
Enter Email: alice@example.com
Member added with ID: 201
```

```
Enter your choice: 3
Enter Book ID to issue: 101
Enter Member ID: 201
Book ID 101 issued to Member ID 201
```

---

### 📂 Data Files

* **books.txt**

  ```
  101,Atomic Habits,James Clear,Self Help,false
  ```
* **members.txt**

  ```
  201,Alice,alice@example.com,101
  ```

---

### 🧰 Java Concepts Used

* Exception Handling (`try`, `catch`, `finally`, `throw`, `throws`)
* File Handling with `BufferedReader`, `BufferedWriter`, and `Files`
* Collections Framework (`HashMap`, `ArrayList`, `HashSet`)
* Comparable & Comparator Interfaces
* Encapsulation and Modularity

---


---

### 👨‍💻 Developed By

**Name:** Pratyush Jha
**Roll:** 2401201017
**University:** K.R. Mangalam University
**Course:** BCA in AI & Data Science (IBM Collaboration)
**Session:** 2025–26
**GitHub:** [JUSSTTMEE07](https://github.com/JUSSTTMEE07)

---

