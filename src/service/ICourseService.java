package service;

import model.Course;

public interface ICourseService extends Service<Course> {

    @Override
    Course findById(String id);

    @Override
    void display();

    @Override
    void add(Course c);
    
    void delete(Course c);

    @Override
    void update(Course c);

    @Override
    void save();
}
