

public class NestedLoops {
    
    public static void main(String[] args) {
        // for(int i=1;i<11;i++)
        // {
        //     for(int j=1;j<11;j++)
        //     {
        //         System.out.print(i + " x " + j + " = " + i*j + "\t");
        //     }
        //     System.out.println();
        // }

        System.out.println("Right Triangle Pattern:");
        int n=5;
        for(int i=n;i>=1;i--){
            for(int j=i;j<n;j++){
                System.out.print("*");
            }
            System.out.println();
        }





        
        for (int row = 1; row <= 5; row++) {

            for (int col = 1; col <= row; col++) {
                System.out.print("*");
            }

            System.out.println();
        }

        System.out.println();
    }
}   