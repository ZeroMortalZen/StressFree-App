package com.linx.stress_free_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.linx.stress_free_app.AnimationController.ProgressBarAnimation;
import com.linx.stress_free_app.AppUsageController.AppUsageAdapter;
import com.linx.stress_free_app.AppUsageController.AppUsageAsyncTask;
import com.linx.stress_free_app.StressSystem.HelperClass;

import java.util.ArrayList;

public class DisplayAppUsageFragment extends Fragment {

    private FragmentSubmitListener submitListener;
    private RecyclerView recyclerView;
    private AppUsageAdapter adapter;
    private AppUsageAsyncTask appUsageAsyncTask;
    ProgressBar progressBar;
    TextView PrecentageView;
    TextView typewriterText;
    Button Submit;
    HelperClass helperClass = new HelperClass();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private long totalAppUsage = 0;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_app_usage, container, false);



        FirebaseDatabase database = FirebaseDatabase.getInstance();




        Submit=view.findViewById(R.id.Submit);

        //TypeWriter
        typewriterText = (TextView) view.findViewById(R.id.typewriter_text3);
        handler.postDelayed(runnable, 500); // adjust the delay to make it start after a certain time

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AppUsageAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        appUsageAsyncTask = new AppUsageAsyncTask(getActivity(), recyclerView, adapter);
        fetchTotalAppUsage();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitListener.onSubmit(0);
            }
        });





        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        appUsageAsyncTask.execute();
    }



    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        int i = 0;
        final String text = "Checking App Data Usage will help tell us how much you use apps.";
        @Override
        public void run() {
            if (i <= text.length()) {
                String str = text.substring(0, i);
                typewriterText.setText(str);
                i++;
                handler.postDelayed(this, 50); // adjust the delay to make it faster or slower
            }
        }
    };


    private void fetchTotalAppUsage() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = database.getReference("users").child(userId);

            userRef.child("totalAppUsage").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        totalAppUsage = dataSnapshot.getValue(Long.class);
                        handler.postDelayed(runnable, 500);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        submitListener = null;
    }

}

