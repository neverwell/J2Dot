package com.nemo.test;

public class ListNode {

    public int val;

    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int[] arr) {
        if(arr == null || arr.length == 0) {
            throw new IllegalArgumentException("arr can not be empty");
        }

        this.val = arr[0];
        ListNode cur = this;
        for(int i = 1; i < arr.length; i++) {
            cur.next = new ListNode(arr[i]);
            cur = cur.next;
        }
    }

    public ListNode getNext() {
        return next;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        ListNode cur = this;
        sb.append(cur.val + " ");
        while (cur.next != null) {
            cur = cur.next;
            sb.append(cur.val + " ");
        }
        return sb.toString();
    }
}
