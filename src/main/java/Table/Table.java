package Table;

import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;


public class Table <T> extends TableView
{
    
    public Table(Pane parent)
    {
        super();
        parent.getChildren().add(this);
    }

}
