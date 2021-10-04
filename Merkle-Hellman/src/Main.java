import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        boolean flag = true;
        String s;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите шифруемую строку");
        s = scanner.nextLine();
        while (flag)
        {
            try
            {
                MerkleHellman merkleHellman = new MerkleHellman();
                int[] cipher = merkleHellman.encrypt(s);
                System.out.println("Ваша зашифрованная строка теперь выглядит так:");
                for (int num : cipher)
                    System.out.print(num + " ");
                System.out.println();
                System.out.println("Расшифрованная строка: " + merkleHellman.decrypt(cipher));
                flag = false;
            } catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Введите шифруемую строку");
                s = scanner.nextLine();
            }
        }
    }
}














/*
for (char symbol : s.toCharArray())
{
toBinary(symbol);
System.out.println();
}*/
