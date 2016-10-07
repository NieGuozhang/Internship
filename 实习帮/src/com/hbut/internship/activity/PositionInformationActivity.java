package com.hbut.internship.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplicationUtil;
import com.hbut.internship.util.TextToDBCUtil;
import com.hbut.internship.util.ToastUtil;
import com.internship.model.Apply;
import com.internship.model.Collect;
import com.internship.model.Position;

public class PositionInformationActivity extends BaseActivity implements
		OnClickListener {

	private SharedPreferences preferences;

	private Button applyButton;
	private ImageButton back, collect;

	private TextView positionnameTextView, ennameTextView, salaryTV,
			worktimeTV, poAddressTV, educationTV, poTypeTV, numbersTV,
			closingdateTV, jobdesciption, enType;
	private LinearLayout linearLayout, linearLayout_address;

	private Position position;
	private Apply theApply;
	private Collect theCollect;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 该职位已被收藏
				collect.setImageResource(R.drawable.sc2);
				collect.setEnabled(false);// 设置Button不能点击。
				break;
			case 1:// 该职位已被申请
				applyButton.setText("该职位已被申请");
				applyButton.setBackgroundResource(R.drawable.button_bg_huise);
				applyButton.setEnabled(false);// 申请按钮的点击事件取消
				break;
			case 2:
				ToastUtil
						.showToast(MyApplicationUtil.getContext(), "申请失败，请重试！");
				break;
			case 3:
				ToastUtil.showToast(MyApplicationUtil.getContext(),
						"收藏失败，请重新操作！");
				break;
			case 4:
				applyButton.setText("已实习过该职位");
				applyButton.setBackgroundResource(R.drawable.button_bg_huise);
				applyButton.setEnabled(false);// 申请按钮的点击事件取消
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_positioninformation);

		preferences = getSharedPreferences("Student", MODE_PRIVATE);// 获取学生的ID

		class MyThread implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					/*
					 * 获取上一层界面传递下来的对象。
					 */
					final Bundle bundle = getIntent().getExtras();
					position = (Position) bundle.getSerializable("position");
					preferences = getSharedPreferences("Student", MODE_PRIVATE);// 获取学生的ID
					theApply = Internet.GetApplyInformation(
							preferences.getInt("studentID", 0),
							position.getPoId());
					if (theApply != null) {// 该职位存在申请表中
						if (theApply.getApply_state() != 4) {
							new Thread(new Runnable() {
								public void run() {
									Message msg = new Message();
									msg.what = 1;
									handler.sendMessage(msg);
								}
							}).start();
						} else {
							new Thread(new Runnable() {
								public void run() {
									Message msg = new Message();
									msg.what = 4;// 该职位状态是实习结束
									handler.sendMessage(msg);
								}
							}).start();
						}
					}
					theCollect = Internet.OneCollect(
							preferences.getInt("studentID", 0),
							position.getPoId());
					if (theCollect != null) {
						new Thread(new Runnable() {
							public void run() {
								Message msg1 = new Message();
								msg1.what = 0;
								handler.sendMessage(msg1);
							}
						}).start();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		MyThread myThread = new MyThread();
		Thread t = new Thread(myThread);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		findView();
		initView();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		applyButton.setOnClickListener(this);// 申请按钮的点击事件注册
		collect.setOnClickListener(this);// 收藏按钮的点击事件注册
		linearLayout.setOnClickListener(this);
		linearLayout_address.setOnClickListener(this);// 实习地点的点击注册事件

	}

	private void findView() {
		// 初始化控件
		positionnameTextView = (TextView) findViewById(R.id.tv_poname);// 实习职位名称
		ennameTextView = (TextView) findViewById(R.id.tv_enname);// 企业名称
		enType = (TextView) findViewById(R.id.tv_pi_entype);
		salaryTV = (TextView) findViewById(R.id.tv_salary);// 薪水
		worktimeTV = (TextView) findViewById(R.id.tv_worktime);// 工作时间
		poAddressTV = (TextView) findViewById(R.id.tv_poaddress);// 实习地点
		educationTV = (TextView) findViewById(R.id.tv_poeducation);// 学历要求
		poTypeTV = (TextView) findViewById(R.id.tv_potype);// 职位类别
		numbersTV = (TextView) findViewById(R.id.tv_popeoplenumber);// 招聘人数
		closingdateTV = (TextView) findViewById(R.id.tv_pi_closingdate);// 截止日期
		jobdesciption = (TextView) findViewById(R.id.tv_jobdescription);// 职位描述
		applyButton = (Button) findViewById(R.id.bt_applyposition);// 申请按钮
		back = (ImageButton) findViewById(R.id.bt_pi_back);// 返回按钮
		collect = (ImageButton) findViewById(R.id.imbt_collect);
		linearLayout = (LinearLayout) findViewById(R.id.learLayout_enterprise);
		linearLayout_address = (LinearLayout) findViewById(R.id.linearLayout_poaddress);
	}

	private void initView() {
		/*
		 * 获取上一层界面传递下来的对象。
		 */
		final Bundle bundle = getIntent().getExtras();
		position = (Position) bundle.getSerializable("position");

		// 将对象中的值对每个对应的控件进行赋值。
		positionnameTextView.setText(position.getPoName());

		ennameTextView.setText(position.getEnterprise().getEnName());
		enType.setText(position.getEnterprise().getEnType());

		String salary = position.getSalarymin() + "-" + position.getSalarymax();
		salaryTV.setText(salary + "元");// 薪资水平

		worktimeTV.setText(position.getWorkTime());
		poAddressTV.setText(position.getPoAddress());
		educationTV.setText(position.getPoEducation());
		poTypeTV.setText(position.getPoType());
		numbersTV.setText(position.getNumbers() + "人");
		closingdateTV.setText(position.getClosingdate().toString()
				.substring(0, 10));
		jobdesciption.setText(TextToDBCUtil.toDBC(position.getDescription()));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_collect:// 收藏职位的按钮事件

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (Internet.collect(position.getPoId(),
								preferences.getString("studentname", ""))) {
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
							ToastUtil.showToast(MyApplicationUtil.getContext(),
									"收藏成功！");
						} else {
							Message msg = new Message();
							msg.what = 3;
							handler.sendMessage(msg);

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.bt_applyposition:// 申请职位的按钮事件
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					PositionInformationActivity.this);
			dialog.setTitle("提示：");
			dialog.setMessage("确定要申请该职位吗？");
			dialog.setCancelable(false);
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new Thread(new Runnable() {

								@Override
								public void run() {

									// TODO Auto-generated method stub
									try {
										boolean isTrue = Internet.ApplyInformation(
												position.getPoId(), preferences
														.getInt("studentID", 0));
										if (isTrue) {

											Message msg1 = new Message();
											msg1.what = 1;
											handler.sendMessage(msg1);
											ToastUtil.showToast(
													MyApplicationUtil
															.getContext(),
													"申请成功！");
										} else {
											Message msg1 = new Message();
											msg1.what = 2;
											handler.sendMessage(msg1);

										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}).start();
							dialog.dismiss();
						}
					});
			dialog.setNegativeButton("取消", null);
			dialog.show();
			break;
		case R.id.learLayout_enterprise:// 企业名称的点击事件
			Intent intent = new Intent(PositionInformationActivity.this,
					EnterpriseInformationActivity.class);
			Bundle b = getIntent().getExtras();
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.linearLayout_poaddress:// 实习地点的点击事件
			Intent it = new Intent(PositionInformationActivity.this,
					DaohangActivity.class);
			Bundle bundle = getIntent().getExtras();
			it.putExtras(bundle);
			startActivity(it);
			break;
		default:
			break;
		}
	}

}
