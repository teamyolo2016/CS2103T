package main.java.gui;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.data.Tasks;


public class DisplayWrapper {
    private Tasks taskToDo;
    private StringProperty taskName;
//    private IntegerProperty linesOfCode;
//    private DoubleProperty proportion;

    public DisplayWrapper(Tasks taskToDo) {
        this.taskToDo = taskToDo;
        this.taskName = new SimpleStringProperty(taskToDo.getName());
//        this.linesOfCode = new SimpleIntegerProperty(author.getLinesOfCode());
//        this.proportion = new SimpleDoubleProperty(author.getProportion());
    }

//    public StringProperty taskProperty() {
//        return name;
//    }

//    public IntegerProperty linesOfCodeProperty() {
//        return linesOfCode;
//    }
//
//    public DoubleProperty proportionProperty() {
//        return proportion;
//    }
//
//    public ArrayList<CodeSnippet> getCodeSnippets() {
//        return author.getCodeSnippets();
//    }
    
}
