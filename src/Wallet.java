import java.util.ArrayList;

public class Wallet {
    private String uuid;
    private double balance;
    private ArrayList<Operation> operations = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();

    // для чтения из JSON
    public Wallet() {
    }

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

    public void setOperations(ArrayList<Operation> operations) {
        if (operations == null) {
            this.operations = new ArrayList<>();
        } else {
            this.operations = operations;
        }
    }

    public void setCategories(ArrayList<Category> categories) {
        if (categories == null) {
            this.categories = new ArrayList<>();
        } else {
            this.categories = categories;
        }
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

    public double getTotalIncome() {
        double sum = 0;
        for (Operation operation : operations) {
            if (operation.getType() == OperationType.INCOME) {
                sum = sum + operation.getAmount();
            }
        }
        return sum;
    }

    public double getTotalExpense() {
        double sum = 0;
        for (Operation operation : operations) {
            if (operation.getType() == OperationType.EXPENSE) {
                sum = sum + operation.getAmount();
            }
        }
        return sum;
    }

    // сумма доходов по категории
    public double getIncomeSumByCategory(String categoryName) {
        double sum = 0;
        for (Operation operation : operations) {
            if (operation.getType() == OperationType.INCOME
                    && operation.getCategoryName().equals(categoryName)) {
                sum = sum + operation.getAmount();
            }
        }
        return sum;
    }

    // сумма расходов по категории
    public double getExpenseSumByCategory(String categoryName) {
        double sum = 0;
        for (Operation operation : operations) {
            if (operation.getType() == OperationType.EXPENSE
                    && operation.getCategoryName().equals(categoryName)) {
                sum = sum + operation.getAmount();
            }
        }
        return sum;
    }
}
