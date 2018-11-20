package com.example.foxtz.gameplan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;

public class filterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //category spinner init
        final Spinner categorySpinner = findViewById(R.id.categorySpinnerFilter);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        //game spinner init
        final Spinner gameSpinner = findViewById(R.id.gameSpinnerFilter);

        ArrayAdapter<CharSequence> gameAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                R.array.ball_games_array, android.R.layout.simple_spinner_item);
        gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(gameAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        ArrayAdapter<CharSequence> gameBallAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.ball_games_array, android.R.layout.simple_spinner_item);
                        gameBallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gameSpinner.setAdapter(gameBallAdapter);
                        break;
                    case 1:
                        ArrayAdapter<CharSequence> gameVideoAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.video_games_array, android.R.layout.simple_spinner_item);
                        gameVideoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gameSpinner.setAdapter(gameVideoAdapter);
                        break;
                    case 2:
                        ArrayAdapter<CharSequence> gameBoardAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.board_games_array, android.R.layout.simple_spinner_item);
                        gameBoardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gameSpinner.setAdapter(gameBoardAdapter);
                        break;
                    case 3:
                        ArrayAdapter<CharSequence> gameWorkoutAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.workout_array, android.R.layout.simple_spinner_item);
                        gameWorkoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gameSpinner.setAdapter(gameWorkoutAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //from year spinner init
        final Spinner fromYearSpinner = findViewById(R.id.fromYearSpinnerFilter);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                R.array.years_array, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromYearSpinner.setAdapter(yearAdapter);

        //from month spinner init
        final Spinner fromMonthSpinner = findViewById(R.id.fromMonthSpinnerFilter);
        ArrayAdapter<CharSequence> monthsAdapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromMonthSpinner.setAdapter(monthsAdapter);

        //from day spinner init
        final Spinner fromDaySpinner = findViewById(R.id.fromDaySpinnerFilter);

        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                R.array.day_31_array, android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromDaySpinner.setAdapter(daysAdapter);

        fromMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                    case 2:
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                        ArrayAdapter<CharSequence> days31Adapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.day_31_array, android.R.layout.simple_spinner_item);
                        days31Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fromDaySpinner.setAdapter(days31Adapter);
                        break;
                    case 3:
                    case 5:
                    case 8:
                    case 10:
                        ArrayAdapter<CharSequence> days30Adapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.day_30_array, android.R.layout.simple_spinner_item);
                        days30Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fromDaySpinner.setAdapter(days30Adapter);
                        break;
                    case 1:
                        ArrayAdapter<CharSequence> days28Adapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.day_28_array, android.R.layout.simple_spinner_item);
                        days28Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fromDaySpinner.setAdapter(days28Adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //to year spinner init
        final Spinner toYearSpinner = findViewById(R.id.toYearSpinnerFilter);
        ArrayAdapter<CharSequence> toYearAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                R.array.years_array, android.R.layout.simple_spinner_item);
        toYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toYearSpinner.setAdapter(toYearAdapter);

        //to month spinner init
        final Spinner toMonthSpinner = findViewById(R.id.toMonthSpinnerFilter);
        ArrayAdapter<CharSequence> toMonthsAdapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        toMonthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toMonthSpinner.setAdapter(toMonthsAdapter);

        //to day spinner init
        final Spinner toDaySpinner = findViewById(R.id.toDaySpinnerFilter);

        ArrayAdapter<CharSequence> toDaysAdapter = ArrayAdapter.createFromResource(filterActivity.this,
                R.array.day_31_array, android.R.layout.simple_spinner_item);
        toDaysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toDaySpinner.setAdapter(toDaysAdapter);

        toMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                    case 2:
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                        ArrayAdapter<CharSequence> days31Adapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.day_31_array, android.R.layout.simple_spinner_item);
                        days31Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        toDaySpinner.setAdapter(days31Adapter);
                        break;
                    case 3:
                    case 5:
                    case 8:
                    case 10:
                        ArrayAdapter<CharSequence> days30Adapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.day_30_array, android.R.layout.simple_spinner_item);
                        days30Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        toDaySpinner.setAdapter(days30Adapter);
                        break;
                    case 1:
                        ArrayAdapter<CharSequence> days28Adapter = ArrayAdapter.createFromResource(filterActivity.this,
                                R.array.day_28_array, android.R.layout.simple_spinner_item);
                        days28Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        toDaySpinner.setAdapter(days28Adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //city spinner init
        final Spinner citySpinner = findViewById(R.id.citySpinnerFilter);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        Button filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get info
                String category = categorySpinner.getItemAtPosition(categorySpinner.getSelectedItemPosition()).toString();
                String game = gameSpinner.getItemAtPosition(gameSpinner.getSelectedItemPosition()).toString();
                String city = citySpinner.getItemAtPosition(citySpinner.getSelectedItemPosition()).toString();
                String fromDay = fromDaySpinner.getItemAtPosition(fromDaySpinner.getSelectedItemPosition()).toString();
                String fromMonth = fromMonthSpinner.getItemAtPosition(fromMonthSpinner.getSelectedItemPosition()).toString();
                String fromYear = fromYearSpinner.getItemAtPosition(fromYearSpinner.getSelectedItemPosition()).toString();
                String toDay = toDaySpinner.getItemAtPosition(toDaySpinner.getSelectedItemPosition()).toString();
                String toMonth = toMonthSpinner.getItemAtPosition(toMonthSpinner.getSelectedItemPosition()).toString();
                String toYear = toYearSpinner.getItemAtPosition(toYearSpinner.getSelectedItemPosition()).toString();
//                String dateString = day+"-"+month+"-"+year;


                //verify all fields are valid

                DateTime today = new org.joda.time.DateTime();
                int thisYear = today.getYear();
                int thisMonth = today.getMonthOfYear();
                int thisDay = today.getDayOfMonth();
                StringsClass strings = new StringsClass();
                String monthsList[] = strings.getMonths();
                int monthInt = 0;
                for (int i = 0; i < monthsList.length; i++){
                    if(monthsList[i].equals(fromMonth)){
                        monthInt = i+1;
                    }
                }
                if((thisYear >= Integer.valueOf(fromYear)) && (thisMonth >= monthInt) && (thisDay > Integer.valueOf(fromDay))){
                    Toast.makeText(filterActivity.this, "Start date is too early",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                DateTime start = new DateTime(Integer.valueOf(fromYear),Integer.valueOf(fromMonth),Integer.valueOf(fromDay),1,1, 1, 1);
                DateTime end = new DateTime(Integer.valueOf(toYear),Integer.valueOf(toMonth),Integer.valueOf(toDay),1,1, 1, 1);
                if(start.isAfter(end)){
                    Toast.makeText(filterActivity.this, "End date is before start date",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(filterActivity.this, "changeMe", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                PostFilter filter = new PostFilter();
                filter.setFilterFull();
                filter.setDateAsStart(Integer.valueOf(fromYear),Integer.valueOf(fromMonth),Integer.valueOf(fromDay));
                filter.setDateAsEnd(Integer.valueOf(toYear),Integer.valueOf(toMonth),Integer.valueOf(toDay));
                filter.setGameFilter(category,game);
                filter.setCityFilter(city);
                intent.putExtra("filter",filter);
                setResult(Activity.RESULT_OK,intent);

            }
        });
    }
}
