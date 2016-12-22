package com.sleep.usermanager;

import com.taiir.sleepcare.home.R;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserMediaclFHistory extends Activity {
	private TextView t1_1, t1_2;
	private TextView t2_1, t2_2;
	private TextView t3_1, t3_2;
	private TextView t4_1, t4_2;
	private TextView t5_1, t5_2;
	private TextView t6_1, t6_2;
	private TextView t7_1, t7_2;

	private int status1 = 0;// 标记状态初始化
	private int status2 = 0;
	private int status3 = 0;
	private int status4 = 0;
	private int status5 = 0;
	private int status6 = 0;
	private int status7 = 0;
	private int status8 = 0;
	private int status9 = 0;
	private int status10 = 0;
	private int status11 = 0;
	private int status12 = 0;
	private int status13 = 0;
	private int status14 = 0;
	
	private Button mSubmit;
	private ImageView backiv;
	public static UserMediaclFHistory instance = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_medical_f_history);
		instance=this;
		ExitActivity.getInstance().addActivity(this);
		Intent intent=getIntent();
		backiv=(ImageView) findViewById(R.id.imageView1_bsxz);
		backiv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserMediaclFHistory.this.finish();
				overridePendingTransition(R.anim.right_left_out,R.anim.right_left_in);
				
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
		
		t1_1 = (TextView) findViewById(R.id.s_m_f);		
		t1_1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				switch (status1) {
				case 0:
					t1_1.setTextColor(getResources().getColor(R.color.white));
					t1_1.setBackgroundResource(R.drawable.sregister0003);
					status1 = 1;
					break;
				case 1:
					t1_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t1_1.setBackgroundResource(R.drawable.sregister0002);
					status1 = 0;
					break;
				default:
					break;
				}
			}
		});
		t1_2 = (TextView) findViewById(R.id.t_n_b_f);
		t1_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status2) {
				case 0:
					t1_2.setTextColor(getResources().getColor(R.color.white));
					t1_2.setBackgroundResource(R.drawable.sregister0003);
					status2 = 1;
					break;
				case 1:
					t1_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t1_2.setBackgroundResource(R.drawable.sregister0002);
					status2 = 0;
					break;
				default:
					break;
				}
			}
		});

		t2_1 = (TextView) findViewById(R.id.g_x_y_f);
		t2_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status3) {
				case 0:
					t2_1.setTextColor(getResources().getColor(R.color.white));
					t2_1.setBackgroundResource(R.drawable.sregister0003);
					status3 = 1;
					break;
				case 1:
					t2_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t2_1.setBackgroundResource(R.drawable.sregister0002);
					status3 = 0;
					break;
				default:
					break;
				}
			}
		});
		t2_2 = (TextView) findViewById(R.id.g_x_b_f);
		t2_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status4) {
				case 0:
					t2_2.setTextColor(getResources().getColor(R.color.white));
					t2_2.setBackgroundResource(R.drawable.sregister0003);
					status4 = 1;
					break;
				case 1:
					t2_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t2_2.setBackgroundResource(R.drawable.sregister0002);
					status4 = 0;
					break;
				default:
					break;
				}
			}
		});
		t3_1 = (TextView) findViewById(R.id.x_l_s_j_f);
		t3_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status5) {
				case 0:
					t3_1.setTextColor(getResources().getColor(R.color.white));
					t3_1.setBackgroundResource(R.drawable.sregister0003);
					status5 = 1;
					break;
				case 1:
					t3_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t3_1.setBackgroundResource(R.drawable.sregister0002);
					status5 = 0;
					break;
				default:
					break;
				}
			}
		});
		t3_2 = (TextView) findViewById(R.id.x_l_s_c_f);
		t3_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status6) {
				case 0:
					t3_2.setTextColor(getResources().getColor(R.color.white));
					t3_2.setBackgroundResource(R.drawable.sregister0003);
					status6 = 1;
					break;
				case 1:
					t3_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t3_2.setBackgroundResource(R.drawable.sregister0002);
					status6 = 0;
					break;
				default:
					break;
				}
			}
		});
		t4_1 = (TextView) findViewById(R.id.b_q_z_s_f);
		t4_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status7) {
				case 0:
					t4_1.setTextColor(getResources().getColor(R.color.white));
					t4_1.setBackgroundResource(R.drawable.sregister0003);
					status7 = 1;
					break;
				case 1:
					t4_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t4_1.setBackgroundResource(R.drawable.sregister0002);
					status7 = 0;
					break;
				default:
					break;
				}
			}
		});
		t4_2 = (TextView) findViewById(R.id.c_q_x_y_f);
		t4_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status8) {
				case 0:
					t4_2.setTextColor(getResources().getColor(R.color.white));
					t4_2.setBackgroundResource(R.drawable.sregister0003);
					status8 = 1;
					break;
				case 1:
					t4_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t4_2.setBackgroundResource(R.drawable.sregister0002);
					status8 = 0;
					break;
				default:
					break;
				}
			}
		});
		t5_1 = (TextView) findViewById(R.id.n_x_g_j_b_f);
		t5_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status9) {
				case 0:
					t5_1.setTextColor(getResources().getColor(R.color.white));
					t5_1.setBackgroundResource(R.drawable.sregister0003);
					status9 = 1;
					break;
				case 1:
					t5_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t5_1.setBackgroundResource(R.drawable.sregister0002);
					status9 = 0;
					break;
				default:
					break;
				}
			}
		});
		t5_2 = (TextView) findViewById(R.id.s_g_n_s_h_f);
		t5_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status10) {
				case 0:
					t5_2.setTextColor(getResources().getColor(R.color.white));
					t5_2.setBackgroundResource(R.drawable.sregister0003);
					status10 = 1;
					break;
				case 1:
					t5_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t5_2.setBackgroundResource(R.drawable.sregister0002);
					status10 = 0;
					break;
				default:
					break;
				}
			}
		});
		t6_1 = (TextView) findViewById(R.id.z_j_j_y_w_f);
		t6_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status11) {
				case 0:
					t6_1.setTextColor(getResources().getColor(R.color.white));
					t6_1.setBackgroundResource(R.drawable.sregister0003);
					status11 = 1;
					break;
				case 1:
					t6_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t6_1.setBackgroundResource(R.drawable.sregister0002);
					status11 = 0;
					break;
				default:
					break;
				}
			}
		});
		t6_2 = (TextView) findViewById(R.id.c_q_d_l_y_j_f);
		t6_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status12) {
				case 0:
					t6_2.setTextColor(getResources().getColor(R.color.white));
					t6_2.setBackgroundResource(R.drawable.sregister0003);
					status12 = 1;
					break;
				case 1:
					t6_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t6_2.setBackgroundResource(R.drawable.sregister0002);
					status12 = 0;
					break;
				default:
					break;
				}
			}
		});
		t7_1= (TextView) findViewById(R.id.x_y_c_c_d_f);
		t7_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status13) {
				case 0:
					t7_1.setTextColor(getResources().getColor(R.color.white));
					t7_1.setBackgroundResource(R.drawable.sregister0003);
					status13 = 1;
					break;
				case 1:
					t7_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t7_1.setBackgroundResource(R.drawable.sregister0002);
					status13 = 0;
					break;
				default:
					break;
				}
			}
		});
		t7_2= (TextView) findViewById(R.id.o_s_s_j_z_s_f);
		t7_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status14) {
				case 0:
					t7_2.setTextColor(getResources().getColor(R.color.white));
					t7_2.setBackgroundResource(R.drawable.sregister0003);
					status14 = 1;
					break;
				case 1:
					t7_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
					t7_2.setBackgroundResource(R.drawable.sregister0002);
					status14 = 0;
					break;
				default:
					break;
				}
			}
		});
		mSubmit = (Button) findViewById(R.id.sure_submit1_f);
		mSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				String losesleep=t1_1.getText().toString(); 
				String diabetes=t1_2.getText().toString();				
				String hypertension=t2_1.getText().toString();
				String coronaryheartdisease=t2_2.getText().toString();
				String heartfailure=t3_1.getText().toString();
				String arrhythmia=t3_2.getText().toString();
				String congestion=t4_1.getText().toString();
				String longsmoking=t4_2.getText().toString();
				String cerebrovasculardisease=t5_1.getText().toString();
				String renalfailure=t5_2.getText().toString();
				String takesedatives=t6_1.getText().toString();
				String longdrinking=t6_2.getText().toString();
				String whetherxyccd=t7_1.getText().toString();
				String whetherfmhy=t7_2.getText().toString();
				
				Intent intent = new Intent();
				//失眠
				if (status1 == 1) {
					intent.putExtra("losesleep", losesleep);
				}else if(status1 == 0){
					intent.putExtra(null, losesleep);
				}
				//糖尿病
				if (status2 == 1) {
					intent.putExtra("diabetes", diabetes);
				}else if(status2 == 0){
					intent.putExtra(null, diabetes);
				}
				//高血压
				if (status3 == 1) {
					intent.putExtra("hypertension", hypertension);
				}else if(status3 == 0){
					intent.putExtra(null, hypertension);
				}
				//冠心病
				if (status4 == 1) {
					intent.putExtra("coronaryheartdisease", coronaryheartdisease);
				}else if(status4 == 0){
					intent.putExtra(null, coronaryheartdisease);
				}
				//心力衰竭
				if (status5 == 1) {
					intent.putExtra("heartfailure", heartfailure);
				}else if(status5 == 0){
					intent.putExtra(null, heartfailure);
				}
				//心律失常
				if (status6 == 1) {
					intent.putExtra("arrhythmia", arrhythmia);
				}else if(status6 == 0){
					intent.putExtra(null, arrhythmia);
				}
				//鼻腔阻塞
				if (status7 == 1) {
					intent.putExtra("congestion", congestion);
				}else if(status7 == 0){
					intent.putExtra(null, congestion);
				}
				//长期吸烟
				if (status8 == 1) {
					intent.putExtra("longsmoking", longsmoking);
				}else if(status8 == 0){
					intent.putExtra(null, longsmoking);
				}
				//脑血管疾病
				if (status9 == 1) {
					intent.putExtra("cerebrovasculardisease", cerebrovasculardisease);
				}else if(status9 == 0){
					intent.putExtra(null, cerebrovasculardisease);				}
				//肾功能损害
				if (status10 == 1) {
					intent.putExtra("renalfailure", renalfailure);
				}else if(status10 == 0){
					intent.putExtra(null, renalfailure);
				}
				//服用镇静剂
				if (status11 == 1) {
					intent.putExtra("takesedatives", takesedatives);
				}else if(status11 == 0){
					intent.putExtra(null, takesedatives);
				}
				//长期大量饮酒
				if (status12 == 1) {
					intent.putExtra("longdrinking", longdrinking);
				}else if(status12 == 0){
					intent.putExtra(null, longdrinking);
				}
				// 是否悬雍垂粗大
				if (status13 == 1) {
					intent.putExtra("whetherxyccd", whetherxyccd);
				}else if(status13 == 0){
					intent.putExtra(null, whetherxyccd);
				}
				// 是否OSAHS的家族史
				if (status14 == 1) {
					intent.putExtra("whetherfmhy", whetherfmhy);
				}else if(status14 == 0){
					intent.putExtra(null, whetherfmhy);
				}
				intent.putExtra("name",name);
				intent.putExtra("sex",sex);
				intent.putExtra("age",age);
				intent.putExtra("weight",weight);
				intent.putExtra("height",height);
				
				intent.setClass(UserMediaclFHistory.this, UserQuestionActivity.class);
				intent.putExtra("umfhclose", 1);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.right_left_out,R.anim.right_left_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
