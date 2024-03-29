package top.rgb39.ecs.arch;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import top.rgb39.ecs.annotation.Component;
import top.rgb39.ecs.util.Lists;

public class Table {
    private List<Class<?>> headers = new LinkedList<>();
    private List<Row> table = new LinkedList<>();

    public Row createRow(long rowId) {
        Row row = new Row(rowId);
        row.expand(this.headers.size());
        this.table.add(row);
        return row;
    }

    public boolean createColumn(Class<?> cls) {
        Component anno = (Component) cls.getAnnotation(Component.class);
        if (Objects.nonNull(anno) && !anno.singleton()) {
            this.headers.add(cls);
            for (Row row : this.table) {
                row.expand(this.headers.size());
            }
            return true;
        }
        return false;
    }

    public void deleteRow(long rowId) {
        this.table.removeIf(row -> {
            return row.getRowId() == rowId;
        });
    }

    public void deleteColumn(Class<?> cls) {
        int index = this.headers.indexOf(cls);
        if (index == -1) {
            return;
        }
        for (Row row : this.table) {
            row.deleteCell(index);
        }
    }

    public Row reachCell(Class<?> cls, long rowId) {
        if (!this.headers.contains(cls) && !createColumn(cls)) {
            return null;
        }
        for (Row row : this.table) {
            if (row.getRowId() == rowId) {
                return row;
            }
        }
        return createRow(rowId);
    }

    public void setCell(Class<?> cls, long rowId, Object cell) {
        Row row = reachCell(cls, rowId);
        if (Objects.nonNull(row)) {
            row.setCell(this.headers.indexOf(cls), cell);
        }
    }

    public Object getCell(Class<?> cls, long rowId) {
        Row row = reachCell(cls, rowId);
        if (Objects.isNull(row)) {
            return null;
        }
        return row.getCell(this.headers.indexOf(cls));
    }

    public void deleteCell(Class<?> cls, long rowId) {
        Row row = reachCell(cls, rowId);
        row.deleteCell(this.headers.indexOf(cls));
    }

    public Object[] getRowArray(long rowId) {
        Row row = getRow(rowId);
        if (Objects.isNull(row)) {
            return new Object[0];
        }
        return row.getRow();
    }

    public Row getRow(long rowId) {
        return (Row) Lists.find(this.table, r -> {
            return r.getRowId() == rowId;
        });
    }

    public int getColumn(Class<?> cls) {
        return this.headers.indexOf(cls);
    }

    public Row[] getRows() {
        return (Row[]) this.table.toArray(new Row[0]);
    }
}
