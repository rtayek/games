package chatmap.games.core;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class GameEngine {
    private final Clock clock;
    private final Map<GameKind, Instant> startedAt = new EnumMap<>(GameKind.class);

    public GameEngine() {
        this(Clock.systemUTC());
    }

    public GameEngine(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
    }

    public void start(GameKind kind) {
        startedAt.put(Objects.requireNonNull(kind), clock.instant());
    }

    public boolean isRunning(GameKind kind) {
        return startedAt.containsKey(Objects.requireNonNull(kind));
    }

    public Duration elapsed(GameKind kind) {
        Instant start = startedAt.get(Objects.requireNonNull(kind));
        if (start == null) {
            return Duration.ZERO;
        }
        return Duration.between(start, clock.instant());
    }

    public void stop(GameKind kind) {
        startedAt.remove(Objects.requireNonNull(kind));
    }
}
