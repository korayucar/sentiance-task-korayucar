package xyz.korayucar.sentiance.random.generator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by koray on 22/09/17.
 */
public class AlphanumericCharacterGenerator {


    ThreadLocalRandom random;

    @Inject
    AlphanumericCharacterGenerator(ThreadLocalRandom threadLocalRandom){

    }

    private static final char [] ALPHANUMERIC =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    static Stream<Char> generator(){
        new Random().ints(123)
    }



}
