package cultoftheunicorn.marvel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.cultoftheunicorn.marvel.R;


public class LoginActivity extends AppCompatActivity {

    EditText et1,et2;
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn1 = (Button) findViewById(R.id.btn_login);
        et1 = (EditText) findViewById(R.id.et_email);
        et2 = (EditText) findViewById(R.id.et_password);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et1.getText().toString();
                String password = et2.getText().toString();

                if(username.length()==0 || password.length()==0){
                    Toast.makeText(LoginActivity.this, "Please enter username & password", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(username.equals("admin")&&password.equals("admin123")){
                        Toast.makeText(LoginActivity.this, "Login In", Toast.LENGTH_SHORT).show();

                        SharedPreferences pre = getSharedPreferences("AuthPre",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pre.edit();
                        editor.putBoolean("auth",true);
                        editor.commit();
                        Intent in = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(in);
                        finishAffinity();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Username & Password didn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
