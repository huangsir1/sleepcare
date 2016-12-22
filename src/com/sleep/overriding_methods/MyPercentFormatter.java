package com.sleep.overriding_methods;

import java.text.DecimalFormat;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MyPercentFormatter implements ValueFormatter, YAxisValueFormatter {

	protected DecimalFormat mFormat;

	public MyPercentFormatter() {
		mFormat = new DecimalFormat("###,###,##0.00");
	}

	// ValueFormatter
	@Override
	public String getFormattedValue(float value, Entry entry, int dataSetIndex,
			ViewPortHandler viewPortHandler) {
		return mFormat.format(value) + " %";
	}

	// YAxisValueFormatter
	@Override
	public String getFormattedValue(float value, YAxis yAxis) {
		return mFormat.format(value) + " %";
	}
}
