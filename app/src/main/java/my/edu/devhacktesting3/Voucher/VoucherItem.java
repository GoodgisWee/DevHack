package my.edu.devhacktesting3.Voucher;

import android.widget.Button;
import android.widget.TextView;

public class VoucherItem {

    private String id, validDate, voucherName, voucherLogoName;
    private int point, amount;

    public VoucherItem() {
        // Default constructor is required by Firebase for deserialization.
    }

    public VoucherItem(String id, String validDate, String voucherName, String voucherLogoName,
                       int point, int amount) {
        this.id = id;
        this.validDate = validDate;
        this.voucherName = voucherName;
        this.voucherLogoName = voucherLogoName;
        this.point = point;
        this.amount = amount;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherLogoName() {
        return voucherLogoName;
    }

    public void setVoucherLogoName(String voucherLogoName) {
        this.voucherLogoName = voucherLogoName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
