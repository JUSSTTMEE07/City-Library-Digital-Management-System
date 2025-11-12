import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LibraryManager {
    private static final String BOOKS_FILE = "books.txt";
    private static final String MEMBERS_FILE = "members.txt";

    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();
    private Set<String> categories = new HashSet<>();
    private Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        LibraryManager lm = new LibraryManager();
        lm.loadFromFile();
        lm.mainMenu();
        lm.saveToFile();
        System.out.println("Exiting. Data saved.");
    }

    // ---------- Menu ----------
    private void mainMenu() {
        while (true) {
            System.out.println("\n===== City Library Digital Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. Sort Books");
            System.out.println("7. Display All Books");
            System.out.println("8. Display All Members");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addBook(); break;
                    case "2": addMember(); break;
                    case "3": issueBook(); break;
                    case "4": returnBook(); break;
                    case "5": searchBooks(); break;
                    case "6": sortBooksMenu(); break;
                    case "7": displayAllBooks(); break;
                    case "8": displayAllMembers(); break;
                    case "9": return;
                    default: System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Core operations ----------
    private void addBook() {
        System.out.print("Enter Book Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = sc.nextLine().trim();
        System.out.print("Enter Category: ");
        String category = sc.nextLine().trim();

        int nextId = generateNextBookId();
        Book b = new Book(nextId, title, author, category, false);
        books.put(nextId, b);
        categories.add(category);
        saveToFile(); // persist immediately
        System.out.println("Book added with ID: " + nextId);
    }

    private void addMember() {
        System.out.print("Enter Member Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim();
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format. Aborting.");
            return;
        }
        int nextId = generateNextMemberId();
        Member m = new Member(nextId, name, email, new ArrayList<>());
        members.put(nextId, m);
        saveToFile();
        System.out.println("Member added with ID: " + nextId);
    }

    private void issueBook() {
        System.out.print("Enter Book ID to issue: ");
        int bid = parseIntInput(sc.nextLine());
        System.out.print("Enter Member ID: ");
        int mid = parseIntInput(sc.nextLine());

        Book book = books.get(bid);
        Member member = members.get(mid);

        if (book == null) { System.out.println("Book not found."); return; }
        if (member == null) { System.out.println("Member not found."); return; }

        if (book.isIssued()) {
            System.out.println("Book is already issued.");
            return;
        }

        book.markAsIssued();
        member.addIssuedBook(bid);
        saveToFile();
        System.out.println("Book ID " + bid + " issued to Member ID " + mid);
    }

    private void returnBook() {
        System.out.print("Enter Book ID to return: ");
        int bid = parseIntInput(sc.nextLine());
        System.out.print("Enter Member ID: ");
        int mid = parseIntInput(sc.nextLine());

        Book book = books.get(bid);
        Member member = members.get(mid);

        if (book == null) { System.out.println("Book not found."); return; }
        if (member == null) { System.out.println("Member not found."); return; }

        if (!book.isIssued()) {
            System.out.println("Book is not marked as issued.");
            return;
        }

        book.markAsReturned();
        member.returnIssuedBook(bid);
        saveToFile();
        System.out.println("Book ID " + bid + " returned by Member ID " + mid);
    }

    private void searchBooks() {
        System.out.println("Search by: 1-Title 2-Author 3-Category");
        String c = sc.nextLine().trim();
        System.out.print("Enter search text: ");
        String q = sc.nextLine().trim().toLowerCase();

        List<Book> found = books.values().stream().filter(book -> {
            switch (c) {
                case "1": return book.getTitle().toLowerCase().contains(q);
                case "2": return book.getAuthor().toLowerCase().contains(q);
                case "3": return book.getCategory().toLowerCase().contains(q);
                default: return false;
            }
        }).collect(Collectors.toList());

        if (found.isEmpty()) {
            System.out.println("No books found.");
        } else {
            found.forEach(Book::displayBookDetails);
        }
    }

    private void sortBooksMenu() {
        System.out.println("Sort by: 1-Title (default) 2-Author 3-Category");
        String c = sc.nextLine().trim();
        List<Book> list = new ArrayList<>(books.values());
        switch (c) {
            case "2":
                list.sort(Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER));
                break;
            case "3":
                list.sort(Comparator.comparing(Book::getCategory, String.CASE_INSENSITIVE_ORDER));
                break;
            default:
                Collections.sort(list); // Comparable (title)
        }
        list.forEach(Book::displayBookDetails);
    }

    private void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in library.");
            return;
        }
        books.values().forEach(Book::displayBookDetails);
    }

    private void displayAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members.");
            return;
        }
        members.values().forEach(Member::displayMemberDetails);
    }

    // ---------- Persistence ----------
    public void saveToFile() {
        saveBooks();
        saveMembers();
    }

    private void saveBooks() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(BOOKS_FILE))) {
            for (Book b : books.values()) {
                bw.write(b.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save books: " + e.getMessage());
        }
    }

    private void saveMembers() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(MEMBERS_FILE))) {
            for (Member m : members.values()) {
                bw.write(m.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save members: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        loadBooks();
        loadMembers();
    }

    private void loadBooks() {
        try {
            Path p = Paths.get(BOOKS_FILE);
            if (!Files.exists(p)) Files.createFile(p);

            try (BufferedReader br = Files.newBufferedReader(p)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = splitCSV(line, 5);
                    int id = Integer.parseInt(parts[0]);
                    String title = Book.unescape(parts[1]);
                    String author = Book.unescape(parts[2]);
                    String category = Book.unescape(parts[3]);
                    boolean issued = Boolean.parseBoolean(parts[4]);
                    Book b = new Book(id, title, author, category, issued);
                    books.put(id, b);
                    categories.add(category);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load books: " + e.getMessage());
        }
    }

    private void loadMembers() {
        try {
            Path p = Paths.get(MEMBERS_FILE);
            if (!Files.exists(p)) Files.createFile(p);

            try (BufferedReader br = Files.newBufferedReader(p)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = splitCSV(line, 4);
                    int id = Integer.parseInt(parts[0]);
                    String name = Member.unescape(parts[1]);
                    String email = Member.unescape(parts[2]);
                    List<Integer> issued = new ArrayList<>();
                    if (parts.length >= 4 && !parts[3].trim().isEmpty()) {
                        String[] issuedParts = parts[3].split(";");
                        for (String s : issuedParts) {
                            if (!s.trim().isEmpty()) issued.add(Integer.parseInt(s));
                        }
                    }
                    Member m = new Member(id, name, email, issued);
                    members.put(id, m);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load members: " + e.getMessage());
        }
    }

    // small utility: split into up to expected parts (to handle escaped commas)
    private static String[] splitCSV(String line, int expectedParts) {
        // naive split respecting our escape token &#44;
        List<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); ) {
            if (line.startsWith("&#44;", i)) {
                sb.append(',');
                i += 5;
            } else if (line.charAt(i) == ',' && parts.size() < expectedParts - 1) {
                parts.add(sb.toString());
                sb.setLength(0);
                i++;
            } else {
                sb.append(line.charAt(i++));
            }
        }
        parts.add(sb.toString());
        return parts.toArray(new String[0]);
    }

    // ---------- Utilities ----------
    private int generateNextBookId() {
        return books.keySet().stream().max(Integer::compareTo).map(i -> i + 1).orElse(101);
    }

    private int generateNextMemberId() {
        return members.keySet().stream().max(Integer::compareTo).map(i -> i + 1).orElse(201);
    }

    private boolean isValidEmail(String email) {
        // simple regex - not perfect, good enough for assignment
        String regex = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regex, email);
    }

    private int parseIntInput(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
