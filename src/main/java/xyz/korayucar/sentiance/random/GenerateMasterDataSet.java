package xyz.korayucar.sentiance.random;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koray on 23/09/17.
 */
public class GenerateMasterDataSet {

    private static final String PARAMETER_DELIMETER = ",";

    private Logger logger = LogManager.getLogger(GenerateMasterDataSet.class);

    @Parameter(required = true)
    String masterDataSetLocation;

    @Parameter(required = true , names="-size")
    int length;

    @Parameter(required = true,names="-data")
    String dataFolders;



    public static void main(String... args) {
        GenerateMasterDataSet generateMasterDataSet = new GenerateMasterDataSet();
        JCommander.newBuilder().addObject(generateMasterDataSet).build().parse(args);
        generateMasterDataSet.run();
    }

    private void run() {
        Map<String, Integer> dataSizes = parseDataSizes();
        checkMasterDataDirectory();


    }

    private void checkMasterDataDirectory() {
        File file = new File(masterDataSetLocation);
        boolean directoryAlreadyExists = !file.mkdirs();
        if(directoryAlreadyExists && file.list().length != 0)
            throw new IllegalStateException("Master data set directory is non empty and contains data.");
    }

    private Map<String, Integer> parseDataSizes() {
        logger.info("parsing folder sizes.");
        Map<String, Integer> dataSizes = new HashMap<>();
        String[] dataFolderParams = dataFolders.split(PARAMETER_DELIMETER);
        if(dataFolderParams.length %2 != 0)
            throw new IllegalArgumentException("Each folder must have a matching data size defined.");
        for(int i = 0 ; i < dataFolderParams.length ; i+=2) {
            if (dataFolderParams[i].isEmpty())
                throw new IllegalArgumentException("empty folder name is not allowed.");
            try {
                    dataSizes.put(dataFolderParams[i], Integer.parseInt(dataFolderParams[i+1]));
            }catch (NumberFormatException e){
                throw new IllegalArgumentException(" Data size must be a number", e);
            }
        }
        return dataSizes;
    }
}
