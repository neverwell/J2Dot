package com.nemo.test;

public class LinkedList<E> {

    private class Node{
        public E e;
        public Node next;

        public Node(E e, Node next){
            this.e = e;
            this.next = next;
        }

        public Node(E e){
            this(e, null);
        }

        public Node(){
            this(null, null);
        }

        @Override
        public String toString(){
            return e.toString();
        }
    }

//    private Node dummyHead;
    private Node head;
    private int size;

    public LinkedList(){
//        dummyHead = new Node();
        head = null;
        size = 0;
    }

    // 获取链表中的元素个数
    public int getSize(){
        return size;
    }

    // 返回链表是否为空
    public boolean isEmpty(){
        return size == 0;
    }

    // 在链表头添加新的元素e
    public void addFirst(E e){
        add(0, e);
    }

    // 在链表末尾添加新的元素e
    public void addLast(E e){
        add(size, e);
    }

    // 在链表的index(0-based)位置添加新的元素e
    // 在链表中不是一个常用的操作，练习用：）
    public void add(int index, E e){
        if(index < 0 || index > size) {
            throw new IllegalArgumentException("Add failed. Illegal index.");
        }

//        add(index, e, dummyHead, 0);
        head = add(head, index, e);
//        addFunc(dummyHead, index, e);
        size ++;
    }

    private Node addFunc(Node prev, int index, E e) {
        if(index == 0) {
            prev.next = new Node(e, prev.next);
            return prev;
        }
        prev.next = addFunc(prev.next, index - 1, e);
        return prev;
    }

    private Node add(Node node, int index, E e) {
        if(index == 0) {
            Node newNode = new Node(e);
            newNode.next = node;
            return newNode;
        }
        node.next = add(node.next, --index, e);
        return node;
    }

    private void add(int index, E e, Node node, int depth) {
        if(index == depth) {
            node.next = new Node(e, node.next);
            return;
        }
        add(index, e, node.next, ++depth);
    }

    @Override
    public String toString() {
        return printNode(head);
    }

    private String printNode(Node node) {
        if(node == null) {
            return "NULL";
        }
        String value = node.e == null ? "DUMMYHEAD" : node.e.toString();
        return value + "->" + printNode(node.next);
    }
}
