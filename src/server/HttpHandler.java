package server;

import com.google.gson.Gson;
import history.HistoryManager;
import taskmanager.Managers;
import taskmanager.TaskManager;

public abstract interface HttpHandler {
    TaskManager manager = Managers.getDefault();
    Gson gson = new Gson();
    HistoryManager historyManager = Managers.getDefaultHistory();

}
