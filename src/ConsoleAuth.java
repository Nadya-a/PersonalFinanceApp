import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleAuth {
    private String fileName = "users.txt";

    // чтение всех пользователей из файла
    public List<User> readFromFile() {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        List<User> users = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", 3);
                if (parts.length != 3) {
                    continue;
                }
                users.add(new User(parts[0], parts[1], parts[2]));
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Не удалось прочитать файл пользователей");
            return new ArrayList<>();
        }

        return users;
    }

    // сохранение списка в файл
    public void saveToFile(List<User> users) {
        try {
            FileWriter writer = new FileWriter(fileName);
            for (User user : users) {
                writer.write(user.getUuid() + "|" + user.getUsername() + "|" + user.getPassword());
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Не удалось сохранить пользователей");
        }
    }

    // добавление одного пользователя, true = добавился false = уже был
    public boolean addUser(User user) {
        List<User> users = readFromFile();

        for (User existing : users) {
            if (existing.getUsername().equals(user.getUsername())) {
                return false;
            }
        }

        users.add(user);
        saveToFile(users);
        return true;
    }

    // поиск пользователя по логину и паролю
    public User findUser(String username, String password) {
        for (User user : readFromFile()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
