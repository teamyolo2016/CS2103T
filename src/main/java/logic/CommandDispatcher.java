/* @@author A0127481E */
package main.java.logic;


import main.java.data.COMMAND_TYPE;
import main.java.data.Command;
import main.java.parser.AddCommandParser;
import main.java.parser.DeleteCommandParser;
import main.java.parser.EditCommandParser;
import main.java.parser.InvalidInputFormatException;
import main.java.parser.ShowCommandParser;
import main.java.parser.SortCommandParser;
import main.java.parser.StorageCommandParser;


public class CommandDispatcher {

	private static final String EMPTY_STRING = "";
	private static final String WHITE_SPACE = " ";

	public CommandDispatcher() {

	}
	public Command parseCommand(Command command)throws InvalidInputFormatException {
		assert command != null;
		String originalCommand = command.getOriginal();

		if (originalCommand.isEmpty()) {
			throw new InvalidInputFormatException("Empty command is not allowed!");
		}

		command.setType(determineCommandType(originalCommand));
		String commandContent = retrieveCommandContent(command);
		command.setContent(commandContent);
		setParameters(command);
		return command;
	}

	private void setParameters(Command command)throws InvalidInputFormatException {
		assert command != null;
		
		if (command.isCommand(COMMAND_TYPE.ADD)) {
			AddCommandParser parser = new AddCommandParser();
			command.setParameters(parser.determineParameters
					(command.getContent()));
		}
		else if (command.isCommand(COMMAND_TYPE.EDIT)) {
			EditCommandParser parser = new EditCommandParser();
			command.setParameters(parser.determineParameters
					(command.getContent()));
		}
		else if (command.isCommand(COMMAND_TYPE.DELETE)) {
			DeleteCommandParser parser = new DeleteCommandParser();
			command.setParameters(parser.determineParameters
					(command.getContent()));
		}
		else if (command.isCommand(COMMAND_TYPE.DELETE_COMPLETE)) {
			DeleteCommandParser parser = new DeleteCommandParser();
			command.setParameters(parser.determineParameters
					(command.getContent()));
		}
		else if (command.isCommand(COMMAND_TYPE.MOVE)) {
			StorageCommandParser parser = new StorageCommandParser();
			command.setParameters(parser.determineParameters
					(command.getType(),command.getContent()));
		}
		else if (command.isCommand(COMMAND_TYPE.SAVE)) {
			StorageCommandParser parser = new StorageCommandParser();
			command.setParameters(parser.determineParameters
					(command.getType(),command.getContent()));
		}
		else if (command.isCommand(COMMAND_TYPE.SORT)
				|| command.isCommand(COMMAND_TYPE.SORT_COMPLETE)) {
			SortCommandParser parser = new SortCommandParser();
			command.setParameters(parser.determineParameters
					(command.getContent()));
		}
		else if (command.isCommand(COMMAND_TYPE.SHOW) 
				|| command.isCommand(COMMAND_TYPE.SHOW_COMPLETE)) {
			ShowCommandParser parser = new ShowCommandParser();
			command.setParameters(parser.determineParameters
					(command.getContent()));
		}
	
		else if (command.isCommand(COMMAND_TYPE.INVALID)){
			throw new InvalidInputFormatException("Please enter a valid command!");
		}
		
		//other commands which need not to be parsed
		else {
			return;
		}

	}

	private COMMAND_TYPE determineCommandType(String originalCommand) {
		assert originalCommand != null;
		
		COMMAND_TYPE type = getCommandKeyword(originalCommand);
		return type;
	}

	private COMMAND_TYPE getCommandKeyword(String command) {
		assert command != null;
		
		String firstWord = getFirstKeyword(command);

		if (isCommand(COMMAND_TYPE.ADD, firstWord)) {
			return COMMAND_TYPE.ADD;
		}

		else if (isCommand(COMMAND_TYPE.DELETE, firstWord)) {
			return COMMAND_TYPE.DELETE;
		}
		
		else if (isCommand(COMMAND_TYPE.DELETE_COMPLETE, firstWord)) {
			return COMMAND_TYPE.DELETE_COMPLETE;
		}

		else if (isCommand(COMMAND_TYPE.SEARCH, firstWord)) {
			return COMMAND_TYPE.SEARCH;
		}

		else if (isCommand(COMMAND_TYPE.MOVE, firstWord)) {
			return COMMAND_TYPE.MOVE;
		}
		else if (isCommand(COMMAND_TYPE.SAVE, firstWord)) {
			return COMMAND_TYPE.SAVE;
		}

		else if (isCommand(COMMAND_TYPE.SORT, firstWord)) {
			return COMMAND_TYPE.SORT;
		}
		
		else if (isCommand(COMMAND_TYPE.SORT_COMPLETE, firstWord)) {
			return COMMAND_TYPE.SORT_COMPLETE;
		}

		else if (isCommand(COMMAND_TYPE.CLEAR_UPCOMING, firstWord)) {
			return COMMAND_TYPE.CLEAR_UPCOMING;
		}
		
		else if (isCommand(COMMAND_TYPE.CLEAR_ALL, firstWord)) {
			return COMMAND_TYPE.CLEAR_ALL;
		}
		
		else if (isCommand(COMMAND_TYPE.CLEAR_FLOATING, firstWord)) {
			return COMMAND_TYPE.CLEAR_FLOATING;
		}
		
		else if (isCommand(COMMAND_TYPE.CLEAR_OVERDUE, firstWord)) {
			return COMMAND_TYPE.CLEAR_OVERDUE;
		}
		
		else if (isCommand(COMMAND_TYPE.CLEAR_COMPLETE, firstWord)) {
			return COMMAND_TYPE.CLEAR_COMPLETE;
		}

		else if (isCommand(COMMAND_TYPE.EDIT, firstWord)) {
			return COMMAND_TYPE.EDIT;
		}

		else if (isCommand(COMMAND_TYPE.UNDO, firstWord)) {
			return COMMAND_TYPE.UNDO;
		}
		
		else if (isCommand(COMMAND_TYPE.REDO, firstWord)) {
			return COMMAND_TYPE.REDO;
		}
		
		else if (isCommand(COMMAND_TYPE.MARK, firstWord)) {
			return COMMAND_TYPE.MARK;
		}
		
		else if (isCommand(COMMAND_TYPE.UNMARK, firstWord)) {
			return COMMAND_TYPE.UNMARK;
		}
		
		else if (isCommand(COMMAND_TYPE.SWITCH, firstWord)) {
			return COMMAND_TYPE.SWITCH;
		}
		
		else if (isCommand(COMMAND_TYPE.SHOW, firstWord)) {
			return COMMAND_TYPE.SHOW;
		}
		
		else if (isCommand(COMMAND_TYPE.SHOW_COMPLETE, firstWord)) {
			return COMMAND_TYPE.SHOW_COMPLETE;
		}
		
		else {
			return COMMAND_TYPE.INVALID;
		}
		
	}

	private String getFirstKeyword(String command) {
		assert command != null;
		
		//only one word in command -> it is the keyword required
		if (!command.contains(WHITE_SPACE)) {
			return command;
		}
		
		return command.substring(0,command.indexOf(WHITE_SPACE)).trim();
	}

	private boolean isCommand(COMMAND_TYPE type, String keyword) {
		assert keyword != null;
		
		return type.getType().equalsIgnoreCase(keyword);
	}

	private String retrieveCommandContent(Command command) {
		assert command != null;
		
		String original = command.getOriginal();

		//command content is empty
		if (!original.contains(WHITE_SPACE)) {
			return EMPTY_STRING;
		}
		
		//command content is not empty
		else {
			String content = original.substring(original.indexOf(WHITE_SPACE) + 1);
			return content.trim();
		}
	}
}
/* @@author A0127481E */