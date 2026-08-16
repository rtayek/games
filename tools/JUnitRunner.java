package games;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

import java.io.PrintWriter;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public final class JUnitRunner {
    private JUnitRunner() {
    }

    public static void main(String[] args) {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("games"))
                .build();
        Launcher launcher = LauncherFactory.create();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        summary.printFailuresTo(new PrintWriter(System.err, true));
        if (summary.getTestsFoundCount() == 0 || summary.getTestsFailedCount() > 0) {
            throw new AssertionError("JUnit tests found=" + summary.getTestsFoundCount()
                    + " failed=" + summary.getTestsFailedCount());
        }
    }
}
