package my.edu.devhacktesting3.Voucher;

import android.widget.Button;
import android.widget.TextView;

public class VoucherItem {

    private String validDate, voucherName, voucherLogoName;

    public VoucherItem(String validDate, String voucherName, String voucherLogoName) {
        this.validDate = validDate;
        this.voucherName = voucherName;
        this.voucherLogoName = voucherLogoName;
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
}
