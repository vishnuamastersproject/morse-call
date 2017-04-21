package com.project.vactionbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddMemory extends AppCompatActivity {
    int idReceived;
    MemoryDTO memoryDTO;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_layout);
       /* Bundle bundle=getIntent().getExtras();
        idReceived=bundle.getInt("ID");
       */ DataBaseHandler dataBaseHandler=  new DataBaseHandler(this);

        ArrayList<MemoryDTO> memoryDTOs =new ArrayList<>();
        memoryDTOs = (ArrayList<MemoryDTO>) dataBaseHandler.getAllContacts1();
        memoryDTO = memoryDTOs.get(memoryDTOs.size()-1);

        Log.i("COnta", memoryDTO.toString());
        byte[] outImage= memoryDTO._image;
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        ((ImageView)findViewById(R.id.imgIcon12)).setImageBitmap(theImage);

        getSupportActionBar().setTitle("Add New Memory");

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        ((EditText)findViewById(R.id.dateOfCreation)).setText(formattedDate);

        //Make memory Decription Scrollable
        editText=((EditText)findViewById(R.id.entry));
        editText.setScroller(new Scroller(AddMemory.this));
        editText.setMaxLines(10);
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(new ScrollingMovementMethod());


        findViewById(R.id.addEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject=((EditText)findViewById(R.id.subject)).getText().toString();
                String dateOfCreation=((EditText)findViewById(R.id.dateOfCreation)).getText().toString();
                String entry=((EditText)findViewById(R.id.entry)).getText().toString();
                if((subject!=null)&&(dateOfCreation!=null)&&(entry!=null)){

            MemoryDTO newMemoryDTO =new MemoryDTO();
                    newMemoryDTO._memoryDate=dateOfCreation;
                    newMemoryDTO._memoryTitle=subject;
                    newMemoryDTO._memoryDescription=entry;
                    newMemoryDTO._id= memoryDTO._id;
                    new DataBaseHandler(AddMemory.this).updateMemory(newMemoryDTO);
                    Toast.makeText(AddMemory.this,"Entry Saved !",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(AddMemory.this,MainActivity.class);
                    intent.putExtra("id",-1);
                    intent.putExtra("allowMarking",1);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(AddMemory.this,"Please Enter All the Values",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
