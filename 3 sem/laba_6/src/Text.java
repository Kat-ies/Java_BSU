import java.util.StringTokenizer;

public class Text {
    public static String deleteLetters(String str) {
        StringBuilder st = new StringBuilder(str);
        int index = 0;
        int count = 0;
        boolean flag = false;
        boolean closingBracket = false;
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) == '(') {
                index = i;
                flag = true;
                count++;
                if (closingBracket == true) {
                    count = 1;
                    closingBracket=false;
                }
            }
            if (st.charAt(i) == ')')
                closingBracket = true;
            if (st.charAt(i) == ')' && flag == true && count == 1) {
                st.delete(index, i + 1);
                flag = false;
                i = index - 1;
                count = 0;
                closingBracket = false;
            }
        }
        return (st.toString());

    }

    public static void main(String[] args) {
        try{
        System.out.println(deleteLetters(args[0]));}
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Empty string");
        }
    }
}