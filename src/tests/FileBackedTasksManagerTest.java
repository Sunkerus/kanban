package tests;

import errors.ManagerSaveException;
import managers.FileBackedTasksManager;
import managers.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    public FileBackedTasksManagerTest() {
        super((FileBackedTasksManager) Managers.getDefault(new File(".\\Resources\\testSaveConfig.csv")));
    }

    private final FileBackedTasksManager manager = super.getTasksManager();

@Test
public void shoudLoadFromFileFunctionThrowsManagerSaveExceptionWhenFilePathIsIncorrect() {
    FileBackedTasksManager testForExceptions =  (FileBackedTasksManager) Managers.getDefault(new File(".\\Resources\\testSaveConfig.csv"));
    final ManagerSaveException exception = assertThrows(
            ManagerSaveException.class,
            () -> testForExceptions.loadFromFile(new File(".\\Resources\\testSaveConfigEmpty.csv"))
    );

    assertEquals(".\\Resources\\testSaveConfigEmpty.csv (Не удается найти указанный файл)",exception.getMessage());
}

/*@Test
public void saveAndLoadWhenEpicWithoutSubtask() {
    FileBackedTasksManager testForExceptions =  (FileBackedTasksManager) Managers.getDefault(new File(".\\Resources\\testSaveConfig.csv"));

    Epic epic1 = new Epic("epic1","description");
    final ManagerSaveException exception1 = assertThrows(
            ManagerSaveException.class,
            () -> testForExceptions.loadFromFile(new File(".\\Resources\\testSaveConfig.csv"))
    );
    assertEquals("Не удаётся прочитать строчку в файле",exception1.getMessage());
}
*/

}
