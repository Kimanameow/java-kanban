package tasks;

public class Subtask extends Task {
    private int idOfEpic;

    public Subtask(String name, String description, int idOfEpic) {
        super(name, description);
        this.idOfEpic = idOfEpic;
    }

    public Subtask(String name, int id, int idOfEpic) {
        super(name, id);
        this.idOfEpic = idOfEpic;
    }

    public Subtask(String name, int id) {
        super(name, id);
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

    public void setIdOfEpic(int idOfEpic) {
        this.idOfEpic = idOfEpic;
    }
}

