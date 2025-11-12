import java.util.Objects;

public class Book implements Comparable<Book> {
    private int bookId;
    private String title;
    private String author;
    private String category;
    private boolean isIssued;

    public Book(int bookId, String title, String author, String category, boolean isIssued) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isIssued = isIssued;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isIssued() { return isIssued; }

    public void markAsIssued() { isIssued = true; }
    public void markAsReturned() { isIssued = false; }

    public void displayBookDetails() {
        System.out.printf("ID: %d | Title: %s | Author: %s | Category: %s | Issued: %s%n",
                bookId, title, author, category, isIssued ? "Yes" : "No");
    }

    // For sorting default by title
    @Override
    public int compareTo(Book o) {
        return this.title.compareToIgnoreCase(o.title);
    }

    // CSV representation (bookId,title,author,category,isIssued)
    public String toCSV() {
        // escape commas by replacing with &#44; (simple approach)
        return bookId + "," + escape(title) + "," + escape(author) + "," + escape(category) + "," + isIssued;
    }

    public static String escape(String s) {
        return s.replace(",", "&#44;");
    }

    public static String unescape(String s) {
        return s.replace("&#44;", ",");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}
