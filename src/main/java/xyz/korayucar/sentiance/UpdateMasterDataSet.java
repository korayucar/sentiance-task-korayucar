package xyz.korayucar.sentiance;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.korayucar.sentiance.generator.AlphanumericStringGenerator;
import xyz.korayucar.sentiance.generator.RandomLineGenerator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by koray on 23/09/17.
 */
public class UpdateMasterDataSet {

    protected static final int CHARS_PER_LINE = 2000;

    private Logger logger = LogManager.getLogger(UpdateMasterDataSet.class);

    @Parameter(required = true)
    String masterDataSetLocation;

    @Parameter(required = true,names="-data")
    String dataFolders;

    public static void main(String... args) throws InterruptedException, IOException {
        LogManager.getRootLogger().info("Running update task with args:" + Arrays.toString(args));
        UpdateMasterDataSet generateMasterDataSet = new UpdateMasterDataSet();
        JCommander.newBuilder().addObject(generateMasterDataSet).build().parse(args);
        generateMasterDataSet.run();
    }

    private void run() throws InterruptedException, IOException {
        DataSet currentState = MetadataHandler.readCurrentMetaData(masterDataSetLocation);
        DataSet update = DataDefinitionCommandParser.parseCommand(dataFolders,currentState.getFileSizeInMB());
        DataSet finalState = new DataSet(currentState);
        finalState.incrementDataSize(update);
        MetadataHandler.writeAimedDataSetStatusToMetadata(finalState, masterDataSetLocation);
        generateData(currentState, update);
    }


    private void generateData(DataSet currentState, DataSet update) throws InterruptedException {
        new DefaultFileAppendJobGeneratorBuilder()
                .setCalculator(new AlphanumericUtf8DataSizeCalculator())
                .setGenerator(createRandomLineGenerator())
                .setEncoding(SupportedEncoding.ALPHANUMERIC_UTF_8)
                .setMasterDataSet(Paths.get(masterDataSetLocation).toFile())
                .createDefaultFileAppendJobGenerator()
                .getTaskStream(currentState, update)
                .parallel()
                .peek(t -> logger.info("adding new task to executor service :" + t.toString()))
                .forEach(FileAppenderTask::run);
    }

    private RandomLineGenerator createRandomLineGenerator() {
        return new AlphanumericStringGenerator(new Random(), CHARS_PER_LINE );
    }





}
