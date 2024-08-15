package history;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    public HashMap<Integer, Node> history = new HashMap<>();
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
        Node newNode = new Node(task);
        history.put(task.getId(), newNode);
        linkLast(newNode);
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> allTasksInHistory = new ArrayList<>();
        Node thisNode = head;
        while (thisNode != null) {
            allTasksInHistory.add(thisNode.getTask());
            thisNode = thisNode.getNextNode();
        }
        return allTasksInHistory;
    }

    private void linkLast(Node node) {
        if (head == null) {
            head = node;
        } else {
            tail.setNextNode(node);
            node.setLastNode(tail);
        }
        tail = node;
    }

    private void removeNode(Node node) {
        if (node == head) {
            head = node.getNextNode();
            if (head != null) {
                head.setLastNode(null);
            } else {
                tail = null;
            }
        } else if (node == tail) {
            tail = node.getLastNode();
            if (tail != null) {
                tail.setNextNode(null);
            } else {
                head = null;
            }
        } else {
            Node lastNode = node.getLastNode();
            Node nextNode = node.getNextNode();
            lastNode.setNextNode(nextNode);
            nextNode.setLastNode(lastNode);
        }
    }

}
