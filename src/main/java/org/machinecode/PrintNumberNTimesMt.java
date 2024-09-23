package org.machinecode;

public class PrintNumberNTimesMt {
    public static void main(String[] args) throws InterruptedException {
        Foobar foobar = new Foobar(10);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    foobar.foo();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    foobar.bar();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();

    }
}

class Foobar{
    int n;
    int flag=0;

    public Foobar(int n) {
        this.n=n;
    }

    public void foo() throws InterruptedException {
        for(int i=1;i<=n;i++) {
            synchronized (this) {
                while (flag == 1) {
                    this.wait();
                }
                System.out.println("foo");
                flag = 1;
                this.notifyAll();
            }
        }
    }

    public void bar() throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            synchronized (this) {
                while (flag == 0) {
                    this.wait();
                }
                System.out.println("bar");
                flag = 0;
                this.notifyAll();
            }
        }
    }
}
