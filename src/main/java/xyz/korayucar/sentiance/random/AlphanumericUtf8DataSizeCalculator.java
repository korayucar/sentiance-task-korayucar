package xyz.korayucar.sentiance.random;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by koray on 24/09/17.
 */
public class AlphanumericUtf8DataSizeCalculator implements DataSizeCalculator
{
    @Override
    public BigDecimal calculateApproximateSizeInEncoding(String data, DataSizeUnit unit, SupportedEncoding encoding) throws UnsupportedEncodingException {
        Objects.nonNull(data);
        Objects.nonNull(encoding);
        if(SupportedEncoding.ALPHANUMERIC_UTF_8.equals(encoding))
            return new BigDecimal(data.length() ).divide(new BigDecimal( unit.getBytesPerUnit()), BigDecimal.ROUND_UNNECESSARY);
        else
            throw new UnsupportedEncodingException("unrecognized encoding type");
    }
}
