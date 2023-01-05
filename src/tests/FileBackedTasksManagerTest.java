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

    private final FileBackedTasksManager manager = super.getTasksManager();

    public FileBackedTasksManagerTest() {
        super((FileBackedTasksManager) Managers.getDefault(new File(".\\Resources\\testSaveConfig.csv")));
    }

    @Test
    public void shoudLoadFromFileFunctionThrowsManagerSaveExceptionWhenFilePathIsIncorrect() {
        FileBackedTasksManager testForExceptions = (FileBackedTasksManager) Managers.getDefault(new File(".\\Resources\\testSaveConfig.csv"));
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> testForExceptions.loadFromFile(new File(".\\Resources\\testSaveConfigEmpty.csv"))
        );

        assertEquals(".\\Resources\\testSaveConfigEmpty.csv (Не удается найти указанный файл)", exception.getMessage());
    }

    @Test
    public void saveAndLoadEmptyHistory() {
        Epic epic1 = new Epic("epic1", "description");
        manager.createEpic(epic1);

        final ManagerSaveException exception1 = assertThrows(
                ManagerSaveException.class,
                () -> manager.loadFromFile(new File(".\\Resources\\testSaveConfig.csv"))
        );
        assertEquals("Не удаётся прочитать историю задач", exception1.getMessage());
    }


    @Test
    public void shouldEpicReturnCorrectWithoutSubtasks() {
        Epic epic1 = new Epic("epic1", "description");
        manager.createEpic(epic1);
        final int epicId = epic1.getId();
        final Epic baseEpic = manager.getEpicById(epicId);

        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(new File(".\\Resources\\testSaveConfig.csv"));

        final Epic savedEpic1 = newManager.getEpicById(epicId);

        assertEquals(baseEpic.getSubtasksId(), savedEpic1.getSubtasksId(), "Загруженные эпики различны, у них есть подзадачи");

    }

    //add test methods for sort dy time


}
