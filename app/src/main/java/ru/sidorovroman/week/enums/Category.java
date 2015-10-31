package ru.sidorovroman.week.enums;

/**
 * Created by sidorovroman on 31.10.15.
 */
public enum Category {

    HEALTH(0,"Здоровье"),
    FAMILY(1,"Семья, личная жизнь"),
    ENVIRONMENT(2,"Окружение"),
    CAREER(3,"Карьера, бизнес"),
    FINANCES(4,"Финансы"),
    SELF_DEVELOPMENT(5,"Личный рост"),
    CREATION(6,"Духовная жизнь, творчество"),
    BRIGHTNESS(7,"Яркость жизни"),
    OTHER(8,"Прочее?");

    private final String label;
    private final int index;

    Category(int index, String label) {
        this.index = index;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public int getIndex() {
        return index;
    }

    public static Category getCategoryByIndex(Integer categoryIndex) {
        for (Category category : values()) {
            if(categoryIndex.equals(category.getIndex())){
                return category;
            }
        }

        throw new NullPointerException("Нет такой категории? что-то напутал значит");
    }
}
