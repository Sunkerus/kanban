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

        public void linkLast (Node<Task> node, int key) {
            //метод добавляет задачу в конец списка custom linked list
             if (size == 0) {
                 head = node;
             } else {
                 tail = node;
             }
            size++;
            historyList.put (size,node);
        }

    @Override
    public void add(Task task) {
       Node <Task> nodeTask = new Node<>(task);
       if (historyList.containsValue(nodeTask)) {
           int keyNode = size;
           // нужен цикл по получению ключа
           for(Map.Entry<Integer, Node<Task>> taskNode: historyList.entrySet()) {
               if(taskNode.getValue().data.equals(task)){
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
        for (Node<Task> value: historyList.values()) {
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

    public void  removeNode(Node node) {
        if (head == null || node == null){
        } else if (head == node) {
            head = node.next;
        } else if (node.next != null) {
            node.next.prev = node.prev;
        } else if (node.prev != null) {
            node.prev.next = node.next;
        }

    }
    static class Node <Task> {

        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}
