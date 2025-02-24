package org.concurrency.fundamentals;

import java.util.Random;



class RaceCondition {

    public static void main(String[] args) throws InterruptedException {
        RaceLogic.letsgo();
    }

}

class RaceLogic {

    int randomInt;
    static Random random = new Random(System.currentTimeMillis());

    synchronized void printer() { // won't let race condition happen

        int i = 100000;
        while (i != 0) {
            randomInt = random.nextInt(100);
            if (randomInt % 5 == 0) {
                if (randomInt % 5 != 0) {
                    System.out.println(randomInt);
                }
            }
            i--;
        }
    }

    synchronized void modifier() {

        int i = 1000000;
        while (i != 0) {
            randomInt = random.nextInt(100);
            i--;
        }

    }

    public static void letsgo() throws InterruptedException {
        final RaceLogic rl = new RaceLogic();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                rl.printer();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                rl.modifier();
            }
        });


        t1.start();
        t2.start();


        t1.join();
        t2.join();

    }

}
