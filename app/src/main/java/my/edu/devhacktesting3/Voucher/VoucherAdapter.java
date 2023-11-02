package my.edu.devhacktesting3.Voucher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.edu.devhacktesting3.Voucher.VoucherItem;
import my.edu.devhacktesting3.R;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {

    private List<VoucherItem> VoucherItems;
    private onClaimClickListener claimClickListener;

    //constructor
    public VoucherAdapter(List<VoucherItem> VoucherItems, onClaimClickListener listener) {
        this.VoucherItems = VoucherItems;
        this.claimClickListener = listener;
    }

    //when recyclerview need create new viewholder for item in list
    @NonNull
    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.voucher_item, parent, false);
        return new ViewHolder(view);
    }

    //bind data to the view
    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.ViewHolder viewHolder, int position) {
        VoucherItem VoucherItem = VoucherItems.get(position);

        viewHolder.voucherName.setText(VoucherItem.getVoucherName());
        viewHolder.validDate.setText("Valid Till: "+ VoucherItem.getValidDate());
        if(VoucherItem.getVoucherLogoName().equalsIgnoreCase("FOODPANDA")){
            viewHolder.voucherLogo.setImageResource(R.drawable.foodpanda_logo);
        } else {
            viewHolder.voucherLogo.setImageResource(R.drawable.gift);
        }

        viewHolder.claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(claimClickListener != null){
                    claimClickListener.onClaimClick(VoucherItem);
                }
            }
        });
    }

    //retrun totals number of item
    @Override
    public int getItemCount() {
        return VoucherItems.size();
    }

    //represent a single item view within the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView validDate;
        TextView voucherName;
        ImageView voucherLogo;
        Button claimBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            validDate=itemView.findViewById(R.id.validDate);
            voucherName=itemView.findViewById(R.id.voucherName);
            voucherLogo=itemView.findViewById(R.id.voucherLogo);
            claimBtn=itemView.findViewById(R.id.claimBtn);
        }
    }

    public interface onClaimClickListener {
        void onClaimClick(VoucherItem VoucherItem);
    }

}
