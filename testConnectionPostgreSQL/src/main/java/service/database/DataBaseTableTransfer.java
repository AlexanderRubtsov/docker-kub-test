package service.database;

import service.connection.ConnectionService;
import service.table.TableService;

import java.sql.Connection;

public class DataBaseTableTransfer {
    public static void duplicateDataBaseTable(){
        Connection connection = ConnectionService.getConnection();
        String outputNameTable = "employee";
        String inputNameTable = "public.\"employeeTEST\"";
        assert connection != null;
        TableService.replicateTable(connection, connection, outputNameTable, inputNameTable);
    }
}
