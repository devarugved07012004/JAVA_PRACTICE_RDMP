import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Item {

    private int id;

    public Item(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Item))
            return false;

        Item item = (Item) o;

        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

public class Main {

    public static void main(String[] args) {

        Set<Item> items =
                new HashSet<>();

        items.add(new Item(1));
        items.add(new Item(1));

        System.out.println(
                items.size());
    }
}