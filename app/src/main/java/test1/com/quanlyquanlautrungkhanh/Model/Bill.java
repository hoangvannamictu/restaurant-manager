package test1.com.quanlyquanlautrungkhanh.Model;

import java.io.Serializable;
import java.util.List;

public class Bill implements Serializable {
    private String idBill;
    private String keyTable;
    private int idTableBill;
    private String voucherBill;
    private String dateCreatedBill;
    private String dateFinishBill;
    private String statusBill;
    private int totalpriceBill;
    public Bill() {
    }

    public Bill(String idBill,String keyTable, int idTableBill, String dateCreatedBill, String statusBill) {
        this.idBill = idBill;
        this.keyTable = keyTable;
        this.idTableBill = idTableBill;
        this.dateCreatedBill = dateCreatedBill;
        this.statusBill = statusBill;
    }

    public String getKeyTable() {
        return keyTable;
    }

    public void setKeyTable(String keyTable) {
        this.keyTable = keyTable;
    }

    public int getTotalpriceBill() {
        return totalpriceBill;
    }

    public void setTotalpriceBill(int totalpriceBill) {
        this.totalpriceBill = totalpriceBill;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public int getIdTableBill() {
        return idTableBill;
    }

    public void setIdTableBill(int idTableBill) {
        this.idTableBill = idTableBill;
    }

    public String getVoucherBill() {
        return voucherBill;
    }

    public void setVoucherBill(String voucherBill) {
        this.voucherBill = voucherBill;
    }

    public String getDateCreatedBill() {
        return dateCreatedBill;
    }

    public void setDateCreatedBill(String dateCreatedBill) {
        this.dateCreatedBill = dateCreatedBill;
    }

    public String getDateFinishBill() {
        return dateFinishBill;
    }

    public void setDateFinishBill(String dateFinishBill) {
        this.dateFinishBill = dateFinishBill;
    }

    public String getStatusBill() {
        return statusBill;
    }

    public void setStatusBill(String statusBill) {
        this.statusBill = statusBill;
    }
}
