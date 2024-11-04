package service;

import model.User;

public interface IUserService extends Service<User> {

    @Override
    User findById(String id);

    @Override
    void display();

    @Override
    void add(User b);
    
    void delete(User b);

    @Override
    void update(User b);

    @Override
    void save();
}
