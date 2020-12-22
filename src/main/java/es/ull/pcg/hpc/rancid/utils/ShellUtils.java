package es.ull.pcg.hpc.rancid.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

/**
 * Set of utilities used to execute shell commands directly from the Java application.
 */
public class ShellUtils {
    /**
     * Execute a set of commands in the default shell.
     *
     * @param cmds List of commands to run.
     *
     * @throws IOException If an I/O problem occurs.
     */
    public static void shellExec (Collection<String> cmds) throws IOException {
        Runtime rt = Runtime.getRuntime();

        for (String cmd: cmds)
            waitProcess(execProcess(rt, cmd));
    }

    /**
     * Execute a set of commands in the default shell as <i>root</i>, if the <i>su</i> command is
     * available and the user has permissions.
     *
     * @param cmds List of commands to run.
     *
     * @throws IOException If an I/O problem occurs.
     */
    public static void rootExec (Collection<String> cmds) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process su = execProcess(rt, "su");

        try (DataOutputStream stream = new DataOutputStream(su.getOutputStream())) {
            for (String cmd: cmds) {
                stream.writeBytes(cmd);
                stream.writeBytes("\n");
                stream.flush();
            }

            stream.writeBytes("exit\n");
            stream.flush();
            waitProcess(su);
        }
    }

    private ShellUtils () {}

    private static Process execProcess (Runtime rt, String cmd) throws IOException {
        return rt.exec(cmd);
    }

    private static void waitProcess (Process process) {
        boolean finished = false;
        while (!finished) {
            try {
                process.waitFor();
                finished = true;
            } catch (InterruptedException ignored) {}
        }
    }
}
