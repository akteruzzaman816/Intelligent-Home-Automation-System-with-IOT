package com.example.smarthome;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
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

        if(list.get(i).getButtonState().equals("1")){
            holder.icon.setImageResource(R.drawable.bulb);
            holder.pinNumber.setBackgroundResource(R.drawable.circle_green);
        }else{
            holder.icon.setImageResource(R.drawable.bulb1);
            holder.pinNumber.setBackgroundResource(R.drawable.circle_red);

        }


        holder.name.setText(list.get(i).getButtonName());
        holder.state.setText(list.get(i).getButtonState());
        holder.pinNumber.setText(list.get(i).getButtonPinNumber());

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FirebaseDatabase mDatabase;
                mDatabase=FirebaseDatabase.getInstance();
                final String userUID= FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                final String key=list.get(i).getButtonPinNumber();
                mReference= mDatabase.getReference().child("users").child(userUID).child("Buttons").child(key);


                mReference.removeValue();
                Toast.makeText(context, "Button Removed", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                return true;
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,state,pinNumber;
        ImageView icon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_id);
            state=itemView.findViewById(R.id.state);
            icon=itemView.findViewById(R.id.icon);
            pinNumber=itemView.findViewById(R.id.pin_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i=0; i<list.size();i++)
                    {
                        list.get(i).setButtonState("0");
                    }
                    list.get(getAdapterPosition()).setButtonState("1");
                    notifyDataSetChanged();
                }
            });




        }
    }

}
