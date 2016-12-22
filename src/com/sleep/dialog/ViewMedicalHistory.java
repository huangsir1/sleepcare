package com.sleep.dialog;

import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.updatedata.activity.UpdateBsFActivity;
import com.sleep.updatedata.activity.UpdateBsMActivity;
import com.taiir.sleepcare.home.R;
import com.taiir.sleepcare.home.R.color;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewMedicalHistory {
	private Context mContext;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private SharedPreferences msharedPreferences;
	private PublicDao sd;
	private DiabetesHy dh;

	public ViewMedicalHistory(Context context) {

		mContext = context;
		builder = new AlertDialog.Builder(mContext);
		msharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
	}

	/**
	 * 自定义视图对话框
	 */
	public void viewDialog(String title) {
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.look_bs_dialog, null);
		sd = new PublicDaoImpl(mContext);
		dh = sd.findAllDiabetesHyById(msharedPreferences.getInt("muserid", -1));
		final int bsid = msharedPreferences.getInt("muserid", -1);
		final String sex = msharedPreferences.getString("sex", null);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.lin_layout);
		builder.create();

		TextView s_m = (TextView) view.findViewById(R.id.textView2_ess);
		TextView t_n_b = (TextView) view.findViewById(R.id.textView3_ess);
		TextView g_x_y = (TextView) view.findViewById(R.id.textView4_ess);
		TextView g_x_b = (TextView) view.findViewById(R.id.textView5_ess);
		TextView x_l_s_j = (TextView) view.findViewById(R.id.textView6_ess);
		TextView x_l_s_c = (TextView) view.findViewById(R.id.textView7_ess);
		TextView b_q_z_s = (TextView) view.findViewById(R.id.textView8_ess);
		TextView c_q_x_y = (TextView) view.findViewById(R.id.textView9_ess);
		TextView n_x_g_j_b = (TextView) view.findViewById(R.id.textView10_ess);
		TextView s_g_n_s_h = (TextView) view.findViewById(R.id.textView11_ess);
		TextView c_q_y_j = (TextView) view.findViewById(R.id.textView12_ess);
		TextView f_y_z_j_j = (TextView) view.findViewById(R.id.textView13_ess);
		final TextView s_f_j_j = (TextView) view.findViewById(R.id.textView14_ess);
		final TextView s_f_f_p = (TextView) view.findViewById(R.id.textView15_ess);
		final TextView x_y_c_c_d = (TextView) view.findViewById(R.id.textView16_ess);

		TextView r_m_ess_tx = (TextView) view.findViewById(R.id.remove_ess_tx);

		TextView x_g_b_s = (TextView) view.findViewById(R.id.modify_bs_update);
		x_g_b_s.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// String sfjj=s_f_j_j.getText().toString();
				// String sffp=s_f_f_p.getText().toString();
				// Toast.makeText(mContext, "病史修改...",
				// Toast.LENGTH_SHORT).show();
				if ((mContext.getString(R.string.female)).equals(sex)) {
					Intent intent = new Intent(mContext, UpdateBsMActivity.class);
					intent.putExtra("sexmid", bsid);
					mContext.startActivity(intent);
					dialog.dismiss();
					// ((Activity) mContext).finish();
					((Activity) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				} else {
					Intent intent = new Intent(mContext, UpdateBsFActivity.class);
					intent.putExtra("sexfid", bsid);
					mContext.startActivity(intent);
					dialog.dismiss();
					// ((Activity) mContext).finish();
					((Activity) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}

			}
		});
		/**
		 * 附加内容(科普)
		 */
		/**
		 * s_m.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Toast.makeText(mContext,
		 *           "科普小知识", Toast.LENGTH_SHORT).show(); // Intent intent=new
		 *           Intent(mContext, MainActivity.class); //
		 *           mContext.startActivity(intent); } });
		 */
		// 设定显示的View
		builder.setView(view);
		if (dh != null) {
			if (dh.getLosesleep() == null && dh.getDiabetes() == null && dh.getHypertension() == null
					&& dh.getCoronaryheartdisease() == null && dh.getHeartfailure() == null
					&& dh.getArrhythmia() == null && dh.getCongestion() == null && dh.getLongsmoking() == null
					&& dh.getCerebrovasculardisease() == null && dh.getRenalfailure() == null
					&& dh.getTakesedatives() == null && dh.getLongdrinking() == null && dh.getWhetherjjm() == null
					&& dh.getWhetherfmhy() == null && dh.getWhetherxyccd() == null) {

				r_m_ess_tx.setText(mContext.getString(R.string.medical_history_of_the_disease));
				r_m_ess_tx.setTextColor(Color.LTGRAY);
			} else {
				layout.removeView(r_m_ess_tx);
				r_m_ess_tx.setBackgroundResource(color.white);
				r_m_ess_tx.setTextColor(Color.LTGRAY);
			}
			if (dh.getLosesleep() != null) {
				s_m.setText(mContext.getString(R.string.insomnia));
			} else {
				s_m.setText(mContext.getString(R.string.insomnia));
				layout.removeView(s_m);
				s_m.setBackgroundResource(color.white);
				s_m.setTextColor(Color.LTGRAY);
			}
			if (dh.getDiabetes() != null) {
				t_n_b.setText(mContext.getString(R.string.diabetes));
			} else {
				t_n_b.setText(mContext.getString(R.string.diabetes));
				layout.removeView(t_n_b);
				t_n_b.setBackgroundResource(color.white);
				t_n_b.setTextColor(Color.LTGRAY);
			}
			if (dh.getHypertension() != null) {
				g_x_y.setText(mContext.getString(R.string.hypertension));
			} else {
				g_x_y.setText(mContext.getString(R.string.hypertension));
				layout.removeView(g_x_y);
				g_x_y.setBackgroundResource(color.white);
				g_x_y.setTextColor(Color.LTGRAY);
			}
			if (dh.getCoronaryheartdisease() != null) {
				g_x_b.setText(mContext.getString(R.string.coronary_heart_disease));
			} else {
				g_x_b.setText(mContext.getString(R.string.coronary_heart_disease));
				layout.removeView(g_x_b);
				g_x_b.setBackgroundResource(color.white);
				g_x_b.setTextColor(Color.LTGRAY);
			}
			if (dh.getHeartfailure() != null) {
				x_l_s_j.setText(mContext.getString(R.string.heart_failure));
			} else {
				x_l_s_j.setText(mContext.getString(R.string.heart_failure));
				layout.removeView(x_l_s_j);
				x_l_s_j.setBackgroundResource(color.white);
				x_l_s_j.setTextColor(Color.LTGRAY);
			}
			if (dh.getArrhythmia() != null) {
				x_l_s_c.setText(mContext.getString(R.string.arrhythmia));
			} else {
				x_l_s_c.setText(mContext.getString(R.string.arrhythmia));
				layout.removeView(x_l_s_c);
				x_l_s_c.setBackgroundResource(color.white);
				x_l_s_c.setTextColor(Color.LTGRAY);
			}
			if (dh.getCongestion() != null) {
				b_q_z_s.setText(mContext.getString(R.string.nasal_obstruction));
			} else {
				b_q_z_s.setText(mContext.getString(R.string.nasal_obstruction));
				layout.removeView(b_q_z_s);
				b_q_z_s.setBackgroundResource(color.white);
				b_q_z_s.setTextColor(Color.LTGRAY);
			}
			if (dh.getLongsmoking() != null) {
				c_q_x_y.setText(mContext.getString(R.string.long_term_smoking));
			} else {
				c_q_x_y.setText(mContext.getString(R.string.long_term_smoking));
				layout.removeView(c_q_x_y);
				c_q_x_y.setBackgroundResource(color.white);
				c_q_x_y.setTextColor(Color.LTGRAY);
			}
			if (dh.getCerebrovasculardisease() != null) {
				n_x_g_j_b.setText(mContext.getString(R.string.cerebral_vascular_disease));
			} else {
				n_x_g_j_b.setText(mContext.getString(R.string.cerebral_vascular_disease));
				layout.removeView(n_x_g_j_b);
				n_x_g_j_b.setBackgroundResource(color.white);
				n_x_g_j_b.setTextColor(Color.LTGRAY);
			}
			if (dh.getRenalfailure() != null) {
				s_g_n_s_h.setText(mContext.getString(R.string.renal_function_damage));
			} else {
				s_g_n_s_h.setText(mContext.getString(R.string.renal_function_damage));
				layout.removeView(s_g_n_s_h);
				s_g_n_s_h.setBackgroundResource(color.white);
				s_g_n_s_h.setTextColor(Color.LTGRAY);
			}
			if (dh.getTakesedatives() != null) {
				f_y_z_j_j.setText(mContext.getString(R.string.take_a_sedative));
			} else {
				f_y_z_j_j.setText(mContext.getString(R.string.take_a_sedative));
				layout.removeView(f_y_z_j_j);
				f_y_z_j_j.setBackgroundResource(color.white);
				f_y_z_j_j.setTextColor(Color.LTGRAY);
			}
			if (dh.getLongdrinking() != null) {
				c_q_y_j.setText(mContext.getString(R.string.long_term_heavy_drinking));
			} else {
				c_q_y_j.setText(mContext.getString(R.string.long_term_heavy_drinking));
				layout.removeView(c_q_y_j);
				c_q_y_j.setBackgroundResource(color.white);
				c_q_y_j.setTextColor(Color.LTGRAY);
			}
			if (dh.getWhetherjjm() != null) {
				s_f_j_j.setText(mContext.getString(R.string.menopause));
			} else {
				s_f_j_j.setText(mContext.getString(R.string.menopause));
				layout.removeView(s_f_j_j);
				s_f_j_j.setBackgroundResource(color.white);
				s_f_j_j.setTextColor(Color.LTGRAY);
			}
			if (dh.getWhetherfmhy() != null) {
				s_f_f_p.setText(mContext.getString(R.string.a_family_history_of_osahs));
			} else {
				s_f_f_p.setText(mContext.getString(R.string.a_family_history_of_osahs));
				layout.removeView(s_f_f_p);
				s_f_f_p.setBackgroundResource(color.white);
				s_f_f_p.setTextColor(Color.LTGRAY);
			}
			if (dh.getWhetherxyccd() != null) {
				x_y_c_c_d.setText(mContext.getString(R.string.bulky_uvula));
			} else {
				x_y_c_c_d.setText(mContext.getString(R.string.bulky_uvula));
				layout.removeView(x_y_c_c_d);
				x_y_c_c_d.setBackgroundResource(color.white);
				x_y_c_c_d.setTextColor(Color.LTGRAY);
			}
		} else {
			r_m_ess_tx.setText(mContext.getString(R.string.medical_history_of_the_disease));
			r_m_ess_tx.setTextColor(Color.LTGRAY);
		}

		// 设置dialog是否为模态，false表示模态，true表示非模态
		// ab.setCancelable(false);
		// 对话框的创建、显示,这里显示的位置是在屏幕的最下面，但是很不推荐这个种做法，因为距底部有一段空隙
		dialog = builder.create();
		Window window = dialog.getWindow();

		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		lp.x = 0; // 新位置X坐标
		lp.y = 0; // 新位置Y坐标
		// lp.width = 300; // 宽度
		// lp.height = 300; // 高度
		// lp.alpha = 1.0f; // 透明度
		window.setAttributes(lp);

		view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.myAnimationstyles); // 添加动画
		// 设置dialog的边框，这样可以去掉黑色的多余部分
		dialog.setView(view, 0, 0, 0, 0);
		// 对话框的外面点击,是否让对话框消失(fasle 不消失)
		// dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
