package e.gym.hackeam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    TextView gtv;
    TextView res;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        gtv=(TextView)findViewById(R.id.glaucomaTV);
        res=(TextView)findViewById(R.id.resultGlauTV);
        String ratio=getIntent().getStringExtra("res");
        if(ratio.length()>7){
            ratio=ratio.substring(0,7);
        }
        gtv.setText("Ratio :"+ratio);
        ratio=ratio.replace("\"","");
        Double d=Double.parseDouble(ratio);
        if(d>0.69){
            result="Glaucoma : negative";
            res.setText(result);
        }else{
            result="Glaucoma : positive";
            res.setText(result);
        }

    }
}
