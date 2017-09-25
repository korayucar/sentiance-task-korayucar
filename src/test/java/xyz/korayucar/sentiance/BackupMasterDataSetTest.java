package xyz.korayucar.sentiance;

import com.beust.jcommander.ParameterException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by koray on 25/09/17.
 */
public class BackupMasterDataSetTest {

    @Test(expected = ParameterException.class)
    public void main_MissingParameter_FailsProperly() throws Exception {
        BackupMasterDataSet.main("asdasd");
    }

    @Test(expected = FileNotFoundException.class)
    public void main_NonExistentMasterDataSet_FailsProperly() throws Exception {
        BackupMasterDataSet.main("/test/xxxx","-destination","ssss");
    }

    @Test
    public void main_Functional_MasterDataSet_BackupCompletesWithoutError() throws Exception {
        String masterDataSetDirectory = "test/backupTest";
        String backupDirectory = "test/backup";
        FileUtils.deleteDirectory(new File(masterDataSetDirectory));
        FileUtils.deleteDirectory(new File(backupDirectory));
        GenerateMasterDataSet.main(masterDataSetDirectory, "-size", "2", "-data","xx,5,yy,7");
        BackupMasterDataSet.main(masterDataSetDirectory,"-destination", backupDirectory);
        assertEquals(1, Paths.get(backupDirectory,"test").toFile().list().length);
        assertTrue(Paths.get(backupDirectory,"test").toFile().list()[0].contains("backupTest"));
    }
}