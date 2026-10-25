public class Day14 {
    public static void main(String[] args) {
        System.out.println("Hello World");
        int[] a = {1,2,3,4,5};
        System.out.println(secondL(a));
        System.out.println(factorial(5));
    }
    public static int factorial(int n){
        int k = 1;
        for (int i = 1; i <= n; i++) {
            k *= i;
        }
        return k;
    }
    public static int secondL(int[] a){
        int max =a[0];
        int smax=a[0];
        for(int i=0;i<a.length;i++){
            if(a[i]>max){
                smax=max;
                max=a[i];
            }
            else if(a[i]>smax && a[i]!=max){
                smax=a[i];
            }
        }
        return smax;
    }
}