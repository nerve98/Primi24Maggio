public class TabellaMoltiplicazione {
    int fattore1;
    int fattore2;
    int riporto;

    public TabellaMoltiplicazione(int fattore1, int fattore2, int riporto) {
        this.fattore1 = fattore1;
        this.fattore2 = fattore2;
        this.riporto = riporto;
    }

    @Override
    public String toString() {
        return "TabellaMoltiplicazione{" +
                "fattore1=" + fattore1 +
                ", fattore2=" + fattore2 +
                ", riporto=" + riporto +
                '}';
    }
}
