package e.gym.hackeam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CataractActivity extends AppCompatActivity {

    TextView ctv;
    TextView condTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cataract);

        ctv=(TextView)findViewById(R.id.cataractTV);
        condTV=(TextView)findViewById(R.id.cataractCondTV);

        String s=getIntent().getStringExtra("res");
        String devSum=s.split("&")[0];
        String intensity=s.split("&")[1];
        devSum=devSum.replace("\"","");
        intensity=intensity.replace("\"","");


        if (devSum.length()>7){
            devSum=devSum.substring(1,7);
        }

        if(intensity.length()>7){
            intensity=intensity.substring(0,7);
        }
        float d1;
        float d2;
        d1=Float.valueOf(devSum);
        d2=Float.valueOf(intensity);
        String result;
        ctv.setText("devSum:"+d1+"\n"+"intensity:"+d2);
        if(d1>2.0f || d2>90f){
            result="Cataract detected.";
            condTV.setText(result);
        }else {
            result = "Cataract  not detected.";
            condTV.setText(result);
        }

    }
}
