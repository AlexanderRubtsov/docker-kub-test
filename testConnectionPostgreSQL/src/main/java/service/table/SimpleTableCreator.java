package service.table;

import components.SimpleTable;
import components.Table;
import components.TableType;

public class SimpleTableCreator extends TableCreator {
    @Override
    public Table createTable(TableType tableType) {
        Table table = null;
        switch (tableType){
            case SIMPLE:
                table = new SimpleTable();
                break;
        }
        return table;
    }
}
