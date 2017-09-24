package xyz.korayucar.sentiance;

import xyz.korayucar.sentiance.generator.RandomLineGenerator;

import java.io.File;

public class FileAppenderTaskBuilder {
    private File destinationFile;
    private long targetIncrementInBytes;
    private SupportedEncoding encoding;
    private DataSizeCalculator calculator;
    private RandomLineGenerator generator;

    public FileAppenderTaskBuilder setDestinationFile(File destinationFile) {
        this.destinationFile = destinationFile;
        return this;
    }

    public FileAppenderTaskBuilder setTargetIncrementInBytes(long targetIncrementInBytes) {
        this.targetIncrementInBytes = targetIncrementInBytes;
        return this;
    }

    public FileAppenderTaskBuilder setEncoding(SupportedEncoding encoding) {
        this.encoding = encoding;
        return this;
    }

    public FileAppenderTaskBuilder setCalculator(DataSizeCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    public FileAppenderTaskBuilder setGenerator(RandomLineGenerator generator) {
        this.generator = generator;
        return this;
    }

    public FileAppenderTask createFileAppenderTask() {
        return new FileAppenderTask(destinationFile, targetIncrementInBytes, encoding, calculator, generator);
    }
}