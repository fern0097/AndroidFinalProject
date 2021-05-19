package com.example.finalproject.covid19cases;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.finalproject.R;

import java.util.List;

/**
 * MyListAdapter is a class that display the ListView of the Covid-19 cases
 *
 * @author Wilker Fernandes de Sousa
 * @version 1.0
 */
class MyListAdapter extends BaseAdapter {

    /**
     * Context of the calling activity.
     */
    private final CovidCasesListActivity covidCaseActivity;

    /**
     * Field that refers to the array list that contains covid-19 cases object.
     */
    private List<CovidCase> covidCaseList;

    /**
     * Constructor MyListAdapter
     *
     * @param covidCaseActivity
     * @param covidCaseList
     */
    public MyListAdapter(CovidCasesListActivity covidCaseActivity, List<CovidCase> covidCaseList) {
        this.covidCaseActivity = covidCaseActivity;
        this.covidCaseList = covidCaseList;
    }

    /**
     * @return a count list from CovidCaseList.
     */
    @Override
    public int getCount() {
        return covidCaseList.size();
    }

    @Override
    /**
     * Returns the int item list position from CovidCaseList
     */
    public CovidCase getItem(int position) {
        return covidCaseList.get(position);
    }

    /**
     * getItemId is a method that get the position id number from covidCaseList Array list.
     *
     * @param position
     * @return the id from the database
     */
    @Override
    public long getItemId(int position) {
        CovidCase cc = covidCaseList.get(position);
        return cc.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        CovidCase cc = getItem(position);
        v = covidCaseActivity.getLayoutInflater().inflate(R.layout.activity_covid19_row, parent, false);

        TextView textViewProvince = v.findViewById(R.id.textViewProvince);
        TextView textViewCases = v.findViewById(R.id.textViewCases);
        TextView textViewDate = v.findViewById(R.id.textViewDate);
        textViewProvince.setText(cc.getCountryCode() + " - " + cc.getProvince());
        textViewCases.setText(Integer.toString(cc.getCases()));
        textViewDate.setText(cc.getDate());

        return v;
    }
}