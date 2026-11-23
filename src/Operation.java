import java.time.LocalDate;

public class Operation {
    private double amount;
    private OperationType type;
    private String categoryName;
    private String date; //строку проще писать в JSON

    // для чтения из JSON
    public Operation() {
    }

    // Конструктор новой операции
    public Operation(String amount, OperationType type, String categoryName) {
        setAmount(amount);
        setType(type);
        setCategoryName(categoryName);
        setDate();
    }

    // Конструктор для чтения операции  из файла
    public Operation(String amount, OperationType type, String categoryName, String date) {
        setAmount(amount);
        setType(type);
        setCategoryName(categoryName);
        setDate(date);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "amount=" + amount +
                ", type=" + type +
                ", categoryName='" + categoryName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    // Геттеры (чтение)
    public double getAmount() {
        return amount;
    }

    public OperationType getType() {
        return type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDate() {
        return date;
    }

    // Сеттеры (редактирование) с проверками
    public void setAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            throw new IllegalArgumentException("Сумма операции не может быть пустой!");
        }

        double number;
        try {
            number = Double.parseDouble(amount.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректная сумма: '" + amount + "'");
        }

        if (number <= 0) {
            throw new IllegalArgumentException("Сумма операции должна быть больше нуля!");
        }
        this.amount = number;
    }

    public void setType(OperationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип операции не задан!");
        }
        this.type = type;
    }

    public void setCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            // throw new IllegalArgumentException("Категория операции не может быть пустой!");
            this.categoryName = "";
            return;
        }
        this.categoryName = categoryName.trim();
    }

    //создание новой операции
    private void setDate() {
        this.date = LocalDate.now().toString();
    }

    // для чтения операции из файла
    public void setDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Дата операции не может быть пустой!");
        }
        this.date = date.trim();
    }
}
