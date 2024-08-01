package history;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> history = new ArrayList<>();
    private final static int MAX_SIZE_OF_HISTORY = 10;

    @Override
    public void add(Task task) {
        if (history.size() >= MAX_SIZE_OF_HISTORY) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);

    }
}
