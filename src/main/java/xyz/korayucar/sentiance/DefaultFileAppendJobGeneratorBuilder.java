package xyz.korayucar.sentiance;

import xyz.korayucar.sentiance.generator.RandomLineGenerator;

import java.io.File;

public class DefaultFileAppendJobGeneratorBuilder {
    private File masterDataSet;
    private SupportedEncoding encoding;
    private DataSizeCalculator calculator;
    private RandomLineGenerator generator;

    public DefaultFileAppendJobGeneratorBuilder setMasterDataSet(File masterDataSet) {
        this.masterDataSet = masterDataSet;
        return this;
    }

    public DefaultFileAppendJobGeneratorBuilder setEncoding(SupportedEncoding encoding) {
        this.encoding = encoding;
        return this;
    }

    public DefaultFileAppendJobGeneratorBuilder setCalculator(DataSizeCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    public DefaultFileAppendJobGeneratorBuilder setGenerator(RandomLineGenerator generator) {
        this.generator = generator;
        return this;
    }

    public DefaultFileAppendJobGenerator createDefaultFileAppendJobGenerator() {
        return new DefaultFileAppendJobGenerator(masterDataSet, encoding, calculator, generator);
    }
}