package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyList = new ArrayList<>();

    //CustomLinkedList
    public Map<Integer, Node> HashTable = new HashMap<>(); //двусвязный список?

    //метод для Hash table

        //реализация CustomLinkedList
        public Node<Task> head;
        public Node<Task> tail;
        private int size = 0;

        public void linkLast (Task task) {

        }

        public ArrayList<Task> getTasks () {

            return null; //return custoom linked list -> array list <task>

            /*
            """""""""""""""""""""""""""""""""""""""""""
            if (task.equals task Custom linked list )  {
               Node.removeNode(Node node);
               Node.add (task);
               HashTable.add(id);
            }else {
            }
            */
        }

    class Node <T> {

        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        public void  removeNode(Node node) {

        }
    }

    @Override
    public void add(Task task) {
        historyList.add(0, task);
        if (historyList.size() > 10) {
            historyList.remove(10);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void remove(int id) {
        historyList.remove(id);
    }

}
