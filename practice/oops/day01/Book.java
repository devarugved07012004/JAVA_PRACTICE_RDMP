public class Book {
    private String title;
    private Author author;
    private int pages;

    public Book(String title, Author author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public String toString() {
        return "Book{title='" + title +
                "', author=" + author +
                ", pages=" + pages + "}";
    }
}