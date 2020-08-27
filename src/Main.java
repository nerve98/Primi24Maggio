import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.println("Inserisci un numero: ");
        Scanner in = new Scanner(System.in);
        String numero= in.nextLine();
        char[] numeroChars=numero.toCharArray();
        int[] numeroDaAnalizzare=reverseConTraformazione(numeroChars);
        Utils utils= new Utils();
        utils.printTabelle();
        System.out.println(Arrays.toString(numeroDaAnalizzare));
        /*System.out.println(utils.fattoriSconosciuti(5));
        System.out.println(utils.unFattoreSconosciutoUnoNo(5,3));
        System.out.println(utils.moltiplicazioneIncrociata(5,2,3));*/
        System.out.println(utils.calcolaPrimi(numeroDaAnalizzare,2));
        ArrayList<Integer> div1=new ArrayList<>(), div2=new ArrayList<>();
        div1.add(3);
        div1.add(1);
        div2.add(3);
        div2.add(1);
        System.out.println(utils.calcolaCifreRimanentiDivsUguali(numeroDaAnalizzare, 1, div1, div2 , 0));

    }

    /*Reversa la stringa da destra verso sinistra e trasforma i caratteri in numeri interi*/
    private static int[] reverseConTraformazione(char[] numeroChars) {
        int[] numeroDaAnalizzare=new int[numeroChars.length];
        int j=0;
        for(int i=numeroChars.length-1;i>=0;i--){
            numeroDaAnalizzare[j]= Integer.parseInt(numeroChars[i]+"");
            j++;
        }
        return numeroDaAnalizzare;
    }


}