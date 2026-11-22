import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleAuth auth = new ConsoleAuth();
        Scanner input = new Scanner(System.in);
        User currentUser = null;

        while (true) {
            if (currentUser == null) {
                System.out.println("1. Зарегистрироваться");
                System.out.println("2. Войти");
                System.out.println("0. Выход");
                System.out.println("Введите номер действия, которое хотите совершить: ");

                String choice = input.nextLine().trim();

                if (choice.equals("1")) {
                    register(auth, input);
                } else if (choice.equals("2")) {
                    currentUser = login(auth, input);
                } else if (choice.equals("0")) {
                    System.out.println("Программа заканчивает свою работу...");
                    break;
                } else {
                    System.out.println("Введите номер из пункта меню");
                }
            } else {
                System.out.println("Вы вошли как " + currentUser.getUsername());
                System.out.println("1. Выйти из аккаунта");
                System.out.println("0. Выход");
                System.out.println("Введите номер действия, которое хотите совершить: ");

                String choice = input.nextLine().trim();

                if (choice.equals("1")) {
                    System.out.println("Вы вышли из аккаунта " + currentUser.getUsername());
                    currentUser = null;
                } else if (choice.equals("0")) {
                    System.out.println("Программа заканчивает свою работу...");
                    break;
                } else {
                    System.out.println("Введите номер из пункта меню");
                }
            }
        }
    }

    public static void register(ConsoleAuth auth, Scanner input) {
        System.out.print("Введите новое имя пользователя: ");
        String username = input.nextLine().trim();

        System.out.print("Введите новый пароль: ");
        String password = input.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Логин и пароль не могут быть пустыми");
            return;
        }

        User user = new User(username, password);
        if (auth.addUser(user)) {
            System.out.println("Регистрация успешна!");
        } else {
            System.out.println("Пользователь с таким именем уже существует");
        }
    }

    public static User login(ConsoleAuth auth, Scanner input) {
        System.out.print("Введите имя пользователя: ");
        String username = input.nextLine().trim();

        System.out.print("Введите пароль: ");
        String password = input.nextLine().trim();

        User user = auth.findUser(username, password);
        if (user != null) {
            System.out.println("Авторизация успешна! Привет, " + user.getUsername() + "!");
            return user;
        }

        System.out.println("Неверный логин или пароль");
        return null;
    }
}
