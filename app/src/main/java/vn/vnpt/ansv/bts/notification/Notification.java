package vn.vnpt.ansv.bts.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import vn.vnpt.ansv.bts.R;

/**
 * Created by ANSV on 11/23/2017.
 */

public class Notification extends AppCompatActivity {
    TextView titleView;
    TextView messageView;

    String title;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        titleView = (TextView) findViewById(R.id.title);
        messageView = (TextView) findViewById(R.id.message);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");

        titleView.setText(title);
        messageView.setText(message);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");

        titleView.setText("Refreshed Notification: \n"+title);
        messageView.setText(message);

    }
}
