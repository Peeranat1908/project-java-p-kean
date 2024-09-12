package ku.cs.app.services;

import ku.cs.app.models.Category;
import ku.cs.app.models.CategoryList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryReportFileDataSource {

    private String directoryName;
    private String fileName;

    public CategoryReportFileDataSource(){
        this("data/", "category.csv");
    }

    public CategoryReportFileDataSource(String directoryName, String fileName) {
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

    public List readData(CategoryList categoryList) {
        List<Category> categorys = new ArrayList<>();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            String line = "";
            while ( (line = buffer.readLine()) != null ) {
                Category category = new Category();
                String[] data = line.split(",");
                //category.setId(data[0].trim());
                category.setTitle(data[0].trim());
                category.setMoreFirstDetailBox(Boolean.valueOf(data[1].trim()));
                category.setMoreFirstDetailBoxTitle(data[2].trim());
                category.setMoreSecondDetailBox(Boolean.valueOf(data[3].trim()));
                category.setMoreSecondDetailBoxTitle(data[4].trim());
                category.setMoreThirdDetailBox(Boolean.valueOf(data[5].trim()));
                category.setMoreThirdDetailBoxTitle(data[6].trim());
                category.setMoreFourthDetailBox(Boolean.valueOf(data[7].trim()));
                category.setMoreFourthDetailBoxTitle(data[8].trim());
                category.setCategoryColor(data[9].trim());

                categorys.add(category);
                categoryList.addCategory(category.getTitle(), category);
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

        return categorys;
    }

    public void writeData(CategoryList categoryList) throws IOException {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileWriter writer = new FileWriter(file);

        for (String rowData : categoryList.getAllCategory()) {
            String line = rowData + ","
                    + categoryList.find(rowData).getMoreFirstDetailBox() + ","
                    + categoryList.find(rowData).getMoreFirstDetailBoxTitle() + ","
                    + categoryList.find(rowData).getMoreSecondDetailBox() + ","
                    + categoryList.find(rowData).getMoreSecondDetailBoxTitle() + ","
                    + categoryList.find(rowData).getMoreThirdDetailBox() + ","
                    + categoryList.find(rowData).getMoreThirdDetailBoxTitle() + ","
                    + categoryList.find(rowData).getMoreFourthDetailBox() + ","
                    + categoryList.find(rowData).getMoreFourthDetailBoxTitle() + ","
                    + "end";
            writer.append(line);
            writer.append("\n");
        }
        writer.flush();
        writer.close();
    }

    public void createCategory(CategoryList categoryList, String title, Boolean moreFirstDetail, String moreFirstDetailTitle, Boolean moreSecondDetail, String moreSecondDetailTitle , Boolean moreThirdDetail, String moreThirdDetailTitle, Boolean moreFourthDetail, String moreFourthDetailTitle, String categoryColor) throws IOException {
        Category category = new Category();
        category.setTitle(title);
        category.setMoreFirstDetailBox(moreFirstDetail);
        category.setMoreFirstDetailBoxTitle(moreFirstDetailTitle);
        category.setMoreSecondDetailBox(moreSecondDetail);
        category.setMoreSecondDetailBoxTitle(moreSecondDetailTitle);
        category.setMoreThirdDetailBox(moreThirdDetail);
        category.setMoreThirdDetailBoxTitle(moreThirdDetailTitle);
        category.setMoreFourthDetailBox(moreFourthDetail);
        category.setMoreFourthDetailBoxTitle(moreFourthDetailTitle);
        category.setCategoryColor(categoryColor);
        categoryList.addCategory(String.valueOf(categoryList.getAllCategory().size()), category);
        writeData(categoryList);
    }
}
