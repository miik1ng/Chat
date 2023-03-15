package com.mik1ng.chat.observable;

import com.mik1ng.chat.interfaces.Observe;

public class IntegerObservable {
    private int i;

    public IntegerObservable() {
    }

    public IntegerObservable(int i) {
        this.i = i;
    }

    public int get() {
        return i;
    }

    public void set(int i) {
        this.i = i;
        if (observe != null) {
            observe.update(i);
        }
    }

    public void add(int i) {
        this.i = this.i + i;
        if (observe != null) {
            observe.update(i);
        }
    }

    private Observe<Integer> observe;

    public void addObserve(Observe<Integer> observe) {
        this.observe = observe;
    }
}
