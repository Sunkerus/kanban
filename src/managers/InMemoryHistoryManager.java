package managers;

import tasks.*;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    //CustomLinkedList
    private Map<Integer, Node<Task>> historyList = new HashMap<>();

    //реализация CustomLinkedList
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    private void linkLast(Node<Task> node) {
        if (size == 0) {
            head = node;
        } else {
            tail = node;
        }
        int id = node.data.getId();
        historyList.remove(id);
        historyList.put(id, node);
        size++;
    }

    @Override
    public void add(Task task) {

        Node<Task> nodeTask = new Node<>(task);
        if (!historyList.containsKey(task.getId())) {
            linkLast(nodeTask);
        } else {
            removeNode(nodeTask);
            linkLast(nodeTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> values = new ArrayList<>();
        for (Node<Task> value : historyList.values()) {        //change to head
            values.add(value.data);
        }
        return values;
    }


    @Override
    public void remove(int id) {
        removeNode(historyList.remove(id));
         size--;
    }

    private void removeNode(Node<Task> node) {
        if (head == null || node == null) {
            return;
        }
        if (head == node) {
            head = node.next;       //change!
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
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

        //переопределеим equals для сравнения задач в historyList
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node<Task> node = (Node<Task>) o;

            return data.equals(node.data);
        }
    }
}




