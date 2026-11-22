public class Category {
    private String name;
    private OperationType type;
    private Double budget; //null если бюджет не задан

    //новая категория без бюджета
    public Category(String name, OperationType type) {
        setName(name);
        setType(type);
        this.budget = null;
    }

    //категория с бюджетом, бюджет приходит строкой из консоли
    public Category(String name, OperationType type, String budget) {
        setName(name);
        setType(type);
        setBudget(budget);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", budget=" + budget +
                '}';
    }

    // Геттеры (чтение)
    public String getName() {
        return name;
    }

    public OperationType getType() {
        return type;
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

    public void setType(OperationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип категории не задан!");
        }
        this.type = type;
    }

    //если пустая строка то бюджет не задан
    public void setBudget(String budget) {
        if (type == OperationType.INCOME) {
            System.out.println("Для категории доходов бюджет не задается");
            this.budget = null;
            return;
        }

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
