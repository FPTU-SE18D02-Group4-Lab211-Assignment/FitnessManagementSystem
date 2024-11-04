package service;

import model.Coach;

public interface ICoachService extends Service<Coach> {

    @Override
    Coach findById(String id) throws Exception;

    @Override
    void display();

    @Override
    void add(Coach e);
    
    void delete(Coach e);

    @Override
    void update(Coach e);

    @Override
    void save();

}
