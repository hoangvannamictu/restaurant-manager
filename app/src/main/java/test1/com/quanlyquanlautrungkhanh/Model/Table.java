package test1.com.quanlyquanlautrungkhanh.Model;

import java.io.Serializable;

public class Table implements Serializable {
    private String key;
    private int idTable;
    private int typeTable;
    private String statusTable;
    private String idBill;

    public Table(String key, int idTable, int typeTable, String statusTable) {
        this.key = key;
        this.idTable = idTable;
        this.typeTable = typeTable;
        this.statusTable = statusTable;
    }

    public Table() {
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public int getTypeTable() {
        return typeTable;
    }

    public void setTypeTable(int typeTable) {
        this.typeTable = typeTable;
    }

    public String getStatusTable() {
        return statusTable;
    }

    public void setStatusTable(String statusTable) {
        this.statusTable = statusTable;
    }

}
