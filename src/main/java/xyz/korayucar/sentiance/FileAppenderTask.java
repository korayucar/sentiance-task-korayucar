package xyz.korayucar.sentiance;

import org.apache.commons.io.FileUtils;
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

    public FileAppenderTask(File destinationFile, long targetIncrementInBytes, SupportedEncoding encoding, DataSizeCalculator calculator, RandomLineGenerator generator) {
        this.destinationFile = destinationFile;
        this.targetIncrementInBytes = targetIncrementInBytes;
        this.encoding = encoding;
        this.calculator = calculator;
        this.generator = generator;
    }

    @Override
    public void run() {
        createFileIfNotExist();
        generator.lines()
                .peek(s -> currentDataInBytes += 1 + calculator.calculateApproximateSizeInEncoding(s, DataSizeUnit.BYTE, encoding).longValue())
                .peek(l -> {
                    try {
                        FileUtils.writeStringToFile(destinationFile, l + "\n", encoding.getCharsetName(), true);
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to append to destination file", e);
                    }
                })
                .allMatch(e -> currentDataInBytes < targetIncrementInBytes);
        logger.info("Finished apending " + targetIncrementInBytes + " bytes to " +destinationFile.getAbsolutePath());
    }

    private void createFileIfNotExist() {
        try {
            destinationFile.getParentFile().mkdirs();
            destinationFile.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create destination file " + destinationFile.toString());
        }
    }

    @Override
    public String toString() {
        return "FileAppenderTask{" +
                "destinationFile=" + destinationFile +
                ", targetIncrementInBytes=" + targetIncrementInBytes +
                '}';
    }
}
