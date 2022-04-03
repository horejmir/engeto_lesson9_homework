package com.engeto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Philosopher implements Runnable {

    private static boolean[] forks;
    private static final AtomicLong countAllTries = new AtomicLong(0); //counter of all eating tries
    private static final AtomicLong countAllSuccessfulTries = new AtomicLong(0); //counter of all successful eating tries

    private final int id;
    private final int leftForkIndex;
    private final int rightForkIndex;
    private int mealPortion;
    private int countTries = 0;


    private Philosopher(int id, int mealPortion, int leftForkIndex, int rightForkIndex) {
        this.id = id;
        this.mealPortion = mealPortion;
        this.leftForkIndex = leftForkIndex;
        this.rightForkIndex = rightForkIndex;
    }

    public static List<Philosopher> generatePhilosophersDinner(int numberOfPhilosophers, int mealPortion) {

        forks = new boolean[numberOfPhilosophers];
        List<Philosopher> philosophers = new ArrayList<>();

        for (int i = 0; i < numberOfPhilosophers; i++) {
            forks[i] = true;

            int j = i + 1;
            if (j < numberOfPhilosophers)
                philosophers.add(new Philosopher(i + 1, mealPortion, i, j));
            else
                philosophers.add(new Philosopher(i + 1, mealPortion, i, 0));
        }

        System.out.println("Philosopher's dinner created: philosophers: "
                + numberOfPhilosophers + ", meal portions: " + mealPortion);

        return philosophers;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();

        while (mealPortion > 0) {

            countTries++;
            countAllTries.incrementAndGet();

            if (askAndTakeForks(leftForkIndex, rightForkIndex)) { // resources allocated (synchronized method)

                mealPortion--;
                countAllSuccessfulTries.incrementAndGet();

                releaseForks(leftForkIndex, rightForkIndex); // resources released (synchronized method)
            }
        }

        System.out.println("DONE - Philosopher #" + id + " finished his/her meal! Plate check: " + mealPortion
                + ", eating tries: " + countTries + "\n\tDinner finished in: "
                + (System.currentTimeMillis() - startTime) + " msec ,thread: " + Thread.currentThread());
    }

    public static long getAllTries() {
        return countAllTries.longValue();
    }

    public static long getAllSuccessfulTries() {
        return countAllSuccessfulTries.longValue();
    }

    public static boolean[] getForks() {
        return forks;
    }

    private static boolean askAndTakeForks(int leftForkIndex, int rightForkIndex) {
        synchronized (Philosopher.class) {
            if(forks[leftForkIndex] && forks[rightForkIndex]){
                forks[leftForkIndex] = false;
                forks[rightForkIndex] = false;
                return true;
            } else
                return false;
        }
    }

    private static void releaseForks(int leftForkIndex, int rightForkIndex) {
        synchronized (Philosopher.class) {
            forks[leftForkIndex] = true;
            forks[rightForkIndex] = true;
        }
    }
}
