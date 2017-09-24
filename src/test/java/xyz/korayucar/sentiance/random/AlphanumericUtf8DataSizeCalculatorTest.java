package xyz.korayucar.sentiance.random;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Created by koray on 24/09/17.
 */
public class AlphanumericUtf8DataSizeCalculatorTest {

    @Test
    public void calculateApproximateSizeInEncoding_SimpleString_ReturnsSizeWithinEpsilon() throws Exception {
        AlphanumericUtf8DataSizeCalculator calculator = new AlphanumericUtf8DataSizeCalculator();
        assertEquals(3.0, calculator.calculateApproximateSizeInEncoding("abc",DataSizeUnit.BYTE,SupportedEncoding.ALPHANUMERIC_UTF_8).doubleValue(), 0.01);
        assertEquals(0, calculator.calculateApproximateSizeInEncoding("",DataSizeUnit.BYTE,SupportedEncoding.ALPHANUMERIC_UTF_8).doubleValue(), 0.01);
        assertEquals(11, calculator.calculateApproximateSizeInEncoding("xxx yyy zzz",DataSizeUnit.BYTE,SupportedEncoding.ALPHANUMERIC_UTF_8).doubleValue(), 0.01);
    }
    @Test(expected =  UnsupportedEncodingException.class)
    public void calculateApproximateSizeInEncoding_NullEncoding_ThrowsException() throws Exception {
        AlphanumericUtf8DataSizeCalculator calculator = new AlphanumericUtf8DataSizeCalculator();
        calculator.calculateApproximateSizeInEncoding("asd", DataSizeUnit.BYTE, null);
    }
    @Test(expected =  NullPointerException.class)
    public void calculateApproximateSizeInEncoding_NullString_ThrowsException() throws Exception {
        AlphanumericUtf8DataSizeCalculator calculator = new AlphanumericUtf8DataSizeCalculator();
        calculator.calculateApproximateSizeInEncoding(null,DataSizeUnit.BYTE,SupportedEncoding.ALPHANUMERIC_UTF_8);
    }
    @Test(expected =  NullPointerException.class)
    public void calculateApproximateSizeInEncoding_NullUnit_ThrowsException() throws Exception {
        AlphanumericUtf8DataSizeCalculator calculator = new AlphanumericUtf8DataSizeCalculator();
        calculator.calculateApproximateSizeInEncoding("asd", null,SupportedEncoding.ALPHANUMERIC_UTF_8);
    }
}