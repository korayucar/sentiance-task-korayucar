package xyz.korayucar.sentiance;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Created by koray on 25/09/17.
 */
public class MetadataHandler {
    protected static String METADATA_FILE_NAME = "meta";

    public static void checkMasterDataDirectory(String masterDataSetLocation) {
        File file = new File(masterDataSetLocation);
        boolean directoryAlreadyExists = !file.mkdirs();
        if (directoryAlreadyExists && file.list().length != 0)
            throw new IllegalStateException("Master data set directory is non empty and contains data.");
    }


    public static void writeAimedDataSetStatusToMetadata(DataSet dataStatus, String masterDataSetLocation) {
        try {
            FileUtils.writeStringToFile(Paths.get(masterDataSetLocation, "meta").toFile(), new Gson().toJson(dataStatus), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write metadata", e);
        }
    }


    public static DataSet readCurrentMetaData(String masterDataSetLocation) throws IOException {

        return new Gson().fromJson(FileUtils.readFileToString(Paths.get(masterDataSetLocation, METADATA_FILE_NAME).toFile(), StandardCharsets.UTF_8), DataSet.class);
    }
}
