package ku.cs.app.services;

import ku.cs.app.models.Report;

import java.util.Comparator;

public class DateComparator implements Comparator<Report> {
    @Override
    public int compare(Report o1, Report o2) {
        if (o1.getSplitedDateTime().isBefore(o2.getSplitedDateTime())) return 1;
        if (o1.getSplitedDateTime().isAfter(o2.getSplitedDateTime())) return -1;
        return 0;
    }
}
