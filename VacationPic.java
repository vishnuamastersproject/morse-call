package com.project.vactionbook;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class VacationPic extends AppCompatActivity {

    Button addImage;
    ArrayList<MemoryDTO> imageArry = new ArrayList<MemoryDTO>();
    ImageAdapter imageAdapter;
    private static final int CAMERA_REQUEST = 1;
    ProgressDialog progressDialog;
    ListView dataList;
    byte[] imageName;
    int imageId;
    Bitmap theImage;
    DataBaseHandler db;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_picture2);
        startService(new Intent(this, MyService.class));
        dataList = (ListView) findViewById(R.id.list);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vacation Book");
        /**
         * create DatabaseHandler object
         */
        db = new DataBaseHandler(this);
        /**
         * Reading and getting all records from database
         */
        progressDialog=new ProgressDialog(VacationPic.this);
        progressDialog.setMessage("Loading Image....");
        List<MemoryDTO> memoryDTOs = db.getAllContacts();
        for (MemoryDTO cn : memoryDTOs) {
            String log = "ID:" + cn.getID() + " Name: " + cn.getName()
                    + " ,Image: " + cn.getImage();

            // Writing Contacts to log
            Log.d("Result: ", cn.toString());
            // add memoryDTOs data com arrayList
            imageArry.add(cn);

        }
        /**
         * Set Data base Item into listview
         */
        if(memoryDTOs.size()>0){
            imageAdapter = new ImageAdapter(this, R.layout.screen_list2,
                    imageArry);
            dataList.setAdapter(imageAdapter);
        }else{
            Snackbar.make(dataList, "Add Vacation Pics To List", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }


        /**
         * open dialog for choose camera
         */

        final String[] option = new String[] {"Take from Camera"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Log.e("Selected Item", String.valueOf(which));
                if (which == 0) {
                    callCamera();
                }


            }
        });
        final AlertDialog dialog = builder.create();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(VacationPic.this,MainActivity.class);
                intent.putExtra("id",position);
                intent.putExtra("allowMarking",0);
                startActivity(intent);
                finish();
            }
        });



    }

    /**
     * On activity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CAMERA_REQUEST:

                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap yourImage = extras.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();

                    // Inserting Contacts
                    Log.d("Insert: ", "Inserting ..");
                    db.addContact(new MemoryDTO("Android", imageInByte));
                    progressDialog.show();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Intent i = new Intent(VacationPic.this,
                                    AddMemory.class);

                            startActivity(i);
                            finish();
                        }
                    },1000);


                }
                break;
        }
    }

    /**
     * open camera method
     */
    public void callCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 200);
    }

}
