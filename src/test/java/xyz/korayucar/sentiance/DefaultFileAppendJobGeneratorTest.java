package xyz.korayucar.sentiance;

import org.junit.Test;
import xyz.korayucar.sentiance.generator.AlphanumericStringGenerator;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by koray on 25/09/17.
 */
public class DefaultFileAppendJobGeneratorTest {

    @Test
    public void getetTasksInFolder_SingleDirectory_StreamsSingleTaskWith1MBIncrement() throws Throwable {
        DefaultFileAppendJobGenerator generator = getSimpleJobGenerator();
        Stream tasks = generator.getTasksInFolder("xx", new DataSet(Collections.singletonMap("xx", 2), 10), new DataSet(Collections.singletonMap("xx", 1), 10));
        FileAppenderTask task = (FileAppenderTask) tasks.findFirst().orElseThrow(() -> new RuntimeException("test failed."));
        assertEquals(DataSizeUnit.MAGA_BYTE.getBytesPerUnit(),task.targetIncrementInBytes);
        assertTrue(task.destinationFile.toString().contains("file1"));
    }

    @Test
    public void getetTasksInFolder_UpdateNewDirectory_StreamsSingleTaskWith1MBIncrement() throws Throwable {
        DefaultFileAppendJobGenerator generator = getSimpleJobGenerator();
        Stream tasks = generator.getTasksInFolder("yy", new DataSet(Collections.singletonMap("xx", 2), 10), new DataSet(Collections.singletonMap("yy", 1), 10));
        FileAppenderTask task = (FileAppenderTask) tasks.findFirst().orElseThrow(() -> new RuntimeException("test failed."));
        assertEquals(DataSizeUnit.MAGA_BYTE.getBytesPerUnit(),task.targetIncrementInBytes);
        assertTrue(task.destinationFile.getAbsolutePath().contains("file1"));
        assertTrue(task.destinationFile.getAbsolutePath().contains("yy"));
    }

    private DefaultFileAppendJobGenerator getSimpleJobGenerator() {
        return new DefaultFileAppendJobGeneratorBuilder().setCalculator(new AlphanumericUtf8DataSizeCalculator())
                .setGenerator(new AlphanumericStringGenerator(new Random(),100))
                .setEncoding(SupportedEncoding.ALPHANUMERIC_UTF_8)
                .setMasterDataSet(Paths.get("test/jobgenerator/t1").toFile())
                .createDefaultFileAppendJobGenerator();
    }
}