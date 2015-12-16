package com.elbbbird.android.crop.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.elbbbird.android.crop.CropImage;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
//TODO samsung
// android:hardwareAccelerated="false"
// android:configChanges="orientation|keyboardHidden|screenSize"

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CROP = 6709;
    public static final int REQUEST_CAMERA = 6710;
    public static final int REQUEST_GALLARY = 6711;

    interface Extra {
        String ASPECT_X = "aspect_x";
        String ASPECT_Y = "aspect_y";
        String MAX_X = "max_x";
        String MAX_Y = "max_y";
        String ERROR = "error";
    }

    private File avatar;
    private File avatar_crop;
    private Uri inUri;
    private Uri outUri;

    @Bind(R.id.main_btn_crop)
    Button btnCrop;
    @Bind(R.id.main_btn_camera)
    Button btnCamera;
    @Bind(R.id.main_btn_gallery)
    Button btnGallary;
    @Bind(R.id.main_iv_crop)
    ImageView ivCrop;

    @OnClick(R.id.main_btn_crop)
    public void crop() {
        crop(inUri);
    }

    public void crop(Uri uri) {
        Intent cropIntent = new Intent();
        cropIntent.setData(uri);
        Bundle extras = new Bundle();
        extras.putParcelable(MediaStore.EXTRA_OUTPUT, outUri);
//        extras.putString("circleCrop", "true");
//        cropIntent.putExtra(Extra.ASPECT_X, 1);
//        cropIntent.putExtra(Extra.ASPECT_Y, 1);
//        cropIntent.putExtra(Extra.MAX_X, 512);
//        cropIntent.putExtra(Extra.MAX_Y, 512);
        cropIntent.putExtras(extras);
        cropIntent.setClass(this, CropImage.class);
        startActivityForResult(cropIntent, REQUEST_CROP);
    }

    @OnClick(R.id.main_btn_camera)
    public void camera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, inUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @OnClick(R.id.main_btn_gallery)
    public void gallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLARY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //init
        avatar = new File(getPath(this), "avatar.jpg");
        avatar_crop = new File(getPath(this), "avatar_crop.jpg");
        try {
            avatar_crop.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inUri = Uri.fromFile(avatar);
        outUri = Uri.fromFile(avatar_crop);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getPath(Context context) {
        boolean v1 = Environment.getExternalStorageState().equals("mounted");
        String v3 = String.valueOf(context.getPackageName()) + "/images/";
        String v4 = v1 ? "/sdcard/Pictures/" + v3 : "/data/data/" + v3;
        File v0 = new File(v4);
        if (!v0.exists()) {
            v0.mkdirs();
        }

        return v0.getPath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CROP:
                try {
                    ivCrop.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), outUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CAMERA:
                crop(inUri);
                break;
            case REQUEST_GALLARY:
                crop(data.getData());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LEON-LONG", "onDestroy");
    }
}
