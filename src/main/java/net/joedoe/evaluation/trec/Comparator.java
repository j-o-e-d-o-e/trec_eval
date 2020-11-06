package net.joedoe.evaluation.trec;

import net.joedoe.utilities.Utilities;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

public class Comparator {
    private final File output;
    private final String binaryPath;
    private final String params;
    private final Logger logger = Logger.getLogger(Comparator.class.getName());

    public Comparator(String outputDirPath, String actualResultsPath) throws IOException {
        this.output = new File(outputDirPath + "/trec-results.txt");
        Properties prop = new Utilities().getProperties();
        this.binaryPath = prop.get("binary").toString();
        String params = prop.get("params").toString();
        String qrelFilePath = new File(prop.get("qrel").toString()).getAbsolutePath();
        this.params = String.join(" ", params, qrelFilePath, actualResultsPath);
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
        } else
            logger.info("ERROR: trec_eval exec not found.");
        FileWriter fw = new FileWriter(output);
        fw.write(result.toString());
        fw.close();
    }
}
