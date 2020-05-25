package components;

import java.util.List;

public abstract class Table {
    private List<Row> rowList;
    private String name;

    public Table(String name) {
        this.name = name;
    }

    public Table() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Row> getRowList() {
        return rowList;
    }

    public void setRowList(List<Row> rowList) {
        this.rowList = rowList;
    }

    public static class Row{
        private List<Field> fields;

        public Row(List<Field> fields) {
            this.fields = fields;
        }

        public Row() {
        }

        public List<Field> getFields() {
            return fields;
        }

        public void setFields(List<Field> fields) {
            this.fields = fields;
        }

        public static class Field{
           private String field;
           private String type;

            public Field(String field, String type) {
                this.field = field;
                this.type = type;
            }

            public Field() {
            }

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
