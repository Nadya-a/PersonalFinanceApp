import java.util.UUID;

public class User {
    private String uuid;
    private String username;
    private String password;

    // Конструктор создания нового пользователя
    public User(String username, String password) {
        setUUID(UUID.randomUUID().toString());
        setUsername(username);
        setPassword(password);
    }

    // Конструктор для чтения из файла
    public User(String uuid, String username, String password) {
        setUUID(uuid);
        setUsername(username);
        setPassword(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    // Геттеры (чтение)
    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Сеттеры (редактирование) с проверками
    public void setUUID(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            throw new IllegalArgumentException("Идентефикатор пользователя некорректен!");
        }
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым!");
        }
        this.username = username.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым!");
        }
        this.password = password;
    }
}
