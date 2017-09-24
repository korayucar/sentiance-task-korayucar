package xyz.korayucar.sentiance;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * Created by koray on 25/09/17.
 */
public class BackupMasterDataSet {


    @Parameter(required = true)
    String masterDataSetLocation;

    @Parameter(required = true,names = "-destination")
    String backupLocation;

    public static void main(String... args) throws InterruptedException, IOException {
        LogManager.getRootLogger().info("Running backup with args:" + Arrays.toString(args));
        BackupMasterDataSet backupMasterDataSet = new BackupMasterDataSet();
        JCommander.newBuilder().addObject(backupMasterDataSet).build().parse(args);
        backupMasterDataSet.run();
    }

    private void run() throws IOException {
        FileUtils.copyDirectory(Paths.get(masterDataSetLocation).toFile(), Paths.get(backupLocation, getBackupFolderName()).toFile());

    }

    private String getBackupFolderName() {
        return masterDataSetLocation + "-" + getDateStamp() + "-" + UUID.randomUUID().toString();
    }

    public String getDateStamp() {
        Date d = new Date();
        return new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format (d);
    }
}
