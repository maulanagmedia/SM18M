package gmedia.net.id.semargres2018merchant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SettingPromo extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    public static ImageView openCamera;
    private ImageView showCamera;
    EditText judul,link ,keterangan;
    Bitmap bitmap;
    int bitWidth,bitHeight;
    private float curScale = 1F;
    private float curRotate = 0F;
    Bitmap photo;
    private ProgressDialog progressDialog;
    private Button proses;
    private RelativeLayout utama;
    private RelativeLayout back;
    private RelativeLayout home;
    private RelativeLayout logout;
    private ImageView gbrHome;
    private ImageView gbrLogout;
    private TextView txtGbrHome;
    private TextView txtGbrLogout;
    private String bitmapString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_promo);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        judul = (EditText) findViewById(R.id.titleSettingPromo);
        link = (EditText) findViewById(R.id.urlSettingPromo);
        keterangan = (EditText) findViewById(R.id.keteranganSettingPromo);

        utama = (RelativeLayout) findViewById(R.id.layoutUtamaSettingPromo);
        utama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard.hideSoftKeyboard(SettingPromo.this);
            }
        });

        back = (RelativeLayout) findViewById(R.id.backSettingPromo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home = (RelativeLayout) findViewById(R.id.btnHomeCreatePromo);
        logout = (RelativeLayout) findViewById(R.id.btnLogout);

        gbrHome = (ImageView) findViewById(R.id.gambarHome);
        gbrLogout = (ImageView) findViewById(R.id.gambarLogout);
        txtGbrHome = (TextView) findViewById(R.id.textGambarHome);
        txtGbrLogout = (TextView) findViewById(R.id.textGambarLogout);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Home.isHome){
                    Intent i = new Intent(SettingPromo.this,Home.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gbrHome.setImageResource(R.drawable.home_grey);
                txtGbrHome.setTextColor(Color.parseColor("#FF5F5D5D"));
                gbrLogout.setImageResource(R.drawable.logout_red);
                txtGbrLogout.setTextColor(Color.parseColor("#e40112"));
                final Dialog dialog = new Dialog(SettingPromo.this);
                dialog.setContentView(R.layout.popuplogout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RelativeLayout ya = dialog.findViewById(R.id.logoutYa);
                RelativeLayout tidak = dialog.findViewById(R.id.logoutTidak);
                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SettingPromo.this, Login.class);
                        startActivity(i);
                        finish();
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

        showCamera = findViewById(R.id.showCameraSettingPromo);

        openCamera = findViewById(R.id.openCameraSettingPromo);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        proses = (Button) findViewById(R.id.btnSettingPromo);
        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!judul.getText().equals("") && !keterangan.getText().equals("") && photo!=null){

                    if(!bitmapString.equals("")){
                        prepareCreateDataSettingPromo();
                    }else{
                        Toast.makeText(SettingPromo.this, "Harap pilih gambar terlebih dahulu", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Silahkan Lengkapi Data Anda",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void prepareCreateDataSettingPromo() {

        proses.setEnabled(false);
        showProgressDialog();

        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("title", judul.getText().toString());
            jBody.put("link",link.getText().toString());
            jBody.put("gambar", bitmapString);
            jBody.put("keterangan", keterangan.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(this, jBody, "POST", URL.urlSettingPromo, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                proses.setEnabled(true);
                dismissProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    final String status = object.getJSONObject("response").getString("status");
                    String message = object.getJSONObject("response").getString("message");
                    if (status.equals("1")) {
                        Intent intent = new Intent(SettingPromo.this,CreatePromo.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat menyimpan data, harap ulangi", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat menyimpan data, harap ulangi", Toast.LENGTH_LONG).show();
                dismissProgressDialog();
                proses.setEnabled(true);
            }
        });
    }

    private Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                             boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                showCamera.setImageBitmap(Bitmap.createScaledBitmap(photo, 640, 640, true));
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*bitWidth = photo.getWidth();
            bitHeight = photo.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(curScale, curScale);
            matrix.postRotate(curRotate);
            Bitmap reziseBitmap = Bitmap.createBitmap(photo,0,0,bitWidth,bitHeight,matrix,true);*/
            BitmapDrawable drawable = (BitmapDrawable) showCamera.getDrawable();
            bitmap = drawable.getBitmap();

            bitmap = scaleDown(bitmap, 512, true);
            bitmapString = EncodeBitmapToString.convert(bitmap);
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(SettingPromo.this,CreatePromo.class);
        startActivity(i);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(SettingPromo.this,
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

}
