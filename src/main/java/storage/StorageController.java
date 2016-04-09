//@@author A0125084L
package main.java.storage;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.java.Log.EventLog;
import main.java.data.PRIORITY_LEVEL;
import main.java.data.TASK_NATURE;
import main.java.data.TASK_STATUS;
import main.java.data.Task;

public class StorageController {

	private static final int PENDING_TASK = 0;
	private static final int COMPLETED_TASK = 1;
	private static final int BOTH_TYPE = 2;
	
	private PendingTaskTempStorage pendingTemp;
	private CompletedTaskTempStorage completedTemp;
	private int lastAction;

	public StorageController() {	
		pendingTemp = new PendingTaskTempStorage();
		completedTemp = new CompletedTaskTempStorage();
	}

	public void addTask(Task task) {
		assert task != null;
		
		task.setLastModified(true);
		pendingTemp.writeToTemp(task);
		lastAction = PENDING_TASK;
	}
	
	public ArrayList<Task> displayPendingTasks() {
		return pendingTemp.displayTemp();
	}
	
	public ArrayList<Task> displayCompletedTasks() {
		return completedTemp.displayTemp();
	}
	
	public void editPendingTask(Task taskToEdit, Task editedTask) {
		assert taskToEdit != null;
		assert editedTask != null;
		
		editedTask.setLastModified(true);
		pendingTemp.editToTemp(taskToEdit, editedTask);
		lastAction = PENDING_TASK;
	}
	
	public void editCompletedTask(Task taskToEdit, Task editedTask) {
		assert taskToEdit != null;
		assert editedTask != null;
		
		editedTask.setLastModified(true);
		completedTemp.editToTemp(taskToEdit, editedTask);
		lastAction = COMPLETED_TASK;
	}
	
	public void deletePendingTask(Task task) {
		assert task != null;
		
		pendingTemp.deleteFromTemp(task);
		lastAction = PENDING_TASK;
	}
	
	public void deleteCompletedTask(Task task) {
		assert task != null;
		
		completedTemp.deleteFromTemp(task);
		lastAction = COMPLETED_TASK;
	}
	
	public void clearPendingTasks() {	
		pendingTemp.clearTemp();
		lastAction = PENDING_TASK;
	}
	
	public void clearUpcomingTasks() {
		pendingTemp.clearUpcoming();
		lastAction = PENDING_TASK;
	}
	
	public void clearFloatingTasks() {
		pendingTemp.clearFloating();
		lastAction = PENDING_TASK;
	}
	
	public void clearOverdueTasks() {
		pendingTemp.clearOverdue();
		lastAction = PENDING_TASK;
	}
	
	public void clearCompletedTasks() {		
		completedTemp.clearTemp();
		lastAction = COMPLETED_TASK;
	}
	
	public void sortPendingByTaskName() {
		pendingTemp.sortByTaskName();
		lastAction = PENDING_TASK;
	}
	
	public void sortPendingByTime() {
		pendingTemp.sortByTime();
		lastAction = PENDING_TASK;
	}
	
	public void sortPendingByPriority() {
		pendingTemp.sortByPriority();
		lastAction = PENDING_TASK;
	}
	
	public void sortCompletedByTaskName() {
		completedTemp.sortByTaskName();
		lastAction = COMPLETED_TASK;
	}
	
	public void sortCompletedByTime() {
		completedTemp.sortByTime();
		lastAction = COMPLETED_TASK;
	}
	
	public void sortCompletedByPriority() {
		completedTemp.sortByPriority();
		lastAction = COMPLETED_TASK;
	}
	
	public void moveTaskToComplete(Task task) {
		assert task != null;
		
		pendingTemp.deleteFromTemp(task);
		
		Task taskCopy = new Task(task.getTask(), task.getTime(), task.getPriority(), 
				task.getType(), TASK_STATUS.COMPLETED);
		
		taskCopy.setLastModified(true);	
		completedTemp.writeToTemp(taskCopy);	
		lastAction = BOTH_TYPE;
	}
	
	public void moveTaskToPending(Task task) {
		assert task != null;
		
		completedTemp.deleteFromTemp(task);
		
		Task taskCopy = new Task(task.getTask(), task.getTime(), task.getPriority(), 
				task.getType(), determineStatus(task.getTime()));
		
		taskCopy.setLastModified(true);
		pendingTemp.writeToTemp(taskCopy);		
		lastAction = BOTH_TYPE;
	}
	
	public void undo() {
		if(lastAction == PENDING_TASK) {
			pendingTemp.undoPrevious();
		}
		else if(lastAction == COMPLETED_TASK) {
			completedTemp.undoPrevious();
		}
		else if(lastAction == BOTH_TYPE) {
			pendingTemp.undoPrevious();
			completedTemp.undoPrevious();
		}
	}
	
	public void redo() {
		if(lastAction == PENDING_TASK) {
			pendingTemp.redoPrevious();
		}
		else if(lastAction == COMPLETED_TASK) {
			completedTemp.redoPrevious();
		}
		else if(lastAction == BOTH_TYPE) {
			pendingTemp.redoPrevious();
			completedTemp.redoPrevious();
		}
	}
	
	public ArrayList<Task> showAllPendingByDate(Date date) {
		return pendingTemp.showAllByDate(date);
	}
	
	public ArrayList<Task> showAllPendingByPriority(PRIORITY_LEVEL priority) {
		return pendingTemp.showAllByPriority(priority);
	}
	
	public ArrayList<Task> showAllCompletedByDate(Date date) {
		return completedTemp.showAllByDate(date);
	}
	
	public ArrayList<Task> showAllCompletedByPriority(PRIORITY_LEVEL priority) {
		return completedTemp.showAllByPriority(priority);
	}
	
	public ArrayList<Task> searchMatchPending(String newValue) {
		return pendingTemp.searchMatch(newValue);
	}
	
	public ArrayList<Task> searchMatchCompleted(String newValue) {
		return completedTemp.searchMatch(newValue);
	}
	
	public void moveToLocation(String path) throws IOException {
		assert path != null;
		
		pendingTemp.moveToLocation(path);
	}

	public void loadFromFile(String path) {
		assert path != null;
		
		pendingTemp.loadFromFile(path);
	}
	
	public void saveToLocation(String path) throws Exception {
		assert path != null;
		
		pendingTemp.saveToLocation(path);
	}
	
	public ArrayList<Task> checkOverdue(Date date) {
		return pendingTemp.checkOverdue(date);
	}
	
	private TASK_STATUS determineStatus(List<Date> dates) {
		int size = dates.size();
		
		if (size == 0) {
			return TASK_STATUS.FLOATING;
		}
		else if (dates.get(size - 1).before(new Date())) {
			return TASK_STATUS.OVERDUE;
		}
		else {
			return TASK_STATUS.UPCOMING;
		}
	}
}
//@@author A0125084L