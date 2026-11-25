import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WalletService {
    private String fileName = "wallets.json";
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // читаем все кошельки из файла
    public List<Wallet> readFromFile() {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try {
            FileReader reader = new FileReader(fileName);
            Type type = new TypeToken<ArrayList<Wallet>>(){}.getType();
            List<Wallet> wallets = gson.fromJson(reader, type);
            reader.close();

            if (wallets == null) {
                return new ArrayList<>();
            }

            for (Wallet wallet : wallets) {
                if (wallet.getOperations() == null) {
                    wallet.setOperations(new ArrayList<>());
                }
                if (wallet.getCategories() == null) {
                    wallet.setCategories(new ArrayList<>());
                }
                wallet.recountBalance();
            }

            return wallets;
        } catch (Exception e) {
            System.out.println("Не удалось прочитать файл с данными кошельков :(");
            return new ArrayList<>();
        }
    }

    // сохраняем весь список в файл
    public void saveToFile(List<Wallet> wallets) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(gson.toJson(wallets));
            writer.close();
        } catch (Exception e) {
            System.out.println("Не удалось сохранить обновленные данные кошельков :(");
        }
    }

    // сохраняем один кошелёк: обновляем его в списке или добавляем
    public void saveWallet(Wallet wallet) {
        List<Wallet> wallets = readFromFile();
        boolean found = false;

        for (int i = 0; i < wallets.size(); i++) {
            if (wallets.get(i).getUuid().equals(wallet.getUuid())) {
                wallets.set(i, wallet);
                found = true;
                break;
            }
        }

        if (!found) {
            wallets.add(wallet);
        }

        saveToFile(wallets);
    }

    // берём сохранённый кошелёк или создаём новый
    public Wallet getOrCreateWallet(String uuid) {
        List<Wallet> wallets = readFromFile();

        for (Wallet wallet : wallets) {
            if (wallet.getUuid().equals(uuid)) {
                return wallet;
            }
        }

        Wallet wallet = new Wallet(uuid);
        wallets.add(wallet);
        saveToFile(wallets);
        System.out.println("--- Создан новый кошелёк ---");
        return wallet;
    }
}
