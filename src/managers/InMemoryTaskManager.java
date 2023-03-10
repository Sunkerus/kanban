package managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import tasks.*;
import errors.ManagerSaveException;
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
        prTask.removeIf(storageTask::containsValue);
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
        prTask.remove(storageTask.get(id));
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
        prTask.removeIf(storageSubtask::containsValue);
        storageSubtask.clear();
        storageEpic.clear();

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
        storageEpic.put(generateId(), epic);
        epic.setId(generateId);
    }

    @Override
    public void updateEpic(Epic epic) {
        storageTask.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException {
        try {
            ArrayList<Integer> tempSubtaskIdArr = new ArrayList<>(storageEpic.get(id).getSubtasksId());

            for (Integer iterator : tempSubtaskIdArr) {
                prTask.remove(storageSubtask.get(iterator));
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
            updateEpicTime(iterator);
        }
        prTask.removeIf(storageSubtask::containsValue);
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
        updateEpicTime(storageEpic.get(subtask.getEpicId()));
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        checkTheTaskCompletionTime(subtask);
        storageSubtask.put(subtask.getId(), subtask);
        updateEpicStatus(storageEpic.get(subtask.getEpicId()));
        updateEpicTime(storageEpic.get(subtask.getEpicId()));
    }

    @Override
    public void deleteSubtaskById(Integer id) throws ManagerSaveException {
        try {
            Integer epicId = storageSubtask.get(id).getEpicId();
            prTask.remove(storageSubtask.get(id));
            storageEpic.get(epicId).deleteId(id);
            storageSubtask.remove(id);
            updateEpicTime(storageEpic.get(epicId));
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

    protected void updateEpicTime(Epic epic) {
        if (!epic.getSubtasksId().isEmpty()) {
            LocalDateTime startTime = storageSubtask.get(epic.getSubtasksId().get(0)).getStartTime();
            LocalDateTime endTime = storageSubtask.get(epic.getSubtasksId().get(0)).getEndTime();
            for (Integer subtaskId : epic.getSubtasksId()) {
                if (storageSubtask.get(subtaskId).getStartTime() != null && storageSubtask.get(subtaskId).getStartTime() != null) {
                    if (storageSubtask.get(subtaskId).getStartTime().isBefore(startTime)) {
                        startTime = storageSubtask.get(subtaskId).getStartTime();
                    }
                    if (storageSubtask.get(subtaskId).getEndTime().isAfter(endTime)) {
                        endTime = storageSubtask.get(subtaskId).getEndTime();
                    }
                }
            }
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            if (startTime != null && endTime != null) {
                epic.setDuration(Duration.between(startTime, endTime).toMinutes());
            }
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
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


    public void checkTheTaskCompletionTime(Task task) {

        boolean isStartTimeIdent = false;
        boolean notCorrectTimeEnd = false;
        boolean notCorrectTimeStart = false;
        if (task.getStartTime() == null || prTask.size() == 0) {
            prTask.add(task);
        } else {

            for (Task iteratorTask : prTask) {
                if (iteratorTask.getStartTime() != null && !task.equals(iteratorTask)) {
                    isStartTimeIdent = iteratorTask.getStartTime().equals(task.getStartTime());
                    notCorrectTimeEnd = task.getEndTime().isAfter(iteratorTask.getStartTime()) && task.getEndTime().isBefore(iteratorTask.getEndTime());
                    notCorrectTimeStart = task.getStartTime().isAfter(iteratorTask.getStartTime()) && task.getStartTime().isBefore(iteratorTask.getEndTime());
                }
                if (isStartTimeIdent || notCorrectTimeEnd || notCorrectTimeStart) {
                    throw new RuntimeException("???????????? ???? ?????? ?????????? ?????? ????????????????????");
                }
            }
        }

        prTask.add(task);

    }
}