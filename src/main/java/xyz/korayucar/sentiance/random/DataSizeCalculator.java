package xyz.korayucar.sentiance.random;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Created by koray on 24/09/17.
 */
public interface DataSizeCalculator {

    BigDecimal calculateApproximateSizeInEncoding( String data, DataSizeUnit unit,  SupportedEncoding encoding) throws UnsupportedEncodingException;

}
