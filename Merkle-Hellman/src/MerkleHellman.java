import java.util.ArrayList;
import java.util.Collections;

public class MerkleHellman
{
    private final int[] closeKey; //закрытый ключ
    private final int[] openKey;  //открытый ключ
    private final int[] NM;       //множитель и модуль

    public MerkleHellman()
    {
        closeKey = initClose();
        NM = initNM();
        openKey = initOpen();
    }

    public int[] encrypt(String encr)
    {//Функция шифрования
        if (encr == null || encr.length() == 0)
            throw new IllegalArgumentException("The encrypted string cannot be of length 0");
        if (isRussian(encr)) throw new IllegalArgumentException("Only English!");
        int[] cipher = new int[encr.length()];
        int counter = 0;
        for (char symbol : encr.toCharArray())
        {
            ArrayList<Integer> binary = toBinary(symbol);
            int sum = 0;
            for (int i = 0; i < binary.size(); i++)
                if (binary.get(i) == 1) sum += openKey[i];
            cipher[counter] = sum;
            counter++;
        }
        return cipher;
    }

    public String decrypt(int[] cipher)
    {//Функция расшифровки
        if (cipher.length == 0) throw new IllegalArgumentException("The cipher cannot be of length 0");
        String original = "";
        int revN = revNumber();

        for (int i = 0; i < cipher.length; i++)
        {
            int counter = 7;
            String symbolStr = "";
            int sum = (cipher[i] * revN) % NM[1];
            while (counter >= 0)
            {
                if (sum >= closeKey[counter])
                {
                    sum -= closeKey[counter];
                    symbolStr += '1';
                } else symbolStr += '0';
                counter--;
            }
            original += (char) Integer.parseInt(new StringBuilder(symbolStr).reverse()
                    .toString(), 2);
        }
        return original;
    }

    private int[] initClose()
    {//Функция инициализации закрытого ключа
        int[] key = new int[8];
        key[0] = 1;
        int sum = 0;
        for (int i = 1; i < key.length; i++)
        {
            sum += key[i - 1];
            boolean flag = true;
            while (flag)
            {
                int chek = (int) (sum + Math.random() * 10);
                if (chek > sum)
                {
                    key[i] = chek;
                    flag = false;
                }
            }
        }
        return key;
    }

    private int[] initNM()
    {//Функция инициализации модуля и множителя
        int sum = 0;
        for (int num : closeKey)
            sum += num;
        sum += 10;
        int n = 0;
        for (int i = 3; i < 100000; i++)
        {
            if (NOD(i, sum) == 1)
            {
                n = i;
                break;
            }
        }
        return new int[]{n, sum};
    }

    private int[] initOpen()
    {//Функци инициализации открытого ключа
        int[] key = new int[8];
        for (int i = 0; i < key.length; i++)
            key[i] = (closeKey[i] * NM[0]) % NM[1];
        return key;
    }

    private int NOD(int a, int b)
    {//Функция нахождения наибольшего общего делителя
        while (a > 0 && b > 0)
            if (a > b)
                a %= b;
            else
                b %= a;

        return a + b;
    }

    private ArrayList<Integer> toBinary(char symbol)
    {//Функция перевода в двоичное представление
        ArrayList<Integer> binary = new ArrayList<>();
        for (int i = 0; i < 8; i++)
        {
            binary.add((symbol & 128) == 0 ? 0 : 1);
            symbol <<= 1;
        }
        return binary;
    }

    private int revNumber()
    {//Функция поиска обратного числа
        int revN = 1;
        while (true)
        {
            if (((revN * NM[0]) % NM[1] == 1)) break;
            else revN++;
        }
        return revN;
    }

    private boolean isRussian(String str)
    {
        for (char a : str.toCharArray())
            if (Character.UnicodeBlock.of(a) == Character.UnicodeBlock.CYRILLIC) return true;
        return false;
    }

}
