package ku.cs.app.services;

import ku.cs.app.models.Report;
import ku.cs.app.models.ReportList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserReportFileDataSource {
    private String directoryName;
    private String fileName;
//    public UserReportFileDataSource(){
//        this("data/", "report.csv");
//    }

    public UserReportFileDataSource(){
        this("data/", "report.csv");
    }
    public UserReportFileDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if ( ! file.exists()) {
            file.mkdirs();
        }

        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (! file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List readData(ReportList reportList) {
        List<Report> reports = new ArrayList<>();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            String line = "";
            while ( (line = buffer.readLine()) != null ) {
                Report report = new Report();
                String[] data = line.split(",");

                report.setReportId(data[0].trim());
                report.setTitle(data[1].trim());
                report.setDate(data[2].trim());
                report.setCategoryName(data[3].trim());
                report.setDetail(data[4].trim());
                report.setStatus(data[5].trim());
                report.setHowTo(data[6].trim());
                report.setStaffName(data[7].trim());
                report.setAgencyName(data[8].trim());
                report.setLikeAmount(Integer.valueOf(data[9].trim()));
                report.setUser(data[10].trim());
                report.setGotReported(data[11].trim());
                report.setMoreFirstDetail(data[12].trim());
                report.setMoreSecondDetail(data[13].trim());
                report.setMoreThirdDetail(data[14].trim());
                report.setMoreFourthDetail(data[15].trim());
                report.setTime(data[16].trim());

                reports.add(report);
                reportList.addReport(report.getReportId(), report);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return reports;
    }

    public void writeData(ReportList reportList) throws IOException {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileWriter writer = new FileWriter(file);

        for (String rowData : reportList.getAllReport()) {
            String line = rowData + ","
                    + reportList.find(rowData).getTitle() + ","
                    + reportList.find(rowData).getDate() + ","
                    + reportList.find(rowData).getCategoryName() + ","
                    + reportList.find(rowData).getDetail() + ","
                    + reportList.find(rowData).getStatus() + ","
                    + reportList.find(rowData).getHowTo() + ","
                    + reportList.find(rowData).getStaffName() + ","
                    + reportList.find(rowData).getAgencyName() + ","
                    + reportList.find(rowData).getLikeAmount() + ","
                    + reportList.find(rowData).getUser() + ","
                    + reportList.find(rowData).getGotReported() + ","
                    + reportList.find(rowData).getMoreFirstDetail() + ","
                    + reportList.find(rowData).getMoreSecondDetail() + ","
                    + reportList.find(rowData).getMoreThirdDetail() + ","
                    + reportList.find(rowData).getMoreFourthDetail() + ","
                    + reportList.find(rowData).getTime() + ","
                    + "end";

            writer.append(line);
            writer.append("\n");
        }
        writer.flush();
        writer.close();
    }
    public void createPost(ReportList reportList, String title, String detail, String date, String user, String category, String agency, String howTo, String staffName, String moreFirstDetail, String moreSecondDetail, String moreThirdDetail, String moreFourthDetail, String time) throws IOException {
        Report report = new Report();
        report.setTitle(title);
        report.setDetail(detail);
        report.setReportId(String.valueOf(reportList.getAllReport().size()));
        report.setUser(user);
        report.setStatus("ยังไม่รับเรื่อง");
        report.setLikeAmount(0);
        report.setCategoryName(category);
        report.setDate(date);
        report.setAgencyName(agency);
        report.setHowTo(howTo);
        report.setStaffName(staffName);
        report.setMoreFirstDetail(moreFirstDetail);
        report.setMoreSecondDetail(moreSecondDetail);
        report.setMoreThirdDetail(moreThirdDetail);
        report.setMoreFourthDetail(moreFourthDetail);
        report.setGotReported("0");
        report.setTime(time);
        reportList.addReport(String.valueOf(reportList.getAllReport().size()), report);
        writeData(reportList);
    }
}
