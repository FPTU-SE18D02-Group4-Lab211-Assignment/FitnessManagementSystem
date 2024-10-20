package service;

import model.Workout;

public interface IWorkoutService extends Service<Workout> {

    @Override
    Workout findById(String id);

    @Override
    void display();

    @Override
    void add(Workout e);

    @Override
    void update(Workout e);

    @Override
    void save();
}
