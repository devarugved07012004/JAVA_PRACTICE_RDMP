public class Main {
    public static void main(String[] args) {

        // One Author object
        Author author = new Author(
                "Joshua Bloch",
                "joshua@example.com"
        );

        // Two Books sharing the same Author
        Book book1 = new Book(
                "Effective Java",
                author,
                416
        );

        Book book2 = new Book(
                "Java Puzzlers",
                author,
                384
        );

        System.out.println("Before changing author:");
        System.out.println(book1);
        System.out.println(book2);

        System.out.println();

        // Change the author's name
        author.setName("Joshua J. Bloch");

        System.out.println("After changing author:");
        System.out.println(book1);
        System.out.println(book2);
    }
}