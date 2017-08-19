package com.parag.remoteservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class RandomNumberService extends Service {
    private boolean generateRandonNumberFlag;
    private int randomNumber;

    public RandomNumberService() {}

    class RandomNumberRequestHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 0: Message message = Message.obtain(null,0);
                    message.arg1 = getRandomNumber();
                    try {
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
            }
            super.handleMessage(msg);
        }
    }

    Messenger messenger = new Messenger(new RandomNumberRequestHandler());
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this,"onbind",Toast.LENGTH_SHORT).show();
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        generateRandonNumberFlag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                generateRandomNumber();
            }
        }).start();
        return START_STICKY;
    }

    private void generateRandomNumber()
    {
        Random random = new Random();
        while(generateRandonNumberFlag)
        {
            randomNumber = random.nextInt(100) + 1;
            Log.d("kkkk",""+randomNumber);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        generateRandonNumberFlag = false;
        super.onDestroy();
    }

    public int getRandomNumber()
    {
        return randomNumber;
    }
}
