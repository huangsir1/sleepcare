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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.ESSAndDisEaseHistoryDto;
import taiyi.web.model.dto.Status;

public class UpdateBsFActivity extends Activity {
	private ImageView imageViewback;

	private TextView tbs1_1, tbs1_2;
	private TextView tbs2_1, tbs2_2;
	private TextView tbs3_1, tbs3_2;
	private TextView tbs4_1, tbs4_2;
	private TextView tbs5_1, tbs5_2;
	private TextView tbs6_1, tbs6_2;
	private TextView tbs7_1, tbs7_2;

	private int stbs1_1_1 = 0;// 标记状态初始化
	private int stbs1_1_2 = 0;
	private int stbs1_2_1 = 0;
	private int stbs1_2_2 = 0;

	private int stbs2_1_1 = 0;
	private int stbs2_1_2 = 0;
	private int stbs2_2_1 = 0;
	private int stbs2_2_2 = 0;

	private int stbs3_1_1 = 0;
	private int stbs3_1_2 = 0;
	private int stbs3_2_1 = 0;
	private int stbs3_2_2 = 0;

	private int stbs4_1_1 = 0;
	private int stbs4_1_2 = 0;
	private int stbs4_2_1 = 0;
	private int stbs4_2_2 = 0;

	private int stbs5_1_1 = 0;
	private int stbs5_1_2 = 0;
	private int stbs5_2_1 = 0;
	private int stbs5_2_2 = 0;

	private int stbs6_1_1 = 0;
	private int stbs6_1_2 = 0;
	private int stbs6_2_1 = 0;
	private int stbs6_2_2 = 0;

	private int stbs7_1_1 = 0;
	private int stbs7_1_2 = 0;
	private int stbs7_2_1 = 0;
	private int stbs7_2_2 = 0;

	private Button mtbsSubmit;
	private SubmitDialog sdialog;

	private PublicDao sd;
	private DiabetesHy dh;
	private int id;
	private UserManager um;
	private Epworth ep;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_f_user_medical_history);
		ExitActivity.getInstance().addActivity(this);
		Intent intent = getIntent();
		sd = new PublicDaoImpl(UpdateBsFActivity.this);
		id = intent.getIntExtra("sexfid", -1);
		dh = sd.findAllDiabetesHyById(id);
		um = sd.findAllUserById(id);
		ep = sd.findAllEpworthById(id);
		sdialog = new SubmitDialog(UpdateBsFActivity.this);
		imageViewback = (ImageView) findViewById(R.id.imageView1_bs);
		// imageViewback.setAlpha(0);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UpdateBsFActivity.this.finish();
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			}
		});
		// 失眠
		tbs1_1 = (TextView) findViewById(R.id.s_m_upbs);
		if (dh.getLosesleep() != null) {
			tbs1_1.setTextColor(getResources().getColor(R.color.white));
			tbs1_1.setBackgroundResource(R.drawable.sregister0003);
			tbs1_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs1_1_1) {
					case 0:
						tbs1_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs1_1.setBackgroundResource(R.drawable.sregister0002);
						stbs1_1_1 = 1;
						break;
					case 1:
						tbs1_1.setTextColor(getResources().getColor(R.color.white));
						tbs1_1.setBackgroundResource(R.drawable.sregister0003);
						stbs1_1_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs1_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs1_1.setBackgroundResource(R.drawable.sregister0002);
			tbs1_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs1_1_2) {
					case 0:
						tbs1_1.setTextColor(getResources().getColor(R.color.white));
						tbs1_1.setBackgroundResource(R.drawable.sregister0003);
						stbs1_1_2 = 1;
						break;
					case 1:
						tbs1_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs1_1.setBackgroundResource(R.drawable.sregister0002);
						stbs1_1_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 糖尿病
		tbs1_2 = (TextView) findViewById(R.id.t_n_b_upbs);
		if (dh.getDiabetes() != null) {
			tbs1_2.setTextColor(getResources().getColor(R.color.white));
			tbs1_2.setBackgroundResource(R.drawable.sregister0003);
			tbs1_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs1_2_1) {
					case 0:
						tbs1_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs1_2.setBackgroundResource(R.drawable.sregister0002);
						stbs1_2_1 = 1;
						break;
					case 1:
						tbs1_2.setTextColor(getResources().getColor(R.color.white));
						tbs1_2.setBackgroundResource(R.drawable.sregister0003);
						stbs1_2_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs1_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs1_2.setBackgroundResource(R.drawable.sregister0002);
			tbs1_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs1_2_2) {
					case 0:
						tbs1_2.setTextColor(getResources().getColor(R.color.white));
						tbs1_2.setBackgroundResource(R.drawable.sregister0003);
						stbs1_2_2 = 1;
						break;
					case 1:
						tbs1_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs1_2.setBackgroundResource(R.drawable.sregister0002);
						stbs1_2_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 高血压
		tbs2_1 = (TextView) findViewById(R.id.g_x_y_upbs);
		if (dh.getHypertension() != null) {
			tbs2_1.setTextColor(getResources().getColor(R.color.white));
			tbs2_1.setBackgroundResource(R.drawable.sregister0003);
			tbs2_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs2_1_1) {
					case 0:
						tbs2_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs2_1.setBackgroundResource(R.drawable.sregister0002);
						stbs2_1_1 = 1;
						break;
					case 1:
						tbs2_1.setTextColor(getResources().getColor(R.color.white));
						tbs2_1.setBackgroundResource(R.drawable.sregister0003);
						stbs2_1_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs2_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs2_1.setBackgroundResource(R.drawable.sregister0002);
			tbs2_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs2_1_2) {
					case 0:
						tbs2_1.setTextColor(getResources().getColor(R.color.white));
						tbs2_1.setBackgroundResource(R.drawable.sregister0003);
						stbs2_1_2 = 1;
						break;
					case 1:
						tbs2_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs2_1.setBackgroundResource(R.drawable.sregister0002);
						stbs2_1_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 冠心病
		tbs2_2 = (TextView) findViewById(R.id.g_x_b_upbs);
		if (dh.getCoronaryheartdisease() != null) {
			tbs2_2.setTextColor(getResources().getColor(R.color.white));
			tbs2_2.setBackgroundResource(R.drawable.sregister0003);
			tbs2_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs2_2_1) {
					case 0:
						tbs2_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs2_2.setBackgroundResource(R.drawable.sregister0002);
						stbs2_2_1 = 1;
						break;
					case 1:
						tbs2_2.setTextColor(getResources().getColor(R.color.white));
						tbs2_2.setBackgroundResource(R.drawable.sregister0003);
						stbs2_2_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs2_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs2_2.setBackgroundResource(R.drawable.sregister0002);
			tbs2_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs2_2_2) {
					case 0:
						tbs2_2.setTextColor(getResources().getColor(R.color.white));
						tbs2_2.setBackgroundResource(R.drawable.sregister0003);
						stbs2_2_2 = 1;
						break;
					case 1:
						tbs2_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs2_2.setBackgroundResource(R.drawable.sregister0002);
						stbs2_2_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 心力衰竭
		tbs3_1 = (TextView) findViewById(R.id.x_l_s_j_upbs);
		if (dh.getHeartfailure() != null) {
			tbs3_1.setTextColor(getResources().getColor(R.color.white));
			tbs3_1.setBackgroundResource(R.drawable.sregister0003);
			tbs3_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs3_1_1) {
					case 0:
						tbs3_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs3_1.setBackgroundResource(R.drawable.sregister0002);
						stbs3_1_1 = 1;
						break;
					case 1:
						tbs3_1.setTextColor(getResources().getColor(R.color.white));
						tbs3_1.setBackgroundResource(R.drawable.sregister0003);
						stbs3_1_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs3_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs3_1.setBackgroundResource(R.drawable.sregister0002);
			tbs3_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs3_1_2) {
					case 0:
						tbs3_1.setTextColor(getResources().getColor(R.color.white));
						tbs3_1.setBackgroundResource(R.drawable.sregister0003);
						stbs3_1_2 = 1;
						break;
					case 1:
						tbs3_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs3_1.setBackgroundResource(R.drawable.sregister0002);
						stbs3_1_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 心律失常
		tbs3_2 = (TextView) findViewById(R.id.x_l_s_c_upbs);
		if (dh.getArrhythmia() != null) {
			tbs3_2.setTextColor(getResources().getColor(R.color.white));
			tbs3_2.setBackgroundResource(R.drawable.sregister0003);
			tbs3_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs3_2_1) {
					case 0:
						tbs3_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs3_2.setBackgroundResource(R.drawable.sregister0002);
						stbs3_2_1 = 1;
						break;
					case 1:
						tbs3_2.setTextColor(getResources().getColor(R.color.white));
						tbs3_2.setBackgroundResource(R.drawable.sregister0003);
						stbs3_2_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs3_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs3_2.setBackgroundResource(R.drawable.sregister0002);
			tbs3_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs3_2_2) {
					case 0:
						tbs3_2.setTextColor(getResources().getColor(R.color.white));
						tbs3_2.setBackgroundResource(R.drawable.sregister0003);
						stbs3_2_2 = 1;
						break;
					case 1:
						tbs3_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs3_2.setBackgroundResource(R.drawable.sregister0002);
						stbs3_2_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 鼻腔阻塞
		tbs4_1 = (TextView) findViewById(R.id.b_q_z_s_upbs);
		if (dh.getCongestion() != null) {
			tbs4_1.setTextColor(getResources().getColor(R.color.white));
			tbs4_1.setBackgroundResource(R.drawable.sregister0003);
			tbs4_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs4_1_1) {
					case 0:
						tbs4_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs4_1.setBackgroundResource(R.drawable.sregister0002);
						stbs4_1_1 = 1;
						break;
					case 1:
						tbs4_1.setTextColor(getResources().getColor(R.color.white));
						tbs4_1.setBackgroundResource(R.drawable.sregister0003);
						stbs4_1_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs4_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs4_1.setBackgroundResource(R.drawable.sregister0002);
			tbs4_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs4_1_2) {
					case 0:
						tbs4_1.setTextColor(getResources().getColor(R.color.white));
						tbs4_1.setBackgroundResource(R.drawable.sregister0003);
						stbs4_1_2 = 1;
						break;
					case 1:
						tbs4_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs4_1.setBackgroundResource(R.drawable.sregister0002);
						stbs4_1_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 长期吸烟
		tbs4_2 = (TextView) findViewById(R.id.c_q_x_y_upbs);
		if (dh.getLongsmoking() != null) {
			tbs4_2.setTextColor(getResources().getColor(R.color.white));
			tbs4_2.setBackgroundResource(R.drawable.sregister0003);
			tbs4_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs4_2_1) {
					case 0:
						tbs4_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs4_2.setBackgroundResource(R.drawable.sregister0002);
						stbs4_2_1 = 1;
						break;
					case 1:
						tbs4_2.setTextColor(getResources().getColor(R.color.white));
						tbs4_2.setBackgroundResource(R.drawable.sregister0003);
						stbs4_2_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs4_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs4_2.setBackgroundResource(R.drawable.sregister0002);
			tbs4_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs4_2_2) {
					case 0:
						tbs4_2.setTextColor(getResources().getColor(R.color.white));
						tbs4_2.setBackgroundResource(R.drawable.sregister0003);
						stbs4_2_2 = 1;
						break;
					case 1:
						tbs4_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs4_2.setBackgroundResource(R.drawable.sregister0002);
						stbs4_2_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 悬雍垂粗大
		tbs5_1 = (TextView) findViewById(R.id.x_y_c_c_d_upbs);
		if (dh.getWhetherxyccd() != null) {
			tbs5_1.setTextColor(getResources().getColor(R.color.white));
			tbs5_1.setBackgroundResource(R.drawable.sregister0003);
			tbs5_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs5_1_1) {
					case 0:
						tbs5_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs5_1.setBackgroundResource(R.drawable.sregister0002);
						stbs5_1_1 = 1;
						break;
					case 1:
						tbs5_1.setTextColor(getResources().getColor(R.color.white));
						tbs5_1.setBackgroundResource(R.drawable.sregister0003);
						stbs5_1_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs5_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs5_1.setBackgroundResource(R.drawable.sregister0002);
			tbs5_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs5_1_2) {
					case 0:
						tbs5_1.setTextColor(getResources().getColor(R.color.white));
						tbs5_1.setBackgroundResource(R.drawable.sregister0003);
						stbs5_1_2 = 1;
						break;
					case 1:
						tbs5_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs5_1.setBackgroundResource(R.drawable.sregister0002);
						stbs5_1_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// OSAHS的家族史
		tbs5_2 = (TextView) findViewById(R.id.o_s_s_j_z_s_upbs);
		if (dh.getWhetherfmhy() != null) {
			tbs5_2.setTextColor(getResources().getColor(R.color.white));
			tbs5_2.setBackgroundResource(R.drawable.sregister0003);
			tbs5_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs5_2_1) {
					case 0:
						tbs5_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs5_2.setBackgroundResource(R.drawable.sregister0002);
						stbs5_2_1 = 1;
						break;
					case 1:
						tbs5_2.setTextColor(getResources().getColor(R.color.white));
						tbs5_2.setBackgroundResource(R.drawable.sregister0003);
						stbs5_2_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs5_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs5_2.setBackgroundResource(R.drawable.sregister0002);
			tbs5_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs5_2_2) {
					case 0:
						tbs5_2.setTextColor(getResources().getColor(R.color.white));
						tbs5_2.setBackgroundResource(R.drawable.sregister0003);
						stbs5_2_2 = 1;
						break;
					case 1:
						tbs5_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs5_2.setBackgroundResource(R.drawable.sregister0002);
						stbs5_2_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 脑血管疾病
		tbs6_1 = (TextView) findViewById(R.id.n_x_g_j_b_upbs);
		if (dh.getCerebrovasculardisease() != null) {
			tbs6_1.setTextColor(getResources().getColor(R.color.white));
			tbs6_1.setBackgroundResource(R.drawable.sregister0003);
			tbs6_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs6_1_1) {
					case 0:
						tbs6_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs6_1.setBackgroundResource(R.drawable.sregister0002);
						stbs6_1_1 = 1;
						break;
					case 1:
						tbs6_1.setTextColor(getResources().getColor(R.color.white));
						tbs6_1.setBackgroundResource(R.drawable.sregister0003);
						stbs6_1_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs6_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs6_1.setBackgroundResource(R.drawable.sregister0002);
			tbs6_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs6_1_2) {
					case 0:
						tbs6_1.setTextColor(getResources().getColor(R.color.white));
						tbs6_1.setBackgroundResource(R.drawable.sregister0003);
						stbs6_1_2 = 1;
						break;
					case 1:
						tbs6_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs6_1.setBackgroundResource(R.drawable.sregister0002);
						stbs6_1_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 肾功能损害
		tbs6_2 = (TextView) findViewById(R.id.s_g_n_s_h_upbs);
		if (dh.getRenalfailure() != null) {
			tbs6_2.setTextColor(getResources().getColor(R.color.white));
			tbs6_2.setBackgroundResource(R.drawable.sregister0003);
			tbs6_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs6_2_1) {
					case 0:
						tbs6_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs6_2.setBackgroundResource(R.drawable.sregister0002);
						stbs6_2_1 = 1;
						break;
					case 1:
						tbs6_2.setTextColor(getResources().getColor(R.color.white));
						tbs6_2.setBackgroundResource(R.drawable.sregister0003);
						stbs6_2_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs6_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs6_2.setBackgroundResource(R.drawable.sregister0002);
			tbs6_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs6_2_2) {
					case 0:
						tbs6_2.setTextColor(getResources().getColor(R.color.white));
						tbs6_2.setBackgroundResource(R.drawable.sregister0003);
						stbs6_2_2 = 1;
						break;
					case 1:
						tbs6_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs6_2.setBackgroundResource(R.drawable.sregister0002);
						stbs6_2_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 用镇静剂/药物
		tbs7_1 = (TextView) findViewById(R.id.z_j_j_y_w_upbs);
		if (dh.getTakesedatives() != null) {
			tbs7_1.setTextColor(getResources().getColor(R.color.white));
			tbs7_1.setBackgroundResource(R.drawable.sregister0003);
			tbs7_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs7_1_1) {
					case 0:
						tbs7_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs7_1.setBackgroundResource(R.drawable.sregister0002);
						stbs7_1_1 = 1;
						break;
					case 1:
						tbs7_1.setTextColor(getResources().getColor(R.color.white));
						tbs7_1.setBackgroundResource(R.drawable.sregister0003);
						stbs7_1_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs7_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs7_1.setBackgroundResource(R.drawable.sregister0002);
			tbs7_1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs7_1_2) {
					case 0:
						tbs7_1.setTextColor(getResources().getColor(R.color.white));
						tbs7_1.setBackgroundResource(R.drawable.sregister0003);
						stbs7_1_2 = 1;
						break;
					case 1:
						tbs7_1.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs7_1.setBackgroundResource(R.drawable.sregister0002);
						stbs7_1_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}
		// 长期大量饮酒 
		tbs7_2 = (TextView) findViewById(R.id.c_q_d_l_y_j_upbs);
		if (dh.getLongdrinking() != null) {
			tbs7_2.setTextColor(getResources().getColor(R.color.white));
			tbs7_2.setBackgroundResource(R.drawable.sregister0003);
			tbs7_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs7_2_1) {
					case 0:
						tbs7_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs7_2.setBackgroundResource(R.drawable.sregister0002);
						stbs7_2_1 = 1;
						break;
					case 1:
						tbs7_2.setTextColor(getResources().getColor(R.color.white));
						tbs7_2.setBackgroundResource(R.drawable.sregister0003);
						stbs7_2_1 = 0;
						break;
					default:
						break;
					}
				}
			});
		} else {
			tbs7_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
			tbs7_2.setBackgroundResource(R.drawable.sregister0002);
			tbs7_2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (stbs7_2_2) {
					case 0:
						tbs7_2.setTextColor(getResources().getColor(R.color.white));
						tbs7_2.setBackgroundResource(R.drawable.sregister0003);
						stbs7_2_2 = 1;
						break;
					case 1:
						tbs7_2.setTextColor(getResources().getColor(R.color.bs_zt_ys));
						tbs7_2.setBackgroundResource(R.drawable.sregister0002);
						stbs7_2_2 = 0;
						break;
					default:
						break;
					}
				}
			});
		}

		mtbsSubmit = (Button) findViewById(R.id.sure_submit1_upbs);
		mtbsSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				String losesleep = tbs1_1.getText().toString();
				String diabetes = tbs1_2.getText().toString();
				String hypertension = tbs2_1.getText().toString();
				String coronaryheartdisease = tbs2_2.getText().toString();
				String heartfailure = tbs3_1.getText().toString();
				String arrhythmia = tbs3_2.getText().toString();
				String congestion = tbs4_1.getText().toString();
				String longsmoking = tbs4_2.getText().toString();
				String cerebrovasculardisease = tbs5_1.getText().toString();
				String renalfailure = tbs5_2.getText().toString();
				String takesedatives = tbs6_1.getText().toString();
				String longdrinking = tbs6_2.getText().toString();
				String whetherxyccd = tbs7_1.getText().toString();
				String whetherfmhy = tbs7_2.getText().toString();

				if (stbs1_1_1 == 0 && stbs1_1_2 == 1) {
					dh.setLosesleep(losesleep);
				} else if (stbs1_1_1 == 1 && stbs1_1_2 == 0) {
					dh.setLosesleep(null);
				}
				if (stbs1_2_1 == 0 && stbs1_2_2 == 1) {
					dh.setDiabetes(diabetes);
				} else if (stbs1_2_1 == 1 && stbs1_2_2 == 0) {
					dh.setDiabetes(null);
				}
				if (stbs2_1_1 == 0 && stbs2_1_2 == 1) {
					dh.setHypertension(hypertension);
				} else if (stbs2_1_1 == 1 && stbs2_1_2 == 0) {
					dh.setHypertension(null);
				}
				if (stbs2_2_1 == 0 && stbs2_2_2 == 1) {
					dh.setCoronaryheartdisease(coronaryheartdisease);
				} else if (stbs2_2_1 == 1 && stbs2_2_2 == 0) {
					dh.setCoronaryheartdisease(null);
				}
				if (stbs3_1_1 == 0 && stbs3_1_2 == 1) {
					dh.setHeartfailure(heartfailure);
				} else if (stbs3_1_1 == 1 && stbs3_1_2 == 0) {
					dh.setHeartfailure(null);
				}
				if (stbs3_2_1 == 0 && stbs3_2_2 == 1) {
					dh.setArrhythmia(arrhythmia);
				} else if (stbs3_2_1 == 1 && stbs3_2_2 == 0) {
					dh.setArrhythmia(null);
				}
				if (stbs4_1_1 == 0 && stbs4_1_2 == 1) {
					dh.setCongestion(congestion);
				} else if (stbs4_1_1 == 1 && stbs4_1_2 == 0) {
					dh.setCongestion(null);
				}
				if (stbs4_2_1 == 0 && stbs4_2_2 == 1) {
					dh.setLongsmoking(longsmoking);
				} else if (stbs4_2_1 == 1 && stbs4_2_2 == 0) {
					dh.setLongsmoking(null);
				}
				if (stbs6_1_1 == 0 && stbs6_1_2 == 1) {
					dh.setCerebrovasculardisease(cerebrovasculardisease);
				} else if (stbs6_1_1 == 1 && stbs6_1_2 == 0) {
					dh.setCerebrovasculardisease(null);
				}
				if (stbs6_2_1 == 0 && stbs6_2_2 == 1) {
					dh.setRenalfailure(renalfailure);
				} else if (stbs6_2_1 == 1 && stbs6_2_2 == 0) {
					dh.setRenalfailure(null);
				}
				if (stbs7_1_1 == 0 && stbs7_1_2 == 1) {
					dh.setTakesedatives(takesedatives);
				} else if (stbs7_1_1 == 1 && stbs7_1_2 == 0) {
					dh.setTakesedatives(null);
				}
				if (stbs7_2_1 == 0 && stbs7_2_2 == 1) {
					dh.setLongdrinking(longdrinking);
				} else if (stbs7_2_1 == 1 && stbs7_2_2 == 0) {
					dh.setLongdrinking(null);
				}
				if (stbs5_2_1 == 0 && stbs5_2_2 == 1) {
					dh.setWhetherfmhy(whetherfmhy);
				} else if (stbs5_2_1 == 1 && stbs5_2_2 == 0) {
					dh.setWhetherfmhy(null);
				}
				if (stbs5_1_1 == 0 && stbs5_1_2 == 1) {
					dh.setWhetherxyccd(whetherxyccd);
				} else if (stbs5_1_1 == 1 && stbs5_1_2 == 0) {
					dh.setWhetherxyccd(null);
				}

				boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
				boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());

				if (isConnected || isConnected3g) {
					new Thread() {
						public void run() {
							sd = new PublicDaoImpl(UpdateBsFActivity.this);
							final UserNumber un = sd.findTelorEmail();
							try {
								if (un != null && un.getToken() != null) {
									Status testToken = new WebAPI().testToken(un.getToken());
									if (!Status.isSuccess(testToken)) {
										mHandler.sendEmptyMessage(1);
									} else {
										mHandler.sendEmptyMessage(2);
										ESSAndDisEaseHistoryDto ed = WebHelper.userESS(um.getUuid(), ep, dh);
										// Looper.prepare();
										Thread.sleep(3000);
										Status status = new WebAPI().uploadEssAndDiseaseHistory(ed);
										if (Status.isSuccess(status)) {
											sd.modifyDiabetesHy(dh);
											UpdateBsFActivity.this.finish();
											overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
											Log.d("TAG", "病史修改成功!" + status);
											mHandler.sendEmptyMessage(0);
										} else {
											Log.d("TAG", "病史修改失败!" + status);
											mHandler.sendEmptyMessage(1);
										}
										// finally {
										// Looper.loop();
										// }
										mHandler.sendEmptyMessage(3);
									}
								} else {
									mHandler.sendEmptyMessage(2);
									ESSAndDisEaseHistoryDto ed = WebHelper.userESS(um.getUuid(), ep, dh);
									// Looper.prepare();
									Thread.sleep(3000);
									Status status = new WebAPI().uploadEssAndDiseaseHistory(ed);
									if (Status.isSuccess(status)) {
										sd.modifyDiabetesHy(dh);
										UpdateBsFActivity.this.finish();
										overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
										Log.d("TAG", "病史修改成功!" + status);
										mHandler.sendEmptyMessage(0);
									} else {
										Log.d("TAG", "病史修改失败!" + status);
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
					Toast.makeText(UpdateBsFActivity.this,
							UpdateBsFActivity.this.getString(R.string.modify_failed_please_open_the_network),
							Toast.LENGTH_LONG).show();
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
				Toast.makeText(UpdateBsFActivity.this,
						UpdateBsFActivity.this.getString(R.string.history_revision_success), Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(UpdateBsFActivity.this,
						UpdateBsFActivity.this.getString(R.string.history_revision_failure), Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Log.d("TAG", "mHandler, case 2:");
				if (sdialog == null) {
					sdialog = new SubmitDialog(UpdateBsFActivity.this);
				}
				sdialog.setContent(UpdateBsFActivity.this
						.getString(R.string.please_wait_while_modifying_the_medical_history_information));
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
