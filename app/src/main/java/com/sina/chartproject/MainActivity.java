package com.sina.chartproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sina.chartproject.data.ElementData;
import com.sina.chartproject.element.DateElement;
import com.sina.chartproject.element.DegreeElement;
import com.sina.chartproject.element.ElementView;
import com.sina.chartproject.element.HistogramElement;
import com.sina.chartproject.element.LineElement;
import com.sina.chartproject.element.OutlineElement;
import com.sina.chartproject.view.BaseViewEngine;
import com.sina.chartproject.view.CapitalChartView;
import com.sina.chartproject.view.TestViewEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private CapitalChartView charView;
    private TextView tvStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        charView = findViewById(R.id.char_view);
        tvStart = findViewById(R.id.tv_start);

        Random random = new Random();
        List<ElementData> lineDatas1 = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            lineDatas1.add(new ElementData(i * Math.abs(random.nextInt(20))));
        }
        List<ElementData> lineDatas2 = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            lineDatas2.add(new ElementData(i * Math.abs(random.nextInt(20))));
        }

        List<ElementData> lineDatas3 = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            lineDatas3.add(new ElementData(i + 10));
        }

        //柱状图
        HistogramElement histogramElement = new HistogramElement(this, lineDatas3);
        histogramElement.setScape(false);
        List<ElementView> elementViews = new ArrayList<>();
        //折线图1--50个点
        LineElement lineElement1 = new LineElement(this, lineDatas1);
        lineElement1.setLineConfig(R.color.color_508cee);
        lineElement1.setScape(true);
        //折线图2--50个点
        LineElement lineElement2 = new LineElement(this, lineDatas2);
        lineElement2.setLineConfig(R.color.color_fba85c);
        lineElement2.setScape(false);
        //轮廓图
        OutlineElement element = new OutlineElement(this);
        element.setHasXDivider(true, 2);
        element.setHasYDivider(true, 3);
        //日期图
        DateElement dateElement = new DateElement(this);
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dates.add("2021-02-" + i);
        }
        dateElement.setValues(dates);
        //刻度线
        DegreeElement degreeElement = new DegreeElement(this);
        List<String> yValues = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            yValues.add("销售量" + i * 12323);
        }
        degreeElement.setyValueList(yValues, yValues);
        //添加元素
        elementViews.add(element);
        elementViews.add(lineElement1);
        elementViews.add(lineElement2);
        elementViews.add(dateElement);
        elementViews.add(degreeElement);
        elementViews.add(histogramElement);

        TestViewEngine testViewEngine = new TestViewEngine(this);
        testViewEngine.setElementViews(elementViews);
        charView.postDelayed(new Runnable() {
            @Override
            public void run() {
                charView.setCurrentView(testViewEngine);
            }
        }, 300);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                charView.setCurrentView(testViewEngine);
                charView.invalidate();
            }
        });
    }


}