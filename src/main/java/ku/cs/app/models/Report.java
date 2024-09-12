package ku.cs.app.models;

import java.time.LocalDateTime;

public class Report {
    private String categoryName;
    private String date;
    private String detail;
    private String howTo;
    private String agencyName;
    private String staffName;
    private String status;
    private Integer likeAmount;
    private String title;
    private String reportId;
    private String user;
    private String moreFirstDetail;
    private String moreSecondDetail;
    private String moreThirdDetail;
    private String moreFourthDetail;

//    public Report() {
//    }

//    public Report( String reportId, String title, String date, String categoryName, String detail, String status, String howTo, String staffName, String agencyName,  Integer likeAmount, String user, String gotReported, String moreFirstDetail, String moreSecondDetail, String moreThirdDetail, String moreFourthDetail) {
//        this.categoryName = categoryName;
//        this.date = date;
//        this.detail = detail;
//        this.howTo = howTo;
//        this.agencyName = agencyName;
//        this.staffName = staffName;
//        this.status = status;
//        this.likeAmount = likeAmount;
//        this.title = title;
//        this.reportId = reportId;
//        this.user = user;
//        this.moreFirstDetail = moreFirstDetail;
//        this.moreSecondDetail = moreSecondDetail;
//        this.moreThirdDetail = moreThirdDetail;
//        this.moreFourthDetail = moreFourthDetail;
//        this.gotReported = gotReported;
//    }
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGotReported() {
        return gotReported;
    }

    public void setGotReported(String gotReported) {
        this.gotReported = gotReported;
    }

    private String gotReported;

    public String getMoreFirstDetail() {
        return moreFirstDetail;
    }

    public void setMoreFirstDetail(String moreFirstDetail) {
        this.moreFirstDetail = moreFirstDetail;
    }

    public String getMoreThirdDetail() {
        return moreThirdDetail;
    }

    public void setMoreThirdDetail(String moreThirdDetail) {
        this.moreThirdDetail = moreThirdDetail;
    }

    public String getMoreFourthDetail() {
        return moreFourthDetail;
    }

    public void setMoreFourthDetail(String moreFourthDetail) {
        this.moreFourthDetail = moreFourthDetail;
    }

    public String getMoreSecondDetail() {
        return moreSecondDetail;
    }

    public void setMoreSecondDetail(String moreSecondDetail) {
        this.moreSecondDetail = moreSecondDetail;
    }

    public String getHowTo() {
        return howTo;
    }

    public void setHowTo(String howTo) {
        this.howTo = howTo;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryName() { return categoryName; }

    public Integer getLikeAmount() {
        return likeAmount;
    }

    public void setLikeAmount(Integer likeAmount) {
        this.likeAmount = likeAmount;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public void addLike(Integer amount) {
        this.likeAmount += amount;
    }

    public void addGotReported(Integer amount) {
        this.gotReported = String.valueOf(Integer.parseInt(gotReported) + amount);
    }

    @Override
    public String toString() {
        return "Report{" +
                "categoryName='" + categoryName + '\'' +
                ", date='" + date + '\'' +
                ", detail='" + detail + '\'' +
                ", howTo='" + howTo + '\'' +
                ", agencyName='" + agencyName + '\'' +
                ", staffName='" + staffName + '\'' +
                ", status='" + status + '\'' +
                ", likeAmount=" + likeAmount +
                ", title='" + title + '\'' +
                ", reportId='" + reportId + '\'' +
                ", user='" + user + '\'' +
                ", moreFirstDetail='" + moreFirstDetail + '\'' +
                ", moreSecondDetail='" + moreSecondDetail + '\'' +
                ", moreThirdDetail='" + moreThirdDetail + '\'' +
                ", moreFourthDetail='" + moreFourthDetail + '\'' +
                ", gotReported='" + gotReported + '\'' +
                '}';
    }
    public LocalDateTime getSplitedDateTime() {
        String[] splitedTime = time.split(":");
        int hour = Integer.parseInt(splitedTime[0]);
        int minute = Integer.parseInt(splitedTime[1]);
        int second = Integer.parseInt(splitedTime[2]);
        String[] splitedDate = date.split("/");
        int year = Integer.parseInt(splitedDate[2]);
        int month = Integer.parseInt(splitedDate[1]);
        int day = Integer.parseInt(splitedDate[0]);

        return LocalDateTime.of(year, month, day, hour, minute, second);
    }
}