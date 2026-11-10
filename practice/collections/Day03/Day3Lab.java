import java.util.*;

public class Day3Lab {

    public static void main(String[] args) {

        List<Integer> nums =
                new ArrayList<>();

        nums.add(10);
        nums.add(20);
        nums.add(10);
        nums.add(30);
        nums.add(10);

        System.out.println(
                "Original : " + nums);

        Collections.reverse(nums);

        System.out.println(
                "Reversed : " + nums);

        int count = 0;

        for(Integer num : nums)
        {
            if(num == 10)
            {
                count++;
            }
        }

        System.out.println(
                "Frequency of 10 : "
                        + count);

        List<Integer> values =
                new ArrayList<>();

        values.add(1);
        values.add(2);
        values.add(3);
        values.add(4);
        values.add(5);
        values.add(6);

        values.removeIf(
                n -> n % 2 == 0);

        System.out.println(
                "Odds Only : "
                        + values);
    }
}