package my.edu.devhacktesting3.MyVoucher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.edu.devhacktesting3.MyVoucher.MyVoucherItem;
import my.edu.devhacktesting3.R;

public class MyVoucherAdapter extends RecyclerView.Adapter<MyVoucherAdapter.ViewHolder> {

    private List<MyVoucherItem> MyVoucherItems;
    private onUseClickListener useClickListener;

    //constructor
    public MyVoucherAdapter(List<MyVoucherItem> MyVoucherItems, onUseClickListener listener) {
        this.MyVoucherItems = MyVoucherItems;
        this.useClickListener = listener;
    }

    //when recyclerview need create new viewholder for item in list
    @NonNull
    @Override
    public MyVoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myvoucher_item, parent, false);
        return new ViewHolder(view);
    }

    //bind data to the view
    @Override
    public void onBindViewHolder(@NonNull MyVoucherAdapter.ViewHolder viewHolder, int position) {
        MyVoucherItem MyVoucherItem = MyVoucherItems.get(position);

        viewHolder.voucherName.setText(MyVoucherItem.getVoucherName());
        viewHolder.validDate.setText("Valid Till: "+ MyVoucherItem.getValidDate());
        if(MyVoucherItem.getVoucherLogoName().equalsIgnoreCase("FOODPANDA")){
            viewHolder.voucherLogo.setImageResource(R.drawable.foodpanda_logo);
        } else {
            viewHolder.voucherLogo.setImageResource(R.drawable.gift);
        }

        viewHolder.useBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(useClickListener != null){
                    useClickListener.onUseClick(MyVoucherItem);
                }
            }
        });
    }

    //retrun totals number of item
    @Override
    public int getItemCount() {
        return MyVoucherItems.size();
    }

    //represent a single item view within the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView validDate;
        TextView voucherName;
        ImageView voucherLogo;
        Button useBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            validDate=itemView.findViewById(R.id.validDate);
            voucherName=itemView.findViewById(R.id.voucherName);
            voucherLogo=itemView.findViewById(R.id.voucherLogo);
            useBtn=itemView.findViewById(R.id.useBtn);
        }
    }

    public interface onUseClickListener {
        void onUseClick(MyVoucherItem MyVoucherItem);
    }

}
