package service.processors.input;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import service.command.Command;
import service.session.Session;

/**
 * Input processor to create metrics based on the message content
 */
@Component
public class MetricsProcessor implements IInputProcessor {
    private final Counter messageCounter;
    private final DistributionSummary messageLengthDistributionSummary;

    public MetricsProcessor(MeterRegistry meterRegistry) {
        this.messageCounter = meterRegistry.counter("chatter.message.count");
        this.messageLengthDistributionSummary = DistributionSummary.builder("chatter.message.length")
                .baseUnit("characters")
                .register(meterRegistry);
    }

    @Override
    public Flux<Command> decorate(Flux<Command> commandFlux, Session session) {
        return commandFlux.doOnNext((command) -> {
            messageCounter.increment();
            messageLengthDistributionSummary.record(command.getText().length());
        });
    }
}
