package xyz.korayucar.sentiance;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by koray on 25/09/17.
 */
public class DataDefinitionCommandParser {

    protected static final String PARAMETER_DELIMETER = ",";

    static DataSet parseCommand(String command, Integer maxFileSizeInMB ){
        Map<String, Integer> dataSizes = new HashMap<>();
        String[] dataFolderParams = command.split(PARAMETER_DELIMETER);
        if(dataFolderParams.length %2 != 0)
            throw new IllegalArgumentException("Each folder must have a matching data size defined.");
        populateDataSizes(dataSizes, dataFolderParams);
        return new DataSet(dataSizes, maxFileSizeInMB);
    }


    private static void populateDataSizes(Map<String, Integer> dataSizes, String[] dataFolderParams) {
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
