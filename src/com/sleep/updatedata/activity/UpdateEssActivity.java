package com.sleep.updatedata.activity;

import com.taiir.sleepcare.home.R;


import com.loopj.android.application.MyApplication;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.dialog.SubmitDialog;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.sleepservice.WebHelper;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;
import com.sleep.utils.NetworkProber;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.ESSAndDisEaseHistoryDto;
import taiyi.web.model.dto.Status;

public class UpdateEssActivity extends Activity {
	private ImageView imageViewback;
	// 1
	private CheckBox b1_1, b1_2, b1_3, b1_4;
	// 2
	private CheckBox b2_1, b2_2, b2_3, b2_4;;
	// 3
	private CheckBox b3_1, b3_2, b3_3, b3_4;;
	// 4
	private CheckBox b4_1, b4_2, b4_3, b4_4;;
	// 5
	private CheckBox b5_1, b5_2, b5_3, b5_4;;
	// 6
	private CheckBox b6_1, b6_2, b6_3, b6_4;;
	// 7
	private CheckBox b7_1, b7_2, b7_3, b7_4;;
	// 8
	private CheckBox b8_1, b8_2, b8_3, b8_4;;

	private TextView t1, t2, t3, t4, t5, t6, t7, t8;

	private Button mtessSubmit;
	private SubmitDialog sdialog;

	private int Question1 = 0;
	private int Question2 = 0;
	private int Question3 = 0;
	private int Question4 = 0;
	private int Question5 = 0;
	private int Question6 = 0;
	private int Question7 = 0;
	private int Question8 = 0;

	private PublicDao sd;
	private Epworth ep;
	private UserManager um;
	private DiabetesHy dh;

	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_user_questionnaire);
		ExitActivity.getInstance().addActivity(this);
		Intent intent = getIntent();
		sd = new PublicDaoImpl(UpdateEssActivity.this);
		id = intent.getIntExtra("essid", -1);
		ep = sd.findAllEpworthById(id);
		um = sd.findAllUserById(id);
		dh = sd.findAllDiabetesHyById(id);
		sdialog = new SubmitDialog(UpdateEssActivity.this);
		imageViewback = (ImageView) findViewById(R.id.imageView1_bs_updthy);
		// imageViewback.setAlpha(0);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UpdateEssActivity.this.finish();
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			}
		});
		b1_1 = (CheckBox) findViewById(R.id.checkBox1_1_zzyd_updthy);
		b1_2 = (CheckBox) findViewById(R.id.checkBox1_2_zzyd_updthy);
		b1_3 = (CheckBox) findViewById(R.id.checkBox1_3_zzyd_updthy);
		b1_4 = (CheckBox) findViewById(R.id.checkBox1_4_zzyd_updthy);
		t1 = (TextView) findViewById(R.id.textView6_zzyd_updthy);
		b1_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b1_1.isChecked()) {
					b1_1.setChecked(true);
					b1_2.setChecked(false);
					b1_3.setChecked(false);
					b1_4.setChecked(false);
					str = b1_1.getText().toString();
					t1.setText(str);
				}
				if (!b1_1.isChecked()) {
					t1.setText("");
				}
			}
		});
		b1_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b1_2.isChecked()) {
					b1_1.setChecked(false);
					b1_2.setChecked(true);
					b1_3.setChecked(false);
					b1_4.setChecked(false);
					str = b1_2.getText().toString();
					t1.setText(str);
				}
				if (!b1_2.isChecked()) {
					t1.setText("");
				}
			}
		});
		b1_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b1_3.isChecked()) {
					b1_1.setChecked(false);
					b1_2.setChecked(false);
					b1_3.setChecked(true);
					b1_4.setChecked(false);
					str = b1_3.getText().toString();
					t1.setText(str);
				}
				if (!b1_3.isChecked()) {
					t1.setText("");
				}
			}
		});
		b1_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b1_4.isChecked()) {
					b1_1.setChecked(false);
					b1_2.setChecked(false);
					b1_3.setChecked(false);
					b1_4.setChecked(true);
					str = b1_4.getText().toString();
					t1.setText(str);
				}
				if (!b1_4.isChecked()) {
					t1.setText("");
				}
			}
		});
		b2_1 = (CheckBox) findViewById(R.id.checkBox2_1_kdss_updthy);
		b2_2 = (CheckBox) findViewById(R.id.checkBox2_2_kdss_updthy);
		b2_3 = (CheckBox) findViewById(R.id.checkBox2_3_kdss_updthy);
		b2_4 = (CheckBox) findViewById(R.id.checkBox2_4_kdss_updthy);
		t2 = (TextView) findViewById(R.id.textView6_2_kdss_updthy);
		b2_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b2_1.isChecked()) {
					b2_1.setChecked(true);
					b2_2.setChecked(false);
					b2_3.setChecked(false);
					b2_4.setChecked(false);
					str = b2_1.getText().toString();
					t2.setText(str);
				}
				if (!b2_1.isChecked()) {
					t2.setText("");
				}
			}
		});
		b2_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b2_2.isChecked()) {
					b2_1.setChecked(false);
					b2_2.setChecked(true);
					b2_3.setChecked(false);
					b2_4.setChecked(false);
					str = b2_2.getText().toString();
					t2.setText(str);
				}
				if (!b2_2.isChecked()) {
					t2.setText("");
				}
			}
		});
		b2_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b2_3.isChecked()) {
					b2_1.setChecked(false);
					b2_2.setChecked(false);
					b2_3.setChecked(true);
					b2_4.setChecked(false);
					str = b2_3.getText().toString();
					t2.setText(str);
				}
				if (!b2_3.isChecked()) {
					t2.setText("");
				}
			}
		});
		b2_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b2_4.isChecked()) {
					b2_1.setChecked(false);
					b2_2.setChecked(false);
					b2_3.setChecked(false);
					b2_4.setChecked(true);
					str = b2_4.getText().toString();
					t2.setText(str);
				}
				if (!b2_4.isChecked()) {
					t2.setText("");
				}
			}
		});
		b3_1 = (CheckBox) findViewById(R.id.checkBox3_1_ggchbd_updthy);
		b3_2 = (CheckBox) findViewById(R.id.checkBox3_2_ggchbd_updthy);
		b3_3 = (CheckBox) findViewById(R.id.checkBox3_3_ggchbd_updthy);
		b3_4 = (CheckBox) findViewById(R.id.checkBox3_4_ggchbd_updthy);
		t3 = (TextView) findViewById(R.id.textView6_3_ggchbd_updthy);
		b3_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b3_1.isChecked()) {
					b3_1.setChecked(true);
					b3_2.setChecked(false);
					b3_3.setChecked(false);
					b3_4.setChecked(false);
					str = b3_1.getText().toString();
					t3.setText(str);
				}
				if (!b3_1.isChecked()) {
					t3.setText("");
				}
			}
		});
		b3_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b3_2.isChecked()) {
					b3_1.setChecked(false);
					b3_2.setChecked(true);
					b3_3.setChecked(false);
					b3_4.setChecked(false);
					str = b3_2.getText().toString();
					t3.setText(str);
				}
				if (!b3_2.isChecked()) {
					t3.setText("");
				}
			}
		});
		b3_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b3_3.isChecked()) {
					b3_1.setChecked(false);
					b3_2.setChecked(false);
					b3_3.setChecked(true);
					b3_4.setChecked(false);
					str = b3_3.getText().toString();
					t3.setText(str);
				}
				if (!b3_3.isChecked()) {
					t3.setText("");
				}
			}
		});
		b3_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b3_4.isChecked()) {
					b3_1.setChecked(false);
					b3_2.setChecked(false);
					b3_3.setChecked(false);
					b3_4.setChecked(true);
					str = b3_4.getText().toString();
					t3.setText(str);
				}
				if (!b3_4.isChecked()) {
					t3.setText("");
				}
			}
		});
		b4_1 = (CheckBox) findViewById(R.id.checkBox4_1_csjzcbxx_updthy);
		b4_2 = (CheckBox) findViewById(R.id.checkBox4_2_csjzcbxx_updthy);
		b4_3 = (CheckBox) findViewById(R.id.checkBox4_3_csjzcbxx_updthy);
		b4_4 = (CheckBox) findViewById(R.id.checkBox4_4_csjzcbxx_updthy);
		t4 = (TextView) findViewById(R.id.textView6_4_csjzcbxx_updthy);
		b4_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b4_1.isChecked()) {
					b4_1.setChecked(true);
					b4_2.setChecked(false);
					b4_3.setChecked(false);
					b4_4.setChecked(false);
					str = b4_1.getText().toString();
					t4.setText(str);
				}
				if (!b4_1.isChecked()) {
					t4.setText("");
				}
			}
		});
		b4_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b4_2.isChecked()) {
					b4_1.setChecked(false);
					b4_2.setChecked(true);
					b4_3.setChecked(false);
					b4_4.setChecked(false);
					str = b4_2.getText().toString();
					t4.setText(str);
				}
				if (!b4_2.isChecked()) {
					t4.setText("");
				}
			}
		});
		b4_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b4_3.isChecked()) {
					b4_1.setChecked(false);
					b4_2.setChecked(false);
					b4_3.setChecked(true);
					b4_4.setChecked(false);
					str = b4_3.getText().toString();
					t4.setText(str);
				}
				if (!b4_3.isChecked()) {
					t4.setText("");
				}
			}
		});
		b4_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b4_4.isChecked()) {
					b4_1.setChecked(false);
					b4_2.setChecked(false);
					b4_3.setChecked(false);
					b4_4.setChecked(true);
					str = b4_4.getText().toString();
					t4.setText(str);
				}
				if (!b4_4.isChecked()) {
					t4.setText("");
				}
			}
		});
		b5_1 = (CheckBox) findViewById(R.id.checkBox5_1_zzyrth_updthy);
		b5_2 = (CheckBox) findViewById(R.id.checkBox5_2_zzyrth_updthy);
		b5_3 = (CheckBox) findViewById(R.id.checkBox5_3_zzyrth_updthy);
		b5_4 = (CheckBox) findViewById(R.id.checkBox5_4_zzyrth_updthy);
		t5 = (TextView) findViewById(R.id.textView6_5_zzyrth_updthy);
		b5_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b5_1.isChecked()) {
					b5_1.setChecked(true);
					b5_2.setChecked(false);
					b5_3.setChecked(false);
					b5_4.setChecked(false);
					str = b5_1.getText().toString();
					t5.setText(str);
				}
				if (!b5_1.isChecked()) {
					t5.setText("");
				}
			}
		});
		b5_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b5_2.isChecked()) {
					b5_1.setChecked(false);
					b5_2.setChecked(true);
					b5_3.setChecked(false);
					b5_4.setChecked(false);
					str = b5_2.getText().toString();
					t5.setText(str);
				}
				if (!b5_2.isChecked()) {
					t5.setText("");
				}
			}
		});
		b5_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b5_3.isChecked()) {
					b5_1.setChecked(false);
					b5_2.setChecked(false);
					b5_3.setChecked(true);
					b5_4.setChecked(false);
					str = b5_3.getText().toString();
					t5.setText(str);
				}
				if (!b5_3.isChecked()) {
					t5.setText("");
				}
			}
		});
		b5_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b5_4.isChecked()) {
					b5_1.setChecked(false);
					b5_2.setChecked(false);
					b5_3.setChecked(false);
					b5_4.setChecked(true);
					str = b5_4.getText().toString();
					t5.setText(str);
				}
				if (!b5_4.isChecked()) {
					t5.setText("");
				}
			}
		});
		b6_1 = (CheckBox) findViewById(R.id.checkBox6_1_fhxxs_updthy);
		b6_2 = (CheckBox) findViewById(R.id.checkBox6_2_fhxxs_updthy);
		b6_3 = (CheckBox) findViewById(R.id.checkBox6_3_fhxxs_updthy);
		b6_4 = (CheckBox) findViewById(R.id.checkBox6_4_fhxxs_updthy);
		t6 = (TextView) findViewById(R.id.textView6_6_fhxxs_updthy);
		b6_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b6_1.isChecked()) {
					b6_1.setChecked(true);
					b6_2.setChecked(false);
					b6_3.setChecked(false);
					b6_4.setChecked(false);
					str = b6_1.getText().toString();
					t6.setText(str);
				}
				if (!b6_1.isChecked()) {
					t6.setText("");
				}
			}
		});
		b6_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b6_2.isChecked()) {
					b6_1.setChecked(false);
					b6_2.setChecked(true);
					b6_3.setChecked(false);
					b6_4.setChecked(false);
					str = b6_2.getText().toString();
					t6.setText(str);
				}
				if (!b6_2.isChecked()) {
					t6.setText("");
				}
			}
		});
		b6_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b6_3.isChecked()) {
					b6_1.setChecked(false);
					b6_2.setChecked(false);
					b6_3.setChecked(true);
					b6_4.setChecked(false);
					str = b6_3.getText().toString();
					t6.setText(str);
				}
				if (!b6_3.isChecked()) {
					t6.setText("");
				}
			}
		});
		b6_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b6_4.isChecked()) {
					b6_1.setChecked(false);
					b6_2.setChecked(false);
					b6_3.setChecked(false);
					b6_4.setChecked(true);
					str = b6_4.getText().toString();
					t6.setText(str);
				}
				if (!b6_4.isChecked()) {
					t6.setText("");
				}
			}
		});
		b7_1 = (CheckBox) findViewById(R.id.checkBox7_1_kcdhlds_updthy);
		b7_2 = (CheckBox) findViewById(R.id.checkBox7_2_kcdhlds_updthy);
		b7_3 = (CheckBox) findViewById(R.id.checkBox7_3_kcdhlds_updthy);
		b7_4 = (CheckBox) findViewById(R.id.checkBox7_4_kcdhlds_updthy);
		t7 = (TextView) findViewById(R.id.textView6_7_kcdhlds_updthy);
		b7_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b7_1.isChecked()) {
					b7_1.setChecked(true);
					b7_2.setChecked(false);
					b7_3.setChecked(false);
					b7_4.setChecked(false);
					str = b7_1.getText().toString();
					t7.setText(str);
				}
				if (!b7_1.isChecked()) {
					t7.setText("");
				}
			}
		});
		b7_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b7_2.isChecked()) {
					b7_1.setChecked(false);
					b7_2.setChecked(true);
					b7_3.setChecked(false);
					b7_4.setChecked(false);
					str = b7_2.getText().toString();
					t7.setText(str);
				}
				if (!b7_2.isChecked()) {
					t7.setText("");
				}
			}
		});
		b7_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b7_3.isChecked()) {
					b7_1.setChecked(false);
					b7_2.setChecked(false);
					b7_3.setChecked(true);
					b7_4.setChecked(false);
					str = b7_3.getText().toString();
					t7.setText(str);
				}
				if (!b7_3.isChecked()) {
					t7.setText("");
				}
			}
		});
		b7_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b7_4.isChecked()) {
					b7_1.setChecked(false);
					b7_2.setChecked(false);
					b7_3.setChecked(false);
					b7_4.setChecked(true);
					str = b7_4.getText().toString();
					t7.setText(str);
				}
				if (!b7_4.isChecked()) {
					t7.setText("");
				}
			}
		});
		b8_1 = (CheckBox) findViewById(R.id.checkBox8_1_xwjwxxs_updthy);
		b8_2 = (CheckBox) findViewById(R.id.checkBox8_2_xwjwxxs_updthy);
		b8_3 = (CheckBox) findViewById(R.id.checkBox8_3_xwjwxxs_updthy);
		b8_4 = (CheckBox) findViewById(R.id.checkBox8_4_xwjwxxs_updthy);
		t8 = (TextView) findViewById(R.id.textView6_8_xwjwxxs_updthy);
		b8_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b8_1.isChecked()) {
					b8_1.setChecked(true);
					b8_2.setChecked(false);
					b8_3.setChecked(false);
					b8_4.setChecked(false);
					str = b8_1.getText().toString();
					t8.setText(str);
				}
				if (!b8_1.isChecked()) {
					t8.setText("");
				}
			}
		});
		b8_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b8_2.isChecked()) {
					b8_1.setChecked(false);
					b8_2.setChecked(true);
					b8_3.setChecked(false);
					b8_4.setChecked(false);
					str = b8_2.getText().toString();
					t8.setText(str);
				}
				if (!b8_2.isChecked()) {
					t8.setText("");
				}
			}
		});
		b8_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b8_3.isChecked()) {
					b8_1.setChecked(false);
					b8_2.setChecked(false);
					b8_3.setChecked(true);
					b8_4.setChecked(false);
					str = b8_3.getText().toString();
					t8.setText(str);
				}
				if (!b8_3.isChecked()) {
					t8.setText("");
				}
			}
		});
		b8_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String str = "";
				if (b8_4.isChecked()) {
					b8_1.setChecked(false);
					b8_2.setChecked(false);
					b8_3.setChecked(false);
					b8_4.setChecked(true);
					str = b8_4.getText().toString();
					t8.setText(str);
				}
				if (!b8_4.isChecked()) {
					t8.setText("");
				}
			}
		});
		mtessSubmit = (Button) findViewById(R.id.sure_submit2_updthy);
		mtessSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				String q1 = t1.getText().toString();
				String q2 = t2.getText().toString();
				String q3 = t3.getText().toString();
				String q4 = t4.getText().toString();
				String q5 = t5.getText().toString();
				String q6 = t6.getText().toString();
				String q7 = t7.getText().toString();
				String q8 = t8.getText().toString();
				if (b1_1.isChecked()) {
					ep.setSatreading(0);
					Question1 = 0;
				} else if (b1_2.isChecked()) {
					ep.setSatreading(1);
					Question1 = 1;
				} else if (b1_3.isChecked()) {
					ep.setSatreading(2);
					Question1 = 2;
				} else if (b1_4.isChecked()) {
					ep.setSatreading(3);
					Question1 = 3;
				}
				if (b2_1.isChecked()) {
					ep.setWatchtv(0);
					Question2 = 0;
				} else if (b2_2.isChecked()) {
					ep.setWatchtv(1);
					Question2 = 1;
				} else if (b2_3.isChecked()) {
					ep.setWatchtv(2);
					Question2 = 2;
				} else if (b2_4.isChecked()) {
					ep.setWatchtv(3);
					Question2 = 3;
				}
				if (b3_1.isChecked()) {
					ep.setSatnotmove(0);
					Question3 = 0;
				} else if (b3_2.isChecked()) {
					ep.setSatnotmove(1);
					Question3 = 1;
				} else if (b3_3.isChecked()) {
					ep.setSatnotmove(2);
					Question3 = 2;
				} else if (b3_4.isChecked()) {
					ep.setSatnotmove(3);
					Question3 = 3;
				}
				if (b4_1.isChecked()) {
					ep.setLongnotrest(0);
					Question4 = 0;
				} else if (b4_2.isChecked()) {
					ep.setLongnotrest(1);
					Question4 = 1;
				} else if (b4_3.isChecked()) {
					ep.setLongnotrest(2);
					Question4 = 2;
				} else if (b4_4.isChecked()) {
					ep.setLongnotrest(3);
					Question4 = 3;
				}
				if (b5_1.isChecked()) {
					ep.setSatconversation(0);
					Question5 = 0;
				} else if (b5_2.isChecked()) {
					ep.setSatconversation(1);
					Question5 = 1;
				} else if (b5_3.isChecked()) {
					ep.setSatconversation(2);
					Question5 = 2;
				} else if (b5_4.isChecked()) {
					ep.setSatconversation(3);
					Question5 = 3;
				}
				if (b6_1.isChecked()) {
					ep.setAfterdinnerrest(0);
					Question6 = 0;
				} else if (b6_2.isChecked()) {
					ep.setAfterdinnerrest(1);
					Question6 = 1;
				} else if (b6_3.isChecked()) {
					ep.setAfterdinnerrest(2);
					Question6 = 2;
				} else if (b6_4.isChecked()) {
					ep.setAfterdinnerrest(3);
					Question6 = 3;
				}
				if (b7_1.isChecked()) {
					ep.setTrafficlights(0);
					Question7 = 0;
				} else if (b7_2.isChecked()) {
					ep.setTrafficlights(1);
					Question7 = 1;
				} else if (b7_3.isChecked()) {
					ep.setTrafficlights(2);
					Question7 = 2;
				} else if (b7_4.isChecked()) {
					ep.setTrafficlights(3);
					Question7 = 3;
				}
				if (b8_1.isChecked()) {
					ep.setJingworest(0);
					Question8 = 0;
				} else if (b8_2.isChecked()) {
					ep.setJingworest(1);
					Question8 = 1;
				} else if (b8_3.isChecked()) {
					ep.setJingworest(2);
					Question8 = 2;
				} else if (b8_4.isChecked()) {
					ep.setJingworest(3);
					Question8 = 3;
				}

				if (q1.length() == 0) {
					Toast.makeText(UpdateEssActivity.this,
							UpdateEssActivity.this.getString(R.string.the_first_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q2.length() == 0) {
					Toast.makeText(UpdateEssActivity.this,
							UpdateEssActivity.this.getString(R.string.the_second_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q3.length() == 0) {
					Toast.makeText(UpdateEssActivity.this,
							UpdateEssActivity.this.getString(R.string.the_third_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q4.length() == 0) {
					Toast.makeText(UpdateEssActivity.this,
							UpdateEssActivity.this.getString(R.string.the_fourth_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q5.length() == 0) {
					Toast.makeText(UpdateEssActivity.this,
							UpdateEssActivity.this.getString(R.string.the_fifth_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q6.length() == 0) {
					Toast.makeText(UpdateEssActivity.this,
							UpdateEssActivity.this.getString(R.string.the_sixth_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q7.length() == 0) {
					Toast.makeText(UpdateEssActivity.this,
							UpdateEssActivity.this.getString(R.string.the_seventh_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q8.length() == 0) {
					Toast.makeText(UpdateEssActivity.this,
							UpdateEssActivity.this.getString(R.string.the_eighth_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q1 != null && !"".endsWith(q1) && q2 != null && !"".endsWith(q2) && q3 != null
						&& !"".endsWith(q3) && q4 != null && !"".endsWith(q4) && q5 != null && !"".endsWith(q5)
						&& q6 != null && !"".endsWith(q6) && q7 != null && !"".endsWith(q7) && q8 != null
						&& !"".endsWith(q8)) {
					int sum_question = Question1 + Question2 + Question3 + Question4 + Question5 + Question6 + Question7
							+ Question8;
					ep.setSumscore(sum_question);

					boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
					boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
					mHandler.sendEmptyMessage(2);
					if (isConnected || isConnected3g) {
						new Thread() {
							public void run() {
								sd = new PublicDaoImpl(UpdateEssActivity.this);
								final UserNumber un = sd.findTelorEmail();
								try {
									if (un != null && un.getToken() != null) {
										Status testToken = new WebAPI().testToken(un.getToken());
										if (!Status.isSuccess(testToken)) {
											mHandler.sendEmptyMessage(1);
										} else {
											ESSAndDisEaseHistoryDto ed = WebHelper.userESS(um.getUuid(), ep, dh);
											// Looper.prepare();
											Thread.sleep(3000);
											Status status = new WebAPI().uploadEssAndDiseaseHistory(ed);
											if (Status.isSuccess(status)) {
												sd.modifyepworth(ep);
												UpdateEssActivity.this.finish();
												overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
												Log.d("TAG", "嗜睡修改成功!" + status);
												mHandler.sendEmptyMessage(0);
											} else {
												Log.d("TAG", "嗜睡修改失败!" + status);
												mHandler.sendEmptyMessage(1);
											}
											// finally {
											// Looper.loop();
											// }
											mHandler.sendEmptyMessage(3);
										}
									} else {
										ESSAndDisEaseHistoryDto ed = WebHelper.userESS(um.getUuid(), ep, dh);
										// Looper.prepare();
										Thread.sleep(3000);
										Status status = new WebAPI().uploadEssAndDiseaseHistory(ed);
										if (Status.isSuccess(status)) {
											sd.modifyepworth(ep);
											UpdateEssActivity.this.finish();
											overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
											Log.d("TAG", "嗜睡修改成功!" + status);
											mHandler.sendEmptyMessage(0);
										} else {
											Log.d("TAG", "嗜睡修改失败!" + status);
											mHandler.sendEmptyMessage(1);
										}
										// finally {
										// Looper.loop();
										// }
										mHandler.sendEmptyMessage(3);
									}
								} catch (Exception e) {
									e.printStackTrace();
									mHandler.sendEmptyMessage(3);
								}
							}
						}.start();
					} else {
						mHandler.sendEmptyMessage(3);
						Toast.makeText(UpdateEssActivity.this,
								UpdateEssActivity.this.getString(R.string.modify_failed_please_open_the_network),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(UpdateEssActivity.this,
						UpdateEssActivity.this.getString(R.string.sleep_modification_success), Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				Toast.makeText(UpdateEssActivity.this,
						UpdateEssActivity.this.getString(R.string.sleep_modification_failure), Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				Log.d("TAG", "mHandler, case 2:");
				if (sdialog == null) {
					sdialog = new SubmitDialog(UpdateEssActivity.this);
				}
				sdialog.setContent(
						UpdateEssActivity.this.getString(R.string.modifying_sleepiness_information_please_wait));
				sdialog.show();
				break;
			case 3:
				Log.d("TAG", "mHandler, case 3:");
				if (sdialog != null && sdialog.isShowing()) {
					sdialog.cancel();
				}
				break;
			default:
				break;
			}
		}
	};
}
