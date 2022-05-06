package com.mac.covidtracker;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mac.covidtracker.api.CountryData;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewAdapter> {

    private Context context;
    private List<CountryData>list;

    public CountryAdapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CountryViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.countries_item_layout,parent,false);


        return new CountryViewAdapter(view);
    }

    public void filterList(List<CountryData> filterList){

        list =filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewAdapter holder, int position) {

        CountryData currentItem = list.get(position);
        holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(currentItem.getCases())));
        holder.countryName.setText(currentItem.getCountry());
        holder.sno.setText(String.valueOf(position+1));

        Map<String,String> img = currentItem.getCountryInfo();
        Picasso.get().load(img.get("flag")).into(holder.countryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("country",currentItem.getCountry());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CountryViewAdapter extends RecyclerView.ViewHolder {
        private TextView sno, cases,countryName;
        private ImageView countryImage;

        public CountryViewAdapter(@NonNull View itemView) {
            super(itemView);

            sno=itemView.findViewById(R.id.sno);
            cases=itemView.findViewById(R.id.cases);
            countryName=itemView.findViewById(R.id.countryName);
            countryImage=itemView.findViewById(R.id.countryImage);
        }
    }
}
