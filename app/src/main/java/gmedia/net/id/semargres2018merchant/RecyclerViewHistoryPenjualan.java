package gmedia.net.id.semargres2018merchant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bayu on 22/03/2018.
 */

public class RecyclerViewHistoryPenjualan extends RecyclerView.Adapter<RecyclerViewHistoryPenjualan.ViewHolder> {
    private ArrayList<CustomRecyclerViewHistoryPenjualan> rvData;
    private Context context;

    public RecyclerViewHistoryPenjualan(Context context, ArrayList<CustomRecyclerViewHistoryPenjualan> rvData) {
        this.context = context;
        this.rvData = rvData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tanggal, email, jumlah_kupon, total_belanja;

        public ViewHolder(View itemView) {
            super(itemView);
            tanggal = itemView.findViewById(R.id.tanggal_history_penjualan);
            email = itemView.findViewById(R.id.email_history_penjualan);
            jumlah_kupon = itemView.findViewById(R.id.jumlah_kupon_history_penjualan);
            total_belanja = itemView.findViewById(R.id.total_belanja_history_penjualan);
        }
    }
    public void addMoreData(){
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_history_penjualan, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHistoryPenjualan.ViewHolder holder, int position) {
        holder.tanggal.setText(rvData.get(position).getTanggal());
        holder.email.setText(rvData.get(position).getEmail());
        holder.jumlah_kupon.setText(rvData.get(position).getJumlah_kupon());
        holder.total_belanja.setText(rvData.get(position).getTotal_belanja());
    }


    @Override
    public int getItemCount() {
        return rvData.size();
    }


}
