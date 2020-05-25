package service.table;

import components.Table;
import components.TableType;

import java.sql.*;
import java.util.ArrayList;

public class TableService {
    private static final String  SELECT_FROM = "SELECT * FROM %s";

    public static void replicateTable(Connection ownerConnection, Connection targetConnection, String ownerSource, String targetSource){
        Table table = new SimpleTableCreator().createTable(TableType.SIMPLE);
        fillTableFromSQLTable(ownerConnection, table, ownerSource);
        insertTable(targetConnection, table, targetSource);
    }

    private static Table fillTableFromSQLTable(Connection connection, Table table, String nameSQLTable){

        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(SELECT_FROM, nameSQLTable))) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            fillTable(resultSet,metaData, table);
        } catch (SQLException e) {
            System.out.println("Переданное соединение невалидно!");
            e.printStackTrace();
        }
        return table;
    }

    private static void fillTable(ResultSet resultSet, ResultSetMetaData metaData, Table table) throws SQLException {
        ArrayList<Table.Row> rows = new ArrayList<>();
        while (resultSet.next()){
            Table.Row row = new Table.Row();
            ArrayList<Table.Row.Field> fields = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                Table.Row.Field field = new Table.Row.Field(resultSet.getString(i), metaData.getColumnClassName(i));
                fields.add(field);
            }
            row.setFields(fields);
            rows.add(row);
        }
        table.setRowList(rows);
    }
    private static void insertTable(Connection connection, Table table, String nameInsertTable){
        final String  FIRST_PART = "INSERT INTO " + nameInsertTable + " VALUES (" + getQuestionMark(table.getRowList().get(0)) + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIRST_PART);
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute("TRUNCATE TABLE " + nameInsertTable + ";");
            for (Table.Row row: table.getRowList()
            ) {
                for (int i = 0; i < row.getFields().size(); i++
                ) {
                    switch (row.getFields().get(i).getType()){
                        case("java.lang.String"):
                            preparedStatement.setString(i+1, row.getFields().get(i).getField());
                            break;
                        case("java.lang.Integer"):
                            preparedStatement.setInt(i+1, Integer.parseInt(row.getFields().get(i).getField()));
                            break;
                        case("java.lang.Long"):
                            preparedStatement.setLong(i+1, Long.parseLong(row.getFields().get(i).getField()));
                            break;
                        case("java.lang.Double"):
                            preparedStatement.setDouble(i+1,Double.parseDouble(row.getFields().get(i).getField()));
                            break;
                        case("java.lang.Float"):
                            preparedStatement.setFloat(i+1, Float.parseFloat(row.getFields().get(i).getField()));
                            break;
                    }
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static String getQuestionMark(Table.Row row){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < row.getFields().size(); i++) {
            if (i != row.getFields().size()-1) stringBuilder.append("?, "); else
                stringBuilder.append('?');
        }
        return stringBuilder.toString();
    }
}
