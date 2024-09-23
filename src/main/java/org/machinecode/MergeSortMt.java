package org.machinecode;

import java.util.Random;

public class MergeSortMt {

    private static int SIZE = 10;
    private static Random random = new Random(System.currentTimeMillis());
    private static int[] input = new int[SIZE];

    static private void createTestData() {
        for (int i = 0; i < SIZE; i++) {
            input[i] = random.nextInt(10000);
        }
    }

    static private void printArray(int[] input) {
        System.out.println();
        for (int i = 0; i < input.length; i++)
            System.out.print(" " + input[i] + " ");
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
            createTestData();
            printArray(input);
            long start = System.currentTimeMillis();
        (new MultiThreadedMergeSort()).mergeSort(input,0, input.length-1);
        long end = System.currentTimeMillis();
        System.out.println("\n\nTime taken to sort = " + (end - start) + " milliseconds");
        System.out.println("Sorted Array");
        printArray(input);
    }
}

class MultiThreadedMergeSort {
    static int  size=10;
    private int[] input = new int[size];
    private int[] arrayUserForMerging = new int[size];

    public void mergeSort(final int[] inputArray, final int start, final int end) throws InterruptedException {
        if(start == end) {return;}
        final int mid = (start + end) / 2;

        Thread worker1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    mergeSort(inputArray, start, mid);
                }
                catch(Exception e) {

                }
            }
        });

        Thread worker2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    mergeSort(inputArray, mid+1, end);
                } catch (Exception e) {
                }
            }
        });

        worker1.start();
        worker2.start();

        worker1.join();
        worker2.join();

        // merge the two halves of sorted array done by above thread
        int i=start;
        int j=mid+1;
        int k;

        for(k=start;k<=end;k++) { // make copy of array
            arrayUserForMerging[k]=inputArray[k];
        }

        k=start;
        while(k<=end) {

            if(i<=mid && j<=end) {
                inputArray[k] = Math.min(arrayUserForMerging[i],arrayUserForMerging[j]);//which ever minimum select
                if(inputArray[k]==arrayUserForMerging[i]) {
                    i++;
                }
                else{
                    j++;
                }
            }
            else if(i<=mid && j>end){ // if j has breached the check if k element in input == i elment then i++
                inputArray[k] = arrayUserForMerging[i];
                i++;
            }
            else{
                inputArray[k] = arrayUserForMerging[j]; // if i  has breached mid check if k element in input == j element j++;
                j++;
            }
            k++;
        }
    }



}
