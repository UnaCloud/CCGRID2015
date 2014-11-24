package generadorInforme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

public class ProcesadorResultados {

	public static void processFile(File f){
		Map<Long,double[]> consumoCpu=new TreeMap<Long, double[]>();
		File mon=new File(f.getParentFile(),f.getName().replace(".csv",".txt"));
		try{
			if(mon.exists()){
				try(BufferedReader br=new BufferedReader(new FileReader(mon))){
					br.readLine();
					for(String h,j[];(h=br.readLine())!=null;){
						j=h.split("\\t");
						long t=Long.parseLong(j[0]);
						double[] da=new double[3];
						for(int e=0;e<da.length;e++)da[e]=Double.parseDouble(j[e+1]);
						consumoCpu.put(t, da);
					}
				}
			}
			BufferedReader br=new BufferedReader(new FileReader(f));
			for(String h,j[];(h=br.readLine())!=null;){
				j=h.split(";");
				if(j[0].equals("Prueba")){
					int cores=Integer.parseInt(j[1]);
					j=(h=br.readLine()).split(";");
					int cantidad=Integer.parseInt(j[1]);
					long total=0;
					long minTime=Long.MAX_VALUE,maxTime=0;
					br.readLine();
					for(int e=0;e<cantidad;e++){
						j=(h=br.readLine()).split(";");
						long endTime=Long.parseLong(j[3]);
						long startTime=Long.parseLong(j[2]);
						minTime=Math.min(minTime, startTime);
						maxTime=Math.max(minTime, endTime);
						long diff=endTime-startTime;
						total+=diff;
					}
					double[] valor=new double[3];
					final long limitInf=minTime,limitSup=maxTime;
					int[] totalMediciones=new int[1];
					consumoCpu.forEach((key,value)->{if(key>=limitInf&&key<=limitSup){
						for(int e=0;e<valor.length;e++)valor[e]+=value[e];
						totalMediciones[0]++;
					}});
					total/=cantidad;
					if(totalMediciones[0]==0)System.out.println(cores+"\t"+total);
					else{
						for(int e=0;e<valor.length;e++)valor[e]/=totalMediciones[0];
						System.out.print(cores+"\t"+total);
						for(int e=0;e<valor.length;e++)System.out.print("\t"+valor[e]);
						System.out.println();
					}
				}
			}
			br.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void processFile2(File f){
		try{
			BufferedReader br=new BufferedReader(new FileReader(f));
			for(String h,j[];(h=br.readLine())!=null;){
				j=h.split(";");
				if(j[0].equals("Prueba")){
					int cores=Integer.parseInt(j[1]);
					List<long[]> parejas=new ArrayList<long[]>();
					j=(h=br.readLine()).split(";");
					int cantidad=Integer.parseInt(j[1]);
					br.readLine();
					for(int e=0;e<cantidad;e++){
						j=(h=br.readLine()).split(";");
						long diff=Long.parseLong(j[3])-Long.parseLong(j[2]);
						parejas.add(new long[]{Long.parseLong(j[2]),diff});
					}
					Collections.sort(parejas,new Comparator<long[]>() {
						public int compare(long[] o1, long[] o2) {
							return Long.compare(o1[0], o2[0]);
						}
					});
					System.out.print(cores);
					for(long[] l:parejas)System.out.print("\t"+l[1]);
					System.out.println();
				}
			}
			br.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	static File root=new File("D:\\Trabajo\\201420\\Papers\\CCGrid2015\\Wayra");
	public static void main(String[] args) {
		/*for(int e=15;e<40;e+=5){
			System.out.println("4770-nointr"+e);
			processFile(new File(root,"4770-nointr"+e+".csv"));
			
		}
		System.out.println("4770-preferencias");
		processFile(new File(root,"4770-preferencias.csv"));*/
		System.out.println("4770-siempre");
		processFile(new File(root,"sinboinc.csv"));
	}
}
