package xyz.korayucar.sentiance;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import xyz.korayucar.sentiance.generator.AlphanumericStringGenerator;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by koray on 25/09/17.
 */
public class FileAppenderTaskTest {

    @Test
    public void run_SimpleTask_CreatesDirectories() throws Exception {
        FileUtils.deleteQuietly(Paths.get("test/test").toFile());
        new FileAppenderTaskBuilder().setCalculator(new AlphanumericUtf8DataSizeCalculator())
                .setTargetIncrementInBytes(5000)
                .setGenerator(new AlphanumericStringGenerator(new Random(),100))
                .setEncoding(SupportedEncoding.ALPHANUMERIC_UTF_8)
                .setDestinationFile(Paths.get("test/test/test/fileAppenderTaskTest.txt").toFile())
                .createFileAppenderTask().run();
        File file = Paths.get("test/test/test/fileAppenderTaskTest.txt").toFile();
        assertTrue(file.exists());
    }

    @Test
    public void run_SimpleTask_CreatesFile() throws Exception {
        FileUtils.deleteQuietly(Paths.get("test/fileAppenderTaskTest.txt").toFile());
        new FileAppenderTaskBuilder().setCalculator(new AlphanumericUtf8DataSizeCalculator())
                .setTargetIncrementInBytes(5000)
                .setGenerator(new AlphanumericStringGenerator(new Random(), 100))
                .setEncoding(SupportedEncoding.ALPHANUMERIC_UTF_8)
                .setDestinationFile(Paths.get("test/fileAppenderTaskTest.txt").toFile())
                .createFileAppenderTask().run();
        File file = Paths.get("test/fileAppenderTaskTest.txt").toFile();
        assertTrue(file.exists());
    }

    @Test
    public void run_SimpleTask_WritesExpectedAmountOfData() throws Exception {
        String fileName = "test/xxfileAppenderTaskTest.txt";
        FileUtils.deleteQuietly(Paths.get(fileName).toFile());
        new FileAppenderTaskBuilder().setCalculator(new AlphanumericUtf8DataSizeCalculator())
                .setTargetIncrementInBytes(5000)
                .setGenerator(new AlphanumericStringGenerator(new Random(),100))
                .setEncoding(SupportedEncoding.ALPHANUMERIC_UTF_8)
                .setDestinationFile(Paths.get(fileName).toFile())
                .createFileAppenderTask().run();
        File file = Paths.get(fileName).toFile();
        String content = FileUtils.readFileToString(file, Charset.forName(SupportedEncoding.ALPHANUMERIC_UTF_8.getCharsetName()));
        assertEquals(5050, content.length());
    }
}