
public class StringDrill {
    public static void main(String[] args) {
        System.out.println("Hello World");
        String s = "Hello World";
        String s1="JAVA";
        String s2 = "java";
        System.out.println("===" + (s1==s2));
        System.out.println("equals() " + s1.equals(s2));
        System.out.println("equalsIgnoreCase() " + s1.equalsIgnoreCase(s2));
        System.out.println("compareTo() " + s1.compareTo(s2));
        System.out.println("compareToIgnoreCase() " + s1.compareToIgnoreCase(s2));
        System.out.println("s.length() " + s.length());
        System.out.println("s.charAt(0) " + s.charAt(0));
        System.out.println("s.charAt(s.length()-1) " + s.charAt(s.length()-1));
        System.out.println("s.substring(0,5) " + s.substring(0,5));
        System.out.println("s.substring(5) " + s.substring(5));
        System.out.println("s.substring(0,5)+s.substring(5) " + s.substring(0,5)+s.substring(5));
    }
}
