import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;

public class CPUReader extends Thread{
	public static final int PERIODO = 15000;
	public static final int VENTANA = 8;
	public File monitoreoFile=new File("mon.txt");
	private Sigar proxy=new Sigar();
	private double[] idle=new double[VENTANA],boinc=new double[VENTANA],usuario=new double[VENTANA];
	ArrayList<InfoPoint> datos=new ArrayList<InfoPoint>();
	@Override
	public void run() {
		long startTime=System.currentTimeMillis(),n=1;
		for(int v=0;;v=(v+1)%VENTANA,n++){
			try{
				CpuPerc cpu=proxy.getCpuPerc();
				idle[v]=cpu.getIdle()*100;
				boinc[v]=0;
				for(long pid:proxy.getProcList()){
		            try{
		            	ProcState ps=proxy.getProcState(pid);
		            	String name=ps.getName();
		            	int prioridad=ps.getPriority();
		            	if(prioridad<=6||name.equals("boinc")){
		            		boinc[v]+=proxy.getProcCpu(pid).getPercent();
		            	}
		            }catch(Exception ex){
		            	ex.printStackTrace();
		            }
		        }
				boinc[v]*=(100d/8d);
				usuario[v]=(100-idle[v]-boinc[v]);
				datos.add(new InfoPoint(idle[v], usuario[v], boinc[v]));
				imprimirReporte();
				long aEsperar=n*PERIODO+startTime-System.currentTimeMillis();
				Thread.sleep(aEsperar);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	public class CPUInfo{
		public double idle,user,boinc,boincmng;
		public CPUInfo() {
			for(int e=0;e<VENTANA;e++){
				idle+=CPUReader.this.idle[e];
				user+=CPUReader.this.usuario[e];
				boinc+=CPUReader.this.boinc[e];
			}
			idle/=VENTANA;
			user/=VENTANA;
			boinc/=VENTANA;
		}
		@Override
		public String toString() {
			return "CPUInfo [idle=" + idle + ", user=" + user + ", boinc="
					+ boinc + "]";
		}
	}
	public class InfoPoint{
		long time;
		double idle,user,boinc;
		public InfoPoint(double idle, double user, double boinc) {
			time=System.currentTimeMillis();
			this.idle = idle;
			this.user = user;
			this.boinc = boinc;
		}
		@Override
		public String toString() {
			return time+"\t"+idle+"\t"+user+"\t"+boinc;
		}
	}
	public void imprimirReporte(){
		if(datos.size()>=20){
			boolean header=!monitoreoFile.exists();
			try{
				PrintWriter pw=new PrintWriter(new FileOutputStream(monitoreoFile, true));
				if(header)pw.println("time\tidle\tuser\theader");
				for(int e=0,s=datos.size();e<s;e++){
					pw.println(datos.get(e));
				}
				pw.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			datos.clear();
		}
		
	}
}

            
