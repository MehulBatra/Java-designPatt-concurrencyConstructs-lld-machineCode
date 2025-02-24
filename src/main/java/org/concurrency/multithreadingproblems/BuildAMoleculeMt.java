package org.concurrency.multithreadingproblems;

public class BuildAMoleculeMt {
    public static void main(String[] args) {

        MoleculeMachine mm = new MoleculeMachine();
        MoleculeRun t1 = new MoleculeRun("Hydrogen", mm);
        MoleculeRun t2 = new MoleculeRun("Oxygen", mm);
        MoleculeRun t3 = new MoleculeRun("Hydrogen", mm);
        MoleculeRun t4 = new MoleculeRun("Hydrogen", mm);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}


class MoleculeMachine{
    int hydrogenCount=0;
    int oxygenCount=0;
    int capacity=3;
    final Object lock = new Object();

    public void formHydrogen() throws InterruptedException {
        synchronized(lock){
            while(hydrogenCount==2){
                lock.wait();
            }
            hydrogenCount++;
            if(hydrogenCount+oxygenCount==capacity){
                System.out.print("HydrogenCount" + hydrogenCount + "OxygenCount" + oxygenCount + "\n");
                hydrogenCount=0;
                oxygenCount=0;
            }
            lock.notifyAll();
        }

    }

    public void formOxygen() throws InterruptedException {
        synchronized(lock){
            while(oxygenCount==1){
                lock.wait();
            }
            oxygenCount++;
            if(hydrogenCount+oxygenCount==capacity){
                System.out.print("HydrogenCount" + hydrogenCount + "OxygenCount" + oxygenCount + "\n");
                hydrogenCount=0;
                oxygenCount=0;
            }
            lock.notifyAll();
        }
    }
}


class MoleculeRun extends Thread{

    String name;
    MoleculeMachine mm;

    public MoleculeRun(String name, MoleculeMachine mm){
        this.name = name;
        this.mm = mm;
    }

    public void run(){
        if(name.equals("Hydrogen")){
            try {
                mm.formHydrogen();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else if(name.equals("Oxygen")){
            try {
                mm.formOxygen();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
