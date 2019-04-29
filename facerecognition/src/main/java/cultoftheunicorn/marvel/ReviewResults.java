package cultoftheunicorn.marvel;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// uncomment when you enable firebase
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import org.opencv.cultoftheunicorn.marvel.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class Attendees {

    public String names;
    public String date;

    public Attendees() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Attendees(String names, String date) {
        this.names  = names;
        this.date = date;
    }

}

public class ReviewResults extends AppCompatActivity implements ReviewListAdapter.ClickListener {

    private List<String> commitList = new ArrayList<>();
private static final String SERVER_URL="http://v4masters.com/PJCINFOTECH/swati/marvelwebservice/mark_attendance.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_results);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Button commitButton = (Button) findViewById(R.id.button);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Review and Mark");
        }

        List<String> reviewList = Arrays.asList(getIntent().getStringArrayExtra("list"));

        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(this, reviewList);
        reviewListAdapter.setClickListener(this);
        recyclerView.setAdapter(reviewListAdapter);

        //Setting LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*//For adding dividers in the list
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);*/

    }

    @Override
    public void onItemClick(String name) {
        if(commitList.contains(name))
            commitList.remove(name);
        else
            commitList.add(name);
    }

    public void commit() {




StringBuilder stu_collection=new StringBuilder();
        if(commitList.size() != 0) {

            for(Iterator it=commitList.iterator();it.hasNext();)
            {

           String studentname=(String) it.next();

                     studentname=studentname.replace(" ","+");
                stu_collection.append(studentname+",");

            }
        Toast.makeText(this,stu_collection.toString(),Toast.LENGTH_SHORT);
            ServerCon con=new ServerCon();
            con.execute(stu_collection.toString());
//            Enable firebase and then uncomment the following lines

//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("attendence");

//            convert to a comma separated string
//            this has to be the worst way to push data to a db
//            StringBuilder sb = new StringBuilder();
//            for (String s : commitList) {
//                sb.append(s);
//                sb.append(",");
//            }

//            Attendees at = new Attendees(sb.toString(), (new Date()).toString());
//            String key = myRef.push().getKey();
//            myRef.child(key).setValue(at);

            Toast.makeText(getApplicationContext(), "Sending to AWS server", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ReviewResults.this,HomeActivity.class));
            finish();


//            System.out.println(sb.toString());

        }
        else {
            Toast.makeText(getApplicationContext(), "Please select at least one student", Toast.LENGTH_SHORT).show();
        }
    }


    public class ServerCon extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... ar) {
           String stu_collection=ar[0];
           String complet_url=SERVER_URL+"?data="+stu_collection;
           System.out.println("sending.. "+complet_url);
           publishProgress("Url Created "+complet_url);
            String[] em = stu_collection.split(",");
           for(int i =0;i<em.length;i++) {
               String email = getEmail(em[i]);
               if (email != null) {
                   sendMail(email);

               } else {
                   System.out.println("Email is " + em[i]);
               }
           }

            StringBuilder response=new StringBuilder();
           try {
               URL url = new URL(complet_url);
           publishProgress("Before Connection Started");
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
               publishProgress("Connection created");
               System.out.println("Connection Created ");

               InputStream is= connection.getInputStream();
               publishProgress("Response Generated");
               System.out.println("Response Generated");
               BufferedReader br=new BufferedReader(new InputStreamReader(is));
               String line=null;
               while((line=br.readLine())!=null)
               {
                   response.append(line);
               }
publishProgress("response prepared "+response.toString());
               System.out.println("Response "+response.toString());

           }
           catch(Exception e)
           {}
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(ReviewResults.this, "Mail Sent !", Toast.LENGTH_SHORT).show();
            Toast.makeText(ReviewResults.this, "Data Submitted to Server "+s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(ReviewResults.this, values[0], Toast.LENGTH_SHORT).show();
        }

        public String getEmail(String name)
        {
            String email=null;
            DBHelper helper=new DBHelper(ReviewResults.this);
            SQLiteDatabase db= helper.getReadableDatabase();
            Cursor c=db.rawQuery("select * from student",null);
            while(c.moveToNext()) {
                String n = c.getString(0);
                if(name.equalsIgnoreCase(n))
                {
                    email = c.getString(1);
                    break;
                }

            }
            c.close();
            db.close();
            helper.close();

            return email;
        }
        public void sendMail(String email)
        {
            Properties props = new Properties();
            //Configuring properties for gmail
            //If you are not using gmail you may need to change the values
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            //Creating a new session
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        //Authenticating the password
                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication("chaitanyagupta91@gmail.com", "gupta1997");
                            return new PasswordAuthentication("grey.akash@gmail.com", "9463628155");
                        }
                    });
            try {
                //Creating MimeMessage object
                Message mm = new MimeMessage(session);
                //Setting sender address
                mm.setFrom(new InternetAddress("admin"));
                //Adding receiver
                mm.addRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                //Adding subject
                mm.setSubject("Attendance Notification");
                //Adding message
                mm.setText("Dear "+email+"\n"+"" +
                        "Attendance have been marked for " +new Date()+""+
                        "\n" +
                        "Regards," +
                        "PJC Attendance System");
                //Sending email
                Transport.send(mm);

            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
    }
}
