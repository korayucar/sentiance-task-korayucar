package xyz.korayucar.sentiance.random;

import java.util.Map;
import java.util.Objects;

/**
 * Created by koray on 24/09/17.
 */
public class DataSetStatus {

    Map<String, Integer> dataSizes;

    public void incrementDataSize(String folderName, int delta){
        if(delta <=0)
            throw new IllegalArgumentException("delta value :" + delta+" is not an increment");
        Integer oldValue = dataSizes.get(folderName);
        if(oldValue == null)
            oldValue = 0;
        dataSizes.put(folderName, oldValue+delta);

    }

    public void incrementDataSize( DataSetStatus other){
        Objects.nonNull(other);
        other.getDataSizes().forEach(this::incrementDataSize);
    }

    public DataSetStatus(Map<String, Integer> dataSizes) {
        this.dataSizes = dataSizes;
    }

    public Map<String, Integer> getDataSizes() {
        return dataSizes;
    }

    public void setDataSizes(Map<String, Integer> dataSizes) {
        this.dataSizes = dataSizes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSetStatus that = (DataSetStatus) o;
        return Objects.equals(dataSizes, that.dataSizes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataSizes);
    }
}
