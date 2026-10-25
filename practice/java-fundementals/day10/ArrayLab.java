public class ArrayLab {

    public static void main(String[] args) {

        int[] numbers = fillSample();

        System.out.println("Original Array:");
        print(numbers);

        reverse(numbers);

        System.out.println("Reversed Array:");
        print(numbers);

        System.out.println("Maximum Value: " + max(numbers));
        System.out.println("Minimum Value: " + min(numbers));
    }

    public static int[] fillSample() {

        int[] numbers = {12, 45, 7, 89, 23, 56, 3, 90};

        return numbers;
    }

    public static void print(int[] a) {

        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }

        System.out.println();
    }

    // In-place reverse (modifies original array)
    public static void reverse(int[] a) {

        int start = 0;
        int end = a.length - 1;

        while (start < end) {

            int temp = a[start];
            a[start] = a[end];
            a[end] = temp;

            start++;
            end--;
        }
    }

    public static int max(int[] a) {

        int maxValue = a[0];

        for (int i = 1; i < a.length; i++) {

            if (a[i] > maxValue) {
                maxValue = a[i];
            }
        }

        return maxValue;
    }

    public static int min(int[] a) {

        int minValue = a[0];

        for (int i = 1; i < a.length; i++) {

            if (a[i] < minValue) {
                minValue = a[i];
            }
        }

        return minValue;
    }
}