package cultoftheunicorn.marvel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.cultoftheunicorn.marvel.R;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    String mid = "Admin";

    Button btn_training,btn_recognize,btn_viewrecords;
    ImageView img,iv_home,iv_scan,iv_search;
    TextView tv_edit,tv_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_edit= (TextView) findViewById(R.id.tv_edit);
        btn_recognize = (Button) findViewById(R.id.btn_recognize);
        btn_training = (Button) findViewById(R.id.btn_training);
        btn_viewrecords=(Button) findViewById(R.id.btn_viewrecords);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_scan = (ImageView) findViewById(R.id.iv_scan);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        img = (ImageView) findViewById(R.id.img);
        tv_edit.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        btn_training.setOnClickListener(this);
        btn_recognize.setOnClickListener(this);
        btn_viewrecords.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_scan.setOnClickListener(this);
        iv_home.setOnClickListener(this);



        Bitmap fdp=loadDp();
        if(fdp!=null){
            img.setImageBitmap(fdp);
        }
    }
    public Bitmap loadDp(){
        FileInputStream fis;
        Bitmap bitmap = null;
        try
        {
            fis=this.openFileInput(mid+".png");
            bitmap= BitmapFactory.decodeStream(fis);
//            Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
//            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    public void saveDP(Bitmap bitmap){
        FileOutputStream fos;
        try
        {
            fos= this.openFileOutput(mid+".png", Context.MODE_PRIVATE);

            bitmap.compress(Bitmap.CompressFormat.PNG,80,fos);
            fos.close();
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_edit:
            {
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in,1);

                break;
            }
            case R.id.btn_training:
            {
                startActivity(new Intent(HomeActivity.this,NameActivity.class));
                finish();
                break;
            }
            case R.id.btn_recognize:
            {
                startActivity(new Intent(HomeActivity.this,Recognize.class));
                finish();
                break;
            }
            case R.id.btn_viewrecords:
            {
                startActivity(new Intent(HomeActivity.this,view_records.class));
                break;
            }
            case R.id.iv_home:
            {
//                startActivity(new Intent(HomeActivity.this,NameActivity.class));
//                finish();
                break;
            }
            case R.id.iv_scan:
            {

                startActivity(new Intent(HomeActivity.this,NameActivity.class));
                finish();
                break;
            }
            case R.id.iv_search:
            {

                startActivity(new Intent(HomeActivity.this,Recognize.class));
                finish();
                break;
            }
            case R.id.tv_logout:
            {
                SharedPreferences pre = getSharedPreferences("AuthPre",MODE_PRIVATE);
                SharedPreferences.Editor editor = pre.edit();
                editor.putBoolean("auth",false);
                editor.commit();

                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finishAffinity();
                break;

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(data!=null)
            {
            Uri uri = data.getData();

                String filepathcolumn[] = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filepathcolumn, null, null, null);
                cursor.moveToNext();

                int index = cursor.getColumnIndex(filepathcolumn[0]);
                String picpath = cursor.getString(index);
                Bitmap bitmap = BitmapFactory.decodeFile(picpath);
                saveDP(bitmap);

                Bitmap fdp = loadDp();
                if (fdp != null) {
                    img.setImageBitmap(fdp);
                }
            }
            else{
                Toast.makeText(this, "No Image selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
    long onBack;
    @Override
    public void onBackPressed() {
        if (onBack + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finishAffinity();
        }
        else{
            Toast.makeText(getBaseContext(), "Press once again to exit",
                    Toast.LENGTH_SHORT).show();
            onBack = System.currentTimeMillis();
        }
    }
}
