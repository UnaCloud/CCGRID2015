import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;



public class BoincManager {
	static CPUReader cpu=new CPUReader();
	static int PM_CORES=Runtime.getRuntime().availableProcessors();
	static double META=15;
	public static void main(String[] args) throws Exception{
		if(args.length>0)META=Integer.parseInt(args[0]);
		cpu.start();
		if(META!=0)while(true){
			Thread.sleep(CPUReader.VENTANA*CPUReader.PERIODO);
			CPUReader.CPUInfo cpuInfo = cpu.new CPUInfo();
			List<BoinkTask> tareas=BoinkTask.getCurrentTasks();
			int max = getMaxVMs(cpuInfo.user);
			int ejecutando = 0;
			for(BoinkTask bt:tareas)if(bt.isRunning())ejecutando++;
			System.out.println("Ejecutando "+ejecutando+" Puedo "+max+" Total "+tareas.size());
			System.out.println("Usuario "+cpuInfo.user+" idle "+cpuInfo.idle+" "+cpuInfo.boinc);
			System.out.println();
			for(int e=0;ejecutando<max&&e<tareas.size();e++)if(tareas.get(e).isNotRunning()){
				tareas.get(e).resume();
				ejecutando++;
			}
			for(int e=tareas.size()-1;ejecutando>max&&e>=0;e--)if(tareas.get(e).isRunning()){
				tareas.get(e).suspend();
				ejecutando--;
			}
		}
		
	}
	static double[] intrusividades;
	public static int getMaxVMs(double userCPU){
		if(intrusividades==null){
			try{
				BufferedReader br=new BufferedReader(new FileReader("cpuinfo.txt"));
				intrusividades=new double[PM_CORES+1];
				for(String h,j[];(h=br.readLine())!=null;){
					j=h.split("\t");
					intrusividades[Integer.parseInt(j[0])]=Integer.parseInt(j[1]);
				}
				br.close();
			}catch (IOException e) {
				e.printStackTrace();
				return 0;
			}
		}
		int userCores = (int)((userCPU*PM_CORES)/100+0.5);
		if(userCores==0)userCores=1;
		for(int e=PM_CORES;e>userCores;e--){
			double intrusividadGenerada=(intrusividades[e]-intrusividades[userCores])/intrusividades[userCores];
			intrusividadGenerada*=100;
			if(intrusividadGenerada<META)return e-userCores;
		}
		return 0;
	}
}
