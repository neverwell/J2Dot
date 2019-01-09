package com.nemo.test;

public class Solution {

    //删除链表中所有值为val的节点
    public static ListNode removeElements(ListNode head, int val) {
        if(head == null) {
            return head;
        }

        ListNode dummyHead = new ListNode(-1);
        dummyHead.next = head;

        ListNode cur = dummyHead;
        while (cur.next != null) {
            ListNode next = cur.next;
            if(next.val == val) {
                cur.next = next.next;
                next.next = null;
            } else {
                cur = cur.next;
            }
        }

        return dummyHead.next;
    }

    //删除链表中所有值为val的节点
    public static ListNode removeElements2(ListNode head, int val) {
        if(head == null) {
            return null;
        }

        //下一次更小的规模
        ListNode next = removeElements2(head.next, val);

        //递归中每次返回都是上一层的调用位置
        if(head.val == val) {
            return next;
        } else {
            head.next = next;
            return head;
        }
    }

    public static void main(String[] args) {
        int[] arr = {6, 61,1, 2, 3, 4, 5, 6, 7, 8, 6, 9};

        ListNode listNode = new ListNode(arr);

//        ListNode ret = removeElements(listNode, 6);

        ListNode ret = removeElements2(listNode, 6);

        System.out.println(ret);





    }
}
