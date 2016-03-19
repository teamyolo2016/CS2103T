package main.java.flash;



import java.io.IOException;
import java.util.ArrayList;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import main.java.logic.Logic;
import main.java.data.Task;
import main.java.gui.CommandBarController;
import main.java.gui.EmptyTableController;
import main.java.gui.TabsController;
import main.java.gui.TasksItemController;
import main.java.gui.TasksTableController;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	private Logic logic;
	private Task task;

	private TasksTableController tableControl;
	private CommandBarController barControl;
	private EmptyTableController emptyTable;
	private TabsController tabControl;

	private ArrayList<String> historyLog;
	private ArrayList<Task> result;
	private ArrayList<Task> finalResult = new ArrayList<Task>();
	private ArrayList<Task> searchResult = new ArrayList<Task>();

	private static final String EMPTY_STRING = "";
	private static final String SPACE = " ";
	private static final String SPLIT = "\\s+";
	private static final int COMMAND_INDEX = 0;
	
	private int pointer;
	private boolean isFeedback = false;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		// this.primaryStage.initStyle(StageStyle.TRANSPARENT);;
		this.primaryStage.setTitle("Flashpoint");
		this.primaryStage.getIcons().add(new Image("/main/resources/images/cache.png"));
		
		initControllers(this);	
		initLogic();
		initRootLayout();
		checkIsTasksEmpty();
	}

	
	
	/**********************************Initialisation***********************************************/
	/***********************************************************************************************/
	private void initControllers(Main main) {
		tableControl =  new TasksTableController();
		barControl =  new CommandBarController(this);
		tabControl = new TabsController();
	}
	
	private void initLogic() throws Exception {
		logic = new Logic();
	}

	private void checkIsTasksEmpty() throws Exception {
		if (isListEmpty()) {
			tabControl.setUpcomingTab(new EmptyTableController());
		} else {
			tabControl.setUpcomingTab(tableControl);
			updateList();
		}
	}

	/**
	 * Initialises the RootLayout that will contain all other JavaFX components.
	 */
	private void initRootLayout() {

		try {
			// load root layout from fxml file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/layouts/RootLayout.fxml"));
			rootLayout = loader.load();

			Scene scene = new Scene(rootLayout);
	        
			primaryStage.setScene(scene);

			scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
				if (key.getCode() == KeyCode.ESCAPE) {
					primaryStage.hide();
				}
			});
			
            showTabs();
			showCommandBar();
			showTasks();
			initLog();
			listenerForTaskList();

			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void showTabs() {
		// TODO Auto-generated method stub
		rootLayout.setTop(tabControl);
	}

	private void showTasks() {
		tabControl.setUpcomingTab(tableControl);
	}

	private void showCommandBar() {
		rootLayout.setBottom(barControl);
		barControl.setText("What is your main focus for today?");
		barControl.getFocus();
		barControl.setBgColour("med");
	}
	
	private void initLog() {
		// TODO Auto-generated method stub
		historyLog = new ArrayList<String>();
	}
	

	public void handleSearch(String oldValue, String newValue) throws Exception {
		
		if(newValue.contains(",")){
			return;
		}

		String[] fragments = null;
		fragments = newValue.split(SPLIT);
		boolean isEdit = fragments[COMMAND_INDEX].equalsIgnoreCase("edit");
		boolean isDelete = fragments[COMMAND_INDEX].equalsIgnoreCase("delete");
		boolean isSearch = fragments[COMMAND_INDEX].equalsIgnoreCase("search");
         
//		if (newValue.contains(SPACE)) {
//			updateList();
//		}
		
		if (oldValue != null && (newValue.length() < oldValue.length())) {
			updateList();
		}
		
		if(isFeedback||newValue.equals(EMPTY_STRING)){
			removeAllStyle(barControl.getCommandBar());
			barControl.setBgColour("med");    
		}
				
		if(logic.isCommand(fragments[COMMAND_INDEX])){
			removeAllStyle(barControl.getCommandBar());
			barControl.setBgColour("best");  
		}else if(!logic.isCommand(fragments[COMMAND_INDEX])&&!newValue.equals(EMPTY_STRING)){		
			removeAllStyle(barControl.getCommandBar());
			barControl.setBgColour("bad");    
		}
			
		if ((isEdit || isDelete || isSearch) && fragments.length > 1) {
			newValue = fragments[1];		
			String[] parts = null;
			parts = newValue.toLowerCase().split(SPACE);
			ObservableList<TasksItemController> temp = FXCollections.observableArrayList();
            searchResult.clear();
            
			int count = 0;
			for (Task task : logic.display()) {
				boolean match = true;
				String taskMatch = task.getTask() + task.getPriority() + task.getTime();
				for (String part : parts) {
					String withoutComma = part.substring(0,part.length()-1);
					if(taskMatch.toLowerCase().contains(withoutComma)&& newValue.contains(",")){
						match = true;
						break;
					}
					if (!taskMatch.toLowerCase().contains(part)) {
						match = false;
						break;
					}
				}
				if (match) {
					temp.add(new TasksItemController(task));
					searchResult.add(task);
				}
			}
			tableControl.clearTask();
			tableControl.setItems(temp);
		 }

 
	}

	public void handleKeyPress(CommandBarController commandBarController, KeyEvent event, String text)
			throws Exception {
		assert commandBarController != null;
		if (event.getCode() == KeyCode.ENTER) {
			handleEnterPress(commandBarController, text);
		} else if ((event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) && !historyLog.isEmpty()) {
			event.consume(); // nullifies the default behavior of UP and DOWN on
								// a TextArea
			handleGetPastCommands(event);
		} else if ((event.getCode() == KeyCode.TAB)) {
			tableControl.controlToList();
		}
	}

	private void listenerForTaskList() {
		ListView<TasksItemController> tasksDisplay = tableControl.getListView();

		tasksDisplay.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					handleEnterKey();
				} else if (e.getCode() == KeyCode.ESCAPE) {
					// handleEscKey();
				} else if (e.getCode() == KeyCode.DELETE) {
					handleDeleteKey();
				}
			}

			private void handleEnterKey() {
				TasksItemController chosen = tasksDisplay.getSelectionModel().getSelectedItem();
				barControl.updateUserInput("edit " + chosen.getTaskName());
				barControl.getFocus();
			}

			private void handleDeleteKey() {
				TasksItemController chosen = tasksDisplay.getSelectionModel().getSelectedItem();
				barControl.updateUserInput("delete " + chosen.getTaskName());
				barControl.getFocus();
			}

		});

	}

	private void handleGetPastCommands(KeyEvent event) {
		assert event != null;
		String pastCommand = getPastCommandFromHistory(event.getCode());
		barControl.updateUserInput(pastCommand);
	}

	private String getPastCommandFromHistory(KeyCode code) {
		
		if (code == KeyCode.DOWN) {
			return getNextCommand();
		} else if (code == KeyCode.UP) {
			return getPreviousCommand();
		} else {
			return EMPTY_STRING;
		}
	}

	private String getPreviousCommand() {
		if (pointer > 0) {
			pointer--;
		}
		return historyLog.get(pointer);
	}

	private String getNextCommand() {
		if (pointer < historyLog.size() - 1) {
			pointer++;
		}
		return historyLog.get(pointer);
	}

	private void handleEnterPress(CommandBarController commandBarController, String userInput) throws Exception {
		assert commandBarController != null;

		if (userInput.isEmpty()) {
			return;

		} else {
			// normal command
			historyLog.add(userInput);
			
			if (!logic.isDisplayCommand(userInput)) {
				result = new ArrayList<Task>(logic.handleUserCommand(userInput, result));
				
				if (userInput.indexOf(" ") != -1) {
					if (logic.isDeleteCommand(userInput)) {
						handleDeleteCommand(userInput);
					}
					if (logic.isEditCommand(userInput)) {
					    handleEditCommand(userInput);
					}
				}

			}
		}
		checkIsTasksEmpty();
			
		setFeedback(commandBarController, userInput);
		new CommandBarController();
		commandBarController.clear();
	}
	
	private void handleDeleteCommand(String userInput) throws Exception {
		assert userInput != null;
		for (Task temp : searchResult) {
			if (userInput.equalsIgnoreCase("delete " + temp.getTask()) || searchResult.size()==1) {
				logic.delete(temp);			
				break;
			}
			
		}
	}

	private void handleEditCommand(String userInput) throws Exception {
		assert userInput != null;
		
		String sub = userInput.substring(5, userInput.indexOf(","));
		finalResult.clear();
		for (Task temp : searchResult) {
			if (sub.equals(temp.getTask())) {				
		     	finalResult.add(temp);	  
		     	finalResult.add(result.get(1));
		     	
		     	Task original = finalResult.get(0);
		     	Task updated = finalResult.get(1);
		     	
		     	if(updated.getTime().equals(EMPTY_STRING)){
		     		updated.setTime(original.getTime());
		     	}
		     	if(updated.getPriority().equals(EMPTY_STRING)){
		     		updated.setPriority(original.getPriority());
		     	}
		     	
				logic.edit(finalResult);
				
				break;
			}
			
		}

	}
	

	private boolean isListEmpty() throws Exception {
		return logic.display().isEmpty();
	}

	private void setFeedback(CommandBarController commandBarController, String userInput) {
		assert commandBarController != null;
		int i = 1;
		isFeedback = true;
		if (userInput.indexOf(' ') != -1) {
			i = userInput.indexOf(' ');
			String firstWord = userInput.substring(0, i);
			String subString = userInput.substring(i + 1);
			commandBarController.setFeedback("  Successfully " + firstWord + "ed " + "' " + subString + " ' ");
		} else {
			commandBarController.setFeedback("  Successfully " + userInput + "ed   ");
		}
	}
	
	// Method that returns the first word
	public static String firstWord(String input) {
	    String result = input;  // if no space found later, input is the first word

	    for(int i = 0; i < input.length(); i++)
	    {
	        if(input.charAt(i) == ' ')
	        {
	            result = input.substring(0, i);
	            break;
	        }
	    }
	    return result; 
	}
	
	
	public void removeAllStyle(Node n){
		n.getStyleClass().removeAll("bad","med","good","best"); 
	}


	public void populateList(TasksTableController tableControl, ArrayList<Task> result) {
		tableControl.clearTask();
		for (Task temp : result) {
			tableControl.addTask(temp);
		}
	}

	private void updateList() {
		tableControl.clearTask();
		try {
			for (Task temp : logic.display()) {
				tableControl.addTask(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
