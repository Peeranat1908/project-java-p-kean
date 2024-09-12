package ku.cs.app.services;


import java.io.IOException;

public interface DataSource<T> {
    T readData();
    void writeData(T t) throws IOException;
}
