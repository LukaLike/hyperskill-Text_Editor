package editor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class FileManager {
    File file;

    public FileManager(File fileName) {
        this.file = new File(String.valueOf(fileName.getAbsoluteFile()));
    }

    public String readFileAsString() throws IOException {
        return new String(Files.readAllBytes(Path.of(file.getAbsolutePath())));
    }

    public void writeToFile(String text) throws IOException {
        FileWriter writer = new FileWriter(file.getAbsolutePath());
        writer.write(text);
        writer.close();
    }
}
