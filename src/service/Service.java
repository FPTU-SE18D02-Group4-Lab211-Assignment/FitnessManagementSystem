package service;

public interface Service<C> {

    C findById(String id) throws Exception;

    void display();

    void add(C c);
    
    void update(C c);

    void save();
}
