package gmedia.net.id.semargres2018merchant.NavTambahUser.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gmedia.net.id.semargres2018merchant.ApiVolley;
import gmedia.net.id.semargres2018merchant.CircleTransform;
import gmedia.net.id.semargres2018merchant.EditPromo;
import gmedia.net.id.semargres2018merchant.R;
import gmedia.net.id.semargres2018merchant.URL;
import gmedia.net.id.semargres2018merchant.Utils.CustomItem;

/**
 * Created by Bayu on 19/03/2018.
 */

public class ListAkunAdapter extends RecyclerView.Adapter<ListAkunAdapter.ViewHolder> {
    public static ArrayList<CustomItem> rvData;
    private Context context;
    private ProgressDialog progressDialog;

    public ListAkunAdapter(Context context, ArrayList<CustomItem> rvData) {
        this.context = context;
        this.rvData = rvData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem1;
        private RelativeLayout rvDelete;
        private LinearLayout llContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItem1 = (TextView) itemView.findViewById(R.id.tv_item1);
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            rvDelete = (RelativeLayout) itemView.findViewById(R.id.rv_delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_akun, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAkunAdapter.ViewHolder holder, final int position) {

        final CustomItem selectedItem = rvData.get(position);
        holder.tvItem1.setText(selectedItem.getItem2());

        holder.rvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_delete_promo);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
                tvTitle.setText("Apakah anda yakin ingin menghapus akun " +selectedItem.getItem2() +" ?");
                RelativeLayout ya = dialog.findViewById(R.id.logoutYa);
                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final JSONObject jBody = new JSONObject();
                        try {
                            jBody.put("id", selectedItem.getItem1());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showProgressDialog();
                        ApiVolley request = new ApiVolley(context, jBody, "POST", URL.urlDeleteAkun, "", "", 0, new ApiVolley.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {

                                dismissProgressDialog();
                                try {
                                    JSONObject object = new JSONObject(result);
                                    final String status = object.getJSONObject("response").getString("status");
                                    String message = object.getJSONObject("response").getString("message");
                                    if (status.equals("1")) {
                                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                        rvData.remove(position);
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    } else {

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(String result) {
                                Toast.makeText(context, "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                                dismissProgressDialog();
                            }
                        });
                    }
                });
                RelativeLayout tidak = dialog.findViewById(R.id.logoutTidak);
                tidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Custom_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setMessage("Menghapus...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }


}
