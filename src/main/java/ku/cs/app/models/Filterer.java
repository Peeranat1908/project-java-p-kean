package ku.cs.app.models;

public interface Filterer<T> {
    boolean filter(T t);
}
