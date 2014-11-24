import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class BoinkTask implements Comparable<BoinkTask>{
	public static List<BoinkTask> getCurrentTasks(){
		List<BoinkTask> tasks=new ArrayList<BoinkTask>();
		try{
			Process o = Runtime.getRuntime().exec(new String[]{"C:\\Program Files\\BOINC\\boinccmd.exe","--get_simple_gui_info"});
			BufferedReader br=new BufferedReader(new InputStreamReader(o.getInputStream()));
			while(!br.readLine().startsWith("======== Tasks ========"));
			for(String h=br.readLine();h!=null&&!h.endsWith("-----");h=br.readLine());
			while(true)tasks.add(new BoinkTask(br));
		}catch(Exception ex){
		}
		Collections.sort(tasks);
		return tasks;
	}
	private String name,WUname,projectURL,deadline,CPUtime,state,schedulerState,activeTaskState;
	private boolean readyToReport,serverACK,suspended;
	private int exitStatus,signal;
	private double estimatedCPUtimeRemaining;
	private Map<String, String> parametros=new TreeMap<String, String>();
	public BoinkTask(BufferedReader br)throws Exception{
		for(String h=br.readLine();h!=null&&!h.contains("-----------");h=br.readLine())getFromString(h);
		if(parametros.isEmpty())throw new Exception("No more tasks");
		name=parametros.get("name");
		WUname=parametros.get("WU name");
		projectURL=parametros.get("project URL");
		deadline=parametros.get("report deadline");
		readyToReport=parametros.get("ready to report").equals("yes");
		serverACK = parametros.get("got server ack").equals("yes");
		state = parametros.get("state");
		schedulerState = parametros.get("scheduler state");
		exitStatus = Integer.parseInt(parametros.get("exit_status"));
		signal = Integer.parseInt(parametros.get("signal"));
		suspended = parametros.get("suspended via GUI").equals("yes");
		activeTaskState= parametros.get("active_task_state");
		estimatedCPUtimeRemaining = Double.parseDouble(parametros.get("estimated CPU time remaining"));
	}
	private void getFromString(String h){
		h=h.trim();
		String key=h.substring(0,h.indexOf(':')).trim();
		String value=h.substring(h.indexOf(':')+1).trim();
		parametros.put(key, value);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWUname() {
		return WUname;
	}
	public void setWUname(String wUname) {
		WUname = wUname;
	}
	public String getProjectURL() {
		return projectURL;
	}
	public void setProjectURL(String projectURL) {
		this.projectURL = projectURL;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getCPUtime() {
		return CPUtime;
	}
	public void setCPUtime(String cPUtime) {
		CPUtime = cPUtime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSchedulerState() {
		return schedulerState;
	}
	public void setSchedulerState(String schedulerState) {
		this.schedulerState = schedulerState;
	}
	public String getActiveTaskState() {
		return activeTaskState;
	}
	public void setActiveTaskState(String activeTaskState) {
		this.activeTaskState = activeTaskState;
	}
	public boolean isReadyToReport() {
		return readyToReport;
	}
	public void setReadyToReport(boolean readyToReport) {
		this.readyToReport = readyToReport;
	}
	public boolean isServerACK() {
		return serverACK;
	}
	public void setServerACK(boolean serverACK) {
		this.serverACK = serverACK;
	}
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	public int getExitStatus() {
		return exitStatus;
	}
	public void setExitStatus(int exitStatus) {
		this.exitStatus = exitStatus;
	}
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public double getEstimatedCPUtimeRemaining() {
		return estimatedCPUtimeRemaining;
	}
	public void setEstimatedCPUtimeRemaining(double estimatedCPUtimeRemaining) {
		this.estimatedCPUtimeRemaining = estimatedCPUtimeRemaining;
	}
	public void resume(){
		try{
			Process o = Runtime.getRuntime().exec(new String[]{"C:\\Program Files\\BOINC\\boinccmd.exe","--task",projectURL,name,"resume"});
			BufferedReader br=new BufferedReader(new InputStreamReader(o.getInputStream()));
			for(String h;(h=br.readLine())!=null;)System.out.println(h);
		}catch(Exception ex){
		}
	}
	public void suspend(){
		try{
			Process o = Runtime.getRuntime().exec(new String[]{"C:\\Program Files\\BOINC\\boinccmd.exe","--task",projectURL,name,"suspend"});
			BufferedReader br=new BufferedReader(new InputStreamReader(o.getInputStream()));
			for(String h;(h=br.readLine())!=null;)System.out.println(h);
		}catch(Exception ex){
		}
	}
	public boolean isRunning(){
		return activeTaskState != null&&(activeTaskState.equals("EXECUTING"));
	}
	public boolean isNotRunning(){
		return activeTaskState == null||(!activeTaskState.equals("EXECUTING"));
	}
	@Override
	public String toString() {
		return "BoinkTask [name=" + name + ", WUname=" + WUname
				+ ", projectURL=" + projectURL + ", deadline=" + deadline
				+ ", CPUtime=" + CPUtime + ", state=" + state
				+ ", schedulerState=" + schedulerState + ", activeTaskState="
				+ activeTaskState + ", readyToReport=" + readyToReport
				+ ", serverACK=" + serverACK + ", suspended=" + suspended
				+ ", exitStatus=" + exitStatus + ", signal=" + signal
				+ ", estimatedCPUtimeRemaining=" + estimatedCPUtimeRemaining
				+ ", parametros=" + parametros + "]";
	}
	@Override
	public int compareTo(BoinkTask o) {
		int a=Double.compare(estimatedCPUtimeRemaining, o.estimatedCPUtimeRemaining);
		return a==0?name.compareTo(o.name):a;
	}
}
