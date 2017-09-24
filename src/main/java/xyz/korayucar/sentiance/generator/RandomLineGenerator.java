package xyz.korayucar.sentiance.generator;

import java.util.stream.Stream;

/**
 * Created by koray on 23/09/17.
 */
@FunctionalInterface
public interface RandomLineGenerator {
    Stream<String> lines();
}
