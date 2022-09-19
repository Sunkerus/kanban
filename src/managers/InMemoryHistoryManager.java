package managers;

import tasks.*;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    //CustomLinkedList
    public Map<Integer, Node<Task>> historyList = new HashMap<>();

    //реализация CustomLinkedList
    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;

    public void linkLast(Node<Task> node, int key) {
        //метод добавляет задачу в конец списка custom linked list
        if (size == 0) {
            head = node;
        } else {
            tail = node;
        }
        historyList.put(key, node);
        size++;
    }

    @Override
    public void add(Task task) {
        int keyNode = size;
        Node<Task> nodeTask = new Node<>(task);
        if (!historyList.containsValue(nodeTask)) {
            linkLast(nodeTask, keyNode);
            // нужен цикл по получению ключа
        } else {
            for (Map.Entry<Integer, Node<Task>> taskNode : historyList.entrySet()) {
                if (taskNode.getValue().data.equals(task)) {
                    keyNode = taskNode.getKey();
                }
            }
            removeNode(nodeTask);
            linkLast(nodeTask, keyNode);
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
        if (head == node) {
            head = node.next;
        } else if (node.next != null) {
            node.next.prev = node.prev;
        } else if (node.prev != null) {
            node.prev.next = null;
        }
    }

     class Node<Task> {

        private Task data;
        private Node<Task> next;
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
