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

        public void linkLast (Node<Task> node) {
            //метод добавляет задачу в конец списка custom linked list
             if (size == 0) {
                 head = node;
             } else {
                 tail = node;
             }
            size++;
            historyList.put (size,node);
        }

        public ArrayList<Task> getTasks () {
            ArrayList<Task> values = new ArrayList<>();
            for (Node<Task> value: historyList.values()) {
                values.add(value.data);
            }
            return values;
          }

    @Override
    public void add(Task task) {        //add task and remove if task already existing

    }

    @Override
    public List<Task> getHistory() {    //method must returned history list
        return null;
    }

    @Override
    public void remove(int id) {        //method must remove task from HashMap historyList

    }

    class Node <Task> {

        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        public void  removeNode(Node node) {

        }
    }
}
