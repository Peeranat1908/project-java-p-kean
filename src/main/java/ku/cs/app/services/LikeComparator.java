package ku.cs.app.services;

import ku.cs.app.models.Report;

import java.util.Comparator;

public class LikeComparator implements Comparator<Report> {

    @Override
    public int compare(Report o1, Report o2) {
        if (o1.getLikeAmount() < o2.getLikeAmount()) return -1;
        if (o1.getLikeAmount() > o2.getLikeAmount()) return 1;
        return 0;
    }
}
