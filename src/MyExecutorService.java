import java.util.concurrent.*;

/*public class MyExecutorService {
    static ExecutorService executorService;

    public MyExecutorService(){
        if(executorService!=null){
            executorService.shutdownNow();
        }
        executorService= new ForkJoinPool();
    }

    public Divisori run(int[] numeroDaAnalizzare){
        Divisori res= (Divisori) executorService.submit(new ThreadCalcolo(numeroDaAnalizzare,0,null,0));
        executorService.shutdownNow();
        return res;
    }
}*/
