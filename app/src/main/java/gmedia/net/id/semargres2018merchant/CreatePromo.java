package gmedia.net.id.semargres2018merchant;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreatePromo extends AppCompatActivity {
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CustomRecyclerViewCreatePromo> dataSet;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_promo);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }
        session = new SessionManager(getApplicationContext());
        rvView = findViewById(R.id.rv_create_promo);
        layoutManager = new LinearLayoutManager(CreatePromo.this);
        prepareDataViewPromo();
        rvView.setLayoutManager(layoutManager);
        RelativeLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        RelativeLayout home = findViewById(R.id.btnHomeCreatePromo);
        RelativeLayout logout = findViewById(R.id.btnLogout);
        final ImageView gbrHome = findViewById(R.id.gambarHome);
        final ImageView gbrLogout = findViewById(R.id.gambarLogout);
        final TextView txtGbrHome = findViewById(R.id.textGambarHome);
        final TextView txtGbrLogout = findViewById(R.id.textGambarLogout);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gbrHome.setImageResource(R.drawable.home_grey);
                txtGbrHome.setTextColor(Color.parseColor("#FF5F5D5D"));
                gbrLogout.setImageResource(R.drawable.logout_red);
                txtGbrLogout.setTextColor(Color.parseColor("#e40112"));
                final Dialog dialog = new Dialog(CreatePromo.this);
                dialog.setContentView(R.layout.popuplogout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RelativeLayout ya = dialog.findViewById(R.id.logoutYa);
                RelativeLayout tidak = dialog.findViewById(R.id.logoutTidak);
                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        session.logoutUser();
                    }
                });
                tidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gbrLogout.setImageResource(R.drawable.logout_grey);
                        txtGbrLogout.setTextColor(Color.parseColor("#FF5F5D5D"));
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
        ImageView tambahPromo = findViewById(R.id.tambahPromo);
        tambahPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatePromo.this, SettingPromo.class);
                startActivity(i);
            }
        });
    }

    private void prepareDataViewPromo() {
        ApiVolley request = new ApiVolley(this, new JSONObject(), "GET", URL.urlViewPromo, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                dataSet = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("200")) {
//                        Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
                        JSONArray array = object.getJSONArray("response");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject isi = array.getJSONObject(i);
                            dataSet.add(new CustomRecyclerViewCreatePromo(
                                    isi.getString("id"),
                                    isi.getString("gambar"),
                                    isi.getString("title"),
                                    isi.getString("status"),
                                    isi.getString("id_k")
                            ));
                        }
                        rvView.setAdapter(null);
                        adapter = new RecyclerViewCreatePromo(CreatePromo.this, dataSet);
                        rvView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreatePromo.this, Home.class);
        startActivity(i);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
