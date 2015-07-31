package spiritstoolkit.popup.jetty.output;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class ConsoleUnit {

    public static MessageConsoleStream stream = null;

    public ConsoleUnit() {

        MessageConsole console =
            new MessageConsole("Spirits Tool Kit running...", null);
        stream = console.newMessageStream();

        IConsoleManager manager =
            ConsolePlugin.getDefault().getConsoleManager();
        manager.addConsoles(new IConsole[]{console});
        manager.showConsoleView(console);
    }

    public void outPutStream(String value) {
        stream.println(value);
    }
}