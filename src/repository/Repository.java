package repository;

import java.io.File;

public interface Repository<T, C> {
    final String path = new File("src").getAbsolutePath();
    
    C readFile();
    
    void writeFile(C entities);
}
