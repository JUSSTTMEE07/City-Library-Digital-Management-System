import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private List<Integer> issuedBooks; // list of book IDs

    public Member(int memberId, String name, String email, List<Integer> issuedBooks) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.issuedBooks = issuedBooks != null ? issuedBooks : new ArrayList<>();
    }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Integer> getIssuedBooks() { return issuedBooks; }

    public void displayMemberDetails() {
        System.out.printf("Member ID: %d | Name: %s | Email: %s | Issued Books: %s%n",
                memberId, name, email, issuedBooks.isEmpty() ? "None" : issuedBooks.toString());
    }

    public void addIssuedBook(int bookId) {
        if (!issuedBooks.contains(bookId)) {
            issuedBooks.add(bookId);
        }
    }

    public void returnIssuedBook(int bookId) {
        issuedBooks.remove((Integer) bookId);
    }

    // CSV representation: memberId,name,email,book1;book2;book3
    public String toCSV() {
        StringJoiner sj = new StringJoiner(";");
        for (Integer b : issuedBooks) sj.add(b.toString());
        return memberId + "," + escape(name) + "," + escape(email) + "," + sj.toString();
    }

    public static String escape(String s) {
        return s.replace(",", "&#44;");
    }

    public static String unescape(String s) {
        return s.replace("&#44;", ",");
    }
}
