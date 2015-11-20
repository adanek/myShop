package team1.myshop.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Category {
    public int id;
    public String name;

    @Override
    public boolean equals(Object obj){
        boolean result = false;

        if(obj == null){
            return false;
        }

        Category other = (Category) obj;

        return other.id == this.id && other.name.equals(this.name);
    }

    public static Category parse(data.model.Category category) {
        if (category == null) {
            return null;
        }

        Category cat = new Category();

        cat.id = category.getId();
        cat.name = category.getName();

        return cat;
    }

    public static Collection<Category> parse(Collection<data.model.Category> categories) {

        List<Category> cats = new ArrayList<>();

        if (categories != null) {
            for (data.model.Category category : categories) {
                cats.add(Category.parse(category));
            }
        }

        // Katzen zurückgeben
        return cats;
    }
}


