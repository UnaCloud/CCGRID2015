package boinc;

import java.util.concurrent.Callable;

public class BoincTask implements Callable<Long>{
	public long startTime,endTime,result;
	public long size;
	public long threadId;
	public BoincTask(long size) {
		this.size=size;
	}
	@Override
	public Long call() throws Exception {
		threadId=Thread.currentThread().getId();
		startTime=System.currentTimeMillis();
		result=0;
		for(long i = 2,c=0; i < size; i++,c=0){
            for (long j = 2; j < i; j++)if(i%j==0)c++;
            if(c==0)result++;
        }
		endTime=System.currentTimeMillis();
		
        return result;
	}
	@Override
	public String toString() {
		return size+";"+threadId+";"+startTime+";"+endTime+";"+result;
	}
}
