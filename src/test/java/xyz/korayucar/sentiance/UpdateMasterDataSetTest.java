package xyz.korayucar.sentiance;

import com.beust.jcommander.ParameterException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by koray on 25/09/17.
 */
public class UpdateMasterDataSetTest {

    @Test(expected = ParameterException.class)
    public void main_MissingParameter_FailsProperly() throws Exception {
        UpdateMasterDataSet.main("asdasd");
    }

    @Test(expected = FileNotFoundException.class)
    public void main_NonExistentMasterDataSet_FailsProperly() throws Exception {
        UpdateMasterDataSet.main("/xxxx","-data","xx,1,yy,3");
    }

    @Test
    public void main_Functional_UpdateCompletesWithoutError() throws Exception {
        String masterDataSetDirectory = "test/updateTest";
        FileUtils.deleteDirectory(new File(masterDataSetDirectory));
        GenerateMasterDataSet.main(masterDataSetDirectory, "-size", "2", "-data","xx,5,yy,7");
        UpdateMasterDataSet.main(masterDataSetDirectory,"-data", "zz,5");
        assertEquals(3, Paths.get(masterDataSetDirectory, "zz").toFile().list().length);
    }

    @Test
    public void main_Functional_UpdateResultInExpectedNumberOfFiles() throws Exception {
        String masterDataSetDirectory = "test/updateTest2";
        FileUtils.deleteDirectory(new File(masterDataSetDirectory));
        GenerateMasterDataSet.main(masterDataSetDirectory, "-size", "2", "-data","xx,5,yy,7");
        UpdateMasterDataSet.main(masterDataSetDirectory,"-data", "xx,5");
        assertEquals(5, Paths.get(masterDataSetDirectory, "xx").toFile().list().length);
    }
}