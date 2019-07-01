package com.example.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowActivity extends AppCompatActivity {

    public ArrayList<Map<String, Object>> mainlist=new ArrayList<Map<String,Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);
        mainlist = (ArrayList<Map<String, Object>>)getIntent().getExtras().get("key");
        LineChart mLineChart = (LineChart) findViewById(R.id.lineChart);
        //显示边界
        mLineChart.setDrawBorders(true);
        //设置数据
        List<Entry> entries = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i,Float.parseFloat((String) mainlist.get(i).get("po")) ) );
        }
        //准备好每个点对应的x轴数值
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(TimeStamp2Date((String) mainlist.get(i).get("1")));
        }
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(list));

        //一个LineDataSet就是一条线
        LineDataSet lineDataSet;
        lineDataSet = new LineDataSet(entries, "温度");
        LineData data = new LineData(lineDataSet);
        mLineChart.setData(data);

    }
    public String TimeStamp2Date(String timestampString){
        Long timestamp = Long.parseLong(timestampString);
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        return date;
    }
}