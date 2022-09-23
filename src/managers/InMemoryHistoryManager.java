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

    private void linkLast(Node<Task> node) { //***
        if (size == 0) {
            head = node;
            tail = head;
            head.next = tail;
            tail.prev = head;
        }else{
            Node<Task> tempTail = tail;
            head.next = tempTail;
            tempTail.next = node;
            node.prev = tempTail;
            tail = node;
        }
        size++;
    }

    @Override
    public void add(Task task) {
        Integer id = task.getId();      //получаем id для добавления в historyList
        Node<Task> nodeTask = new Node<>(task);
        if (historyList.containsKey(id)) {
            historyList.remove(id);
            linkLast(nodeTask);
        }else {
            historyList.put(id, nodeTask);
            linkLast(nodeTask);
        }
    }

    @Override
    public List<Task> getHistory() {        //***
        ArrayList<Task> values = new ArrayList<>();
        Node <Task> tempNode = head;
        while (tempNode != tail) {
            values.add(tempNode.data);
            tempNode = tempNode.next;
        }
        values.add(tail.data);

        return values;
    }


    @Override
    public void remove(int id) {
        removeNode(historyList.remove(id));
         size--;
    }

    private void removeNode(Node<Task> node) { //***
        // Base case
        if (head == null || node == null) {
            return;
        }

        if (head == node) {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }else {
            node.prev.next = null;
            tail = node.prev;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        }
        return;
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




