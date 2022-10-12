package managers;

import tasks.*;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    //CustomLinkedList
    private final Map<Integer, Node<Task>> historyList = new HashMap<>();

    private Node<Task> head = null;
    private Node<Task> tail = null;
    private int size = 0;

    private void linkLast(Node<Task> node) {
        if(head == null) {
            head = node;
            tail = head;
        } else if (head == tail) {
            head.next = node;
            node.prev = head;
            tail = node;
        }else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    @Override
    public void add(Task task) {
        Integer id = task.getId();
        Node<Task> node = new Node<>(task);
        historyList.remove(id);
        linkLast(node);
        historyList.put(id, node);
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> values = new ArrayList<>();
        Node <Task> tempNode = head;
        while (tempNode != null) {
            values.add(tempNode.data);
            tempNode = tempNode.next;
        }
        return values;
    }


    @Override
    public void remove(int id) {
        removeNode(historyList.remove(id));
    }


    private void removeNode(Node<Task> node) {
        if (node != null) {
            if (node.next == null) {
                node.prev.next = null;
                tail = node.prev;
            } else if (node.prev == null) {
                node.next.prev = null;
                head = node.next;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            size--;
        }
}

    private class Node<Task> {

        protected Task data;
        protected Node<Task> next;
        protected Node<Task> prev;

        private Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}


