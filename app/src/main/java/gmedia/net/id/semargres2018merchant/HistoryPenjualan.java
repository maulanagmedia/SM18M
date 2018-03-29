package gmedia.net.id.semargres2018merchant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bayu on 22/03/2018.
 */

public class HistoryPenjualan extends AppCompatActivity {
    private RecyclerView rvView;
    private RecyclerViewHistoryPenjualan adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CustomRecyclerViewHistoryPenjualan> historyPenjualan;
    private ArrayList<CustomRecyclerViewHistoryPenjualan> historyBaru;
    private final String tanggal[] =
            {
                    "Jum'at, 23-06-2017",
                    "Rabu, 20-06-2017",
                    "Senin, 27-06-2017",
                    "Jum'at, 23-06-2017",
                    "Rabu, 20-06-2017",
                    "Senin, 27-06-2017"
            };
    private final String email[] =
            {
                    "hsx@gmail.com",
                    "hahaha@gmail.com",
                    "hihhi@gmail.com",
                    "hsx@gmail.com",
                    "hahaha@gmail.com",
                    "hihhi@gmail.com"
            };
    private final String jumlah_kupon[] =
            {
                    "450",
                    "250",
                    "500",
                    "450",
                    "250",
                    "500"
            };
    private final String total_belanja[] =
            {
                    "Rp 250.000",
                    "Rp 150.000",
                    "Rp 550.000",
                    "Rp 250.000",
                    "Rp 150.000",
                    "Rp 550.000"
            };
    int total;
    int start = 0, count=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_penjualan);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }
        RelativeLayout back = findViewById(R.id.backHistoryPenjualan);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rvView = findViewById(R.id.rv_history_penjualan);
        layoutManager = new LinearLayoutManager(HistoryPenjualan.this);
        rvView.setLayoutManager(layoutManager);
        prepareDataHistoryPenjualan();
//        CustomRecyclerViewHistoryPenjualan history = historyPenjualan.get(2);

//        ArrayList<CustomRecyclerViewHistoryPenjualan> historyPenjualan = prepareDataHistoryPenjualan();

    }

    private void prepareDataHistoryPenjualan() {
        ApiVolley requestApiVolley = new ApiVolley(this, new JSONObject(), "GET", URL.urlHistoryPenjualan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                historyPenjualan = new ArrayList<>();
                historyBaru = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("message");
                    if (status.equals("Success!")){
//                        Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
                        JSONArray array = object.getJSONArray("response");
                        FormatRupiah request = new FormatRupiah();
                        for (int i = 0; i<array.length(); i++){
                            JSONObject isi = array.getJSONObject(i);
                            historyPenjualan.add(new CustomRecyclerViewHistoryPenjualan(
                                    isi.getString("tanggal"),
                                    isi.getString("email"),
                                    isi.getString("kupon"),
                                    request.ChangeToRupiahFormat(isi.getString("total"))
                            ));
                        }
                        for (int i = start; i<count; i++){
                            if (i<historyPenjualan.size()) historyBaru.add(historyPenjualan.get(i));
                        }
                        rvView.setAdapter(null);
                        adapter = new RecyclerViewHistoryPenjualan(HistoryPenjualan.this,historyBaru);
                        rvView.setAdapter(adapter);
                        EndlessScroll scrollListener = new EndlessScroll((LinearLayoutManager) layoutManager) {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                                start += count;
                                for (int i = start; i<start+count; i++){
                                    if (i<historyPenjualan.size()) historyBaru.add(historyPenjualan.get(i));
                                }
                                adapter.addMoreData();
                            }
                        };
                        rvView.addOnScrollListener(scrollListener);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*private ArrayList<CustomRecyclerViewHistoryPenjualan> prepareDataHistoryPenjualan() {
            ArrayList<CustomRecyclerViewHistoryPenjualan> rvData = new ArrayList<>();
            for (int i = 0; i < tanggal.length; i++) {
                CustomRecyclerViewHistoryPenjualan custom = new CustomRecyclerViewHistoryPenjualan();
                custom.setTanggal(tanggal[i]);
                custom.setEmail(email[i]);
                custom.setJumlah_kupon(jumlah_kupon[i]);
                custom.setTotal_belanja(total_belanja[i]);
                rvData.add(custom);
            }
            return rvData;
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HistoryPenjualan.this,Home.class);
        startActivity(intent);
        finish();
    }


}
