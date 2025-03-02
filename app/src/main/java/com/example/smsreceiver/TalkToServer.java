package com.example.smsreceiver;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class TalkToServer {

    private String url;
    private Context mycontext;

    public TalkToServer(String urlOfServer, Context context){
        this.url = urlOfServer;
        this.mycontext = context;
    }

    public int sendData(ArrayList<String> data){
        try{
            URL url = new URL(this.url);

            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            client.setRequestProperty("Accept", "application/json");
            client.setDoInput(true);
            client.setDoOutput(true);

            for(int i = 0; i< data.size(); i++) {
                client.getOutputStream().write(data.get(i).getBytes("utf-8"));
            }

            Log.d("Status Code", "Status Code From Server: " + client.getResponseCode());
            Log.i("Server message", "Message from server: " + client.getResponseMessage());

            DataInputStream in = new DataInputStream(client.getInputStream());

            StringBuffer output = new StringBuffer();

            String str;

            while ((str = in.readLine()) != null ){
                output.append(str);
            }

            in.close();

            Log.d("Output", output.toString());
        }
        catch(MalformedURLException ex){
            ex.printStackTrace();
            return 201;
        }
        catch (IOException ex1){
            ex1.printStackTrace();
            return 202;
        }

        catch(Exception ex3){
            ex3.printStackTrace();
            return 204;
        }

        return 200;
    }

    public int execute(ArrayList<String> data){

        Context mine = this.mycontext;
        Thread mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int Sucess = sendData(data);

                    if(Sucess == 200) Log.d("Success", "Data sent to server");
                    else{
                        Log.d("Failure", "There was an error to server");
                    }
                }
            catch (Exception ex){
                ex.printStackTrace();

                }
            }
        });

        mythread.start();
        return 100;
    }
}
