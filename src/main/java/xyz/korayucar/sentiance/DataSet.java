package xyz.korayucar.sentiance;

import java.util.Map;
import java.util.Objects;

/**
 * Created by koray on 24/09/17.
 */
public class DataSet {

    Map<String, Integer> dataSizes;
    int fileSizeInMB;

    public void incrementDataSize(String folderName, int delta){
        if(delta <=0)
            throw new IllegalArgumentException("delta value :" + delta+" is not an increment");
        Integer oldValue = dataSizes.get(folderName);
        if(oldValue == null)
            oldValue = 0;
        dataSizes.put(folderName, oldValue+delta);

    }

    public void incrementDataSize(DataSet other){
        Objects.nonNull(other);
        other.getDataSizes().forEach(this::incrementDataSize);
    }

    public DataSet(Map<String, Integer> dataSizes) {
        this.dataSizes = dataSizes;
    }

    public DataSet(Map<String, Integer> dataSizes, int fileSizeInMB) {
        this.dataSizes = dataSizes;
        this.fileSizeInMB = fileSizeInMB;
    }

    public Map<String, Integer> getDataSizes() {
        return dataSizes;
    }

    public void setDataSizes(Map<String, Integer> dataSizes) {
        this.dataSizes = dataSizes;
    }

    public int getFileSizeInMB() {
        return fileSizeInMB;
    }

    public void setFileSizeInMB(int fileSizeInMB) {
        this.fileSizeInMB = fileSizeInMB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSet that = (DataSet) o;
        return Objects.equals(fileSizeInMB, that.fileSizeInMB) &&
                Objects.equals(dataSizes, that.dataSizes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataSizes, fileSizeInMB);
    }
}
