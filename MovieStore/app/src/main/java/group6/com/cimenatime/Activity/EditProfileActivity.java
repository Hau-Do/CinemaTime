package group6.com.cimenatime.Activity;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import group6.com.cimenatime.Fragment.DateDialogFragment;
import group6.com.cimenatime.Other.CircleImageView;
import group6.com.cimenatime.R;

/**
 * Created by Dinh-Chuong on 03/12/2017.
 */
public class EditProfileActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    TextView txvSetDate;
    Spinner spinnerGender;
    EditText edtName, edtEmail;
    ImageView imgImages;
    String imgDecodableString;
    String NameValue, EmailValue, DateTimeValue;
    int SpinnerValue;
    private static final int RESULT_LOAD_IMG = 1;
    Bitmap bitmap;
    public static final int REQUEST_READ_PERMISSION = 786;
    public static final int REQUEST_READ_PERMISSION2 = 787;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Init();
        Event();

        readdata();
    }

    /**
     * get Id for view and set adapter and list for spinner
     */
    private void Init() {
        txvSetDate = (TextView) findViewById(R.id.EditProfile_DateTime);
        spinnerGender = (Spinner) findViewById(R.id.EditProfile_Gender);
        edtName = (EditText) findViewById(R.id.EditProfile_textName);
        edtEmail = (EditText) findViewById(R.id.EditProfile_Email);
        imgImages = (ImageView) findViewById(R.id.EditProfile_ImageView1);

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.Gender));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(dataAdapter);
        registerForContextMenu(imgImages);


    }

    /**
     * Check if user click to check button
     * if wrong show snackbar
     */
    private void check() {
        NameValue = edtName.getText().toString();
        EmailValue = edtEmail.getText().toString();
        DateTimeValue = txvSetDate.getText().toString();
        SpinnerValue = spinnerGender.getSelectedItemPosition();

        if (!NameValue.trim().equals("") && !EmailValue.trim().equals("") && !DateTimeValue.trim().equals("")) {
            savingdata();
            finish();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Bạn Chưa Nhập Đủ Dữ Liệu", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Set text when click to choose your birthday.
     */
    private void Event() {
        txvSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment dateDialog = new DateDialogFragment(v);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                dateDialog.show(fragmentTransaction, "ft");

            }
        });
        /**
         * click to choose Image
         */
        imgImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openContextMenu(v);
                requestPermission();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editprofile, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_editprofile_check:
                check();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * choose images from gallery
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();


                bitmap = BitmapFactory.decodeFile(imgDecodableString);
                imgImages.setImageBitmap(new CircleImageView().getCircleBitmap(bitmap));
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * Use Sharedpreferences to Save Data, with name, email, datetime, spinner, bitmap
     */
    private void savingdata() {
        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        SharedPreferences.Editor b = pre.edit();
        b.putString("Name", NameValue);
        b.putString("Email", EmailValue);
        b.putString("DateTime", DateTimeValue);
        b.putInt("Spinner", SpinnerValue);
//        b.putString("Bitmap",BitMapToString(bitmap));
        b.putString("Bitmap", imgDecodableString);
        b.commit();
    }

    /**
     * Use Sharedpreferences to read Data, with name, email, datetime, spinner, bitmap
     */
    private void readdata() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (ContextCompat.checkSelfPermission(EditProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                Toast.makeText(this, "request permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(EditProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_PERMISSION2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else {
                SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
                String Name = pre.getString("Name", "");
                String Email = pre.getString("Email", "");
                String DateTime = pre.getString("DateTime", "");
                int Spinner = pre.getInt("Spinner", 0);
                String Bitmap = pre.getString("Bitmap", "");
                edtName.setText(Name);
                edtEmail.setText(Email);
                txvSetDate.setText(DateTime);
                spinnerGender.setSelection(Spinner);
                if (!Bitmap.equals("")) {
                    bitmap = BitmapFactory.decodeFile(Bitmap);
                    imgImages.setImageBitmap(new CircleImageView().getCircleBitmap(bitmap));
                } else {
                    Toast.makeText(EditProfileActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
            String Name = pre.getString("Name", "");
            String Email = pre.getString("Email", "");
            String DateTime = pre.getString("DateTime", "");
            int Spinner = pre.getInt("Spinner", 0);
            String Bitmap = pre.getString("Bitmap", "");
            edtName.setText(Name);
            edtEmail.setText(Email);
            txvSetDate.setText(DateTime);
            spinnerGender.setSelection(Spinner);
            if (!Bitmap.equals("")) {
                bitmap = BitmapFactory.decodeFile(Bitmap);
                imgImages.setImageBitmap(new CircleImageView().getCircleBitmap(bitmap));
            } else {
                Toast.makeText(EditProfileActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (ContextCompat.checkSelfPermission(EditProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                Toast.makeText(this, "request permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(EditProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            else{
                getImagesFromGallery();
            }

        } else {
            getImagesFromGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            readdata();
            getImagesFromGallery();
        } else {
            Toast.makeText(this, "You're Deny", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUEST_READ_PERMISSION2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
            String Name = pre.getString("Name", "");
            String Email = pre.getString("Email", "");
            String DateTime = pre.getString("DateTime", "");
            int Spinner = pre.getInt("Spinner", 0);
            String Bitmap = pre.getString("Bitmap", "");
            edtName.setText(Name);
            edtEmail.setText(Email);
            txvSetDate.setText(DateTime);
            spinnerGender.setSelection(Spinner);
            if (!Bitmap.equals("")) {
                bitmap = BitmapFactory.decodeFile(Bitmap);
                imgImages.setImageBitmap(new CircleImageView().getCircleBitmap(bitmap));
            } else {
                Toast.makeText(EditProfileActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getImagesFromGallery() {
        Uri uri = Uri.parse("content://media/external/");

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
//        Intent galleryIntent = new Intent();
//        galleryIntent.setType("images/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//        // Start the Intent
//        startActivityForResult(Intent.createChooser(galleryIntent,
//                "Select Picture"), RESULT_LOAD_IMG);
    }
}
