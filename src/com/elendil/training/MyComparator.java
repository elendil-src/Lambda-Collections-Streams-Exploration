package com.elendil.training;


import java.util.Comparator;
import java.util.function.Function;

@FunctionalInterface
public interface MyComparator<T> extends Comparator<T> {

    /**
     * Compares two objects
     * @param t1 primary object to be compared
     * @param t2 secondary object to be compared
     * @return >0 if t1 is > t2, <0 if t2 is > t1, 0 if the same
     */
    int compare(T t1, T t2);


    /**
     * Returns a comparator that calls two functions that implements Function and Comparable on two given object
     * @param f function that apply() will retrieve the value to be compared
     * @param <U> ???
     * @return returns a comparator for type U
     */
//  static <U> MyComparator<U> comparing(Function<U, Comparable> f)
    static <T,U extends Comparable<? super U>> MyComparator<T>
     comparing(Function<? super T,? extends U> f)
     {
        return (p1, p2) -> f.apply(p1).compareTo(f.apply(p2));
     }

    /**
     * Returns a comparator that can  by two attributes, in order
     * @param cmp comparator that compares by second attribute
     * @return comparator that compares using two attributes
     */
    default MyComparator<T> thenComparing( MyComparator<T> cmp) {
        return (p1, p2) -> compare(p1, p2) == 0 ? cmp.compare(p1, p2) : compare(p1, p2);
    }

    /**
     * Returns a comparator that can  by two attributes, in order
     * @param f method reference  that compares by second attribute
     * @return comparator that compares using two attributes,
     */
//    default MyComparator<T> thenComparing( Function<T,  Comparable> f) {
//        return thenComparing(comparing(f));
//    }
    default <U extends Comparable<? super U>>
    Comparator<T> thenComparing(Function<? super T, ? extends U> f) {
        return thenComparing(comparing(f));
    }


}



