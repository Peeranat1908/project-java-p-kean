package ku.cs.app.models;

public class Category {

    private String title;
    private Boolean moreFirstDetailBox;
    private String moreFirstDetailBoxTitle;
    private Boolean moreSecondDetailBox;
    private String moreSecondDetailBoxTitle;
    private Boolean moreThirdDetailBox;
    private String moreThirdDetailBoxTitle;
    private Boolean moreFourthDetailBox;
    private String moreFourthDetailBoxTitle;
    private String categoryColor;

    public String getCategoryColor() {
        return categoryColor;
    }
    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
    public String getMoreFourthDetailBoxTitle() {
        return moreFourthDetailBoxTitle;
    }

    public void setMoreFourthDetailBoxTitle(String moreFourthDetailBoxTitle) {
        this.moreFourthDetailBoxTitle = moreFourthDetailBoxTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Boolean getMoreFirstDetailBox() {
        return moreFirstDetailBox;
    }

    public void setMoreFirstDetailBox(Boolean moreFirstDetailBox) {
        this.moreFirstDetailBox = moreFirstDetailBox;
    }

    public String getMoreFirstDetailBoxTitle() {
        return moreFirstDetailBoxTitle;
    }

    public void setMoreFirstDetailBoxTitle(String moreFirstDetailBoxTitle) {
        this.moreFirstDetailBoxTitle = moreFirstDetailBoxTitle;
    }

    public Boolean getMoreSecondDetailBox() {
        return moreSecondDetailBox;
    }

    public void setMoreSecondDetailBox(Boolean moreSecondDetailBox) {
        this.moreSecondDetailBox = moreSecondDetailBox;
    }

    public String getMoreSecondDetailBoxTitle() {
        return moreSecondDetailBoxTitle;
    }

    public void setMoreSecondDetailBoxTitle(String moreSecondDetailBoxTitle) {
        this.moreSecondDetailBoxTitle = moreSecondDetailBoxTitle;
    }

    public Boolean getMoreThirdDetailBox() {
        return moreThirdDetailBox;
    }

    public void setMoreThirdDetailBox(Boolean moreThirdDetailBox) {
        this.moreThirdDetailBox = moreThirdDetailBox;
    }

    public String getMoreThirdDetailBoxTitle() {
        return moreThirdDetailBoxTitle;
    }

    public void setMoreThirdDetailBoxTitle(String moreThirdDetailBoxTitle) {
        this.moreThirdDetailBoxTitle = moreThirdDetailBoxTitle;
    }

    public Boolean getMoreFourthDetailBox() {
        return moreFourthDetailBox;
    }

    public void setMoreFourthDetailBox(Boolean moreFourthDetailBox) {
        this.moreFourthDetailBox = moreFourthDetailBox;
    }


    @Override
    public String toString() {
        return "Category{" +
                "title='" + title + '\'' +
                ", moreFirstDetailBox=" + moreFirstDetailBox +
                ", moreFirstDetailBoxTitle='" + moreFirstDetailBoxTitle + '\'' +
                ", moreSecondDetailBox=" + moreSecondDetailBox +
                ", moreSecondDetailBoxTitle='" + moreSecondDetailBoxTitle + '\'' +
                ", moreThirdDetailBox=" + moreThirdDetailBox +
                ", moreThirdDetailBoxTitle='" + moreThirdDetailBoxTitle + '\'' +
                ", moreFourthDetailBox=" + moreFourthDetailBox +
                ", moreFourthDetailBoxTitle='" + moreFourthDetailBoxTitle + '\'' +
                ", categoryColor='" + categoryColor + '\'' +
                '}';
    }
}
