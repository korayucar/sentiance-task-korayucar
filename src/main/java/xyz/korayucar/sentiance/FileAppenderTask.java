package xyz.korayucar.sentiance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.korayucar.sentiance.generator.RandomLineGenerator;

import java.io.File;
import java.io.IOException;

/**
 * Created by koray on 24/09/17.
 */
public class FileAppenderTask implements Runnable {


    Logger logger = LogManager.getLogger(FileAppenderTask.class);

    File destinationFile;
    long targetIncrementInBytes;
    SupportedEncoding encoding;
    private long currentDataInBytes;
    DataSizeCalculator calculator;
    RandomLineGenerator generator;


    @Override
    public void run() {
        createFileIfNotExist();

    }

    private void createFileIfNotExist() {
        try {
            destinationFile.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create destination file " + destinationFile.toString());
        }
    }
}
