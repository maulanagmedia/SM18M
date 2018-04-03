package gmedia.net.id.semargres2018merchant.NavTambahUser;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gmedia.net.id.semargres2018merchant.ApiVolley;
import gmedia.net.id.semargres2018merchant.Home;
import gmedia.net.id.semargres2018merchant.NavTambahUser.Adapter.ListAkunAdapter;
import gmedia.net.id.semargres2018merchant.R;
import gmedia.net.id.semargres2018merchant.SessionManager;
import gmedia.net.id.semargres2018merchant.URL;
import gmedia.net.id.semargres2018merchant.Utils.CustomItem;

public class MainTambahAkun extends AppCompatActivity {

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CustomItem> dataSet;
    SessionManager session;
    private ImageView ivTambahAkun;
    private EditText edtUsername, edtPassword, edtRePassword;
    private boolean isPassVisible = true, isRepassVisible = true;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tambah_akun);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        session = new SessionManager(getApplicationContext());
        rvView = findViewById(R.id.rv_akun);
        layoutManager = new LinearLayoutManager(MainTambahAkun.this);

        getDataAkun();

        isPassVisible = true;
        isRepassVisible = true;
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
                final Dialog dialog = new Dialog(MainTambahAkun.this);
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

        ivTambahAkun = (ImageView) findViewById(R.id.iv_tambah_akun);

        ivTambahAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTambahAkun();

            }
        });
    }

    private void showTambahAkun(){

        final Dialog dialog = new Dialog(MainTambahAkun.this);
        dialog.setContentView(R.layout.popup_tambah_akun);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ImageView ivPass = dialog.findViewById(R.id.iv_password);
        final ImageView ivRePass = dialog.findViewById(R.id.iv_repassword);
        edtUsername = (EditText) dialog.findViewById(R.id.edt_username);
        edtPassword = dialog.findViewById(R.id.edt_password);
        edtRePassword = dialog.findViewById(R.id.edt_repassword);
        RelativeLayout rvOK = dialog.findViewById(R.id.rv_ok);
        RelativeLayout rvCancel = dialog.findViewById(R.id.rv_cancel);

        ivPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassVisible) {
                    ivPass.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    isPassVisible = false;
                } else {
                    ivPass.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPassVisible = true;
                }
            }
        });

        ivRePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRepassVisible) {
                    ivRePass.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    edtRePassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    isRepassVisible = false;
                } else {
                    ivRePass.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    edtRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isRepassVisible= true;
                }
            }
        });


        rvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtUsername.getText().toString().equals("")){
                    edtUsername.setError("Username harap diisi");
                    edtUsername.requestFocus();
                    return;
                }

                if(edtPassword.getText().toString().equals("")){
                    edtPassword.setError("Password harap diisi");
                    edtPassword.requestFocus();
                    return;
                }

                if(edtRePassword.getText().toString().equals("")){
                    edtRePassword.setError("Password ulang harap diisi");
                    edtRePassword.requestFocus();
                    return;
                }

                if(!edtRePassword.getText().toString().equals(edtPassword.getText().toString())){
                    edtRePassword.setError("Password tidak sama, cek kembali");
                    edtRePassword.requestFocus();
                    return;
                }
                saveNewAccount();
                dialog.dismiss();
            }
        });

        rvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void saveNewAccount() {

        showProgressDialog();
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("id", "");
            jBody.put("username", edtUsername.getText());
            jBody.put("password", edtPassword.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(this, jBody, "POST", URL.urlTambahAkun, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                dismissProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    String pesan = object.getJSONObject("metadata").getString("message");
                    String status = object.getJSONObject("response").getString("status");
                    if (status.equals("1")) {
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getDataAkun();
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                dismissProgressDialog();
            }
        });
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(MainTambahAkun.this,
                R.style.AppTheme_Custom_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setMessage("Menyimpan...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void getDataAkun() {
        ApiVolley request = new ApiVolley(this, new JSONObject(), "GET", URL.urlGetAkun, "", "", 0, new ApiVolley.VolleyCallback() {
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
                            dataSet.add(new CustomItem(
                                    isi.getString("id"),
                                    isi.getString("username")
                            ));
                        }

                        rvView.setAdapter(null);
                        adapter = new ListAkunAdapter(MainTambahAkun.this, dataSet);
                        rvView.setAdapter(adapter);
                    } else {
                        //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(MainTambahAkun.this, Home.class);
        startActivity(i);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
