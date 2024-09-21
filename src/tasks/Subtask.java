package tasks;

public class Subtask extends Task {
    private int idOfEpic;


    public Subtask(String name, String description, int idOfEpic) {
        super(name, description);
        this.idOfEpic = idOfEpic;
    }

    public Subtask(String name, String description, int id, StatusOfTask status, int idOfEpic) {
        super(name, description, id, status);
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

    public TypeOfTask getType() {
        return TypeOfTask.SUBTASK;
    }

    public void setIdOfEpic(int idOfEpic) {
        this.idOfEpic = idOfEpic;
    }

    @Override
    public String toString() {
        String stringForSubtask = super.toString();
        return stringForSubtask + "," + idOfEpic;
    }
}


