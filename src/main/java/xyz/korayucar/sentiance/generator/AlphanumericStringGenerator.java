package xyz.korayucar.sentiance.generator;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by koray on 22/09/17.
 */
public class AlphanumericStringGenerator implements RandomLineGenerator{

    private static final String [] ALPHANUMERIC =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("");

    private int length;
    Random random;

    public AlphanumericStringGenerator(Random random, int length){
        if(length < 0)
            throw  new IllegalArgumentException("length cannot be negative.");
        Objects.requireNonNull(random);
        this.random = random;
        this.length = length;
    }

    @Override
    public Stream<String> lines() {
        return Stream.generate(generateNextLine());
    }

    private Supplier<String> generateNextLine() {
        return ()-> random.ints(length, 0, ALPHANUMERIC.length).mapToObj(i -> ALPHANUMERIC[i]).collect(Collectors.joining());
    }


}
