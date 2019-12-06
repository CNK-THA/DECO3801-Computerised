package com.example.deco3801computerised;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Team Computerised DECO3801 Sem 2 2019
 * @contact c.kachornvuthidej@uqconnect.edu.au
 *
 * Add a user to the system. To be done by the admin.
 */
public class AdminAddUser extends AppCompatActivity {

    List<String> epoch = new ArrayList<String>(); //Frequency of diary logging
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        //Pre-configured epoch
        epoch.add("1 min");
        epoch.add("5 min");
        epoch.add("10 min");
        epoch.add("15 min");
        epoch.add("20 min");
        epoch.add("25 min");
        epoch.add("30 min");

        //epoch selecter as a spinner
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, epoch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        createUser();
    }

    /**
     * Attempt to create the user. Get all the attributes given by the user.
     */
    private void createUser() {
        Button createButton = (Button)findViewById(R.id.createUser); //create user button
        Button addButton = (Button)findViewById(R.id.tagAdd); //add activity button
        final EditText tagContent = (EditText)findViewById(R.id.tagText);
        final EditText userID = (EditText)findViewById(R.id.idNum);
        final Switch graph = (Switch)findViewById(R.id.dataGraph);
        final Switch hip = (Switch)findViewById(R.id.hip);
        final Switch wrist = (Switch)findViewById(R.id.wrist);

        final TagContainerLayout mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tag);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagContainerLayout.addTag(tagContent.getText().toString());
                tagContent.getText().clear();
            }
        });

        //Adding customised activity tags
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                mTagContainerLayout.removeTag(position);
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // do nothing
            }

            @Override
            public void onSelectedTagDrag(int position, String text){
                // do nothing
            }

            @Override
            public void onTagCrossClick(int position) {
                // do nothing
            }
        });

        //Set create account button onclick
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Attributes/informations are saved in the userIdNumber file in internal storage
                String filename = "userIdNumber";

                String fileContents = userID.getText().toString();


                if(fileContents.length() == 0) { //The given ID is empty, error
                    errorCreating();
                } else {
                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(fileContents.getBytes());
                        outputStream.write("\n".getBytes());

                        List<String> tags = mTagContainerLayout.getTags();

                        for (int i = 0; i < tags.size(); i++) {
                            outputStream.write(tags.get(i).getBytes());
                            outputStream.write("\n".getBytes());
                        }

                        outputStream.close();

                        int position = 0;
                        if ((hip.isChecked() && wrist.isChecked()) || (!hip.isChecked() &&
                                !wrist.isChecked())) {
                            errorCreating();
                            position = -1;
                        } else if (hip.isChecked()) {
                            position = 1;
                        }

                        //There was an error before, discontinue
                        if(position != -1) {
                            User newUser = new User(graph.isChecked(), position, 0); //CHANGE 0
                            sendData(fileContents, newUser);
                            startActivity(new Intent(AdminAddUser.this, Menu.class));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * Send data to cloud database (Google Cloud) to be viewed by admin
     * @param id participant id of the current user
     * @param userData User object containing all data about a user
     */
    private void sendData(String id, User userData) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String idNum = id;
        final User data = userData;

        //Authenticate and send data to the server
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();

                    myRef.child(idNum).setValue(data);


                } else {
                    errorCreating();
                }
            }
        });
    }

    /**
     * There is an error creating a user, display the error message to prompt re-try
     */
    private void errorCreating() {
        // Build an AlertDialog
        AlertDialog builder = new AlertDialog.Builder(AdminAddUser.this).create();
        builder.setTitle("Error");
        builder.setMessage("Please ensure all information are inputted correctly");

        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
