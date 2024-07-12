package Service;

import java.io.File;

public class FileService {
    public static boolean checkExistsFilePath(String filePath){
        File file = new File(filePath);
        return file.exists();
    }
}
