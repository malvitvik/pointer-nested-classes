package pointer.nested;

import java.lang.reflect.Array;

public class Collection<T> implements Collect<T> {
    private T[] array;
    private int currentIndex;

    public Collection(T... array) {
        this.array = array;
        currentIndex = array.length;
    }

    @Override
    public void add(T obj) {
        add(currentIndex, obj);
    }

    @Override
    public void add(int index, T obj) {
        if (index > array.length || index < 0) {
            throw new IndexOutOfBoundsException("");
        }

        if (index < array.length) {
            array[index] = obj;
            return;
        }

        currentIndex = index + 1;
        T[] arr = (T[]) Array.newInstance(array[0].getClass(), currentIndex);
        System.arraycopy(array, 0,arr,0,array.length);
        arr[index] = obj;
        array = arr;
    }

    @Override
    public void remove(T obj) {
        for (int i = 0; i < array.length; i++) {
            if (!array[i].equals(obj))
                continue;

            while (i < array.length - 1){
                array[i++] = array[i];
            }
            array[i++] = null;
        }
        currentIndex--;
    }

    @Override
    public T get(int index) {
        if (index >= array.length || array[index] == null)
            throw new IndexOutOfBoundsException("Index is bigger than count of elements in collection");

        return array[index];
    }

    @Override
    public int size() {
        return currentIndex;
    }

    @Override
    public Iterator<T> getIterator(String type){
        switch (type){
            case "directThroughFourElements":
                class Local implements Iterator<T> {
                    private int index;

                    @Override
                    public boolean hasNext() {
                        return index < currentIndex;
                    }

                    @Override
                    public T next() {
                        T value =  array[index];
                        index += 5;
                        return (int)value % 2 == 0 ? value : null;
                    }
                }

                return new Local();
            case "reverseThroughTwoElements":
                return new Iterator<>() {
                    private int index = currentIndex - 1;

                    @Override
                    public boolean hasNext() {
                        return index >= 0;
                    }

                    @Override
                    public T next() {
                        T o = array[index];
                        index -= 3;
                        return (((int) o) % 2 == 1) ? o : null;
                    }
                };
            case "reverseOddPair":
                return new ReverseOddPairIterator(this);
            case "reverseByPair":
                return new ReverseByPairIndexIterator();
            case "directWithZeroOdds":
                return new DirectWithZeroOddsIterator();
            case "direct":
            default:
                 return new Iterator<T>() {
                    int index;

                    @Override
                    public boolean hasNext() {
                        return index < currentIndex;
                    }

                    @Override
                    public T next() {
                        return array[index++];
                    }
                };
        }
    }

    private class DirectWithZeroOddsIterator implements Iterator {
        private int index;

        @Override
        public boolean hasNext() {
            return index < currentIndex;
        }

        @Override
        public Object next() {
            return index++ % 2 == 0 ?  array[index -1] : 0;

        }
    }

    private class ReverseByPairIndexIterator implements Iterator<T> {
        private int index = currentIndex - 1;

        @Override
        public boolean hasNext() {
            return index >= 0;
        }

        @Override
        public T next() {
            T o = array[index];
            index -= 2;
            return o;
        }
    }

    private static class ReverseOddPairIterator implements Iterator {

        private final Collection collection;
        private int currentIndex = 0;

        public ReverseOddPairIterator(Collection collection) {
            this.collection = collection;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < collection.currentIndex;
        }

        @Override
        public Object next() {
            Integer value = (int) collection.array[currentIndex];
            currentIndex += 2;
            return (value % 2 == 0 ?  ++value : value);
        }
    }
}