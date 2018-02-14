package e.gym.hackeam;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button gBN;
    Button cBN;

    Button gBNcfg;
    Button cBNcfg;

    static String imagename;

    int REQUEST_IMAGE_CAPTURE=101;
    ProgressDialog dialog;
    String s;

    String url="http://54.244.152.44/api/name=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog=new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        gBN=(Button)findViewById(R.id.glaucomaBN);
        cBN=(Button)findViewById(R.id.cataractBN);

        gBNcfg=(Button)findViewById(R.id.glaucomaBNcfg);
        cBNcfg=(Button)findViewById(R.id.cataractBNcfg);

        gBNcfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s="gl";
                selectimage();
            }
        });

        cBNcfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s="ca";
                selectimage();
            }
        });


        gBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"gl",Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ) {


                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},2);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                    return;
                }

                s="gl";

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        cBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"ca",Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ) {


                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},2);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                    return;
                }

                s="ca";

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.this.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }

        if (resultCode==RESULT_OK && requestCode ==1001 && data!=null)
        {

            String arr[]=data.getData().toString().split("%");
            imagename=arr[arr.length-1];
            Log.d("CHAT_IMAGE",arr[arr.length-1]);

            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                uploadImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        imageEncoded=imageEncoded.replace("/","@");
        imageEncoded=imageEncoded.replace("=","^");
        dialog.show();
        if(s.equals("gl")){
            makeRequest(imageEncoded,1);
        }else{
            makeRequest(imageEncoded,2);
        }


    }

    public void makeRequest(String im,int id){
        String urlS=url+im+"&id="+Integer.toString(id);
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, urlS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dialog.show();
                if(s.equals("gl")){
                    Intent i;
                    dialog.dismiss();
                    i=new Intent(MainActivity.this,Main3Activity.class);
                    i.putExtra("res",response);
                    requestQueue.stop();
                    startActivity(i);
                }else {
                    Intent i;
                    i=new Intent(MainActivity.this,CataractActivity.class);
                    i.putExtra("res",response);
                    dialog.dismiss();
                    requestQueue.stop();
                    startActivity(i);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                Log.d("aaaaa",error.toString());
                Toast.makeText(MainActivity.this,"Questionable image.",Toast.LENGTH_SHORT).show();
                requestQueue.stop();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                720000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
        requestQueue.start();

    }

    private void selectimage() {
        Intent pickimage = new Intent(Intent.ACTION_PICK);
        pickimage.setType("image/*");
        pickimage.createChooser(pickimage,"Choose Image");
        startActivityForResult(pickimage,1001);
    }

    private void uploadImage(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dialog.show();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        imageEncoded=imageEncoded.replace("/","@");
        imageEncoded=imageEncoded.replace("=","^");
        if(s.equals("gl")){
            makeRequest(imageEncoded,1);
        }else{
            makeRequest(imageEncoded,2);
        }

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference(CHAT_ID);
//        StorageReference imagesRef = storageRef.child(imagename);



    }
}
