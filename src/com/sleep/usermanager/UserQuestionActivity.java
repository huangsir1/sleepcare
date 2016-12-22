package com.sleep.usermanager;

import com.taiir.sleepcare.home.R;

import java.text.SimpleDateFormat;

import com.loopj.android.application.MyApplication;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class UserQuestionActivity extends Activity {
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

	private PublicDao sdao;
	private UserManager se;

	private Epworth ep;
	private DiabetesHy dh;

	private Button mSubmit;
	private UserNumber un;
	private String tel;
	private String email;

	// 自定义标记初始状态
	// private int Question1_1=0;
	// private int Question1_2=1;
	private int Question1 = 0;
	private int Question2 = 0;
	private int Question3 = 0;
	private int Question4 = 0;
	private int Question5 = 0;
	private int Question6 = 0;
	private int Question7 = 0;
	private int Question8 = 0;

	private ImageView backiv;
	public static UserQuestionActivity instance = null;
	private Intent iclose;
	private int int1close;
	private int int2close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_questionnaire);
		instance = this;
		iclose = getIntent();
		int1close = iclose.getIntExtra("umfhclose", -1);
		int2close = iclose.getIntExtra("ummhclose", -1);
		ExitActivity.getInstance().addActivity(this);

		sdao = new PublicDaoImpl(UserQuestionActivity.this);
		un = sdao.findTelorEmail();
		if (un != null) {
			tel = un.getUsertel();
			email = un.getEmails();
		}

		Intent intent = getIntent();
		backiv = (ImageView) findViewById(R.id.imageView1_one);
		backiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);

			}
		});
		/**
		 * 用户信息
		 */
		final String name = intent.getStringExtra("name");
		final String sex = intent.getStringExtra("sex");
		final String age = intent.getStringExtra("age");
		final String weight = intent.getStringExtra("weight");
		final String height = intent.getStringExtra("height");

		/**
		 * 病史
		 */
		final String losesleep = intent.getStringExtra("losesleep");
		final String diabetes = intent.getStringExtra("diabetes");
		final String hypertension = intent.getStringExtra("hypertension");
		final String coronaryheartdisease = intent.getStringExtra("coronaryheartdisease");
		final String heartfailure = intent.getStringExtra("heartfailure");
		final String arrhythmia = intent.getStringExtra("arrhythmia");
		final String congestion = intent.getStringExtra("congestion");
		final String longsmoking = intent.getStringExtra("longsmoking");
		final String cerebrovasculardisease = intent.getStringExtra("cerebrovasculardisease");
		final String renalfailure = intent.getStringExtra("renalfailure");
		final String takesedatives = intent.getStringExtra("takesedatives");
		final String longdrinking = intent.getStringExtra("longdrinking");
		final String whetherjjm = intent.getStringExtra("whetherjjm");
		final String whetherfmhy = intent.getStringExtra("whetherfmhy");
		final String whetherxyccd = intent.getStringExtra("whetherxyccd");

		b1_1 = (CheckBox) findViewById(R.id.checkBox1_1_zzyd);
		b1_2 = (CheckBox) findViewById(R.id.checkBox1_2_zzyd);
		b1_3 = (CheckBox) findViewById(R.id.checkBox1_3_zzyd);
		b1_4 = (CheckBox) findViewById(R.id.checkBox1_4_zzyd);
		t1 = (TextView) findViewById(R.id.textView6_zzyd);
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
		b2_1 = (CheckBox) findViewById(R.id.checkBox2_1_kdss);
		b2_2 = (CheckBox) findViewById(R.id.checkBox2_2_kdss);
		b2_3 = (CheckBox) findViewById(R.id.checkBox2_3_kdss);
		b2_4 = (CheckBox) findViewById(R.id.checkBox2_4_kdss);
		t2 = (TextView) findViewById(R.id.textView6_2_kdss);
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
		b3_1 = (CheckBox) findViewById(R.id.checkBox3_1_ggchbd);
		b3_2 = (CheckBox) findViewById(R.id.checkBox3_2_ggchbd);
		b3_3 = (CheckBox) findViewById(R.id.checkBox3_3_ggchbd);
		b3_4 = (CheckBox) findViewById(R.id.checkBox3_4_ggchbd);
		t3 = (TextView) findViewById(R.id.textView6_3_ggchbd);
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
		b4_1 = (CheckBox) findViewById(R.id.checkBox4_1_csjzcbxx);
		b4_2 = (CheckBox) findViewById(R.id.checkBox4_2_csjzcbxx);
		b4_3 = (CheckBox) findViewById(R.id.checkBox4_3_csjzcbxx);
		b4_4 = (CheckBox) findViewById(R.id.checkBox4_4_csjzcbxx);
		t4 = (TextView) findViewById(R.id.textView6_4_csjzcbxx);
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
		b5_1 = (CheckBox) findViewById(R.id.checkBox5_1_zzyrth);
		b5_2 = (CheckBox) findViewById(R.id.checkBox5_2_zzyrth);
		b5_3 = (CheckBox) findViewById(R.id.checkBox5_3_zzyrth);
		b5_4 = (CheckBox) findViewById(R.id.checkBox5_4_zzyrth);
		t5 = (TextView) findViewById(R.id.textView6_5_zzyrth);
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
		b6_1 = (CheckBox) findViewById(R.id.checkBox6_1_fhxxs);
		b6_2 = (CheckBox) findViewById(R.id.checkBox6_2_fhxxs);
		b6_3 = (CheckBox) findViewById(R.id.checkBox6_3_fhxxs);
		b6_4 = (CheckBox) findViewById(R.id.checkBox6_4_fhxxs);
		t6 = (TextView) findViewById(R.id.textView6_6_fhxxs);
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
		b7_1 = (CheckBox) findViewById(R.id.checkBox7_1_kcdhlds);
		b7_2 = (CheckBox) findViewById(R.id.checkBox7_2_kcdhlds);
		b7_3 = (CheckBox) findViewById(R.id.checkBox7_3_kcdhlds);
		b7_4 = (CheckBox) findViewById(R.id.checkBox7_4_kcdhlds);
		t7 = (TextView) findViewById(R.id.textView6_7_kcdhlds);
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
		b8_1 = (CheckBox) findViewById(R.id.checkBox8_1_xwjwxxs);
		b8_2 = (CheckBox) findViewById(R.id.checkBox8_2_xwjwxxs);
		b8_3 = (CheckBox) findViewById(R.id.checkBox8_3_xwjwxxs);
		b8_4 = (CheckBox) findViewById(R.id.checkBox8_4_xwjwxxs);
		t8 = (TextView) findViewById(R.id.textView6_8_xwjwxxs);
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
		mSubmit = (Button) findViewById(R.id.sure_submit2);
		mSubmit.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				sdao = new PublicDaoImpl(UserQuestionActivity.this);
				se = new UserManager();
				ep = new Epworth();
				dh = new DiabetesHy();
				/**
				 * 增加用户
				 */
				if (tel != null && !"".equals(tel)) {
					se.setMobilenumber(tel);
				} else {
					se.setMobilenumber("");
				}

				if (email != null && !"".equals(email)) {
					se.setUseremail(email);
				} else {
					se.setUseremail("");
				}

				se.setUsername(name);
				se.setUsersex(sex);
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy" + MyApplication.getContext().getString(R.string.years) + "MM"
								+ MyApplication.getContext().getString(R.string.months) + "dd"
								+ MyApplication.getContext().getString(R.string.days));
				String str;
				try {
					str = sdf1.format(sdf.parse(age));
					se.setUserage(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
				se.setUserweight(Integer.parseInt(weight));
				se.setUserheight(Integer.parseInt(height));
				se.setUpload(0);

				int USER_BMI = 0;
				int wt = Integer.parseInt(weight);
				float ht = Integer.parseInt(height) / 100f;
				if (ht != 0) {
					USER_BMI = (int) (wt / (ht * ht));
				} else {
					USER_BMI = 0;
				}
				se.setUserbmi(USER_BMI);
				/**
				 * 增加病史
				 */
				// 糖尿病
				dh.setLosesleep(losesleep);
				// 糖尿病
				dh.setDiabetes(diabetes);
				// 高血压
				dh.setHypertension(hypertension);
				// 冠心病
				dh.setCoronaryheartdisease(coronaryheartdisease);
				// 心力衰竭
				dh.setHeartfailure(heartfailure);
				// 心律失常
				dh.setArrhythmia(arrhythmia);
				// 鼻腔阻塞
				dh.setCongestion(congestion);
				// 长期吸烟
				dh.setLongsmoking(longsmoking);
				// 脑血管疾病
				dh.setCerebrovasculardisease(cerebrovasculardisease);
				// 肾功能损害
				dh.setRenalfailure(renalfailure);
				// 服用镇静剂
				dh.setTakesedatives(takesedatives);
				// 长期大量饮酒
				dh.setLongdrinking(longdrinking);
				// 是否绝经
				dh.setWhetherjjm(whetherjjm);
				// 是否有OSAHS的家族史
				dh.setWhetherfmhy(whetherfmhy);
				// 是否悬雍垂粗大
				dh.setWhetherxyccd(whetherxyccd);
				dh.setUpload(0);
				/**
				 * 调查问卷
				 */
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
					Toast.makeText(UserQuestionActivity.this,
							UserQuestionActivity.this.getString(R.string.the_first_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q2.length() == 0) {
					Toast.makeText(UserQuestionActivity.this,
							UserQuestionActivity.this.getString(R.string.the_second_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q3.length() == 0) {
					Toast.makeText(UserQuestionActivity.this,
							UserQuestionActivity.this.getString(R.string.the_third_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q4.length() == 0) {
					Toast.makeText(UserQuestionActivity.this,
							UserQuestionActivity.this.getString(R.string.the_fourth_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q5.length() == 0) {
					Toast.makeText(UserQuestionActivity.this,
							UserQuestionActivity.this.getString(R.string.the_fifth_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q6.length() == 0) {
					Toast.makeText(UserQuestionActivity.this,
							UserQuestionActivity.this.getString(R.string.the_sixth_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q7.length() == 0) {
					Toast.makeText(UserQuestionActivity.this,
							UserQuestionActivity.this.getString(R.string.the_seventh_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q8.length() == 0) {
					Toast.makeText(UserQuestionActivity.this,
							UserQuestionActivity.this.getString(R.string.the_eighth_question_is_not_selected),
							Toast.LENGTH_SHORT).show();
				} else if (q1 != null && !"".endsWith(q1) && q2 != null && !"".endsWith(q2) && q3 != null
						&& !"".endsWith(q3) && q4 != null && !"".endsWith(q4) && q5 != null && !"".endsWith(q5)
						&& q6 != null && !"".endsWith(q6) && q7 != null && !"".endsWith(q7) && q8 != null
						&& !"".endsWith(q8)) {

					int sum_question = Question1 + Question2 + Question3 + Question4 + Question5 + Question6 + Question7
							+ Question8;
					ep.setSumscore(sum_question);
					ep.setUpload(0);

					sdao.addUser(se);
					sdao.addEpworth(ep);
					sdao.addDiabetesHy(dh);
					Intent intent = new Intent(UserQuestionActivity.this, UserChangeActivity.class);
					startActivity(intent);
					UserQuestionActivity.this.finish();
					if (1 == int1close) {
						UserMediaclFHistory.instance.finish();
					} else if (2 == int2close) {
						UserMediaclMHistory.instance.finish();
					}
					UserRootActivity.instance.finish();
					// overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
