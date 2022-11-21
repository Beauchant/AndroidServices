package ue.project.androidservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Magnifier;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    EditText txtUrl;
    EditText txtEmail;
    EditText txtNumber;
    EditText txtTarget;

    Button btnUrl;
    Button btnEmail;
    Button btnDial;
    Button btnText;
    Button btnCall;
    Button btnMap;
    Button btnSearch;

    final int PERMISSION_TO_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUrl = findViewById(R.id.txtUrl);
        txtEmail = findViewById(R.id.txtEmail);
        txtNumber = findViewById(R.id.txtNumber);
        txtTarget = findViewById(R.id.txtTarget);

        btnUrl = findViewById(R.id.btnUrl);
        btnEmail = findViewById(R.id.btnEmail);
        btnDial = findViewById(R.id.btnDial);
        btnText = findViewById(R.id.btnText);
        btnCall = findViewById(R.id.btnCall);
        btnMap = findViewById(R.id.btnMap);
        btnSearch = findViewById(R.id.btnSearch);

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = txtUrl.getText().toString();
                if(url.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter an url address!", Toast.LENGTH_SHORT).show();
                }else{
                    openWebPage(url);
                }
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] addresses = new String[1];
                String address = txtEmail.getText().toString();
                if (address.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter an email address!", Toast.LENGTH_SHORT).show();
                }else {
                    addresses[0] = address;
                    composeEmail(addresses, "Greeting");
                }
            }
        });

        btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dialPhone = txtNumber.getText().toString();
                if(dialPhone.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a phone number!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialPhoneNumber(dialPhone);
                }
            }
        });

        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dialPhone = txtNumber.getText().toString();
                if(dialPhone.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a phone number!", Toast.LENGTH_SHORT).show();
                }
                else {
                    composeMmsMessage(dialPhone, "Hello");
                }
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = txtNumber.getText().toString();
                if(phoneNumber.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a phone number!", Toast.LENGTH_SHORT).show();
                }
                else {
                    callPhoneNumber(phoneNumber);
                }
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String target = "geo:0,0?q=" + txtTarget.getText().toString();
                if(txtTarget.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter the target location!", Toast.LENGTH_SHORT).show();
                }else {
                    showMap(Uri.parse(target));
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = txtTarget.getText().toString();
                if(search.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter the subject of your search!", Toast.LENGTH_LONG).show();
                }
                else {
                    searchWeb(search);
                }
            }
        });
    }

    // Open Web Page
    public void openWebPage(String url) {
        if(!url.startsWith("http://") || !url.startsWith("https://")){
            url = "http://" + url;
        }

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }

    // Send email
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }

    // Open phone dialog
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }

    // Send SMS
    public void composeMmsMessage(String phoneNumber, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    // Call phone number
    public void callPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_TO_CALL);
        }
        else{
            startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
        }
    }
    // Call phone number suite
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_TO_CALL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    callPhoneNumber(txtNumber.getText().toString());
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(MainActivity.this, "Cannot make a call without your permission!", Toast.LENGTH_SHORT).show();
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    // Open google map
    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }

    // Search frpm web
    public void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }
}
