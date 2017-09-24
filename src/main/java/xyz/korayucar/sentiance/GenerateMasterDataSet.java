package xyz.korayucar.sentiance;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.korayucar.sentiance.generator.AlphanumericStringGenerator;
import xyz.korayucar.sentiance.generator.RandomLineGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by koray on 23/09/17.
 */
public class GenerateMasterDataSet {
    private static final String PARAMETER_DELIMETER = ",";
    private static int NUMBER_OF_THREADS = 10;
    private static final int CHARS_PER_LINE = 2000;

    private Logger logger = LogManager.getLogger(GenerateMasterDataSet.class);

    @Parameter(required = true)
    String masterDataSetLocation;

    @Parameter(required = true , names="-size")
    int maxFileSizeInMB;

    @Parameter(required = true,names="-data")
    String dataFolders;

    public static void main(String... args) throws InterruptedException {
        LogManager.getRootLogger().info("Running with args:" + Arrays.toString(args));
        GenerateMasterDataSet generateMasterDataSet = new GenerateMasterDataSet();
        JCommander.newBuilder().addObject(generateMasterDataSet).build().parse(args);
        generateMasterDataSet.run();
    }

    private void run() throws InterruptedException {
        checkMasterDataDirectory();
        validateFileSize();
        DataSet aimedDataStatus = parseDataSetStatus();
        writeAimedDataSetStatusToMetadata(aimedDataStatus);
        generateData(aimedDataStatus);
    }

    private void generateData(DataSet aimedDataStatus) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        new DefaultFileAppendJobGeneratorBuilder()
                .setCalculator(new AlphanumericUtf8DataSizeCalculator())
                .setGenerator(createRandomLineGenerator())
                .setEncoding(SupportedEncoding.ALPHANUMERIC_UTF_8)
                .setMasterDataSet(Paths.get(masterDataSetLocation).toFile())
                .createDefaultFileAppendJobGenerator()
                .getTaskStream(new DataSet(new HashMap<>(), aimedDataStatus.getFileSizeInMB()), aimedDataStatus)
                .map(service::submit).map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        service.awaitTermination(1L, TimeUnit.DAYS);
    }

    private void validateFileSize() {
        if(maxFileSizeInMB <=0)
            throw new IllegalArgumentException("file size must be positive");
    }

    private RandomLineGenerator createRandomLineGenerator() {
        return new AlphanumericStringGenerator(new Random(), CHARS_PER_LINE );
    }

    private void writeAimedDataSetStatusToMetadata(DataSet dataStatus) {
        try {
            FileUtils.writeStringToFile(Paths.get(masterDataSetLocation , "meta" ).toFile() ,new Gson().toJson(dataStatus), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write metadata",e);
        }
    }

    private void checkMasterDataDirectory() {
        File file = new File(masterDataSetLocation);
        boolean directoryAlreadyExists = !file.mkdirs();
        if(directoryAlreadyExists && file.list().length != 0)
            throw new IllegalStateException("Master data set directory is non empty and contains data.");
    }

    private DataSet parseDataSetStatus() {
        logger.info("parsing folder sizes.");
        Map<String, Integer> dataSizes = new HashMap<>();
        String[] dataFolderParams = dataFolders.split(PARAMETER_DELIMETER);
        if(dataFolderParams.length %2 != 0)
            throw new IllegalArgumentException("Each folder must have a matching data size defined.");
        populateDataSizes(dataSizes, dataFolderParams);
        return new DataSet(dataSizes, maxFileSizeInMB);
    }

    private void populateDataSizes(Map<String, Integer> dataSizes, String[] dataFolderParams) {
        for(int i = 0 ; i < dataFolderParams.length ; i+=2) {
            if (dataFolderParams[i].isEmpty())
                throw new IllegalArgumentException("Empty folder name is not allowed.");
            try {
                    dataSizes.put(dataFolderParams[i], Integer.parseInt(dataFolderParams[i+1]));
            }catch (NumberFormatException e){
                throw new IllegalArgumentException(" Data size must be a number", e);
            }
        }
    }

}
