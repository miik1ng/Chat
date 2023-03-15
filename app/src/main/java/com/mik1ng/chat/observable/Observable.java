package com.mik1ng.chat.observable;

import com.mik1ng.chat.interfaces.Observe;

public class Observable<T> {
    private T t;
    private Observe<T> observe;

    public Observable() {
    }

    public Observable(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
        if (observe != null) {
            observe.update(t);
        }
    }

    public void addObserve(Observe<T> observe) {
        this.observe = observe;
    }
}
