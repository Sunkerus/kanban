package comparators;

import tasks.Task;

import java.util.Comparator;

public class TimeTaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getStartTime() == null && o2.getStartTime() == null) {
            return 0;
        }
        if (o1.getStartTime() == null) {
            return 1;
        }
        if (o2.getStartTime() == null) {
            return -1;
        }
             return o1.getStartTime().compareTo(o2.getStartTime());
    }


}
