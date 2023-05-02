package com.linx.stress_free_app;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linx.stress_free_app.MainMenu.MainMenuActivity;
import com.linx.stress_free_app.StressSystem.HelperClass;

import java.util.Locale;


public class BedTimeFragment extends Fragment {

   View view;
   Button timeButton;
   int hour, minute;
   Button TimeSubmit;
    HelperClass helperClass = new HelperClass();
   TextView typewriter_text2;
   EditText editHoursText;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FragmentSubmitListener submitListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSubmitListener) {
            submitListener = (FragmentSubmitListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentSubmitListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_bed_time, container, false);
        TimeSubmit=(Button)view.findViewById(R.id.sumbitTime);
        typewriter_text2 = (TextView) view.findViewById(R.id.typewriter_text2);
        handler.postDelayed(runnable, 500);

        editHoursText =(EditText)view.findViewById(R.id.editHoursText);


        TimeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String setTime = editHoursText.getText().toString();

                //convert to float
                float timeInFloat = Float.parseFloat(setTime);
                helperClass.setTime(timeInFloat);

                //Database connection & Store Values
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();
                DatabaseReference userRef = database.getReference("users").child(userId);

                userRef.child("time").setValue(timeInFloat);

                if (submitListener != null) {
                    submitListener.onSubmit(2);
                }

                String getTime= String.valueOf(helperClass.getTime());
                Toast.makeText(getActivity(),getTime,Toast.LENGTH_SHORT).show();



            }
        });





        return view;
    }



    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        int i = 0;
        final String text = "Enter Amount of hours you sleep. ";
        @Override
        public void run() {
            if (i <= text.length()) {
                String str = text.substring(0, i);
                typewriter_text2.setText(str);
                i++;
                handler.postDelayed(this, 50); // adjust the delay to make it faster or slower
            }
        }
    };



}