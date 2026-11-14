import java.util.PriorityQueue;

class Task
implements Comparable<Task>
{
    private String name;
    private int priority;

    public Task(String name,
                int priority)
    {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int compareTo(Task other)
    {
        return Integer.compare(
                this.priority,
                other.priority);
    }

    @Override
    public String toString()
    {
        return name + "("
                + priority + ")";
    }
}

public class Day17Lab {

    public static void main(String[] args)
    {
        PriorityQueue<Task> tasks =
                new PriorityQueue<>();

        tasks.offer(
                new Task(
                        "Code Review",
                        3));

        tasks.offer(
                new Task(
                        "Production Issue",
                        1));

        tasks.offer(
                new Task(
                        "Database Fix",
                        2));

        while(!tasks.isEmpty())
        {
            System.out.println(
                    tasks.poll());
        }
    }
}