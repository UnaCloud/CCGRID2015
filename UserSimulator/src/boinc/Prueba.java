package boinc;

import java.util.concurrent.Future;

public class Prueba {

	public int cores;
	public BoincTask[] pruebas;
	public Future<?>[] futures;
	public Prueba(int cores,int times,long size) {
		this.cores = cores;
		pruebas=new BoincTask[cores*times];
		for(int e=0;e<pruebas.length;e++)pruebas[e]=new BoincTask(size);
		futures=new Future<?>[cores*times];
	}
	@Override
	public String toString() {
		return "Prueba;"+cores+"\nCantidad;"+pruebas.length;
	}
	
}
