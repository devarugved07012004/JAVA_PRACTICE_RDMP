public class StringPractice {

    // 1. Palindrome Check (ignoring case)
    static boolean isPalindrome(String s) {

        String lower = s.toLowerCase();

        int left = 0;
        int right = lower.length() - 1;

        while (left < right) {

            if (lower.charAt(left) != lower.charAt(right)) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }

    // 2. Count Vowels
    static int countVowels(String s) {

        int count = 0;

        for (int i = 0; i < s.length(); i++) {

            char ch = s.charAt(i);

            if (ch == 'a' || ch == 'e' || ch == 'i' ||
                ch == 'o' || ch == 'u' || ch == 'A' ||
                ch == 'E' || ch == 'I' || ch == 'O' ||
                ch == 'U') {

                count++;
            }
        }

        return count;
    }

    // 3. Reverse Words
    static String reverseWords(String sentence) {

        String[] words = sentence.split(" ");

        String result = "";

        for (int i = words.length - 1; i >= 0; i--) {

            result += words[i];

            if (i > 0) {
                result += " ";
            }
        }

        return result;
    }
    public static int cv(String s){
        String vowels = "aeiouAEIOU";
        int count = 0;
        for(char i:s.toCharArray()){
            if(vowels.indexOf(i)!=-1){
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {

        // Palindrome Tests
        System.out.println("Racecar -> "
                + isPalindrome("Racecar"));

        System.out.println("hello -> "
                + isPalindrome("hello"));

        System.out.println();

        // Vowel Count Test
        String text = "Java Fundamentals";

        System.out.println("Text: " + text);
        System.out.println("Vowel Count: "
                + countVowels(text));

        System.out.println();

        // Reverse Words Test
        String sentence = "one two three";

        System.out.println("Original: " + sentence);
        System.out.println("Reversed: "
                + reverseWords(sentence));
    }
}