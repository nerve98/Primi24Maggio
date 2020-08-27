import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Divisori {

    ArrayList<Integer> divMax;
    ArrayList<Integer> divMin;

    public Divisori(){
        divMax= new ArrayList<>();
        divMin= new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Divisori)) return false;
        Divisori divisori = (Divisori) o;
        return Objects.equals(divMax, divisori.divMax) &&
                Objects.equals(divMin, divisori.divMin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(divMax, divMin);
    }

    @Override
    protected Object clone() {
        Divisori res= new Divisori();
        res.divMax.addAll(this.divMax);
        res.divMin.addAll(this.divMin);
        return res;
    }
}
