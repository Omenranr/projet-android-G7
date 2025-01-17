package com.rdmn.plutus.blindapp.Activities;

import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rdmn.plutus.blindapp.Common.Common;
import com.rdmn.plutus.blindapp.Model.firebase.Rate;
import com.rdmn.plutus.blindapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RateActivity extends AppCompatActivity {
    Button btnSubmit;
    MaterialRatingBar ratingBar;
    MaterialEditText etComment;

    FirebaseDatabase database;
    DatabaseReference rateDetailRef, helperInformationRef;

    double ratingStars=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        database=FirebaseDatabase.getInstance();
        rateDetailRef=database.getReference(Common.rate_detail_tbl);
        helperInformationRef=database.getReference(Common.user_helper_tbl);

        btnSubmit=findViewById(R.id.btnSubmit);
        ratingBar=(MaterialRatingBar)findViewById(R.id.ratingBar);
        etComment=(MaterialEditText) findViewById(R.id.etComment);

        ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
               ratingStars=rating;
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRateDetails();
            }
        });
    }

    private void submitRateDetails() {
        final AlertDialog alertDialog=new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();

        Rate rate=new Rate();
        rate.setRates(String.valueOf(ratingStars));
        rate.setComment(etComment.getText().toString());

        rateDetailRef.child(Common.helperID)
        .push()
        .setValue(rate)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                rateDetailRef.child(Common.helperID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                double averageStars=0.0;
                                int count=0;
                                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                    Rate rate=postSnapshot.getValue(Rate.class);
                                    averageStars+=Double.parseDouble(rate.getRates());
                                    count++;
                                }
                                double finalAverge=averageStars/count;
                                DecimalFormat df=new DecimalFormat("#.#");
                                String valueUpdate=df.format(finalAverge);

                                Map<String, Object> helperUpdateRate=new HashMap<>();
                                helperUpdateRate.put("rates", valueUpdate);

                                helperInformationRef.child(Common.helperID)
                                        .updateChildren(helperUpdateRate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                alertDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Thank you for submit", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                alertDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Rate updated but can't write to helper information", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Rate failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
