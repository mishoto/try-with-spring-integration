package dev.mihail.config;

import dev.mihail.service.FileTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import java.io.File;
import java.util.function.Consumer;

@Configuration
public class IntegrationFileConfig {

    private FileTransformer fileTransformer;

    @Bean
    public IntegrationFlowBuilder integrationFlow(){

        return IntegrationFlows.from(fileReader(), new Consumer<SourcePollingChannelAdapterSpec>() {
                    @Override
                    public void accept(SourcePollingChannelAdapterSpec sourcePollingChannelAdapterSpec) {
                        sourcePollingChannelAdapterSpec.poller(Pollers.fixedDelay(500));
                    }
                })
                .transform(fileTransformer, "fileChange")
                .handle(fileWriter());
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
        fileSource.setDirectory(new File("source_dir"));
        return fileSource;
    }
}
