package it.unicam.cs.filieraagricola.util;

public class IdGenerator {
    private static long counter = 1;

    public static synchronized long generateId(){return counter++;}
}
