package org.concurrency;

class SumupMt {

    public static void main(String[] args) throws InterruptedException {
        SumNumbers.runTest();
    }
}

class SumNumbers{

        long startNum;
        long endNum;
        long counter=0;
        static long maxNum = Integer.MAX_VALUE;

        public SumNumbers(long startNum, long endNum) {
            this.startNum=startNum;
            this.endNum=endNum;
        }

        public void addition (){
            while(startNum<=endNum) {

                counter += startNum;
                startNum++;

            }

        }

       static public void twoThread() throws InterruptedException {
           long start = System.currentTimeMillis();
            SumNumbers s1= new SumNumbers(1, maxNum/2);
            SumNumbers s2= new SumNumbers(1 + maxNum/2, maxNum);

            Thread t1 = new Thread(() -> s1.addition());


            Thread t2 = new Thread(() -> s2.addition());

            t1.start();
            t2.start();

            t1.join();
            t2.join();

           long finalCount = s1.counter + s2.counter;
           long end = System.currentTimeMillis();
           System.out.println("Two threads final count = " + finalCount + " took " + (end - start));

        }

        static public void noThread(){
            long start = System.currentTimeMillis();
            SumNumbers s= new SumNumbers(1, maxNum);
            s.addition();
            long end = System.currentTimeMillis();
            System.out.println("Single thread final count = " + s.counter + " took " + (end - start));
        }

    public static void runTest() throws InterruptedException {
            noThread();
            twoThread();

    }
}
