import java.math.BigInteger;
import java.sql.Array;
import java.util.*;

public class Main {
    public static void main(String[] args){
        System.out.println("Inserisci un numero: ");
        Scanner in = new Scanner(System.in);
        String numero= in.nextLine();
        for(BigInteger num=new BigInteger("202");num.compareTo(new BigInteger(numero))<1;num=num.add(BigInteger.ONE)) {

            System.out.println("Num analizzato: "+num.toString());
            char[] numeroChars = num.toString().toCharArray();
            int[] numeroDaAnalizzare = reverseConTraformazione(numeroChars);
            UtilsV2 utils = new UtilsV2(numeroDaAnalizzare);
        /*utils.printTabelle();
        System.out.println(Arrays.toString(numeroDaAnalizzare));
        System.out.println(utils.fattoriSconosciuti(5));
        System.out.println(utils.unFattoreSconosciutoUnoNo(5,3));
        System.out.println(utils.moltiplicazioneIncrociata(5,2,3));*/

            Map<String, List<Integer>> ris = utils.calcolaPrimi();
            boolean res=calcolaPrimiTradizionale(Integer.parseInt(num.toString()));
            if (ris != null && res==false) {
                List<Integer> max = ris.get(UtilsV2.MASSIMO_DIVISORE);
                List<Integer> min = ris.get(UtilsV2.MINIMO_DIVISORE);
                Collections.reverse(max);
                Collections.reverse(min);
                System.out.println(max);
                System.out.println(min);
            } else {
                if(ris==null && res==true){
                    System.out.println( "Is prime");
                }
                else{
                    System.out.println("discrepanza");
                    if (ris != null) {
                        List<Integer> max = ris.get(UtilsV2.MASSIMO_DIVISORE);
                        List<Integer> min = ris.get(UtilsV2.MINIMO_DIVISORE);
                        Collections.reverse(max);
                        Collections.reverse(min);
                        System.out.println(max);
                        System.out.println(min);
                    }
                    break;
                }
            }
        }

        //System.out.println(utils.calcolaPrimi(numeroDaAnalizzare,1));
        /*ArrayList<Integer> div1=new ArrayList<>(), div2=new ArrayList<>();
        div1.add(3);
        div1.add(1);
        div2.add(3);
        div2.add(1);
        System.out.println(utils.calcolaCifreRimanentiDivsUguali(numeroDaAnalizzare, 1, div1, div2 , 0));*/

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

    public static boolean calcolaPrimiTradizionale(int num){

        for(int i=2;i<num;i++){
            if(num%i==0){
                return false;
            }
        }
        return true;
    }
}
