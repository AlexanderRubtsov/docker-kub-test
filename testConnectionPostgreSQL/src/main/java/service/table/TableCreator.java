package service.table;

import components.Table;
import components.TableType;

public abstract class TableCreator {
    public abstract Table createTable(TableType tableType);
}
