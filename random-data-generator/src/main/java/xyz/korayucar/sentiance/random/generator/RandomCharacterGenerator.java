package xyz.korayucar.sentiance.random.generator;

/**
 * Created by koray on 23/09/17.
 */
@FunctionalInterface
public interface RandomCharacterGenerator {
    Stream<Char> getNewStream();
}
