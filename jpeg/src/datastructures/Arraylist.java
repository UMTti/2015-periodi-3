/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructures;

import java.util.Arrays;

/**
 *
 * @author pihla
 */
public class Arraylist {

    private Node[] store;
    private int size;

    /**
     * Constructor for arraylist. Size is size of list in use at the moment
     */
    public Arraylist() {
        this.store = new Node[10];
        size = 0;
    }

    /**
     * Get i:th Node in list
     * @param i
     * @return
     */
    public Node get(int i) {
        if (i < size) {
            return store[i];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Add Node to list
     * @param n
     */
    public void add(Node n) {
        if (store.length - size <= 5) {
            increaseList();
        }
        store[size++] = n;
    }

    /**
     * Increase store-array
     */
    public void increaseList() {
        store = Arrays.copyOf(store, store.length * 2);
    }

    /**
     * Return size of list in use at the moment
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Set node given a parameter to i:th place
     * @param i
     * @param n
     */
    public void set(int i, Node n) {
        if (i < size) {
            store[i] = n;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Remove i:th item in list and move others 
     * @param i
     * @return
     */
    public Node remove(int i) {
        if (i < size) {
            Node obj = store[i];
            store[i] = null;
            int tmp = i;
            while (tmp < size) {
                store[tmp] = store[tmp + 1];
                store[tmp + 1] = null;
                tmp++;
            }
            size--;
            return obj;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
}
