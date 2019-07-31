package pointer.nested;

public interface Iterator<T> {
    boolean  hasNext();
    T next();
}

interface Collect<T> {
    void add(T obj);
    void add(int index, T obj);
    void remove(T obj);
    T get(int index);
    int size();
    Iterator<T> getIterator(String type);
}
