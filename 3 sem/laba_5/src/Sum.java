public class Sum {
    static double sum(double x, double e) throws MyException{
        double sum = 0;
        double value = -x*2/3;
        for ( int k = 2; Math.abs(value) > e; k++){
            sum+= value;
            value *= (-1)*(k+1)*x/(3*k);
        }
        if (sum==0)
            throw new MyException ("Ne cxoditcya!");
        return sum;
    }


    static class MyException extends Exception{
        MyException(){}
        MyException(String str){
            super(str);
        }
    }

    public static void main (String args[]){
        // double d = Double.parseDouble(args[0]);
        //d = 1;
        try{
            if (args.length != 2)
                throw new MyException (" Неверное число аргументов!");
            System.out.println("Sum= "+ sum( Double.parseDouble(args[0]), Double.parseDouble(args[1])));
        }
        catch (MyException e){
            System.out.println(e.getMessage());
        }
        catch (NumberFormatException e){
            System.out.println("Неверный формат данных!");
        }
    }
}
