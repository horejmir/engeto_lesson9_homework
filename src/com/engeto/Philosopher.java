package com.engeto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Philosopher implements Runnable {

    private static boolean[] forks;
    private static final AtomicInteger allTries = new AtomicInteger(0);
    private static final AtomicInteger allSuccessfulTries = new AtomicInteger(0);

    private final int id;
    private final int leftForkIndex;
    private final int rightForkIndex;
    private int mealPortion;
    private int eatingTries = 0;


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

            eatingTries++;
            allTries.incrementAndGet();

            /* zdá se že by to fungovalo i takto bez SYNCHRONIZED??
                if (forks[leftForkIndex] == true && forks[rightForkIndex] == true) {

                    forks[leftForkIndex] = false;
                    forks[rightForkIndex] = false;

                    mealPortion--;
                    incrementAllSuccessfulTries();

                    forks[leftForkIndex] = true;
                    forks[rightForkIndex] = true;
                }
            */

            if (getFork(leftForkIndex) && getFork(rightForkIndex)) {

                setFork(leftForkIndex, false);
                setFork(rightForkIndex, false);

                mealPortion--;
                allSuccessfulTries.incrementAndGet();

                setFork(leftForkIndex, true);
                setFork(rightForkIndex, true);
            }
        }

        System.out.println("DONE - Philosopher #" + id + " finished his/her meal! Plate check: " + mealPortion + ", eating tries: " + eatingTries
                + "\n\tDinner finished in: " + (System.currentTimeMillis() - startTime) + " msec ,thread: " + Thread.currentThread());
    }

    public static int getAllTries() {
        return allTries.intValue();
    }

    public static int getAllSuccessfulTries() {
        return allSuccessfulTries.intValue();
    }

    public static boolean[] getForks() {
        return forks;
    }

    private static boolean getFork(int index) {
        synchronized (Philosopher.class) {
            return forks[index];
        }
    }

    private static void setFork(int index, boolean value) {
        synchronized (Philosopher.class) {
            forks[index] = value;
        }
    }
}
