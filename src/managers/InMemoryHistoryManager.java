package managers;

import tasks.*;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    //CustomLinkedList
    public Map<Integer, Node<Task>> historyList = new LinkedHashMap<>();

    //реализация CustomLinkedList
    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;

    public void linkLast(Node<Task> node) {
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
        if (!historyList.containsValue(nodeTask)) {
            linkLast(nodeTask);
        } else {
            removeNode(nodeTask);
            linkLast(nodeTask);
        }
    }

    @Override
    public List<Task> getHistory() {    //method must return history list
        ArrayList<Task> values = new ArrayList<>();
        for (Node<Task> value : historyList.values()) {
            values.add(value.data);
        }
        return values;
    }

    @Override
    public void remove(int id) {        //method must remove task from HashMap historyLis
        removeNode(historyList.get(id));
        historyList.remove(id);
        size--;
    }

    private void removeNode(Node<Task> node) {
            if (head == null || node == null) {
                return;
            }
            if (head == node) {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }

            if (node.prev != null) {
                node.prev.next = node.next;
            }
    }

     class Node<Task> {

        protected Task data;
        protected Node<Task> next;
        protected Node<Task> prev;

        public Node(Task data) {
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
