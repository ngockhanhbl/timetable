package sample;

import java.time.LocalDate;

public class SubjectModel {
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubjectModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
