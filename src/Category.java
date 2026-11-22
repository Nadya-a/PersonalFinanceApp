public class Category {
    private String name;
    private Double budget; //null если бюджет не задан

    //новая категория без указания значения бюджета
    public Category(String name) {
        setName(name);
        this.budget = null;
    }

    //категория с бюджетом, бюджет приходит строкой из консоли
    public Category(String name, String budget) {
        setName(name);
        setBudget(budget);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", budget=" + budget +
                '}';
    }

    // Геттеры (чтение)
    public String getName() {
        return name;
    }

    public Double getBudget() {
        return budget;
    }

    // Сеттеры (редактирование) с проверками
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым!");
        }
        this.name = name.trim();
    }

    //если пустая строка то бюджет не задан
    public void setBudget(String budget) {
        if (budget == null || budget.trim().isEmpty()) {
            this.budget = null;
            return;
        }

        double number;
        try {
            number = Double.parseDouble(budget.trim());
        } catch (NumberFormatException e) {
            System.out.println("Некорректное число: '" + budget + "'. Бюджет не установлен.");
            this.budget = null;
            return;
        }

        if (number < 0) {
            System.out.println("Бюджет не может быть отрицательным. Бюджет не установлен.");
            this.budget = null;
            return;
        }
        this.budget = number;
    }
}
