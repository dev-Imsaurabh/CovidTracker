package com.mac.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.covidtracker.api.ApiUtilities;
import com.mac.covidtracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView country_Name,date,totalConfirmed,todayConfirmed,totalActive,totalRecovered,todayRecovered,totalDeath,todayDeath,totalTest;
    private List<CountryData> list;
    private PieChart pieChart;
    private String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=new ArrayList<>();
        if(getIntent().getStringExtra("country")!=null){
            country=getIntent().getStringExtra("country");
        }
        initialize();
        country_Name.setText(country);




        country_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Country_Activity.class));
            }
        });

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {

                        list.addAll(response.body());
                        for(int i = 0 ; i<list.size(); i++){
                            if(list.get(i).getCountry().equals(country)){
                                int confirm =Integer.parseInt( list.get(i).getCases());
                                int active =Integer.parseInt( list.get(i).getActive());
                                int recover =Integer.parseInt( list.get(i).getRecovered());
                                int death =Integer.parseInt( list.get(i).getDeaths());
                                int test =Integer.parseInt( list.get(i).getTests());

                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalConfirmed.setText(NumberFormat.getInstance().format(confirm));
                                totalRecovered.setText(NumberFormat.getInstance().format(recover));
                                totalDeath.setText(NumberFormat.getInstance().format(death));
                                totalTest.setText(NumberFormat.getInstance().format(test));

                                todayDeath.setText("("+"+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths()))+")");
                                todayRecovered.setText("("+"+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered()))+")");
                                todayConfirmed.setText("("+"+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases()))+")");

                                setText(list.get(i).getUpdated());


                                pieChart.addPieSlice(new PieModel("Confirm",confirm,getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue)));
                                pieChart.addPieSlice(new PieModel("Recovered",recover,getResources().getColor(R.color.green)));
                                pieChart.addPieSlice(new PieModel("Deaths",death,getResources().getColor(R.color.red)));
                                pieChart.startAnimation();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {

                        Toast.makeText(MainActivity.this, "Error"+t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void setText(String updated) {
        DateFormat format=new SimpleDateFormat("MMM dd, yyyy");
        long milliSeconds = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        date.setText("Updated on " + format.format(calendar.getTime()));
    }

    private void initialize() {
        todayConfirmed=findViewById(R.id.todayConfirmed);
        totalConfirmed=findViewById(R.id.totalConfirmed);
        totalActive=findViewById(R.id.totalActive);
        todayRecovered=findViewById(R.id.todayRecovered);
        totalRecovered=findViewById(R.id.totalRecovered);
        totalDeath=findViewById(R.id.totalDeath);
        todayDeath=findViewById(R.id.todayDeath);
        totalTest=findViewById(R.id.totalTest);
        pieChart=findViewById(R.id.piechart);
        date=findViewById(R.id.date);
        country_Name=findViewById(R.id.country_Name);


    }
}