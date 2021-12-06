package com.swecor.aerotek;

public class Main {
    public static void main(String[] args) {
        int[] arr = {-15, -10, -5, 0, 5, 10, 15};
        int x1 = 3, x2 = 30, x3 = -3, x4 = -7, x5 = 8;
        System.out.println(" near x1 : " + find(x1, arr));
        System.out.println(" near x2 : " + find(x2, arr));
        System.out.println(" near x3 : " + find(x3, arr));
        System.out.println(" near x4 : " + find(x4, arr));
        System.out.println(" near x5 : " + find(x5, arr));
    }

    public static int find(int x, int[] arr) {
        int distance = Math.abs(arr[0] - x);
        int idx = 0;
        for (int c = 1; c < arr.length; c++) {
            int cdistance = Math.abs(arr[c] - x);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return arr[idx];
    }
}
