package xyz.korayucar.sentiance;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.korayucar.sentiance.generator.AlphanumericStringGenerator;
import xyz.korayucar.sentiance.generator.RandomLineGenerator;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by koray on 23/09/17.
 */
public class GenerateMasterDataSet extends UpdateMasterDataSet{

    private Logger logger = LogManager.getLogger(GenerateMasterDataSet.class);

    @Parameter(required = true , names="-size")
    int maxFileSizeInMB;


    public static void main(String... args) throws InterruptedException {
        LogManager.getRootLogger().info("Running with args:" + Arrays.toString(args));
        GenerateMasterDataSet generateMasterDataSet = new GenerateMasterDataSet();
        JCommander.newBuilder().addObject(generateMasterDataSet).build().parse(args);
        generateMasterDataSet.run();
    }

    private void run() throws InterruptedException {
        MetadataHandler.checkMasterDataDirectory(masterDataSetLocation);
        validateFileSize();
        DataSet aimedDataStatus = DataDefinitionCommandParser.parseCommand(dataFolders, maxFileSizeInMB);
        MetadataHandler.writeAimedDataSetStatusToMetadata(aimedDataStatus, masterDataSetLocation);
        generateData(aimedDataStatus);
    }

    private void generateData(DataSet aimedDataStatus) throws InterruptedException {
        new DefaultFileAppendJobGeneratorBuilder()
                .setCalculator(new AlphanumericUtf8DataSizeCalculator())
                .setGenerator(createRandomLineGenerator())
                .setEncoding(SupportedEncoding.ALPHANUMERIC_UTF_8)
                .setMasterDataSet(Paths.get(masterDataSetLocation).toFile())
                .createDefaultFileAppendJobGenerator()
                .getTaskStream(new DataSet(new HashMap<>(), aimedDataStatus.getFileSizeInMB()), aimedDataStatus)
                .parallel()
                .peek(t -> logger.info("adding new task to executor service :" + t.toString()))
                .forEach(FileAppenderTask::run);
    }

    private void validateFileSize() {
        if(maxFileSizeInMB <=0)
            throw new IllegalArgumentException("file size must be positive");
    }

    private RandomLineGenerator createRandomLineGenerator() {
        return new AlphanumericStringGenerator(new Random(), CHARS_PER_LINE );
    }



}
