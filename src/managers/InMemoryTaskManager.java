package managers;

import java.util.*;

import errors.ManagerSaveException;
import tasks.*;
import comparators.TimeTaskComparator;

public class InMemoryTaskManager implements TaskManager {

    protected final Set<Task> prTask = new TreeSet<>(new TimeTaskComparator());
    private int generateId = 0;

    protected final HashMap<Integer, Task> storageTask = new HashMap<>();
    protected final HashMap<Integer, Epic> storageEpic = new HashMap<>();
    protected final HashMap<Integer, Subtask> storageSubtask = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public int generateId() {

        generateId += 1;
        return generateId;
    }

    //methods for tasks

    @Override
    public ArrayList<Task> getAllTask() {

        Collection<Task> values = storageTask.values();
        return new ArrayList<>(values);
    }

    @Override
    public void removeAllTask() {
        storageTask.clear();

    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        try {
            Task storageTaskTemp = storageTask.get(id);
            historyManager.add(storageTaskTemp);
            return storageTaskTemp;
        } catch (NullPointerException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createTask(Task task) {
        //add check task status with time
        checkTheTaskCompletionTime(task);
        storageTask.put(generateId(), task);
        task.setId(generateId);
    }

    @Override
    public void updateTask(Task task) {
        checkTheTaskCompletionTime(task);
        storageTask.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        storageTask.remove(id);
        historyManager.remove(id);
    }


    //methods for epics

    @Override
    public ArrayList<Epic> getAllEpic() {

        Collection<Epic> values = storageEpic.values();
        return new ArrayList<>(values);
    }

    @Override
    public void removeAllEpic() {
        storageEpic.clear();
        storageSubtask.clear();
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        try {
            Epic storageEpicTemp = storageEpic.get(id);
            historyManager.add(storageEpicTemp);
            return storageEpicTemp;
        } catch (NullPointerException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createEpic(Epic epic) {
        checkTheTaskCompletionTime(epic);
        storageEpic.put(generateId(), epic);
        epic.setId(generateId);
    }

    @Override
    public void updateEpic(Epic epic) {
        checkTheTaskCompletionTime(epic);
        storageTask.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException {
        try {
            ArrayList<Integer> tempSubtaskIdArr = new ArrayList<>(storageEpic.get(id).getSubtasksId());

            for (Integer iterator : tempSubtaskIdArr) {
                deleteSubtaskById(iterator);
                historyManager.remove(iterator);
            }
            storageEpic.remove(id);
            historyManager.remove(id);
        } catch (NullPointerException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }


    //methods for subtasks

    @Override
    public ArrayList<Subtask> getAllSubtask() {
        Collection<Subtask> values = storageSubtask.values();
        return new ArrayList<>(values);
    }


    @Override
    public void removeAllSubtask() {
        for (Epic iterator : storageEpic.values()) {
            iterator.setStatus(StatusTask.NEW);
            iterator.clearAllId();
        }
        storageSubtask.clear();
    }

    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        try {
            Subtask storageSubtaskTemp = storageSubtask.get(id);
            historyManager.add(storageSubtaskTemp);
            return storageSubtaskTemp;
        } catch (NullPointerException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        checkTheTaskCompletionTime(subtask);
        storageSubtask.put(generateId(), subtask);
        subtask.setId(generateId);
        storageEpic.get(subtask.getEpicId()).addId(subtask.getId());
        updateEpicStatus(storageEpic.get(subtask.getEpicId()));
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        checkTheTaskCompletionTime(subtask);
        storageSubtask.put(subtask.getId(), subtask);
        updateEpicStatus(storageEpic.get(subtask.getEpicId()));
    }

    @Override
    public void deleteSubtaskById(Integer id) throws ManagerSaveException {
        try {
            Integer epicId = storageSubtask.get(id).getEpicId();

            storageEpic.get(epicId).deleteId(id);
            storageSubtask.remove(id);
            updateEpicStatus(storageEpic.get(epicId));
            historyManager.remove(id);
        } catch (NullPointerException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }


    @Override
    public ArrayList<Subtask> getListSubtaskOfEpic(Integer id) {
        ArrayList<Subtask> tempArrSubtask = new ArrayList<>();
        for (Integer iterator : storageEpic.get(id).getSubtasksId()) {
            tempArrSubtask.add(storageSubtask.get(iterator));
        }
        return tempArrSubtask;
    }


    protected void updateEpicStatus(Epic epic) {
        boolean checkStatusNEW = true;
        boolean checkStatusDONE = true;
        if (!epic.getSubtasksId().isEmpty()) {
            for (Integer iterator : epic.getSubtasksId()) {
                if (!Objects.equals(storageSubtask.get(iterator).getStatus(), StatusTask.NEW)) {
                    checkStatusNEW = false;
                }
                if (!Objects.equals(storageSubtask.get(iterator).getStatus(), StatusTask.DONE)) {
                    checkStatusDONE = false;
                }
            }
        }
        if (checkStatusNEW) {
            epic.setStatus(StatusTask.NEW);
        } else if (checkStatusDONE) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prTask;
    }


    protected void checkTheTaskCompletionTime(Task task) {
        if (task.getStartTime() == null || prTask.size() == 0) {
            prTask.add(task);
        } else {
            for (Task iterTask : prTask) {
                boolean startTimeIdent = iterTask.getStartTime()
                        .equals(task.getStartTime());
                boolean notCorrectTimeEnd = task.getEndTime().isAfter(iterTask.getStartTime()) && task.getEndTime().isBefore(iterTask.getEndTime());
                boolean notCorrectTimeStart = task.getStartTime().isAfter(iterTask.getStartTime()) && task.getStartTime().isBefore(iterTask.getEndTime());
                if (startTimeIdent || notCorrectTimeEnd || notCorrectTimeStart) {
                    throw new RuntimeException("Задача на это время уже создана.");
                }
            }
            prTask.add(task);
        }
    }


}