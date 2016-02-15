package com.studyjam.com.studyjamapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdvancedCustomArrayAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] presidents;
    private final Integer[] imageIds;
    public  DataSnapshot snapshot1;
    public Firebase myFirebaseRef;


    public AdvancedCustomArrayAdapter(
            Activity context, String[] presidents, Integer[] imageIds) {
        super(context, R.layout.layout2, presidents);
        this.context = context;
        this.presidents = presidents;
        this.imageIds = imageIds;
        Firebase.setAndroidContext(getContext());
        myFirebaseRef = new Firebase("https://bbsrstudyjam.firebaseio.com/");




    }



    static class ViewContainer {
        public ImageView imageView;
        public TextView txtTitle;
        public TextView txtDescription;

    }



    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewContainer viewContainer;
        View rowView = view;
        final int pos = position;


        //---print the index of the row to examine---
        Log.d("CustomArrayAdapter",String.valueOf(position));

        //---if the row is displayed for the first time---
        if (rowView == null) {

            Log.d("CustomArrayAdapter", "New");
            LayoutInflater inflater = context.getLayoutInflater();

            rowView = inflater.inflate(R.layout.layout2, null, true);

            //---create a view container object---
            viewContainer = new ViewContainer();

            //---get the references to all the views in the row---
            viewContainer.txtTitle = (TextView)
                    rowView.findViewById(R.id.txtPresidentName);
            viewContainer.txtDescription = (TextView)
                    rowView.findViewById(R.id.txtDescription);
            viewContainer.imageView = (ImageView) rowView.findViewById(R.id.icon);

            //---assign the view container to the rowView---
            rowView.setTag(viewContainer);
        } else {

            //---view was previously created; can recycle---            
            Log.d("CustomArrayAdapter", "Recycling");
            //---retrieve the previously assigned tag to get
            // a reference to all the views; bypass the findViewByID() process,
            // which is computationally expensive---
            viewContainer = (ViewContainer) rowView.getTag();

        }


        //---customize the content of each row based on position---
        //Added reference to our firebase object- displaying the data from our firebase project
       myFirebaseRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                viewContainer.txtTitle.setText(String.valueOf(dataSnapshot.child("presidents").child(String.valueOf(pos)).child("Name").getValue()));
                Picasso.with(getContext()).load(String.valueOf(dataSnapshot.child("presidents").child(String.valueOf(pos)).child("imageUrl").getValue())).into(viewContainer.imageView);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        //viewContainer.imageView.setImageResource(imageIds[position]);
        //Picasso.with(getContext()).load("http://presidenteisenhower.net/images/presidenteisenhower.net/dwight-eisenhower.png").into(viewContainer.imageView);
        return rowView;
    }
}