/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructures;

import java.util.*;

/**
 *
 * @author pihla
 */
public class PQ extends AbstractCollection {

    private Comparator c;
    private ArrayList<Node> lista;
    private int size;

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class HeapComparator implements Comparator<Node> {
        @Override
        public int compare(Node x, Node y) {
            if (x.frequency < y.frequency) {
                return -1;
            }
            if (x.frequency > y.frequency) {
                return 1;
            }
            return 0;
        }
    }

    public PQ() {
        this.c = new HeapComparator();
        this.lista = new ArrayList<Node>();
        this.size = 0;
        this.lista.add(new Node(Integer.MAX_VALUE, Integer.MAX_VALUE, null, null));
    }

    public boolean add(Node n) {
        lista.add(n);
        size++;
        int k = size;

        while (k > 1 && c.compare(lista.get(k / 2), n) > 0) {
            lista.set(k, lista.get(k / 2));
            k /= 2;
        }
        lista.set(k, n);

        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Node remove() {
        if (!isEmpty()) {
            Node palautus = lista.get(1);

            lista.set(1, lista.get(size));
            lista.remove(size);
            size--;
            if (size > 1) {
                jarjesta(1);
            }
            return palautus;
        }
        return null;
    }

    public void jarjesta(int vroot) {
        Node last = lista.get(vroot);
        int child, k = vroot;
        while (2 * k <= size) {
            child = 2 * k;
            if (child < size
                    && c.compare(lista.get(child),
                            lista.get(child + 1)) > 0) {
                child++;
            }
            if (c.compare(last, lista.get(child)) <= 0) {
                break;
            } else {
                lista.set(k, lista.get(child));
                k = child;
            }
        }
        lista.set(k, last);
    }
}
