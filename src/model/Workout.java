package model;

public class Workout {

    private String id;
    private String workoutName;
    private String description;
    private int duration; // in minutes
    private String type; // e.g., cardio, strength, flexibility
    private String intensity; // e.g., low, medium, high

    public Workout(String id, String workoutName, String description, int duration, String type, String intensity) {
        this.id = id;
        this.workoutName = workoutName;
        this.description = description;
        this.duration = duration;
        this.type = type;
        this.intensity = intensity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return "Workout Name: " + workoutName + "\n"
                + "Description: " + description + "\n"
                + "Duration: " + duration + " minutes\n"
                + "Type: " + type + "\n"
                + "Intensity: " + intensity + "\n";
    }

}
