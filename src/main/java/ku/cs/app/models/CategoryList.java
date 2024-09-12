package ku.cs.app.models;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CategoryList {

    private Map<String, Category> categorys;

    public CategoryList() {categorys = new TreeMap<>(); }

    public void addCategory(String categoryName, Category category) {
        categorys.put(categoryName, category);
    }

    public Category find(String categoryName) {
        Category found = categorys.get(categoryName);
        if (found == null) {
            throw new RuntimeException(categoryName + " not found in dictionary");
        }
        return found;
    }

    public Set<String> getAllCategory() {
        return categorys.keySet();
    }
}
