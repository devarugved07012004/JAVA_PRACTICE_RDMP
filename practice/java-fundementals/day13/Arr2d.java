import java.util.Scanner;

public class Arr2d {
    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the number of rows");
    int r = sc.nextInt();
    System.out.println("Enter the number of columns");
    int c = sc.nextInt();
    int a[][]  = new int [r][c];
    System.out.println("Enter the elements of the array");
    for(int i=0;i<r;i++){
        for(int j=0;j<c;j++){
            a[i][j] = sc.nextInt();
        }
    }
    System.out.println("The array is");
    printArray(a);
    System.out.println("The transpose of the array is");
    int[][] transpose = transpose(a);
    printArray(transpose);
    sum(transpose);
    sc.close();
}
public static int[][] transpose(int [][] a){
    int[][] transpose = new int[a[0].length][a.length];
    for(int i=0;i<a.length;i++){
        for(int j=0;j<a[i].length;j++){
            transpose[j][i] = a[i][j];
        }
    }
    return transpose;
}
public static void printArray(int [][] a){
    for(int i=0;i<a.length;i++){
        for(int j=0;j<a[i].length;j++){
            System.out.print(a[i][j]+" ");
        }
        System.out.println();
    }
}
public static void sum(int [][] a){
    int k=0;
    for(int i=0;i<a.length;i++){
        int sum = 0;
        for(int j=0;j<a[i].length;j++){
            sum += a[i][j];
            k+=a[i][j];
        }
        System.out.println("The sum of the row "+i+" is "+sum);
    }
    System.out.println("The sum of the array is "+k);
}
}

