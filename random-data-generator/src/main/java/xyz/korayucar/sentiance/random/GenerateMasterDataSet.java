package xyz.korayucar.sentiance.random;

/**
 * Created by koray on 23/09/17.
 */
public class GenerateMasterDataSet {
    @Parameter(names={"--length", "-l"})
    int length;
    @Parameter(names={"--pattern", "-p"})
    int pattern;

    public static void main(String[] args) {
        parseRequest();

    }
}
