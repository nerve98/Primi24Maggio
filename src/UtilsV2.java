import java.util.*;

public class UtilsV2 {

    public final static String MASSIMO_DIVISORE="MassimoDivisore";
    public final static String MINIMO_DIVISORE="MinimoDivisore";
    final String RIPORTO="Riporto";

    static Map<Integer, Set<TabellaMoltiplicazione>> listaMoltiplicazione= new HashMap<>();
    static Map<Integer, Set<TabellaAddizione>> listaAddizione= new HashMap<>();

    private List<Integer> div1=new ArrayList<>();
    private List<Integer> div2=new ArrayList<>();
    List<Integer> numeroRev= new ArrayList<>();
    List<List<TabellaMoltiplicazione>> divisori=new ArrayList<>();
    //List<Set<TabellaMoltiplicazione>> blackListDivs=new ArrayList<>();


    static{
        for(int i=0;i<10;i++) {
            listaMoltiplicazione.put(i, new HashSet<>());
            listaAddizione.put(i, new HashSet<>());
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
        int cifreDivMax=cifreTotDivs-cifreDivMin;
        while(cifreDivMin<=cifreDivMax){

            if(calcolaDivisori(cifreDivMin, cifreDivMax)){
                Map<String,List<Integer>> mappa= new HashMap<>();
                mappa.put(MINIMO_DIVISORE,div1);
                mappa.put(MASSIMO_DIVISORE,div2);
                return mappa;
            }
            if(calcolaDivisori(cifreDivMin, cifreDivMax+1)){
                Map<String,List<Integer>> mappa= new HashMap<>();
                mappa.put(MINIMO_DIVISORE,div1);
                mappa.put(MASSIMO_DIVISORE,div2);
                return mappa;
            }
            cifreDivMin++;
            cifreDivMax=cifreTotDivs-cifreDivMin;
        }
        return null;
    }

    private boolean calcolaDivisori(int cifreDivMin, int cifreDivMax) {

        int riporto;//, cifraToSubtract;
        //blackListDivs.add(new HashSet<>());
        divisori.add(new ArrayList<>(fattoriSconosciuti(numeroRev.get(0))));

        while(divisori.size()>0) {
            riporto=0;
           // cifraToSubtract=0;
            if(divisori.size()-1==0) {
                TabellaMoltiplicazione tab = divisori.get(0).get(0);
                riporto = tab.riporto;
                addToDivs(tab);

            }
            while (cifreDivMin -1> divisori.size()-1) {
                if(divisori.size()-1>0) {
                    ProdRip prodRip = calcoloCifraUnFattoreSconosciuto();

                    int cifraToSubtract = prodRip.cifraProdotto;
                    if (riporto > 0) {
                        cifraToSubtract += riporto % 10;
                        riporto = riporto / 10;
                    }
                    riporto += prodRip.riporto;
                    divisori.add(new ArrayList<>(moltiplicazioneIncrociata(numeroRev.get(divisori.size() - 1), div2.get(0), div1.get(0), cifraToSubtract, divisori.size() - 1)));

                    addToDivs(divisori.get(divisori.size() - 1).get(0));
                }
                else {
                    //blackListDivs.add(new HashSet<>());
                    divisori.add(new ArrayList<>(moltiplicazioneIncrociata(numeroRev.get(divisori.size() - 1), div2.get(0), div1.get(0), riporto, divisori.size() - 1)));

                    addToDivs(divisori.get(divisori.size() - 1).get(0));

                }
            }
            while (cifreDivMax -1> divisori.size()-1) {
                ProdRip pr = calcoloCifraUnFattoreSconosciuto();
                int cifraToSubtract = pr.cifraProdotto;
                if (riporto > 0) {
                    cifraToSubtract += riporto % 10;
                    riporto = riporto / 10;
                }
                riporto += pr.riporto;
                //blackListDivs.add(new HashSet<>());
                divisori.add(new ArrayList<>(unFattoreSconosciutoUnoNo(numeroRev.get(divisori.size()-1) - pr.cifraProdotto, div1.get(0), cifraToSubtract)));

                addToDivs(divisori.get(divisori.size()-1).get(0));

            }
            if (verificaDivisore(riporto)) {
                if (precontrolloRisFinale()) {
                    return true;
                }
            }

            rimozioneRicorsiva();

        }
        return false;
    }

    public void rimozioneRicorsiva(){

        while(divisori.size()>0 && divisori.get(divisori.size()-1).size()<2) {

            if (divisori.get(divisori.size()-1).size() == 0) {
                divisori.remove(divisori.size()-1);


            }
            else{
                int index=divisori.size()-1;
                TabellaMoltiplicazione removed=divisori.get(index).remove(0);
                rimuoviCifreDiv1Div2();
                //blackListDivs.get(index).add(removed);
            }
        }
        if(divisori.size()>0){
            int index=divisori.size()-1;
            TabellaMoltiplicazione removed=divisori.get(index).remove(0);
            rimuoviCifreDiv1Div2();
            //blackListDivs.get(index).add(removed);
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

    private boolean precontrolloRisFinale() {
        if(div1.size()==1 && div1.get(0)<2){
            return false;
        }
        return true;
    }

    private boolean verificaDivisore(int riporto) {
        List<Integer> ultimecifreNum= new ArrayList<>();
        int cifra=0, rip=riporto;

        for(int i=1;i<div1.size();i++){
            int j=div2.size()-1;
            for(int k=i;k<div1.size();k++) {
                ProdRip temp = unitaERiporto(div1.get(i), div2.get(j));
                cifra += temp.cifraProdotto;
                rip += temp.riporto;
                j--;
            }
            if(cifra>0)
                rip+=cifra/10;
            cifra=cifra%10;
            ultimecifreNum.add(cifra);
            cifra=rip%10;
            if(rip>0)
                rip=rip/10;
        }
        while(rip>0){
            cifra=rip%10;
            ultimecifreNum.add(cifra);
            rip=rip/10;
        }
        if(ultimecifreNum.size()==0)
            return false;
        List<Integer> lastCifre=numeroRev.subList(numeroRev.size()-ultimecifreNum.size(), numeroRev.size());
        //return lastCifre.equals(ultimecifreNum);

        for(int i=0;i<ultimecifreNum.size();i++){
            if(!(ultimecifreNum.get(i).equals(lastCifre.get(i)))){
                System.out.println("Cifra calcolata: "+ultimecifreNum.get(i));
                System.out.println("Cifra da analizzare: "+lastCifre.get(i));
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
    public Set<TabellaMoltiplicazione> fattoriSconosciuti(int cifraDaAnalizzare){
        //divisori.add(listaMoltiplicazione.get(cifraDaAnalizzare));
        return listaMoltiplicazione.get(cifraDaAnalizzare);
    }

    /*
    Quando hai una cifra da analizzare e conosci solo una delle due cifre dei divisori
    @return lista di fattori
     */
    public Set<TabellaMoltiplicazione> unFattoreSconosciutoUnoNo(int cifraDaAnalizzare, int fattoreConosciuto, int riporto)  {
        Set<TabellaMoltiplicazione> lista=new HashSet<>();
        int realCifra=Math.abs(cifraDaAnalizzare-riporto);
        for(TabellaMoltiplicazione combinazione : listaMoltiplicazione.get(realCifra)){
            if(combinazione.fattore1==fattoreConosciuto){
                TabellaMoltiplicazione temp=new TabellaMoltiplicazione( combinazione.fattore1, combinazione.fattore2,combinazione.riporto);
                temp.fattore1=null;
                lista.add(temp);
            }
            else{
                if(combinazione.fattore2==fattoreConosciuto){
                    TabellaMoltiplicazione temp=new TabellaMoltiplicazione( combinazione.fattore1, combinazione.fattore2,combinazione.riporto);
                    temp.fattore2=null;
                    lista.add(temp);
                }
            }
        }
        //divisori.add(lista);
        return lista;
    }

    public List<ProdRip> SconosciutoUnoNo(int cifraDaAnalizzare, int fattoreConosciuto){
        List<ProdRip> lista=new ArrayList<>();
        for(TabellaMoltiplicazione combinazione : listaMoltiplicazione.get(cifraDaAnalizzare)){
            if(combinazione.fattore1==fattoreConosciuto ){
                lista.add(new ProdRip(combinazione.fattore2, combinazione.riporto));
            }
            else{
                if(combinazione.fattore2==fattoreConosciuto){
                    lista.add(new ProdRip(combinazione.fattore1, combinazione.riporto));
                }
            }
        }
        return lista;
    }

    public Set<TabellaMoltiplicazione> moltiplicazioneIncrociata(int cifraDaAnalizzare, int fattoreConosciutoDivMax, int fattoreConosciutoDivMin, int riportoUnita, int indexNumRev){
        Set<TabellaMoltiplicazione> lista= new HashSet<>();
        int cifraReal=Math.abs(cifraDaAnalizzare-riportoUnita);
        Set<TabellaAddizione> listaAddizioni=listaAddizione.get(cifraReal);
        for(TabellaAddizione combinazioneSomma: listaAddizioni){
            lista.addAll(assegnaAllaMappa(combinazioneSomma.addendo1, combinazioneSomma.addendo2, fattoreConosciutoDivMax, fattoreConosciutoDivMin));
            //lista.removeAll(blackListDivs.get(indexNumRev));
            //lista.addAll(assegnaAllaMappa(combinazioneSomma.addendo2, combinazioneSomma.addendo1, fattoreConosciutoDivMax, fattoreConosciutoDivMin));

        }

        return lista;

    }


    public Set<TabellaMoltiplicazione> assegnaAllaMappa( int prodottoDivMax, int prodottoDivMin, int fattoreConosciutoDivMax, int fattoreConosciutoDivMin){
        Set<TabellaMoltiplicazione> lista= new HashSet<>();
        List<ProdRip> divisoreMinore=SconosciutoUnoNo(prodottoDivMin, fattoreConosciutoDivMin);
        List<ProdRip> divisoreMassimo=SconosciutoUnoNo(prodottoDivMax,fattoreConosciutoDivMax);
        for(int j=0;j<divisoreMinore.size();j++) {
            for (int i = 0; i < divisoreMassimo.size(); i++) {
                lista.add(new TabellaMoltiplicazione(divisoreMinore.get(j).cifraProdotto, divisoreMassimo.get(i).cifraProdotto, divisoreMinore.get(j).riporto + divisoreMassimo.get(i).riporto));
            }
        }
        return lista;
    }
}
