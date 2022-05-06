package com.mac.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.mac.covidtracker.api.ApiUtilities;
import com.mac.covidtracker.api.CountryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Country_Activity extends AppCompatActivity {
    private RecyclerView countries;
    private List<CountryData>list;
    private ProgressDialog pd;
    private CountryAdapter adapter;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        countries=findViewById(R.id.countries);
        pd=new ProgressDialog(this);
        searchBar=findViewById(R.id.searchBar);

        list=new ArrayList<>();

        countries.setLayoutManager(new LinearLayoutManager(this));
        countries.setHasFixedSize(true);

        adapter=new CountryAdapter(this,list);
        countries.setAdapter(adapter);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();


        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());
                adapter.notifyDataSetChanged();
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {

                Toast.makeText(Country_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();

            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });
    }

    private void filter(String text) {

        List<CountryData> filterList = new ArrayList<>();
        for(CountryData items : list){
            if(items.getCountry().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);

            }
        }
        adapter.filterList(filterList);
    }


}