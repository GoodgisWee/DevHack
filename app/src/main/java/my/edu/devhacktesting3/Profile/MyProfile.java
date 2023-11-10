package my.edu.devhacktesting3.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import my.edu.devhacktesting3.R;


public class MyProfile extends AppCompatActivity {

    private Button saveProfileButton;
    private Button backProfileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Define an array of currency options including the default option
        String[] status = {"Passenger", "Driver"};

        // Find the Spinner by its ID
        Spinner currencySpinner = findViewById(R.id.statusSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        currencySpinner.setAdapter(adapter);

        // Set a default selection (optional)
        currencySpinner.setSelection(0); // Select the "Please select Currency" item as the default

        saveProfileButton = findViewById(R.id.saveButton);
        backProfileButton = findViewById(R.id.backButton);

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the save button click
                onSaveButtonClick();
            }
        });

        backProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the save button click
                onBackButtonClick();
            }
        });

    }

    public void onSaveButtonClick() {
        // Add the logic to save the profile information
        // For example, you can save the selected items from the Spinner or perform any other actions.
        Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onBackButtonClick() {
        // Add the logic to save the profile information
        // For example, you can save the selected items from the Spinner or perform any other actions.
        finish();
    }


}