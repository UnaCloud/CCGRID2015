package boinc;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProbadorMain{
	static int PM_CORES=Runtime.getRuntime().availableProcessors();
	public static void main(String[] args)throws Exception{
		int start = 1;
		if(args.length==2)start=Integer.parseInt(args[1]);
		if(args.length<=2&&args.length>=1){
			for(int e=start;e<=PM_CORES;e++){
				System.out.println(e);
				ExecutorService exec=Executors.newFixedThreadPool(e);
				Prueba p=new Prueba(e,20,110000L);
				for(int j=0;j<p.pruebas.length;j++)p.futures[j]=exec.submit(p.pruebas[j]);
				for(int j=0;j<p.pruebas.length;j++)p.futures[j].get();
				exec.shutdown();
				try{
					PrintWriter pw=new PrintWriter(new FileOutputStream(args[0]+".csv",e!=1));
					pw.println(p);
					pw.println("size;threadId;startTime;endTime;result");
					for(BoincTask bt:p.pruebas)pw.println(bt);
					pw.println();
					pw.close();
				}catch(Exception ex){
					ex.printStackTrace();
				}
				Thread.sleep(5*60*1000l);
			}
		}else{
			System.out.println("Invalid usage. One parameter only.");
		}
		
	}
	
}
