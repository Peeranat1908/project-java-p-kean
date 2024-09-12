package ku.cs.app.services;

import ku.cs.app.models.UserAccount;

import java.util.Comparator;

public class DateTimeComparator implements Comparator<UserAccount> {
    @Override
    public int compare(UserAccount o1, UserAccount o2) {
        if (o1.getSplitedDateTime().isBefore(o2.getSplitedDateTime())) return 1;
        if (o1.getSplitedDateTime().isAfter(o2.getSplitedDateTime())) return -1;
        return 0;
    }
}
