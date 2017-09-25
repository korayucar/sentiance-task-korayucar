package xyz.korayucar.sentiance;

import org.junit.Test;

/**
 * Created by koray on 24/09/17.
 */
public class GenerateMasterDataSetUnitTest {


    @Test(expected = IllegalArgumentException.class )
    public void main_IncorrectLengthOfParameters_FailsProperly() throws Exception {
        GenerateMasterDataSet.main("xxxx", "-size", "2", "-data","xx,5,yy,7,ff");
    }

    @Test(expected = IllegalArgumentException.class )
    public void main_EmptyFolderName_FailsProperly() throws Exception {
        GenerateMasterDataSet.main("xxxx", "-size", "2", "-data","xx,5,,7");
    }
}