package my.edu.devhacktesting3.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.devhacktesting3.MainActivity;
import my.edu.devhacktesting3.R;

public class Login extends AppCompatActivity {

    private EditText loginID, password;
    private Button loginButton;

    // Set the custom firebase URL
    String customDatabaseUrl = "https://busiorempit-default-rtdb.asia-southeast1.firebasedatabase.app";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(customDatabaseUrl);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginID = findViewById(R.id.loginID);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String uid = loginID.getText().toString();
        String userPassword = password.getText().toString();

        if (uid.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Please enter both uid and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get user detail from Firebase
        DatabaseReference userRef = firebaseDatabase.getReference("/User/" + uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String passwordFromFirebase = snapshot.child("password").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);

                    // Check if password correct or not
                    if (passwordFromFirebase.equals(userPassword)) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("type", type);
                        startActivity(intent);
                    } else {
                        // Password is incorrect
                        Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // UID doesn't exist in Firebase
                    Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
