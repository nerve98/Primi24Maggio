public class TabellaAddizione {
    int addendo1;
    int addendo2;
    int riporto;

    public TabellaAddizione(int addendo1, int addendo2, int riporto) {
        this.addendo1 = addendo1;
        this.addendo2 = addendo2;
        this.riporto = riporto;
    }

    @Override
    public String toString() {
        return "TabellaAddizione{" +
                "addendo1=" + addendo1 +
                ", addendo2=" + addendo2 +
                ", riporto=" + riporto +
                '}';
    }
}
