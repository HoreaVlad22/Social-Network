package utils;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class for converting {@link Iterable} instances into sequential {@link Stream}s.
 */
public class StreamUtils {

    /**
     * Converts an {@code Iterable} into a sequential {@code Stream}.
     * @param iterable the iterable to convert
     * @param <T> the type of elements
     * @return a sequential {@code Stream} over the elements of the iterable
     */
    public static <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}