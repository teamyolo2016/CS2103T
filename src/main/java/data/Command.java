package main.java.data;

public class Command {

	private String original;
	private COMMAND_TYPE commandType;
	private String commandContent;
	private String[] commandParameters;
	private static final int TASK = 0;
	private static final int TIME = 1;
	private static final int PRIORITY = 2;
	private static final int TASK_TYPE = 3;
	

	public Command(String command) {
		this.original = command.trim();
		//this.commandParameters = new String[4];
	}

	public COMMAND_TYPE getType() {
		return this.commandType;
	}

	public String getContent() {
		return this.commandContent;
	}

	public String getOriginal() {
		return this.original;
	}

	public void setContent(String commandContent) {
		this.commandContent = commandContent;
	}

	public void setType(COMMAND_TYPE commandType) {
		this.commandType = commandType;
	}

	public void setParameters(String[] parameters) {
		this.commandParameters = parameters;
	}
	
	public String[] getParameters() {
		return this.commandParameters;
	}


	public Task createTask() {

			Task task = new Task(commandParameters[TASK], 
					commandParameters[TIME], commandParameters[PRIORITY], 
					commandParameters[TASK_TYPE]);
		
		return task;

	}
	

	public boolean isCommand(COMMAND_TYPE type) {
		return type ==(this.getType());
	}

}

