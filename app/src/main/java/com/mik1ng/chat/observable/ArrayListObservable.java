package com.mik1ng.chat.observable;

import com.mik1ng.chat.interfaces.Observe;

import java.util.ArrayList;

public class ArrayListObservable<T> {

    private ArrayList<T> list;

    public ArrayListObservable() {
        list = new ArrayList<>();
    }

    public ArrayListObservable(ArrayList<T> list) {
        this.list = list;
    }

    public ArrayList<T> get() {
        return list;
    }

    public T get(int position) {
        return list.get(position);
    }

    public int size() {
        return list.size();
    }

    public void add(T t) {
        list.add(t);
        if (observe != null) {
            observe.update(list);
        }
    }

    public void add(int index, T t) {
        list.add(index, t);
        if (observe != null) {
            observe.update(list);
        }
    }

    public T remove(int index) {
        return list.remove(index);
    }

    private Observe<ArrayList<T>> observe;

    public void addObserve(Observe<ArrayList<T>> observe) {
        this.observe = observe;
    }
}
