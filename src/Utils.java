
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    final String MASSIMO_DIVISORE="MassimoDivisore";
    final String MINIMO_DIVISORE="MinimoDivisore";
    final String RIPORTO="Riporto";

    static Map<Integer, List<TabellaMoltiplicazione>> listaMoltiplicazione= new HashMap<>();
    static Map<Integer, List<TabellaAddizione>> listaAddizione= new HashMap<>();

    private List<Integer> div1=new ArrayList<>();
    private List<Integer> div2=new ArrayList<>();

    static{
        for(int i=0;i<10;i++) {
            listaMoltiplicazione.put(i, new ArrayList<>());
            listaAddizione.put(i, new ArrayList<>());
        }
        for(int i=0;i<10;i++){

            for(int j=i;j<10;j++){
                listaMoltiplicazione.get((j*i)%10).add(new TabellaMoltiplicazione((short) i,(short) j, (short) ((j*i)/10)));
                listaAddizione.get((j+i)%10).add(new TabellaAddizione((short) i,(short) j, (short) ((j+i)/10)));
            }
        }
    }

    public void printTabelle(){
        System.out.println(listaMoltiplicazione.values().toString());
        System.out.println(listaAddizione.values().toString());

    }

    public boolean checkDivs(List<Integer> div1, List<Integer> div2){
        if (div1.get(div1.size() - 1) != 0){
            if (div2.get(div2.size() - 1) != 0){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }


    }

    //per prima cifra
    public Map<String, List<Integer>> calcolaPrimi(int[] numeroDaAnalizzare, int lengthDivMin) {
        Map<String, List<Integer>> result=null;
        List<TabellaMoltiplicazione> possibiliUnita=fattoriSconosciuti(numeroDaAnalizzare[0]);
        for(TabellaMoltiplicazione combinazioneUnita: possibiliUnita){
            List<Integer> div1=new ArrayList<>();
            div1.add(combinazioneUnita.fattore1);

            List<Integer> div2=new ArrayList<>();
            div2.add(combinazioneUnita.fattore2);
            result=calcolaDivisori(numeroDaAnalizzare, lengthDivMin, 1, div1, div2, combinazioneUnita.riporto);
            if(result!=null && checkDivs(result.get(MASSIMO_DIVISORE), result.get(MINIMO_DIVISORE))){
                return result;
            }
            else{
                result=calcolaDivisori(numeroDaAnalizzare, lengthDivMin, 1, div2, div1, combinazioneUnita.riporto);
                if(result!=null && checkDivs(result.get(MASSIMO_DIVISORE), result.get(MINIMO_DIVISORE))){
                    return result;
                }
            }
        }
        return result;
    }

    //mezzo (stesso numero cifre divisore minimo e massimo)
    public Map<String, List<Integer>> calcolaDivisori(int[] numeroDaAnalizzare, int lengthDivMin, int index, List<Integer> div1, List<Integer> div2, long riporto) {
        Map<String, List<Integer>> result=null;
        if(lengthDivMin>index) { //se sei ancora nel mezzo
            ProdRip prodRip = new ProdRip(0,0);
            if (index > 1) {
                prodRip = calcolaProdottiConosciuti(div1, div2);
            }
            long cifraToAnalyze=numeroDaAnalizzare[index]-riporto%10-prodRip.cifraProdotto; //speriamo vada XD
            cifraToAnalyze=Math.abs(cifraToAnalyze);
            long riportoNext=(cifraToAnalyze/10)+(riporto/10)+prodRip.riporto;
            cifraToAnalyze=cifraToAnalyze%10;
            List<Map<String, List<TabellaMoltiplicazione>>> moltipX=moltiplicazioneIncrociata((int) cifraToAnalyze, div1.get(0), div2.get(0));
            List<TabellaMoltiplicazione> newCifreDivs= listaNuoveCifreDivisore(moltipX, div1.get(0), div2.get(0));
            for(TabellaMoltiplicazione tab: newCifreDivs){
                List<Integer> newDiv1= new ArrayList<>();
                newDiv1.addAll(div1);
                newDiv1.add(tab.fattore1);

                List<Integer> newDiv2= new ArrayList<>();
                newDiv2.addAll(div2);
                newDiv2.add(tab.fattore2);

                riportoNext+=tab.riporto;
                result=calcolaDivisori(numeroDaAnalizzare,lengthDivMin,index+1,newDiv1, newDiv2, riportoNext);
                if(result!=null && checkDivs(result.get(MASSIMO_DIVISORE), result.get(MINIMO_DIVISORE))){
                    return result;
                }
            }
            return result;
        }
        else {
            if (index < numeroDaAnalizzare.length / 2) {  //se il divosore massimo ha più cifre del divisore minimo
                return completaDivMax(numeroDaAnalizzare, index, div1, div2, riporto);
            } else {
                return calcolaCifreRimanentiDivsUguali(numeroDaAnalizzare, index, div1, div2, riporto);
            }
        }
    }

    //per cifre maggiori sul divisore massimo
    private Map<String, List<Integer>> completaDivMax(int[] numeroDaAnalizzare, int index, List<Integer> div1, List<Integer> div2, long riporto) {
        int currentRip=(int) riporto%10;
        long nextRip=riporto/10;
        if(div2.size()+div1.size()<numeroDaAnalizzare.length-div2.size()+1){
            ProdRip prodCon = calcolaCifra(currentRip, div1, div2,1);
            int cifraDaAnalizzare=Math.abs(numeroDaAnalizzare[index]-prodCon.cifraProdotto-currentRip);
            nextRip+=prodCon.riporto;

            List<TabellaMoltiplicazione> fattScon = unFattoreSconosciutoUnoNo(cifraDaAnalizzare, div2.get(0));
            for(TabellaMoltiplicazione tab : fattScon){
                List<Integer> newDiv1=new ArrayList<>();
                newDiv1.addAll(div1);
                if(tab.fattore1==div2.get(0)){
                    newDiv1.add(tab.fattore2);
                }
                else{
                    newDiv1.add(tab.fattore1);
                }
                Map<String, List<Integer>> res = completaDivMax(numeroDaAnalizzare, index + 1, div1, div2, nextRip);
                if(res!=null && checkDivs(res.get(MASSIMO_DIVISORE), res.get(MINIMO_DIVISORE))){
                    return res;
                }
            }
            return null;
        }
        else{
            for(int i=index;i<numeroDaAnalizzare.length;i++){
                int j=i-index+1;
                ProdRip prodCon = calcolaCifra(currentRip,div1,div2,j);
                int cifra=prodCon.cifraProdotto+currentRip;
                nextRip+=cifra/10;
                cifra=cifra%10;
                if(cifra!=numeroDaAnalizzare[i]){
                    return null;
                }
            }
            if(nextRip==0){
                Map<String, List<Integer>> mappa= new HashMap<>();
                mappa.put(MASSIMO_DIVISORE, div1);
                mappa.put(MINIMO_DIVISORE, div2);
                return mappa;
            }
            else{
                return null;
            }
        }
    }


    //
    public Map<String, List<Integer>> calcolaCifreRimanentiDivsUguali(int[] numeroDaAnalizzare, int index, List<Integer> div1, List<Integer> div2, long riporto) {
        int currentRip=(int) riporto%10;
        long nextRip=riporto/10;

        int k = 1;//1
        for(int i=index;i<numeroDaAnalizzare.length;i++) {
            int cifraDaAnalizzare = numeroDaAnalizzare[i];
            ProdRip cifraCalcolata = calcolaCifra(currentRip, div1, div2, k);
            if(cifraCalcolata.cifraProdotto!=cifraDaAnalizzare){
                return null;
            }
            nextRip+=cifraCalcolata.riporto;
            currentRip=(int) nextRip%10;
            nextRip=nextRip/10;
            k++;
        }
        if(currentRip==0) {
            Map<String, List<Integer>> mappa = new HashMap<>();
            mappa.put(MASSIMO_DIVISORE, div1);
            mappa.put(MINIMO_DIVISORE, div2);

            return mappa;
        }
        else{
            return null;
        }

    }

    public ProdRip calcolaCifra(int currentRip, List<Integer> div1, List<Integer> div2, int index) {
        ProdRip result= new ProdRip(0,0);
        int prod=0;
        int j=div1.size()-1;
        for(int i=index;i<div2.size();i++){
            prod+=div1.get(j)*div2.get(i);
            j--;
        }
        prod+=currentRip;
        result.cifraProdotto=prod%10;
        result.riporto=prod/10;
        return result;
    }

    public ProdRip calcolaProdottiConosciuti(List<Integer> div1, List<Integer> div2) {
        ProdRip result= new ProdRip(0,0);
        int j=div2.size()-1;
        for(int i=1;i<div1.size();i++){
            int prod=div1.get(i)*div2.get(j);
            int unita=prod%10;
            int rip=prod/10;
            result.cifraProdotto+=unita;
            result.riporto+=rip;
            j--;
        }
        int temp=result.cifraProdotto;
        if(temp>=10){
            result.cifraProdotto=temp%10;
            result.riporto+=temp/10;
        }
        return result;
    }


    public List<TabellaMoltiplicazione> listaNuoveCifreDivisore(List<Map<String, List<TabellaMoltiplicazione>>> lista, int cifraNotaDivMax, int cifraNotaDivMin){
        List<TabellaMoltiplicazione> result= new ArrayList<>();

        for(Map<String, List<TabellaMoltiplicazione>> map: lista){
            List<TabellaMoltiplicazione> newMins=map.get(MASSIMO_DIVISORE);
            List<TabellaMoltiplicazione> newMaxs=map.get(MINIMO_DIVISORE);
            for(int i=0;i<newMins.size();i++){
                TabellaMoltiplicazione newMin= newMins.get(i);
                TabellaMoltiplicazione newMax= newMaxs.get(i);
                result.add(new TabellaMoltiplicazione(estraiCifraNonNota(newMax,cifraNotaDivMin), estraiCifraNonNota(newMin, cifraNotaDivMax), newMin.riporto+newMax.riporto));

            }
        }
        return result;
    }

    public int estraiCifraNonNota(TabellaMoltiplicazione tab, int cifraNota){
        if(tab.fattore1==cifraNota){
            return tab.fattore2;
        }
        else{
            return tab.fattore1;
        }
    }

    /*
    Quando i fattori sono tutti sconosciuti
    @return lista Fattori relative alla prima cifra da analizzare
     */
    public List<TabellaMoltiplicazione> fattoriSconosciuti(int cifraDaAnalizzare){
        return listaMoltiplicazione.get(cifraDaAnalizzare);
    }

    /*
    Quando hai una cifra da analizzare e conosci solo una delle due cifre dei divisori
    @return lista di fattori
     */
    public List<TabellaMoltiplicazione> unFattoreSconosciutoUnoNo(int cifraDaAnalizzare, int fattoreConosciuto){
        List<TabellaMoltiplicazione> lista=new ArrayList<>();
        for(TabellaMoltiplicazione combinazione : listaMoltiplicazione.get(cifraDaAnalizzare)){
            if(combinazione.fattore1==fattoreConosciuto || combinazione.fattore2==fattoreConosciuto){
                lista.add(combinazione);
            }
        }
        return lista;
    }

    public List<Map<String, List<TabellaMoltiplicazione>>> moltiplicazioneIncrociata(int cifraDaAnalizzare, int fattoreConosciutoDivMax, int fattoreConosciutoDivMin){
        List<Map<String, List<TabellaMoltiplicazione>>> lista= new ArrayList<>();
        List<TabellaAddizione> listaAddizioni=listaAddizione.get(cifraDaAnalizzare);
        Map<String, List<TabellaMoltiplicazione>> mappa;
        for(TabellaAddizione combinazioneSomma: listaAddizioni){
            mappa=assegnaAllaMappa(combinazioneSomma.addendo1, combinazioneSomma.addendo2, fattoreConosciutoDivMax, fattoreConosciutoDivMin);
            if(mappa.size()>0){
                lista.add(mappa);
            }
            mappa=assegnaAllaMappa(combinazioneSomma.addendo2, combinazioneSomma.addendo1, fattoreConosciutoDivMax, fattoreConosciutoDivMin);
            if(mappa.size()>0){
                lista.add(mappa);
            }
        }
        return lista;

    }

    public Map<String, List<TabellaMoltiplicazione>> assegnaAllaMappa( int prodottoDivMax, int prodottoDivMin, int fattoreConosciutoDivMax, int fattoreConosciutoDivMin){
        Map<String, List<TabellaMoltiplicazione>> mappa=new HashMap<>();
        List<TabellaMoltiplicazione> divisoreMinore=unFattoreSconosciutoUnoNo(prodottoDivMin, fattoreConosciutoDivMin);
        List<TabellaMoltiplicazione> divisoreMassimo=unFattoreSconosciutoUnoNo(prodottoDivMax,fattoreConosciutoDivMax);
        if(divisoreMassimo.size()>0 && divisoreMinore.size()>0){
            mappa.put(MASSIMO_DIVISORE, divisoreMassimo);
            mappa.put(MINIMO_DIVISORE, divisoreMinore);
        }
        return mappa;
    }
}
