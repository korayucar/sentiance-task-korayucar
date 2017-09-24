package xyz.korayucar.sentiance;

import java.util.stream.Stream;

/**
 * Created by koray on 24/09/17.
 */
public interface FileAppendJobGenerator {


    default Stream<FileAppenderTask> getTaskStream(DataSet original, DataSet updateDefinition){
        return updateDefinition.getDataSizes().entrySet().stream().map(entry -> getTasksInFolder(entry.getKey(),original,updateDefinition)).reduce(Stream::concat).orElse(Stream.empty());
    }

    Stream<FileAppenderTask> getTasksInFolder(String folder, DataSet original, DataSet updateDefinition);

}
