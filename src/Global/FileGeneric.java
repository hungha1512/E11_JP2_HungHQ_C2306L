package Global;

import java.util.List;

public interface FileGeneric<T> {
    List<T> readFile(String filePath);
    T writeFile(String filePath, T t);

}
