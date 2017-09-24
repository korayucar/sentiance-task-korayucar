package xyz.korayucar.sentiance;

/**
 * Created by koray on 24/09/17.
 */
public enum DataSizeUnit {

    BYTE(1l),
    KILO_BYTE(1024l),
    MAGA_BYTE(1024l*1024l);


    private final long bytes;

    DataSizeUnit(long l) {
        bytes = l;
    }

    public long getBytesPerUnit() {
        return bytes;
    }
}
