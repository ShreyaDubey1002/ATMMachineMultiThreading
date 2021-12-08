package com.shreya;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

class ATMClass {
    Semaphore sem;
    boolean[] ATM;
    int n;

    ATMClass (Semaphore sem, int n) {
        this.sem = sem;
        this.n = n;
        ATM = new boolean[n];
        for (int i = 0; i<n; i++) {
            ATM[i] = false;
        }
    }
    ATMClass (int n) {
        this.sem = new Semaphore(n);
        this.n = n;
        this.ATM = new boolean[n];
        for (int i = 0; i<n; i++) {
            this.ATM[i] = false;
        }
    }

    public void withdrawMoney() {
        System.out.println("Thread : " + Thread.currentThread().getName() + " , waiting to get semaphore");

        try {
            sem.acquire();
            System.out.println("Thread : " + Thread.currentThread().getName() + " , acquired semaphore");
            try {
                System.out.println("Thread : " + Thread.currentThread().getName() + " , waiting to get ATM");
                int index = -1;
                synchronized(ATM) {
                    for (int i = 0; i < n; i++) {
                        if (ATM[i] == false) {
                            ATM[i] = true;
                            index = i;
                            break;
                        }
                    }
                }
                System.out.println("Thread : " + Thread.currentThread().getName() + " , index : " + index);
                        System.out.println("Thread : " + Thread.currentThread().getName() + " , got ATM, withdrawing money");
                Thread.sleep(100);

                System.out.println("Thread : " + Thread.currentThread().getName() + " , has withdrawn money, unlocking ATM");

                synchronized(ATM) {
                    ATM[index] = false;
                }

                System.out.println("Thread : " + Thread.currentThread().getName() + " unlocked ATM, releasing semaphore");
            } finally {
                sem.release();
                System.out.println("Thread : " + Thread.currentThread().getName() + " , released semaphore");
            }
        } catch (InterruptedException e) {

        }
    }
}

class WithdrawMoneyTask extends Thread {
    ATMClass ATMClassObj;
    WithdrawMoneyTask (ATMClass ATMClassObj, String name) {
        super(name);
        this.ATMClassObj = ATMClassObj;
    }
    public void run() {
        this.ATMClassObj.withdrawMoney();
    }
}

public class ATMSemaphore {

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int n;
        System.out.println("Enter n : ");
        n = sc.nextInt();
        ATMClass ATMClassObj = new ATMClass(n);
        System.out.println("Enter number of people : ");
        n = sc.nextInt();
        WithdrawMoneyTask[] array = new WithdrawMoneyTask[n];
        for (int i=0;i<n;i++) {
            array[i] = new WithdrawMoneyTask(ATMClassObj, "Thread-" + i);
        }
        for (int i=0;i<n;i++) {
            array[i].start();
        }
    }
}

