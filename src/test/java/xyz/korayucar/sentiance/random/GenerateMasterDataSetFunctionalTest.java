package xyz.korayucar.sentiance.random;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * Created by koray on 24/09/17.
 */
public class GenerateMasterDataSetFunctionalTest {

    @Test
    public void main_HappyPath_CreateNewMasterDataSet() throws Exception {
        String masterDataSetDirectory = "test/xyz";
        FileUtils.deleteDirectory(new File(masterDataSetDirectory));
        GenerateMasterDataSet.main(masterDataSetDirectory, "-size", "2", "-data","xx,5,yy,7");
    }
}