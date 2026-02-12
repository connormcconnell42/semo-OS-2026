import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    int balance = 1000;

    public synchronized void updateBalance(int amount) {
        int currentBalance = this.balance;

        try {
            Thread.sleep(10);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        
        int newBalance = currentBalance + amount;
        this.balance = newBalance;
    }

    public int getBalance() {
        return balance;
    }
    
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        List<Thread> threads = new ArrayList<>();
        final int transactionAmount = 100;

        for (int i = 0; i < 5; i++) {
            threads.add(new Thread(() -> account.updateBalance(transactionAmount)));
            threads.add(new Thread(() -> account.updateBalance(-transactionAmount)));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Final Balance: " + account.getBalance());
        
    }
}