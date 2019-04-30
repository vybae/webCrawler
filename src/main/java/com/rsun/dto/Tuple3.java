package com.rsun.dto;

import groovy.lang.Tuple;

public class Tuple3<T1, T2, T3> extends Tuple {
    public Tuple3(T1 first, T2 second, T3 third) {
        super(new Object[]{first, second, third});
    }

    public T1 getFirst() {
        return (T1) this.get(0);
    }

    public T2 getSecond() {
        return (T2) this.get(1);
    }

    public T3 getThird() {
        return (T3) this.get(2);
    }
}