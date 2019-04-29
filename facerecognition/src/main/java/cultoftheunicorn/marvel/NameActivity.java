package cultoftheunicorn.marvel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.cultoftheunicorn.marvel.R;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText email = (EditText) findViewById(R.id.email);

        Button nextButton = (Button) findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().equals("")) {


                    System.out.println("1111");
                    try{
                        saveData(name.getText().toString(),email.getText().toString());
                        System.out.println("2222");


                    }
                    catch(Exception e)
                    {
                        Toast.makeText(NameActivity.this, "Error "+e, Toast.LENGTH_SHORT).show();
                        System.out.println("3333 "+e);

                    }
                    Intent intent = new Intent(NameActivity.this, Training.class);
                    intent.putExtra("name", name.getText().toString().trim());
                    intent.putExtra("email", email.getText().toString().trim());

                    startActivity(intent);
                }
                else {
                    Toast.makeText(NameActivity.this, "Please enter the name", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void saveData(String name,String email)
    {
        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db= helper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("email",email);
        db.insert("student",null,cv);
        Toast.makeText(this, "Data Proceed....", Toast.LENGTH_SHORT).show();

        db.close();
        helper.close();
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(NameActivity.this,HomeActivity.class));
        finish();
    }
}
