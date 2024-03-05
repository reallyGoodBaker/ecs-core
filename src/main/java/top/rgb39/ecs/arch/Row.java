package top.rgb39.ecs.arch;

import java.util.LinkedList;
import java.util.List;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/arch/Row.class */
public class Row {
    private List<Object> row = new LinkedList<>();
    private long rowId;

    public Row(long rowId) {
        this.rowId = rowId;
    }

    public boolean deleteCell(Object cell) {
        return this.row.remove(cell);
    }

    public boolean deleteCell(int index) {
        if (index > -1 && index < this.row.size()) {
            this.row.remove(index);
            return true;
        }
        return false;
    }

    public boolean setCell(int index, Object cell) {
        if (index == -1) {
            return false;
        }
        this.row.set(index, cell);
        return true;
    }

    public Object getCell(int index) {
        if (index > -1 && index < this.row.size()) {
            return this.row.get(index);
        }
        return null;
    }

    public long getRowId() {
        return this.rowId;
    }

    public Object[] getRow() {
        return this.row.toArray();
    }

    public void expand(int size) {
        int expandNeed = size - this.row.size();
        while (true) {
            int i = expandNeed;
            expandNeed--;
            if (i > 0) {
                this.row.add(null);
            } else {
                return;
            }
        }
    }
}
