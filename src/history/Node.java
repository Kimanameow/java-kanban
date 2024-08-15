package history;

import tasks.Task;

public class Node {
    Task task;
    private Node lastNode;
    private Node nextNode;

    public Node getLastNode() {
        return lastNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setLastNode(Node lastNode) {
        this.lastNode = lastNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public Node(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}