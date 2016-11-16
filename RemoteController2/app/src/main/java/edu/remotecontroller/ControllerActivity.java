package edu.remotecontroller;
import com.erz.joysticklibrary.*;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.erz.joysticklibrary.JoyStick;



public class ControllerActivity extends AppCompatActivity implements JoyStick.JoyStickListener {

    //TcpClient tcpConnection;
    private TCPClient mTcpClient;
    String hostAddr = "";
    String hostPort = "";
    JoyStick joy1;
    JoyStick joy2;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joy1 = (JoyStick) findViewById(R.id.joy1);
        joy1.setListener(this);
        joy1.setPadColor(Color.parseColor("#55ffffff"));
        joy1.setButtonColor(Color.parseColor("#55ff0000"));

        joy2 = (JoyStick) findViewById(R.id.joy2);
        joy2.setListener(this);
        joy2.enableStayPut(true);
        joy2.setPadBackground(1);
        joy2.setButtonDrawable(1);

        Intent i = getIntent();
        //String hostName = i.getStringExtra("hostName");
        hostAddr = i.getStringExtra("hostAddr");
        hostPort = i.getStringExtra("hostPort");

        new connectTask().execute("");
    }
    @Override
    public void onMove(JoyStick joyStick, double angle, double power) {

        if (mTcpClient != null) {
            mTcpClient.sendMessage(
                            "joy1;power="  + (int)joy1.getPower() +
                            ";angle="      + (int)joy1.getAngleDegrees() +
                            ";joy2:power=" + (int)joy2.getPower() +
                            ";angle="      + (int)joy2.getAngleDegrees()+";");
        }
    }

    public class connectTask extends AsyncTask<String, String, TCPClient> {
        @Override
        protected TCPClient doInBackground(String... message) {
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                    @Override
                    //here the messageReceived method is implemented
                    public void messageReceived(String message) {
                      //this method calls the onProgressUpdate
                publishProgress(message);
                }
                });
                mTcpClient.setHostAddr(hostAddr);
                mTcpClient.setHostPort(Integer.parseInt(hostPort));
                mTcpClient.run();

                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }


}
