package com.project.vactionbook;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;



import java.util.concurrent.TimeUnit;



public class MyService extends Service  implements Shaker.Callback
{  private Shaker shaker=null;
    static int count=0;
    static  long timestampCheck=0;
    Time time;
    @Override
    public void onCreate()
    {time=new Time();
        shaker=new Shaker(this, 2.25d, 500, this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    public void shakingStarted() {
        Log.d("ShakerDemo", "Shaking started!");

    }

    public void shakingStopped() {
        Log.d("ShakerDemo", "Shaking stopped!");
        count++;

        if(count>0){//(count==2)
            Log.i("Count",count+"");
            Time t2=new Time();
            t2.setToNow();
            long diff = TimeUnit.MILLISECONDS.toSeconds(t2.toMillis(true)-time.toMillis(true));
           // if(diff<=10)
            {

                Intent i = new Intent();
                i.setClass(this, VacationPic.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
            count=0;

        }else{

            time.setToNow();

        }

    }
}


