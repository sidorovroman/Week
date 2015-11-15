package ru.sidorovroman.week.models;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sidorovroman on 15.11.15.
 */
public class PseudoModel {

    private static final AtomicInteger count = new AtomicInteger(0);

    private final int pseudoId;

    public PseudoModel() {
        pseudoId = count.incrementAndGet();
    }

    public int getPseudoId() {
        return pseudoId;
    }
}
