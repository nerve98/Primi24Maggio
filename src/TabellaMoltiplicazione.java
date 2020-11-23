import java.util.Objects;

public class TabellaMoltiplicazione {
    Integer fattore1;
    Integer fattore2;
    Integer riporto;

    public TabellaMoltiplicazione(int fattore1, int fattore2, int riporto) {
        this.fattore1 = fattore1;
        this.fattore2 = fattore2;
        this.riporto = riporto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabellaMoltiplicazione)) return false;
        TabellaMoltiplicazione that = (TabellaMoltiplicazione) o;
        return Objects.equals(fattore1, that.fattore1) &&
                Objects.equals(fattore2, that.fattore2) &&
                Objects.equals(riporto, that.riporto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fattore1, fattore2, riporto);
    }

    @Override
    public String toString() {
        return "TabellaMoltiplicazione{" +
                "fattore1=" + fattore1 +
                ", fattore2=" + fattore2 +
                ", riporto=" + riporto +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
