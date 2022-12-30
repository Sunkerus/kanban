package tests;

import managers.TaskManager;

public abstract class TaskManagerTest<T extends TaskManager> {

    private final T manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }

    public T getTasksManager() {
        return manager;
    }


}
