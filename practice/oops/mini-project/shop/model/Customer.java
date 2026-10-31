public class Customer {
    private int id;
    private String name;
    private String email;

    public Customer(int id, String name, String email) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name required");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("email required");
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
