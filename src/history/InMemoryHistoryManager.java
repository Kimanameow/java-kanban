package history;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> history = new HashMap<>();

    private Node head;
    private Node tail;


    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
        history.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        if (node != null) {
            removeNode(node);
            history.remove(id);
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> allTasksInHistory = new ArrayList<>();
        Node thisNode = head;
        while (thisNode != null) {
            allTasksInHistory.add(thisNode.task);
            thisNode = thisNode.nextNode;
        }
        return allTasksInHistory;
    }

    private void linkLast(Task task) {
        Node node = new Node(task);
        if (head == null) {
            head = node;
        } else {
            tail.nextNode = node;
            node.lastNode = tail;
        }
        tail = node;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node == head) {
            head = node.nextNode;
            if (head != null) {
                head.lastNode = null;
            } else {
                tail = null;
            }
        } else if (node == tail) {
            tail = node.lastNode;
            if (tail != null) {
                tail.nextNode = null;
            } else {
                head = null;
            }
        } else {
            Node lastNode = node.lastNode;
            Node nextNode = node.nextNode;
            lastNode.nextNode = nextNode;
            nextNode.lastNode = lastNode;
        }
    }


    private static class Node {
        private Task task;
        private Node lastNode;
        private Node nextNode;


        public Node(Task task) {
            this.task = task;
        }
    }
}