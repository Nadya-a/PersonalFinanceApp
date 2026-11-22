import java.util.ArrayList;

public class Wallet {
    private String uuid;
    private double balance;
    private ArrayList<Operation> operations;
    private ArrayList<Category> categories;

    public Wallet(String uuid) {
        setUUID(uuid);
        this.balance = 0;
        this.operations = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "uuid='" + uuid + '\'' +
                ", balance=" + balance +
                ", operations=" + operations +
                ", categories=" + categories +
                '}';
    }

    // Геттеры (чтение)
    public String getUuid() {
        return uuid;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    // Сеттеры для редактирования с проверками
    public void setUUID(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            throw new IllegalArgumentException("Идентификатор пользователя некорректен!");
        }
        this.uuid = uuid;
    }

    // true = категория добавилась, false = такая уже была
    public boolean addCategory(Category category) {
        for (Category existing : categories) {
            if (existing.getName().equals(category.getName())) {
                System.out.println("Такая категория уже есть!");
                return false;
            }
        }
        categories.add(category);
        return true;
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
        recountBalance();
    }

    //баланс = доходы - расходы
    public void recountBalance() {
        double sum = 0;
        for (Operation operation : operations) {
            if (operation.getType() == OperationType.INCOME) {
                sum = sum + operation.getAmount();
            } else {
                sum = sum - operation.getAmount();
            }
        }
        this.balance = sum;
    }

    public Category findCategory(String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }
}
