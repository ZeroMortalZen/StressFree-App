package com.linx.stress_free_app;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.linx.stress_free_app.StressSystem.PersonStressHelperClass;

import java.sql.Time;
import java.util.Locale;


public class BedTimeFragment extends Fragment {

   View view;
   Button timeButton;
   int hour, minute;
   Button TimeSubmit;
   PersonStressHelperClass stressHelperClass = new PersonStressHelperClass();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_bed_time, container, false);
        timeButton=(Button)view.findViewById(R.id.timebutton);
        TimeSubmit=(Button)view.findViewById(R.id.sumbitTime);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        timeButton.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));

                        float hourfloat = selectedHour;
                        float BedTime =hourfloat;

                        stressHelperClass.setTime(BedTime);


                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener,hour,minute,true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });


        TimeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTime= String.valueOf(stressHelperClass.getTime());
                Toast.makeText(getActivity(),getTime,Toast.LENGTH_SHORT).show();

            }
        });





        return view;
    }



}