package xyz.korayucar.sentiance.random.generator;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by koray on 24/09/17.
 */
public class AlphanumericStringGeneratorTest {

    @Test
    public void lines_SingleLine_OfExpectedLength() throws Exception {
        RandomLineGenerator generator0 = new AlphanumericStringGenerator(ThreadLocalRandom.current() , 0);
        Supplier<RuntimeException> runtimeExceptionSupplier = () -> new RuntimeException("Cannot generate line");
        assertEquals(0, generator0.lines().findFirst().orElseThrow(runtimeExceptionSupplier).length());
        RandomLineGenerator generator = new AlphanumericStringGenerator(ThreadLocalRandom.current() , 20);
        assertEquals(20, generator.lines().findFirst().orElseThrow(runtimeExceptionSupplier).length());
        RandomLineGenerator generator2 = new AlphanumericStringGenerator(ThreadLocalRandom.current() , 40);
        assertEquals(40, generator2.lines().findFirst().orElseThrow(runtimeExceptionSupplier).length());
    }

    @Test
    public void lines_SingleLine_AlphanumericOnly() throws Exception {
        RandomLineGenerator generator = new AlphanumericStringGenerator(ThreadLocalRandom.current() , 20);
        for(int i = 0; i < 20 ; i ++ ) {
            String line = generator.lines().findFirst().orElseThrow(() -> new RuntimeException("Cannot generate line"));
            for(int j = 0 ; j < line.length() ; j++)
            {
                char charAtIndex = line.charAt(j);
                boolean isAlphaNumeric =(charAtIndex >= 'a' && charAtIndex <= 'z' ) ||
                                (charAtIndex >= 'A' && charAtIndex <= 'Z' ) ||
                                (charAtIndex >= '0' && charAtIndex <= '9' )  ;
                assertTrue(isAlphaNumeric);
            }
        }
    }

    @Test(timeout = 1000)
    public void lines_PerformanceTest() throws Exception {
        RandomLineGenerator generator = new AlphanumericStringGenerator(ThreadLocalRandom.current() , 20);
        generator.lines().limit(50000).count();
    }


    @Test(expected = IllegalArgumentException.class)
    public void lines_FalseInput_ExceptionThrown() throws Exception {
        new AlphanumericStringGenerator(ThreadLocalRandom.current() , -1);
    }

    @Test(expected = NullPointerException.class)
    public void lines_NullRandom_ExceptionThrown() throws Exception {
        new AlphanumericStringGenerator(null , 0);
    }
}