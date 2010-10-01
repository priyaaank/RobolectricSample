package com.pivotallabs.expects;

import com.pivotallabs.greatexpectations.*;

import java.util.Date;

public class GreatExpectations {
    public static <T> BaseExpectation<T, ?> expect(T actual) {
        return new BaseExpectation<T, BaseExpectation<T, ?>>(actual)
                .setInvertedInstance(new BaseExpectation<T, BaseExpectation<T, ?>>(actual));
    }

    public static <T extends Comparable<T>> ComparableExpectation<T> expect(T actual) {
        return new ComparableExpectation<T>(actual)
                .setInvertedInstance(new ComparableExpectation<T>(actual));
    }

    public static BooleanExpectation expect(Boolean actual) {
        return new BooleanExpectation(actual)
                .setInvertedInstance(new BooleanExpectation(actual));
    }

    public static <T> IterableExpectation<T> expect(Iterable<T> actual) {
        return new IterableExpectation<T>(actual)
                .setInvertedInstance(new IterableExpectation<T>(actual));
    }

    public static StringExpectation expect(String actual) {
        return new StringExpectation(actual)
                .setInvertedInstance(new StringExpectation(actual));
    }

    public static DateExpectation expect(Date actual) {
        return new DateExpectation(actual)
                .setInvertedInstance(new DateExpectation(actual));
    }


}
