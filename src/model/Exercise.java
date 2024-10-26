package model;

public class Exercise {

    private String id, name, detail;
    private int duration;

    public Exercise() {
    }

    public Exercise(String id, String name, String detail, int duration) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-25s | %-50s | %-6s |",
                id,
                name,
                detail,
                duration
        );
    }
}
