package xyz.korayucar.sentiance;

import xyz.korayucar.sentiance.generator.RandomLineGenerator;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by koray on 24/09/17.
 */
public class DefaultFileAppendJobGenerator implements  FileAppendJobGenerator{

    File masterDataSet;
    SupportedEncoding encoding;
    DataSizeCalculator calculator;
    RandomLineGenerator generator;


    public DefaultFileAppendJobGenerator(File masterDataSet, SupportedEncoding encoding, DataSizeCalculator calculator, RandomLineGenerator generator) {
        this.masterDataSet = masterDataSet;
        this.encoding = encoding;
        this.calculator = calculator;
        this.generator = generator;
    }

    @Override
    public Stream<FileAppenderTask> getTasksInFolder(String folder, DataSet original, DataSet updateDefinition) {
        List<FileAppenderTask> tasksOfFolder = new ArrayList<>();
        Integer oldDataSize  = Optional.ofNullable(original.getDataSizes().get(folder)).orElse(0);
        Integer newDataSize  = oldDataSize + Optional.ofNullable(updateDefinition.getDataSizes().get(folder)).orElse(0);
        Integer sizePerFile = original.getFileSizeInMB();
        if(oldDataSize >= newDataSize)
            return Stream.empty();
        int sizeOfLastIncomleteFileIfExist = oldDataSize % sizePerFile;
        boolean thereWasInCompleteFile = sizeOfLastIncomleteFileIfExist != 0;
        int oldCompleteFileCount = oldDataSize/sizePerFile;
        int newSizeOfLastOldFile = Math.min(sizePerFile , newDataSize - oldCompleteFileCount*sizePerFile);
        if(thereWasInCompleteFile)
            tasksOfFolder.add(getTaskBuilderSeed()
                    .setDestinationFile(Paths.get(masterDataSet.toPath().toString(), folder, getFileNameByIndex(oldCompleteFileCount + 1)).toFile())
                    .setTargetIncrementInBytes(mbToBytes((long)newSizeOfLastOldFile - sizeOfLastIncomleteFileIfExist))
            .createFileAppenderTask());
        int numberOfNewCompleteFiles = thereWasInCompleteFile ? Math.max(0,(newDataSize - (oldCompleteFileCount+1)*sizePerFile)/sizePerFile) : (newDataSize-oldDataSize)/sizePerFile;
        int indexOfFirstCompleteNewFile = thereWasInCompleteFile? oldCompleteFileCount+2 : oldCompleteFileCount+1;
        checkForNewCompleteFiles(folder, tasksOfFolder, sizePerFile, numberOfNewCompleteFiles, indexOfFirstCompleteNewFile);
        checkIfThereIsANewIncompleteFile(folder, tasksOfFolder, oldDataSize, newDataSize, sizePerFile);
        return tasksOfFolder.stream();
    }

    long mbToBytes(long mb){
        return mb*DataSizeUnit.MAGA_BYTE.getBytesPerUnit();
    }

    private void checkForNewCompleteFiles(String folder, List<FileAppenderTask> tasksOfFolder, Integer sizePerFile, int numberOfNewCompleteFiles, int indexOfFirstCompleteNewFile) {
        for(int j = 0 ;j < numberOfNewCompleteFiles; j++)
            tasksOfFolder.add(getTaskBuilderSeed()
                    .setDestinationFile(Paths.get(masterDataSet.toPath().toString(), folder, getFileNameByIndex(indexOfFirstCompleteNewFile + j)).toFile())
                    .setTargetIncrementInBytes(mbToBytes(sizePerFile))
                    .createFileAppenderTask());
    }

    private void checkIfThereIsANewIncompleteFile(String folder, List<FileAppenderTask> tasksOfFolder, Integer oldDataSize, Integer newDataSize, Integer sizePerFile) {
        int sizeOfNewIncompleteFile = newDataSize % sizePerFile;
        if(newDataSize - oldDataSize > sizePerFile && sizeOfNewIncompleteFile !=0)
            tasksOfFolder.add(getTaskBuilderSeed()
                    .setDestinationFile(Paths.get(masterDataSet.toPath().toString(), folder, getFileNameByIndex(newDataSize/sizePerFile +1)).toFile())
                    .setTargetIncrementInBytes(mbToBytes(sizeOfNewIncompleteFile))
                    .createFileAppenderTask());
    }

    private String getFileNameByIndex(int index) {
        return "file"+index;
    }

    private FileAppenderTaskBuilder getTaskBuilderSeed() {
        return new FileAppenderTaskBuilder().setCalculator(calculator).setEncoding(encoding).setGenerator(generator);
    }
}
