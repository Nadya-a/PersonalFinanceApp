import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleAuth auth = new ConsoleAuth();
        WalletService walletService = new WalletService();
        Scanner input = new Scanner(System.in);
        User currentUser = null;
        Wallet currentWallet = null;

        boolean work = true;
        while (work) {
            if (currentUser == null) {
                printAuthMenu();
                String choice = input.nextLine().trim();

                switch (choice) {
                    case "1":
                        register(auth, input);
                        break;
                    case "2":
                        currentUser = login(auth, input);
                        if (currentUser != null) {
                            currentWallet = walletService.getOrCreateWallet(currentUser.getUuid());
                        }
                        break;
                    case "0":
                        System.out.println("Программа заканчивает свою работу...");
                        work = false;
                        break;
                    default:
                        System.out.println("Введите номер из пункта меню");
                        break;
                }
            } else {
                printFinanceMenu(currentUser);
                String choice = input.nextLine().trim();

                switch (choice) {
                    case "1":
                        System.out.println("\n === Текущий баланс: " + currentWallet.getBalance() + " ===\n");
                        break;
                    case "2":
                        showOperations(currentWallet);
                        break;
                    case "3":
                        addOperation(currentWallet, walletService, input);
                        System.out.println("\n=== Новая операция добавлена. === \n");
                        showOperations(currentWallet);
                        break;
                    case "4":
                        addCategory(currentWallet, walletService, input);
                        break;
                    case "5":
                        showSummary(currentWallet);
                        break;
                    case "6":
                        walletService.saveWallet(currentWallet);
                        System.out.println("Вы вышли из аккаунта " + currentUser.getUsername());
                        currentUser = null;
                        currentWallet = null;
                        break;
                    case "0":
                        walletService.saveWallet(currentWallet);
                        System.out.println("Программа заканчивает свою работу...");
                        work = false;
                        break;
                    default:
                        System.out.println("Введите номер из пункта меню");
                        break;
                }
            }
        }
    }

    public static void printAuthMenu() {
        System.out.println("1. Зарегистрироваться");
        System.out.println("2. Войти");
        System.out.println("0. Выход");
        System.out.println("Введите номер действия, которое хотите совершить: ");
    }

    public static void printFinanceMenu(User currentUser) {
        System.out.println("=== Меню для пользователя " + currentUser.getUsername() + " ===");
        System.out.println("1. Посмотреть баланс");
        System.out.println("2. Посмотреть историю операций");
        System.out.println("3. Добавить операцию");
        System.out.println("4. Добавить категорию");
        System.out.println("5. Показать сводку");
        System.out.println("6. Выйти из аккаунта");
        System.out.println("0. Выйти из программы");
        System.out.println("Введите номер действия, которое хотите совершить: ");
    }

    public static void register(ConsoleAuth auth, Scanner input) {
        System.out.print("Введите новое имя пользователя: ");
        String username = input.nextLine().trim();

        System.out.print("Введите пароль: ");
        String password = input.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Логин и пароль не могут быть пустыми \n");
            return;
        }

        User user = new User(username, password);
        if (auth.addUser(user)) {
            System.out.println("Регистрация успешна!");
        } else {
            System.out.println("Пользователь с таким именем уже существует \n");
        }
    }

    public static void showOperations(Wallet wallet) {
        if (wallet.getOperations().isEmpty()) {
            System.out.println("\nНа данный момент вы не совершили ни одной операции с вашим кошельком!\n");
            return;
        }

        System.out.println("\n=== Список ваших операций ===");
        int number = 1;
        for (Operation operation : wallet.getOperations()) {
            String typeName;
            if (operation.getType() == OperationType.INCOME) {
                typeName = "Доход";
            } else {
                typeName = "Расход";
            }

            String category = operation.getCategoryName();
            if (category == null || category.isEmpty()) {
                category = "без категории";
            }

            System.out.println(number + ". " + operation.getDate()
                    + " | " + typeName
                    + " | " + operation.getAmount()
                    + " | " + category);
            number++;
        }
        System.out.println();
    }

    public static void showSummary(Wallet wallet) {
        System.out.println("\n=== Сводка доходов и расходов ===");
        System.out.println("Общий доход: " + wallet.getTotalIncome());
        System.out.println("Общие расходы: " + wallet.getTotalExpense());

        System.out.println("\nДоходы по категориям:");
        for (Category category : wallet.getCategories()) {
            double income = wallet.getIncomeSumByCategory(category.getName());
            if (income > 0) {
                System.out.println("- " + category.getName() + ": " + income);
            }
        }

        System.out.println("\nРасходы по категориям:");
        for (Category category : wallet.getCategories()) {
            double expense = wallet.getExpenseSumByCategory(category.getName());

            if (expense > 0) {
                System.out.print("- " + category.getName() + ": " + expense);

                if (category.getBudget() != null) {
                    System.out.print(" | бюджет: " + category.getBudget());
                    if (expense > category.getBudget()) {
                        System.out.print(" | превышен бюджет?: да");
                    } else {
                        System.out.print(" | превышен бюджет?: нет");
                    }
                } else {
                    System.out.print(" | бюджет: не задан | превышен бюджет?: -");
                }

                System.out.println();
            }
        }

        if (wallet.getTotalExpense() > wallet.getTotalIncome()) {
            System.out.println("\n!!! Внимание: расходы превысили доходы !!!");
        }
        System.out.println();
    }

    public static void addCategory(Wallet wallet, WalletService walletService, Scanner input) {
        System.out.print("Введите название категории: ");
        String name = input.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Название категории не может быть пустым");
            return;
        }

        System.out.println("Нужен бюджет на данную категорию?");
        System.out.println("1. Да");
        System.out.println("2. Нет");
        System.out.print("Ваш выбор: ");
        String budgetChoice = input.nextLine().trim();

        Category category;
        try {
            if (budgetChoice.equals("1")) {
                System.out.print("Введите размер бюджета: ");
                String budget = input.nextLine().trim();
                category = new Category(name, budget);
            } else if (budgetChoice.equals("2")) {
                category = new Category(name);
            } else {
                System.out.println("Введите номер из пункта меню");
                return;
            }

            if (wallet.addCategory(category)) {
                walletService.saveWallet(wallet);
                if (category.getBudget() != null) {
                    System.out.println("Категория \"" + name + "\" добавлена с бюджетом в " + category.getBudget() + "!\n");
                } else {
                    System.out.println("Категория \"" + name + "\" добавлена без обраничений по бюджету!\n");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addOperation(Wallet wallet, WalletService walletService, Scanner input) {
        System.out.println("1. Доход");
        System.out.println("2. Расход");
        System.out.print("Выберите тип операции: ");
        String typeChoice = input.nextLine().trim();

        OperationType type;
        if (typeChoice.equals("1")) {
            type = OperationType.INCOME;
        } else if (typeChoice.equals("2")) {
            type = OperationType.EXPENSE;
        } else {
            System.out.println("Неверный тип операции");
            return;
        }

        System.out.print("Введите сумму: ");
        String amount = input.nextLine().trim();

        if (wallet.getCategories().isEmpty()) {
            System.out.println("Сначала добавьте категорию через пункт меню 4");
            return;
        }

        System.out.println("Доступные категории:");
        int categoryNumber = 1;
        for (Category category : wallet.getCategories()) {
            System.out.println(categoryNumber + ". " + category.getName());
            categoryNumber++;
        }

        System.out.print("Введите номер категории: ");
        String categoryChoice = input.nextLine().trim();

        int selectedNumber;
        try {
            selectedNumber = Integer.parseInt(categoryChoice);
        } catch (NumberFormatException e) {
            System.out.println("Нужно ввести номер категории");
            return;
        }

        if (selectedNumber < 1 || selectedNumber > wallet.getCategories().size()) {
            System.out.println("Категории с таким номером нет");
            return;
        }

        String categoryName = wallet.getCategories().get(selectedNumber - 1).getName();

        try {
            Operation operation = new Operation(amount, type, categoryName);
            wallet.addOperation(operation);
            walletService.saveWallet(wallet);
            System.out.println("Операция добавлена!");

            if (type == OperationType.EXPENSE) {
                Category category = wallet.findCategory(categoryName);
                if (category != null && category.getBudget() != null) {
                    double spent = wallet.getExpenseSumByCategory(categoryName);
                    if (spent > category.getBudget()) {
                        System.out.println("!!! Внимание: превышен бюджет по данной категории !!!");
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static User login(ConsoleAuth auth, Scanner input) {
        System.out.print("Введите имя пользователя: ");
        String username = input.nextLine().trim();

        System.out.print("Введите пароль: ");
        String password = input.nextLine().trim();

        User user = auth.findUser(username, password);
        if (user != null) {
            System.out.println("Авторизация успешна! Привет, " + user.getUsername() + " ! \n");
            return user;
        }

        System.out.println("Неверный логин или пароль! Проверьте введенные данные \n");
        return null;
    }
}
