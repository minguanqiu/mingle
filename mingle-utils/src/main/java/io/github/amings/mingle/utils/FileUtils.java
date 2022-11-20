package io.github.amings.mingle.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author Ming
 * 
 */

public class FileUtils {

    public static boolean isExist(String path) {
        return Files.exists(Paths.get(path));
    }

    public static Stream<String> readFile(String path) throws IOException {
        return Files.lines(new File(path).toPath());
    }

    public static Stream<String> readFile(String path, Charset charset) throws IOException {
        return Files.lines(new File(path).toPath(), charset);
    }

}
