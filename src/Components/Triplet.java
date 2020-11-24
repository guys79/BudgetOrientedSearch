package Components;

/**
 * This class represents a triplet
 *
 * @param <T> - A generic class
 * @param <U> - A generic class
 * @param <V> - A generic class
 */
public class Triplet<T, U, V> {

    private final T first;//The first value
    private final U second;//The second value
    private final V third;//The third value

    /**
     * The constructor of the class
     *
     * @param first  - The first value
     * @param second - The second value
     * @param third  - The third value
     */
    public Triplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * This function will return the first value
     *
     * @return - The first value
     */
    public T getFirst() {
        return first;
    }

    /**
     * This function will return the second value
     *
     * @return - The second value
     */
    public U getSecond() {
        return second;
    }


    /**
     * This function will return the third value
     *
     * @return - The third value
     */
    public V getThird() {
        return third;
    }
}