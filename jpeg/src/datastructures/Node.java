/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructures;

/**
 *
 * @author pihla
 */
public class Node {

    public int frequency;
    public int value;
    public Node left;

    public Node right;
    String temparvo;

    /**
     * One constructor for Node class
     *
     * @param value
     * @param frequency
     * @param left
     * @param right
     */
    public Node(int value, int frequency, Node left, Node right) {
        this.frequency = frequency;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    /**
     * Another constructor with temparvo. That is used in decoder.java
     *
     * @param temparvo
     * @param frequency
     * @param left
     * @param right
     */
    public Node(String temparvo, int frequency, Node left, Node right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
        this.temparvo = "";
    }

    /**
     * Checks if node is leaf
     *
     * @return
     */
    public boolean isLeaf() {
        if (this.right == null && this.left == null) {
            return true;
        } else {
            return false;
        }
    }
}
