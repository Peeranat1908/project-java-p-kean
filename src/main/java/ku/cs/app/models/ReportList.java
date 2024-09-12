package ku.cs.app.models;

import java.util.*;

public class ReportList {
    private Map<String, Report> reports;

    public ReportList() {reports = new TreeMap<>(); }
    ReportList reportList;

    public void addReport(String reportId, Report report) {
        reports.put(reportId, report);
    }
    public void removeReport(String reportId, Report report){
        reports.remove(reportId,report);
    }

    public Report find(String reportId) {
        Report found = reports.get(reportId);
        if (found == null) {
            throw new RuntimeException(reportId + " not found");
        }
        return found;
    }

    public boolean check() {
        return reports.isEmpty();
    }

    public Report getReport(String key) {
        if (! reports.containsKey(key)) {
            System.err.println("Key not found");
        }
        return reports.get(key);

    }

    public Set<String> getAllReport() {
        return reports.keySet();
    }

    public List filterBy(Filterer filterer) {
//        ReportList filtered = new ReportList();
        List<Report> filtered = new ArrayList<>();
        for (String key : reports.keySet()) {
            Report found = find(key);
            if (filterer.filter(found)) {
//                filtered.addReport(key, found);
                filtered.add(found);
            }
        }
        return filtered;
    }

    @Override
    public String toString() {
        return "ReportList{" +
                "reports=" + reports +
                ", reportList=" + reportList +
                '}';
    }
}
