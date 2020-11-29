import java.util.*;

public class UtilsV2 {

    public final static String MASSIMO_DIVISORE="MassimoDivisore";
    public final static String MINIMO_DIVISORE="MinimoDivisore";
    final String RIPORTO="Riporto";

    static Map<Integer, List<TabellaMoltiplicazione>> listaMoltiplicazione= new HashMap<>();
    static Map<Integer, List<TabellaAddizione>> listaAddizione= new HashMap<>();

    private List<Integer> div1=new ArrayList<>();
    private List<Integer> div2=new ArrayList<>();
    List<Integer> numeroRev= new ArrayList<>();
    List<List<TabellaMoltiplicazione>> divisori=new ArrayList<>();
    //List<Set<TabellaMoltiplicazione>> blackListDivs=new ArrayList<>();
    Map<Integer,Map<Integer,Integer>> riporti=new HashMap<>();
    //posizione,chi,valore
    static{
        for(int i=0;i<10;i++) {
            listaMoltiplicazione.put(i, new ArrayList<>());
            listaAddizione.put(i, new ArrayList<>());
        }
        for(int i=0;i<10;i++){

            for(int j=0;j<10;j++){
                listaMoltiplicazione.get((j*i)%10).add(new TabellaMoltiplicazione( i, j,  ((j*i)/10)));
                listaAddizione.get((j+i)%10).add(new TabellaAddizione( i, j, ((j+i)/10)));
            }
        }
    }

    public void printTabelle(){
        System.out.println(listaMoltiplicazione.values().toString());
        System.out.println(listaAddizione.values().toString());

    }

    public UtilsV2(int[] numeroDaAnalizzare){

        for(int num:numeroDaAnalizzare) {
            numeroRev.add(num);
            //blackListDivs.add(new HashSet<>());

        }
    }

    public Map<String, List<Integer>> calcolaPrimi(){
        int cifreTotDivs=numeroRev.size();
        int cifreDivMin=1;
        int cifreDivMax=cifreTotDivs-cifreDivMin+1;
        while(cifreDivMin<=cifreDivMax){

            if(cifreDivMin<=(cifreDivMax-1) && calcolaDivisori(cifreDivMin, cifreDivMax-1)){
                Map<String,List<Integer>> mappa= new HashMap<>();
                mappa.put(MINIMO_DIVISORE,div1);
                mappa.put(MASSIMO_DIVISORE,div2);
                return mappa;
            }
            if(calcolaDivisori(cifreDivMin, cifreDivMax)){
                Map<String,List<Integer>> mappa= new HashMap<>();
                mappa.put(MINIMO_DIVISORE,div1);
                mappa.put(MASSIMO_DIVISORE,div2);
                return mappa;
            }
            cifreDivMin++;
            cifreDivMax=cifreTotDivs-cifreDivMin+1;
        }
        return null;
    }

    private boolean calcolaDivisori(int cifreDivMin, int cifreDivMax) {

        divisori.add(new ArrayList<>(fattoriSconosciuti(numeroRev.get(0))));
        TabellaMoltiplicazione tab =divisori.get(divisori.size()-1).get(0);
        addToDivs(tab);
        addRiporto(tab.riporto, 0+1, 0);

        while(divisori.size()>0) {

            while (cifreDivMin -1> divisori.size()-1) {
                ProdRip prodRip;
                int cifraToSubtract=0;
               // if(divisori.size()-1>0) {
                    prodRip = calcoloCifraUnFattoreSconosciuto();
                    cifraToSubtract = prodRip.cifraProdotto;
                //}
                ProdRip prodRip2=sommaRiporti(cifraToSubtract,divisori.size());
                cifraToSubtract=prodRip2.cifraProdotto;

                int realCifra=numeroRev.get(divisori.size())-cifraToSubtract;

                if(realCifra<0){
                    //realCifra=Math.abs(realCifra);
                    realCifra=(numeroRev.get(divisori.size())+10)-cifraToSubtract;
                    addRiporto(1,divisori.size()+1, divisori.size()-1);
                }

                divisori.add(new ArrayList<>(moltiplicazioneIncrociata(realCifra, div2.get(0), div1.get(0),prodRip2.riporto +prodRip.riporto )));
                tab =divisori.get(divisori.size()-1).get(0);
                addToDivs(tab);
                addRiporto(tab.riporto,divisori.size(), divisori.size()-1);

            }

            //da sistemare interno if true
            /*if(div1.size()==div2.size() && div1.size()>1 && !(cifreDivMax > div2.size())){
                int pro=div1.get(div1.size()-1)*div2.get(div2.size()-1);
                addRiporto(pro,divisori.size(), divisori.size()-1);

            }
            else{*/

                while (cifreDivMax > div2.size()) {
                    ProdRip pr = calcoloCifraUnFattoreSconosciuto();

                    int cifraToSubtract = pr.cifraProdotto;
                    ProdRip pr2 = sommaRiporti(cifraToSubtract, divisori.size());
                    cifraToSubtract = pr2.cifraProdotto;
                    //System.out.println("Cifra to subtract: " + cifraToSubtract);

                    int realCifra=numeroRev.get(divisori.size())-cifraToSubtract;
                    if(realCifra<0){
                        realCifra=(numeroRev.get(divisori.size())+10)-cifraToSubtract;
                        addRiporto(1,divisori.size()+1, divisori.size()-1);
                    }

                    List tempList = new ArrayList<>(unFattoreSconosciutoUnoNo(realCifra, div1.get(0)));
                    if (tempList.size() > 0) {
                        divisori.add(tempList);
                        tab = divisori.get(divisori.size() - 1).get(0);
                        addToDivs(tab);
                        addRiporto(tab.riporto + pr2.riporto+pr.riporto, divisori.size(), divisori.size() - 1);
                    }
                    else {
                        break;
                    }/*else {
                       System.out.println("Lista vuota");
                        //printTabelle();
                        rimozioneRicorsiva();
                    }*/


                }


            //}
            if(cifreDivMin==div1.size() && cifreDivMax==div2.size()) {
                moltiplicazioniConclusive();
            }
            if (verificaDivisore( divisori.size()-1)) {
                if (precontrolloRisFinale()) {
                    return true;
                }
            }

            rimozioneRicorsiva();

        }
        return false;
    }

    public void moltiplicazioniConclusive(){
        int index=divisori.size();
        for(int k=1;k<div1.size();k++) {
            int cifra = 0, rip = 0;
            int j = div2.size() - 1;
            for (int i = k; i < div1.size(); i++) {
                ProdRip temp = unitaERiporto(div1.get(i), div2.get(j));
                cifra += temp.cifraProdotto;
                rip += temp.riporto;
                j--;
            }
            if (cifra > 0)
                rip += cifra / 10;
            cifra = cifra % 10;
            addRiporto(cifra + (rip * 10), index, index - 1); // sostituisci index and who
            index++;
        }
    }


    public void rimozioneRicorsiva(){
        int index;

        while(divisori.size()>0 && divisori.get(divisori.size()-1).size()<2) {
            index=divisori.size()-1;
            if (divisori.get(divisori.size()-1).size() == 0) {
                divisori.remove(index);
            }
            else{
                TabellaMoltiplicazione removed=divisori.get(index).remove(0);
                rimuoviCifreDiv1Div2();
                //possibile indice errato
               // removeRiporto(index+1,index);
                removeRiporto(index);
            }
        }
        if(divisori.size()>0 && divisori.get(divisori.size()-1).size()>=2){
            index=divisori.size()-1;
            TabellaMoltiplicazione removed=divisori.get(index).remove(0);
            rimuoviCifreDiv1Div2();
            //removeRiporto(index+1,index);
            removeRiporto(index);
            //da verificare
            index=divisori.size()-1;
            TabellaMoltiplicazione tab =divisori.get(index).get(0);
            addToDivs(tab);

            addRiporto(tab.riporto,divisori.size(), divisori.size()-1);
        }

    }

    public void rimuoviCifreDiv1Div2(){
        if(div2.size()==div1.size()){
            div1.remove(div1.size()-1);
        }
        div2.remove(div2.size()-1);
    }

    public void addToDivs(TabellaMoltiplicazione tab){
        if(tab.fattore1!=null) {
            div1.add(tab.fattore1);
        }
        div2.add(tab.fattore2);
    }


    /*public void removeRiporto(int indexFrom, int who){
        Map<Integer,Integer> map;
        int i=indexFrom;
        while((map=riporti.get(i))!=null){
            map.remove(who);
            if(riporti.get(i).isEmpty()){
                riporti.remove(i);
            }
            i++;
        }
    }*/
    public void removeRiporto(int who) {
        for(Integer pos : riporti.keySet()) {
            riporti.get(pos).keySet().removeIf(whoAnalyze -> whoAnalyze>=who);
        }
        riporti.keySet().removeIf(index->riporti.get(index).isEmpty());
    }

    //da fare dopo add to divs
    public void addRiporto(int riporto, int indexFrom, int who){
        int sum=riporto;
        int cifra;
        int index=indexFrom;
        Map<Integer, Integer> map;
        while(sum>0) {
            cifra=sum%10;
            sum=sum/10;
            map = riporti.get(index);
            if (map != null) {
                Integer value=map.get(who);
                if(value!=null){
                    map.put(who,cifra+value);
                }
                else {
                    map.put(who, cifra);
                }
            } else {
                map = new HashMap<>();
                map.put(who,cifra);
                riporti.put(index, map);
            }
            index++;
        }

    }

    public ProdRip sommaRiporti(int cifraToRemove, int index){
        int sum=sommaRiportiMappa(riporti.get(index));
        sum+=cifraToRemove;
        if(sum>0){
            return new ProdRip(sum%10, sum/10);
        }
        else{
            return new ProdRip(0,0);
        }
    }

    public int sommaRiportiMappa(Map<Integer,Integer> map){
        int sum=0;
        if(map!=null) {
            for (int key : map.keySet()) {
                sum += map.get(key);
            }
        }
        return sum;
    }

    private boolean precontrolloRisFinale() {
        if(div1.size()==1 && div1.get(0)<2){
            return false;
        }
        else{
            if(div2.size()==1 && div2.get(0)<2){
                return false;
            }
        }
        return true;
    }

    private boolean verificaDivisore(int workingCifraIndex) {
        List<Integer> ultimeCifreNum= new ArrayList<>();
        int index=workingCifraIndex+1;
        int sum=0;
        Map<Integer,Integer> mappa;
        while((mappa=riporti.get(index))!=null){
            sum+= sommaRiportiMappa(mappa);
            ultimeCifreNum.add(sum%10);
            sum=sum/10;
            index++;
        }
        while(sum>0){
            ultimeCifreNum.add(sum%10);
            sum=sum/10;
        }
        List<Integer> lastCifre=numeroRev.subList(workingCifraIndex+1, numeroRev.size());
        //return lastCifre.equals(ultimecifreNum);

        if(lastCifre.size()!=ultimeCifreNum.size())
            return false;

        for(int i=0;i<ultimeCifreNum.size();i++){

            if(!(ultimeCifreNum.get(i).equals(lastCifre.get(i)))){
               // System.out.println("Cifra calcolata: "+ultimeCifreNum.get(i));
                //System.out.println("Cifra da analizzare: "+lastCifre.get(i));
                return false;
            }
        }
        return true;
    }

    public ProdRip calcoloCifraUnFattoreSconosciuto() {
        int cifra=0, rip=0;
        int j=div2.size()-1;
        for(int i=1;i<div1.size();i++){
            ProdRip temp=unitaERiporto(div1.get(i),div2.get(j));
            cifra+=temp.cifraProdotto;
            rip+=temp.riporto;
            j--;
        }
        if(cifra>0)
            rip+=cifra/10;
        cifra=cifra%10;
        return new ProdRip(cifra,rip);
    }

    public ProdRip unitaERiporto(int cifra1, int cifra2){
        int prodotto=(cifra1*cifra2);
        return new ProdRip(prodotto%10, prodotto/10);

    }

    /*
    Quando i fattori sono tutti sconosciuti
    @return lista Fattori relative alla prima cifra da analizzare
     */
    public List<TabellaMoltiplicazione> fattoriSconosciuti(int cifraDaAnalizzare){
        //divisori.add(listaMoltiplicazione.get(cifraDaAnalizzare));
        return listaMoltiplicazione.get(cifraDaAnalizzare);
    }

    /*
    Quando hai una cifra da analizzare e conosci solo una delle due cifre dei divisori
    @return lista di fattori
     */
    public List<TabellaMoltiplicazione> unFattoreSconosciutoUnoNo(int realCifra, int fattoreConosciuto)  {
        List<TabellaMoltiplicazione> lista=new ArrayList<>();

       // System.out.println("Realcifra: "+realCifra);
        for(TabellaMoltiplicazione combinazione : listaMoltiplicazione.get(realCifra)){
            if(combinazione.fattore1==fattoreConosciuto){
                TabellaMoltiplicazione temp=new TabellaMoltiplicazione( combinazione.fattore1, combinazione.fattore2,combinazione.riporto);
                temp.fattore1=null;
                lista.add(temp);
            }

        }

        return lista;
    }

    public List<ProdRip> SconosciutoUnoNo(int cifraDaAnalizzare, int fattoreConosciuto){
        List<ProdRip> lista=new ArrayList<>();
        for(TabellaMoltiplicazione combinazione : listaMoltiplicazione.get(cifraDaAnalizzare)){
            if(combinazione.fattore1==fattoreConosciuto ){
                lista.add(new ProdRip(combinazione.fattore2, combinazione.riporto));
            }
            //da togliere l'else
            else{
                if(combinazione.fattore2==fattoreConosciuto){
                    lista.add(new ProdRip(combinazione.fattore1, combinazione.riporto));
                }
            }
        }
        return lista;
    }

    public List<TabellaMoltiplicazione> moltiplicazioneIncrociata(int cifraReal, int fattoreConosciutoDivMax, int fattoreConosciutoDivMin, int riporto){
        List<TabellaMoltiplicazione> lista= new ArrayList<>();

        List<TabellaAddizione> listaAddizioni=listaAddizione.get(cifraReal);
        for(TabellaAddizione combinazioneSomma: listaAddizioni){
            lista.addAll(assegnaAllaMappa(combinazioneSomma.addendo1, combinazioneSomma.addendo2, fattoreConosciutoDivMax, fattoreConosciutoDivMin, combinazioneSomma.riporto+riporto));

        }

        return lista;

    }


    public Set<TabellaMoltiplicazione> assegnaAllaMappa( int prodottoDivMax, int prodottoDivMin, int fattoreConosciutoDivMax, int fattoreConosciutoDivMin, int sommaRip){
        Set<TabellaMoltiplicazione> lista= new HashSet<>();
        List<ProdRip> divisoreMassimo=SconosciutoUnoNo(prodottoDivMin, fattoreConosciutoDivMin);
        List<ProdRip> divisoreMinore=SconosciutoUnoNo(prodottoDivMax,fattoreConosciutoDivMax);
        for(int j=0;j<divisoreMinore.size();j++) {
            for (int i = 0; i < divisoreMassimo.size(); i++) {
                lista.add(new TabellaMoltiplicazione(divisoreMinore.get(j).cifraProdotto, divisoreMassimo.get(i).cifraProdotto, divisoreMinore.get(j).riporto + divisoreMassimo.get(i).riporto+sommaRip));
            }
        }
        return lista;
    }
}
