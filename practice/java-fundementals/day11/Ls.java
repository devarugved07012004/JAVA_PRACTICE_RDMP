import java.util.Scanner;
public class Ls {
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Enter the size of the array");
        int k = sc.nextInt();
        int a[] = new int[k];
        System.out.println("Enter the elements of the array");
        for(int i=0;i<a.length;i++){
            a[i] = sc.nextInt();
        }
        System.out.println("Enter the target element");
        int tar = sc.nextInt();
        System.out.println(ls(a,tar));
        System.out.println(count(a,tar));
    }


    public static int ls(int a[],int num){
        for(int i=0;i<a.length;i++){
            if(a[i]==num){
                return i;
            }
        }
       return -1;
    }
    public static int count(int a[],int tar){
        int count = 0;
        for(int i=0;i<a.length;i++){
            if(a[i]==tar){
                count++;
            }
        }
        return count;
    }
    
    }
