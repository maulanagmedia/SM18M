package gmedia.net.id.semargres2018merchant;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.semargres2018merchant.Utils.OptionItem;

public class Home extends RuntimePermissionsActivity {
    private boolean doubleBackToExitPressedOnce = false;
    public static boolean isHome = true;
    private String TAG = "home";
    private static final int REQUEST_PERMISSIONS = 20;
    SessionManager session;
    EditText isiSettingKupon, isiEmailSendEmail, isiNominalSendEMail;
    private EditText passLama, passBaru, rePassBaru;
    private Boolean klikToVisiblePassLama = true;
    private Boolean klikToVisiblePassBaru = true;
    private Boolean klikToVisibleRePassBaru = true;
    public static IntentResult resultScanBarcode;
    public static EditText isiSettingTotalBelanja;
    TextView totalPenjualan,sisaKupon,settinganKupon;
    private Activity activity;
    private RadioGroup radioGroupScanBarcode, radioGroupEmail;
    private RadioButton radioButtonScanBarcode,radioButtonEmail;
    private String IDscanBarcode = "0", IDemail = "0";
    String version = "", latestVersion="",link="";
    String settingKupon = "";
    private ProgressDialog progressDialog;
    private List<OptionItem> caraBayarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        totalPenjualan = findViewById(R.id.totalPenjualan);
        sisaKupon = findViewById(R.id.sisaKupon);
        settinganKupon = findViewById(R.id.settinganKupon);
        session = new SessionManager(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            SettingPromo.openCamera.setEnabled(true);
            super.requestAppPermissions(new
                            String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.WRITE_SETTINGS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
        }
        RelativeLayout home = findViewById(R.id.btnHome);
        RelativeLayout logout = findViewById(R.id.btnLogout);
        final ImageView gbrHome = findViewById(R.id.gambarHome);
        final ImageView gbrLogout = findViewById(R.id.gambarLogout);
        final TextView txtGbrHome = findViewById(R.id.textGambarHome);
        final TextView txtGbrLogout = findViewById(R.id.textGambarLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gbrHome.setImageResource(R.drawable.home_grey);
                txtGbrHome.setTextColor(Color.parseColor("#FF5F5D5D"));
                gbrLogout.setImageResource(R.drawable.logout_red);
                txtGbrLogout.setTextColor(Color.parseColor("#e40112"));
                final Dialog dialog = new Dialog(Home.this);
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
                        gbrHome.setImageResource(R.drawable.home_merah);
                        txtGbrHome.setTextColor(Color.parseColor("#e40112"));
                        gbrLogout.setImageResource(R.drawable.logout_grey);
                        txtGbrLogout.setTextColor(Color.parseColor("#FF5F5D5D"));
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
        LinearLayout menuPromo = findViewById(R.id.menuPromo);
        menuPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHome = false;
                Intent i = new Intent(Home.this, CreatePromo.class);
                startActivity(i);
            }
        });
        LinearLayout menuSettingKupon = findViewById(R.id.menuSettingKupon);
        menuSettingKupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Home.this);
                dialog.setContentView(R.layout.popup_setting_kupon);
                isiSettingKupon = dialog.findViewById(R.id.isiSettingKupon);
                isiSettingKupon.setText(settingKupon);
                //if(settingKupon!= null && settingKupon.length() > 0) isiSettingKupon.setSelection(settinganKupon.length() - 1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RelativeLayout btnSave = dialog.findViewById(R.id.btnSave);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isiSettingKupon.getText().toString().equals("")){
                            isiSettingKupon.setError("Nominal harap diisi");
                            isiSettingKupon.requestFocus();
                            return;
                        }
                        prepareDataSettingKupon();
                        dialog.dismiss();
                    }
                });
                RelativeLayout btnCancel = dialog.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
        LinearLayout menuScanBarcode = findViewById(R.id.menuscanbarcode);
        menuScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCaraBayarData(0);
            }
        });
        LinearLayout menuEmail = findViewById(R.id.menuEmail);
        menuEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCaraBayarData(1);
            }
        });
        LinearLayout menuProfile = findViewById(R.id.menuProfile);
        menuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHome = false;
                Intent i = new Intent(Home.this, Profile.class);
                startActivity(i);
            }
        });
        RelativeLayout menuSetting = findViewById(R.id.menuSetting);
        menuSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Home.this);
                dialog.setContentView(R.layout.popup_ganti_password);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final ImageView visiblePassLama = dialog.findViewById(R.id.visiblePassLama);
                final ImageView visiblePassBaru = dialog.findViewById(R.id.visiblePassBaru);
                final ImageView visibleRePassBaru = dialog.findViewById(R.id.visibleRePassBaru);
                passLama = dialog.findViewById(R.id.passLama);
                passBaru = dialog.findViewById(R.id.passBaru);
                rePassBaru = dialog.findViewById(R.id.reTypePassBaru);
                visiblePassLama.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (klikToVisiblePassLama) {
                            visiblePassLama.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                            passLama.setInputType(InputType.TYPE_CLASS_TEXT);
                            klikToVisiblePassLama = false;
                        } else {
                            visiblePassLama.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                            passLama.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passLama.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            klikToVisiblePassLama = true;
                        }
                    }
                });
                visiblePassBaru.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (klikToVisiblePassBaru) {
                            visiblePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                            passBaru.setInputType(InputType.TYPE_CLASS_TEXT);
                            klikToVisiblePassBaru = false;
                        } else {
                            visiblePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                            passBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            klikToVisiblePassBaru = true;
                        }
                    }
                });
                visibleRePassBaru.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (klikToVisibleRePassBaru) {
                            visibleRePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                            rePassBaru.setInputType(InputType.TYPE_CLASS_TEXT);
                            klikToVisibleRePassBaru = false;
                        } else {
                            visibleRePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                            rePassBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            rePassBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            klikToVisibleRePassBaru = true;
                        }
                    }
                });
                RelativeLayout OK = dialog.findViewById(R.id.tombolOKgantiPassword);
                OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(passLama.getText().toString().equals("")){
                            passLama.setError("Harap diisi");
                            passLama.requestFocus();
                            return;
                        }

                        if(passBaru.getText().toString().equals("")){
                            passBaru.setError("Harap diisi");
                            passBaru.requestFocus();
                            return;
                        }

                        if(rePassBaru.getText().toString().equals("")){
                            rePassBaru.setError("Harap diisi");
                            rePassBaru.requestFocus();
                            return;
                        }

                        prepareDataGantiPassword();
                        dialog.dismiss();
                    }
                });
                RelativeLayout cancel = dialog.findViewById(R.id.tombolcancelGantiPassword);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });


        /*RelativeLayout infoHistoryPenjualan = findViewById(R.id.historyPenjualan);
        infoHistoryPenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,HistoryPenjualan.class);
                startActivity(intent);
                finish();
            }
        });*/
        LinearLayout menuInfoPenjualan = findViewById(R.id.menuInfoPenjualan);
        menuInfoPenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,HistoryPenjualan.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getCaraBayarData(final int jenis) {

        // 0. barcode, 1. by email

        ApiVolley requestApiVolley = new ApiVolley(this, new JSONObject(), "GET", URL.urlCaraBayar, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    caraBayarList = new ArrayList<>();

                    if (status.equals("200")){

                        JSONArray jArray = object.getJSONArray("response");
                        for(int i = 0 ; i < jArray.length();i++){

                            JSONObject jo = jArray.getJSONObject(i);
                            caraBayarList.add(new OptionItem(jo.getString("value"), jo.getString("text")));
                        }

                        if(jenis == 0 && caraBayarList.size() > 0){

                            showScanBarcodeTransactionDialog(caraBayarList);
                        }else if (jenis == 1 && caraBayarList.size() > 0){

                            showByEmailTrans(caraBayarList);
                        }
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

    private void showScanBarcodeTransactionDialog(List<OptionItem> listItem){

        final Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.popup_scan_barcode);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        isiSettingTotalBelanja = dialog.findViewById(R.id.isiSettingTotalBelanja);
        //radioGroupScanBarcode = dialog.findViewById(R.id.radioGrubScanBarcode);
        final Spinner spCaraBayarBarcode = (Spinner) dialog.findViewById(R.id.sp_cara_bayar_barcode);
        ArrayAdapter adapter = new ArrayAdapter(Home.this, R.layout.layout_simple_list, listItem);
        spCaraBayarBarcode.setAdapter(adapter);
        spCaraBayarBarcode.setSelection(0, true);

        RelativeLayout btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IDscanBarcode = "0";
                OptionItem item = (OptionItem) spCaraBayarBarcode.getSelectedItem();
                IDscanBarcode = item.getItem1();

                if (!isiSettingTotalBelanja.getText().toString().equals("")) {
                    openScanBarcode();
                    dialog.dismiss();
                } else {
                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    isiSettingTotalBelanja.setError("Silahkan isi nominal belanja");
                    isiSettingTotalBelanja.requestFocus();
                    return;
                }
            }
        });
        RelativeLayout btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showByEmailTrans(List<OptionItem> listItem){

        final Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.popup_sendemail);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // radioGroupEmail = dialog.findViewById(R.id.radioGrubEmail);
        isiEmailSendEmail = dialog.findViewById(R.id.isiEmailSendEmail);
        isiNominalSendEMail = dialog.findViewById(R.id.isiNominalSendEMail);

        final Spinner spCaraBayarEmail = (Spinner) dialog.findViewById(R.id.sp_cara_bayar_email);
        ArrayAdapter adapter = new ArrayAdapter(Home.this, R.layout.layout_simple_list, listItem);
        spCaraBayarEmail.setAdapter(adapter);
        spCaraBayarEmail.setSelection(0, true);

        RelativeLayout btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isiEmailSendEmail.getText().toString().equals("")){
                    isiEmailSendEmail.setError("Silahkan mengisi email");
                    isiEmailSendEmail.requestFocus();
                    return;
                }

                if(isiNominalSendEMail.getText().toString().equals("")){
                    isiNominalSendEMail.setError("Silahkan mengisi nominal belanja");
                    isiNominalSendEMail.requestFocus();
                    return;
                }

                IDemail = "0";
                OptionItem item = (OptionItem) spCaraBayarEmail.getSelectedItem();
                IDemail = item.getItem1();

//                        Toast.makeText(getApplicationContext(),String.valueOf(IDemail),Toast.LENGTH_LONG).show();
                prepareDataSendEmail();
                dialog.dismiss();

            }
        });
        RelativeLayout btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(Home.this,
                R.style.AppTheme_Custom_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setMessage("Memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVersion();
        prepareDataDashboard();

    }
    private void checkVersion(){

        PackageInfo pInfo = null;
        version = "";

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version = pInfo.versionName;
//        getSupportActionBar().setSubtitle(getResources().getString(R.string.app_name) + " v "+ version);
        latestVersion = "";
        link = "";

        ApiVolley request = new ApiVolley(Home.this, new JSONObject(), "GET", URL.urlCheckVersion, "", "", 0, new ApiVolley.VolleyCallback() {

            @Override
            public void onSuccess(String result) {

                JSONObject responseAPI;
                try {
                    responseAPI = new JSONObject(result);
                    String status = responseAPI.getJSONObject("metadata").getString("status");

                    if(status.equals("200")){
                        latestVersion = responseAPI.getJSONObject("response").getString("build_version");
                        link = responseAPI.getJSONObject("response").getString("link_update");

                        if(!version.trim().equals(latestVersion.trim()) && link.length() > 0){

                            final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setIcon(R.mipmap.ic_launcher)
                                    .setTitle("Update")
                                    .setMessage("Versi terbaru "+latestVersion+" telah tersedia, mohon download versi terbaru.")
                                    .setPositiveButton("Update Sekarang", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                            startActivity(browserIntent);
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
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

    private void prepareDataDashboard() {
        ApiVolley requestApiVolley = new ApiVolley(this, new JSONObject(), "GET", URL.urlDashboard, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("message");
                    String statusJson = object.getJSONObject("metadata").getString("status");
                    if (statusJson.equals("200")){
                        JSONObject isi = object.getJSONObject("response");
                        FormatRupiah request = new FormatRupiah();
                        totalPenjualan.setText(request.ChangeToRupiahFormat(isi.getString("total_transaksi")));
                        sisaKupon.setText(isi.getString("sisa_kupon"));
                        settinganKupon.setText(request.ChangeToRupiahFormat(isi.getString("setting_kupon")));
                        settingKupon = isi.getString("setting_kupon");
                    }
                    /*else if (status.equals("Unauthorized")){
                        Toast.makeText(getApplicationContext(),"Silahkan Login Ulang",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Home.this,Login.class);
                        startActivity(intent);
                        finish();
                    }*/
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

    private void prepareDataGantiPassword() {

        showProgressDialog();
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("password_lama", passLama.getText());
            jBody.put("password_baru", passBaru.getText());
            jBody.put("re_password", rePassBaru.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(this, jBody, "POST", URL.urlGantiPassword, "", "", 0, new ApiVolley.VolleyCallback() {
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
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                dismissProgressDialog();
            }
        });
    }

    private void prepareDataSendEmail() {

        showProgressDialog();
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("kepada", isiEmailSendEmail.getText());
            jBody.put("total", isiNominalSendEMail.getText());
            jBody.put("cara_bayar",String.valueOf(IDemail));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(this, jBody, "POST", URL.urlSendEmail, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                dismissProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    final String status = object.getJSONObject("response").getString("status");
                    String message = object.getJSONObject("response").getString("message");
                    if (status.equals("1")) {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else if (status.equals("0")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                prepareDataDashboard();
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                dismissProgressDialog();
                prepareDataDashboard();
            }
        });
    }

    private void prepareDataSettingKupon() {

        showProgressDialog();
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("kupon", isiSettingKupon.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(this, jBody, "POST", URL.urlSettingKupon, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                dismissProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    final String status = object.getJSONObject("response").getString("status");
                    String message = object.getJSONObject("response").getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(Home.this,Home.class);
                        startActivity(i);
                        finish();*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                prepareDataDashboard();

            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                dismissProgressDialog();
                prepareDataDashboard();
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        resultScanBarcode = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (resultScanBarcode != null) {

            if (resultScanBarcode.getContents() == null) {
                //Toast.makeText(getApplicationContext(), "gagal brooh", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(getApplicationContext(),resultScanBarcode.getContents(),Toast.LENGTH_LONG).show();
                prepareDataScanBarcode();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void prepareDataScanBarcode() {

        showProgressDialog();
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("user_id", resultScanBarcode.getContents());
            jBody.put("total", isiSettingTotalBelanja.getText().toString());
            jBody.put("cara_bayar", IDscanBarcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(this, jBody, "POST", URL.urlScanBarcode, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                dismissProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    final String status = object.getJSONObject("response").getString("status");
                    String message = object.getJSONObject("response").getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        JSONObject detail = object.getJSONObject("response");
                        Intent i = new Intent(Home.this, BerhasilQrCode.class);
                        i.putExtra("nama", detail.getString("nama"));
                        i.putExtra("email", detail.getString("email"));
                        i.putExtra("telpon", detail.getString("no_telp"));
                        i.putExtra("gambar", detail.getString("foto"));
                        i.putExtra("jumlah_kupon", detail.getString("jumlah_kupon"));
                        startActivity(i);
                        finish();
                        isHome = false;
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                prepareDataDashboard();
            }
            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                dismissProgressDialog();
                prepareDataDashboard();
            }
        });
    }

    private void openScanBarcode() {

        IntentIntegrator integrator = new IntentIntegrator(Home.this);
        //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.initiateScan();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
