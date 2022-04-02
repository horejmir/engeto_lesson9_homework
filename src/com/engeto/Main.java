package com.engeto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static final int NUMBER_OF_PHILOSOPHERS = 10;
    public static final int MEAL_PORTION = 1_000_000;

    public static void main(String[] args) {

        //generate list of philsophers and forks as static parameter of Philosopher Class
        List<Philosopher> philosophers = Philosopher.generatePhilosophersDinner(NUMBER_OF_PHILOSOPHERS,MEAL_PORTION);

        System.out.println("forks availability check before dinner: " + Arrays.toString(Philosopher.getForks()));
        System.out.println("=========================================================================================================================");

        //create and run threads
        List<Thread> threadList = new ArrayList<>();
        for(Philosopher philosopher : philosophers){
            Thread thread = new Thread(philosopher);
            threadList.add(thread);
            thread.start();
        }

        // waiting till all threads are finished
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //final checks and statistic
        System.out.println("======== all " + threadList.size() + " threads finished =================================================================");
        System.out.println("CHECKS - all eating tries: " + Philosopher.getAllTries() + ", all successfull eating tries: " + Philosopher.getAllSuccessfulTries()
                +  " (should be " + (NUMBER_OF_PHILOSOPHERS * MEAL_PORTION)+")");
        System.out.println("forks availability check after dinner: " + Arrays.toString(Philosopher.getForks()));
    }
}
