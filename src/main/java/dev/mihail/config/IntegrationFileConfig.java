package dev.mihail.config;

import dev.mihail.service.FileTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import java.io.File;
import java.util.function.Consumer;

@Configuration
public class IntegrationFileConfig {

    @Autowired
    private FileTransformer fileTransformer;

    @Bean
    public StandardIntegrationFlow integrationFlow(){

        return IntegrationFlows.from(fileReader(),
                        sourcePollingChannelAdapterSpec -> sourcePollingChannelAdapterSpec.poller(Pollers.fixedDelay(500)))
                .transform(fileTransformer, "fileChange")
                .handle(fileWriter())
                .get();
    }

    @Bean
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler fileWritingMessageHandler = new FileWritingMessageHandler(new File("destination_dir"));
        fileWritingMessageHandler.setAutoCreateDirectory(true);
        fileWritingMessageHandler.setExpectReply(false);
        return fileWritingMessageHandler;
    }

    @Bean
    public FileReadingMessageSource fileReader() {
        FileReadingMessageSource fileSource = new FileReadingMessageSource();
        fileSource.setAutoCreateDirectory(true);
        fileSource.setDirectory(new File("source_dir"));
        return fileSource;
    }
}
