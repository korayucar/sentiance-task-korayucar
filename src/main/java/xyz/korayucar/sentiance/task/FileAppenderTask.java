package xyz.korayucar.sentiance.task;

/**
 * Created by koray on 23/09/17.
 */
@FunctionalInterface
public interface FileAppenderTask {
    void appendToFile(String string);
}
