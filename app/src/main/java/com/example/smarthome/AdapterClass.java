package com.example.smarthome;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    ArrayList<ButtonModel> list;
    Context context;


    private DatabaseReference mReference;


    public AdapterClass(ArrayList<ButtonModel> list , Context context) {
        this.list = list;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {


        holder.name.setText(list.get(i).getButtonName());
        holder.state.setText(list.get(i).getButtonState());
        holder.icon.setImageResource(R.drawable.bulb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase mDatabase;
                mDatabase=FirebaseDatabase.getInstance();
                final String userUID= FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                final String key=list.get(i).getButtonPinNumber();
                mReference= mDatabase.getReference().child("users").child(userUID).child("Buttons").child(key);


                String state=holder.state.getText().toString();
                Log.e("state",state);
                Log.e("key",key);

                if (state.equals("1"))
                {
                   // Log.e("state","onFirebase");
                    mReference.child("buttonState").setValue("0");
                }else if (state.equals("0")){
                    //Log.e("state","offFirebase");
                    mReference.child("buttonState").setValue("1");
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,state;
        ImageView icon;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_id);
            icon=itemView.findViewById(R.id.icon);
            state=itemView.findViewById(R.id.state);
            cardView=itemView.findViewById(R.id.view);


        }
    }

}
