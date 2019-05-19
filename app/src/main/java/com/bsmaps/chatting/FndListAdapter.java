package com.bsmaps.chatting;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

    public class FndListAdapter extends ArrayAdapter<Ffriends> {
        List<Ffriends> heroList;

        Context context;

        int resource;
        private MainActivity mainActivity;
        //DBM dbm;
        public FndListAdapter(MainActivity mainActivity,Context context, int resource, List<Ffriends> heroList) {
            super(context, resource, heroList);
            this.context = context;
            this.mainActivity=mainActivity;
            this.resource = resource;
            this.heroList = heroList;
          // dbm =new DBM(context);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(resource, null, false);
            final Ffriends hero = heroList.get(position);

            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.openPlayer(hero.getTeam());
                }
            });
            TextView textViewName = view.findViewById(R.id.textViewName);
            TextView textViewTeam = view.findViewById(R.id.textViewTeam);
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainActivity.openPlayer(hero.getTeam());
                }
            });
            textViewTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.openPlayer(hero.getTeam());
                }
            });

            Button buttonDeleteaddloc = view.findViewById(R.id.buttonDeleteaddloc);
            buttonDeleteaddloc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.openPlayer(hero.getTeam());
                }
            });
            Button buttonDeleteremoveloc = view.findViewById(R.id.buttonDeleteremoveloc);
            buttonDeleteremoveloc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mainActivity.openPlayer(hero.getTeam());
                }
            });

            imageView.setImageDrawable(context.getResources().getDrawable(hero.getImage()));
            textViewName.setText("Channel Name: "+hero.getName());
            textViewTeam.setText("Channel Id: "+hero.getTeam());
            buttonDeleteaddloc.setText(hero.getName());
            buttonDeleteremoveloc.setText(hero.getTeam());

            return view;
        }
        private void removeHero(final int position) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Are you sure you want to delete this?");
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    heroList.remove(position);
//                    notifyDataSetChanged();
//                }
//            });
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
        }
    }
