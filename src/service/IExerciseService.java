package service;

import model.Exercise;

public interface IExerciseService extends Service<Exercise> {

    @Override
    Exercise findById(String id);

    @Override
    void display();

    @Override
    void add(Exercise e);

    void delete(Exercise e);

    @Override
    void update(Exercise e);

    @Override
    void save();

}
