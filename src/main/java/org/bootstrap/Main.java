package org.bootstrap;

public class Main {
    final private static int sampleSize = 10; // n
    final private static int numberOfSamples = 10; // m
    final private static String eventLogFile = "/Users/anandi/Downloads/sampleData.csv";

    public static void main(String[] args) {
        double genValue = new BootstrapGen(sampleSize, numberOfSamples, eventLogFile).calculateBootstrapGen();
        System.out.println(genValue);
    }

}