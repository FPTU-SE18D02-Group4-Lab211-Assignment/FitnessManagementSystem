package repository;

import java.util.ArrayList;
import model.User;

public interface IUserRepository extends Repository<User, ArrayList<User>> {
    final String userPath = "\\data\\user.csv";
    
    @Override
    public ArrayList<User> readFile();
    
    @Override
    public void writeFile(ArrayList<User> users);
}
