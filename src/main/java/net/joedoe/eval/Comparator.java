package net.joedoe.eval;

import net.joedoe.util.Utils;

import java.io.*;
import java.util.logging.Logger;

public class Comparator {
    private final File output;
    private final String binaryPath;
    private final String params;
    private final Logger logger = Logger.getLogger(Comparator.class.getName());

    public Comparator(String outputDirPath, String actualResultsPath) {
        this.output = new File(outputDirPath + "/trec-results.txt");
        this.binaryPath = Utils.getProperty("binary");
        String flags = Utils.getProperty("params");
        String qrelFilePath = new File(Utils.getProperty("qrel")).getAbsolutePath();
        this.params = String.join(" ", flags, qrelFilePath, actualResultsPath);
    }

    public void createTrecResultsFile() throws IOException {
        File exec = new File(binaryPath);
        logger.info("\n"
                + "binary path : " + binaryPath + "\n"
                + "exists      : " + exec.exists() + "\n"
                + "params      : " + params + "\n"
                + "output path : " + output);
        StringBuilder result = new StringBuilder();
        if (exec.exists()) {
            Process process = Runtime.getRuntime().exec(binaryPath + " " + params);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader readerError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while (process.isAlive() || reader.ready() || readerError.ready()) {
                if (reader.ready()) // read output stream
                    result.append(reader.readLine()).append("\n");
                if (readerError.ready()) // read error stream
                    result.append(readerError.readLine()).append("\n");
            }
        } else {
            logger.info("ERROR: trec_eval exec not found.");
        }
        FileWriter writer = new FileWriter(output);
        writer.write(result.toString());
        writer.close();
    }
}
