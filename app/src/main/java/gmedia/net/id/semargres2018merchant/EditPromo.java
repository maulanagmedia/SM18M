package gmedia.net.id.semargres2018merchant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditPromo extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    SessionManager session;
    EditText title,link ,keterangan;
    ImageView gambar, showCamera, openCamera;
    Button simpan;
    Bitmap photo,bitmap;
    String linkID;
    private ProgressDialog progressDialog;
    private LinearLayout utama;
    private RelativeLayout back;
    private RelativeLayout home;
    private RelativeLayout logout;
    private String bitmapString = "";
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_promo);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        utama = (LinearLayout) findViewById(R.id.layoutUtamaEditPromo);
        utama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard.hideSoftKeyboard(EditPromo.this);
            }
        });

        back = (RelativeLayout) findViewById(R.id.backEditPromo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        home = (RelativeLayout) findViewById(R.id.btnHomeCreatePromo);
        logout = (RelativeLayout) findViewById(R.id.btnLogout);
        final ImageView gbrHome = findViewById(R.id.gambarHome);
        final ImageView gbrLogout = findViewById(R.id.gambarLogout);
        final TextView txtGbrHome = findViewById(R.id.textGambarHome);
        final TextView txtGbrLogout = findViewById(R.id.textGambarLogout);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Home.isHome){
                    Intent intent = new Intent(EditPromo.this,Home.class);
                    startActivity(intent);
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
                final Dialog dialog = new Dialog(EditPromo.this);
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

        title = findViewById(R.id.titleEditPromo);
        link = findViewById(R.id.urlEditPromo);
        keterangan = findViewById(R.id.keteranganEditPromo);
//        gambar = findViewById(R.id.showCameraEditPromo);
        showCamera = findViewById(R.id.showCameraEditPromo);
        openCamera = findViewById(R.id.openCameraEditPromo);

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        prepareDataEditPromo();

        simpan = findViewById(R.id.btnEditPromo);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bitmapString.equals("")){
                    prepareEditPromo();
                }else{
                    Toast.makeText(EditPromo.this, "Harap pilih gambar terlebih dahulu", Toast.LENGTH_LONG).show();
                }

            }
        });
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
            bitmapString = bitmap != null ? EncodeBitmapToString.convert(bitmap) : "";
        }
    }

    private void prepareEditPromo() {

        simpan.setEnabled(false);
        showProgressDialog();
        Bundle save = getIntent().getExtras();
        if (save!=null){
            linkID = save.getString("id","");
        }

        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("id", linkID);
            jBody.put("title", title.getText().toString());
            jBody.put("link", link.getText().toString());
            jBody.put("gambar", bitmapString);
            jBody.put("keterangan", keterangan.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiVolley request = new ApiVolley(this, jBody, "POST", URL.urlSettingPromo, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                simpan.setEnabled(true);
                dismissProgressDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    final String status = object.getJSONObject("response").getString("status");
                    String message = object.getJSONObject("response").getString("message");
                    if (status.equals("1")) {
                        Intent intent = new Intent(EditPromo.this,CreatePromo.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
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
                simpan.setEnabled(true);
                dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat menyimpan data, harap ulangi", Toast.LENGTH_LONG).show();
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

    private void prepareDataEditPromo() {

        Bundle save = getIntent().getExtras();
        if (save!=null){
            title.setText(save.getString("title",""));
            link.setText(save.getString("link",""));
            keterangan.setText(save.getString("keterangan",""));
            pbLoading.setVisibility(View.VISIBLE);
            Picasso.with(EditPromo.this).load(save.getString("gambar",""))
                    .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(showCamera, new Callback() {
                        @Override
                        public void onSuccess() {

                            BitmapDrawable drawable = (BitmapDrawable) showCamera.getDrawable();
                            bitmap = drawable.getBitmap();
                            bitmap = scaleDown(bitmap, 512, true);
                            bitmapString = bitmap != null ? EncodeBitmapToString.convert(bitmap) : "";
                            pbLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            pbLoading.setVisibility(View.GONE);
                        }
                    });

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditPromo.this,CreatePromo.class);
        startActivity(i);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(EditPromo.this,
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
