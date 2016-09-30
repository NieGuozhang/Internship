package com.hbut.internship.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.internship.model.Apply;
import com.internship.model.CV;
import com.internship.model.Collect;
import com.internship.model.Position;
import com.internship.model.Student;

import android.net.ConnectivityManager;
import android.app.Activity;
import android.content.Context;

/*
 * 连接服务器。
 */
public class Internet extends Activity {

	private static final String http = "http://192.168.1.106:8080/";

	public boolean isNetworkAvailable() {
		// 得到网络连接信息
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			try {
				String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
				Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
				// ping的状态
				int status = p.waitFor();
				if (status == 0) {
					return true;
				}
			} catch (IOException e) {
			} catch (InterruptedException e) {
			}
		}
		return false;
	}

	static Map<String, Object> connection(String target, Map<String, Object> map)
			throws Exception {
		Map<String, Object> mapin = null;
		byte[] bytes = null;
		bytes = mapToBytes(map);
		HttpURLConnection urlConn = null;
		// 新建一个URL对象
		URL url = new URL(target);
		// 打开一个HttpURLConnection连接
		urlConn = (HttpURLConnection) url.openConnection();
		// 设置连接超时时间
		urlConn.setConnectTimeout(5* 1000);
		// Post请求必须设置允许输出
		urlConn.setDoOutput(true);
		// Post请求不能使用缓存
		urlConn.setUseCaches(false);
		// 设置为Post请求
		urlConn.setRequestMethod("POST");
		urlConn.setInstanceFollowRedirects(true);
		// 配置请求Content-Type
		urlConn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencode");
		// 开始连接
		urlConn.connect();
		// 发送请求参数
		DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
		dos.write(bytes);
		dos.flush();
		dos.close();
		if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			mapin = instreamToMap(urlConn.getInputStream());
		}
		return mapin;
	}

	static Map<String, Object> instreamToMap(InputStream is)
			throws IOException, ClassNotFoundException {
		ByteArrayInputStream bi;
		ObjectInputStream oi;
		byte[] bytes = readIS(is);
		bi = new ByteArrayInputStream(bytes);
		oi = new ObjectInputStream(bi);
		bi.close();
		oi.close();
		return (Map<String, Object>) oi.readObject();
	}

	static byte[] mapToBytes(Map<String, Object> map) throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(map);
		bytes = bo.toByteArray();
		bo.close();
		oo.close();
		return bytes;
	}

	public static byte[] readIS(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buff = new byte[4096];
		int length = 0;
		while ((length = is.read(buff)) != -1) {
			bos.write(buff, 0, length);
		}
		bos.flush();
		bos.close();
		return bos.toByteArray();
	}

	// 判断登录是否成功，成功返回true，失败返回false
	public static boolean login(String email, String password) throws Exception {
		String validateURL = http
				+ "InternshipProject02/StudentLoginAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("password", password);
		mapout.put("email", email);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("check");
	}

	// 判断是否为数据库中存在的邮箱，存在返回true，不存在返回false
	public static boolean sendemail(String email, String newpassword)
			throws Exception {
		String validateURL = http
				+ "InternshipProject02/FindPasswordAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("email", email);
		mapout.put("newpassword", newpassword);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 通过向后台验证自己的原始的密码来修改密码
	public static boolean CheckPassword(String email, String oldpassword,
			String newpassword) throws Exception {
		String validateURL = http
				+ "InternshipProject02/ModifyPasswordAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("email", email);
		mapout.put("oldpassword", oldpassword);
		mapout.put("newpassword", newpassword);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 查询学校推荐
	public static List<Position> querySchRecommend(String schoolNum)
			throws Exception {
		String validateURL = http
				+ "InternshipProject02/SchoolRecommendAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("schNum", schoolNum);
		mapin = connection(validateURL, mapout);
		return (List<Position>) mapin.get("positionlist");
	}

	// 添加简历，true添加成功，false添加失败
	public static boolean AddCv(CV cv) throws Exception {
		String validateURL = http + "InternshipProject02/AddCVAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("CV", cv);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 查询简历
	public static CV queryCv(int stu_Id) throws Exception {
		String validateURL = http + "InternshipProject02/GetCVAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", stu_Id);
		mapin = connection(validateURL, mapout);
		return (CV) mapin.get("CV");
	}

	// 删除简历
	public static boolean deleteCv(int stu_Id) throws Exception {
		String validateURL = http + "InternshipProject02/DeleteCVAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", stu_Id);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 收藏，true收藏成功，false收藏失败
	public static boolean collect(int po_id, String stu_name) throws Exception {
		String validateURL = http
				+ "InternshipProject02/CollectPositionAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuName", stu_name);
		mapout.put("poId", po_id);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 取消单个收藏,true取消成功，false取消失败
	public static boolean deleteCollect(int po_id, String stu_name)
			throws Exception {
		String validateURL = http
				+ "InternshipProject02/CollectDeleteAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("poId", po_id);
		mapout.put("stuName", stu_name);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 删除所有收藏,true删除成功，false删除失败
	public static boolean deleteAllCollect(int stu_id) throws Exception {
		String validateURL = http
				+ "InternshipProject02/AllCollectDeleteAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", stu_id);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 查看收藏
	public static List<Position> queryCollect(String stu_name) throws Exception {
		String validateURL = http
				+ "InternshipProject02/CollectListAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuName", stu_name);
		mapin = connection(validateURL, mapout);
		return (List<Position>) mapin.get("positionlist");
	}

	// 返回学生信息
	public static Student queryStudent(String email) throws Exception {
		String validateURL = http
				+ "InternshipProject02/StudentInformationAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("email", email);
		mapin = connection(validateURL, mapout);
		return (Student) mapin.get("student");
	}

	// 输入企业搜索职位
	public static List<Position> FindIntershipWithEn(String En_name)
			throws Exception {
		String validateURL = http
				+ "InternshipProject02/FindInternshipWithEnAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("enName", En_name);
		mapin = connection(validateURL, mapout);
		return (List<Position>) mapin.get("positions");
	}

	// 输入职位搜索职位
	public static List<Position> FindIntershipWithPo(String Po_name)
			throws Exception {
		String validateURL = http
				+ "InternshipProject02/FindInternshipWithPoAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("poName", Po_name);
		mapin = connection(validateURL, mapout);
		return (List<Position>) mapin.get("positions");
	}

	// 查看申请
	public static List<Apply> GetApply(int stu_id) throws Exception {
		String validateURL = http
				+ "InternshipProject02/GetApplyListAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", stu_id);
		mapin = connection(validateURL, mapout);
		return (List<Apply>) mapin.get("applylist");
	}

	// 撤销申请
	public static boolean RevokeApply(int applyId) throws Exception {
		String validateURL = http
				+ "InternshipProject02/RevokeApplyAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("applyId", applyId);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 编辑评论
	public static boolean EditApplyInformation(int applyId, Float starevalu,
			String evaluation, String summary) throws Exception {
		String validateURL = http
				+ "InternshipProject02/EditApplyInformationAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("applyId", applyId);
		mapout.put("starevalu", starevalu);
		mapout.put("evaluation", evaluation);
		mapout.put("summary", summary);// 实习总结。
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 获取单个申请职位
	public static Apply GetApplyInformation(int stu_id, int po_id)
			throws Exception {
		String validateURL = http
				+ "InternshipProject02/GetOneApplyAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", stu_id);
		mapout.put("poId", po_id);
		mapin = connection(validateURL, mapout);
		return (Apply) mapin.get("apply");
	}

	// 添加申请
	public static boolean ApplyInformation(int Po_id, int stu_id)
			throws Exception {
		String validateURL = http
				+ "InternshipProject02/ApplyInternshipAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", stu_id);
		mapout.put("poId", Po_id);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 查看单个收藏
	public static Collect OneCollect(int stu_id, int Po_id) throws Exception {
		String validateURL = http
				+ "InternshipProject02/OneCollectAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", stu_id);
		mapout.put("poId", Po_id);
		mapin = connection(validateURL, mapout);
		return (Collect) mapin.get("collect");
	}

	// 添加实习
	public static boolean SetInternship(int applyId) throws Exception {
		String validateURL = http
				+ "InternshipProject02/AddInternshipAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("applyId", applyId);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 获取正在实习
	public static Apply GetInternship(int stuId) throws Exception {
		String validateURL = http
				+ "InternshipProject02/GetInternshipAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", stuId);
		mapin = connection(validateURL, mapout);
		return (Apply) mapin.get("apply");
	}

	// 撤销实习
	public static boolean RevokeInternship(int applyId) throws Exception {
		String validateURL = http
				+ "InternshipProject02/RevokeInternshipAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("applyId", applyId);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 获取学生的实习经历
	public static List<Apply> GetExperience(int studentID) throws Exception {
		String validateURL = http
				+ "InternshipProject02/GetInternshipOverAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("stuId", studentID);
		mapin = connection(validateURL, mapout);
		return (List<Apply>) mapin.get("applylist");
	}

	// 保存简历
	public static boolean SetResume(CV cv) throws Exception {
		String validateURL = http + "InternshipProject02/AddCVAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("CV", cv);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 修改简历
	public static boolean ModifyResume(CV cv) throws Exception {
		String validateURL = http + "InternshipProject02/ModifyCVAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("CV", cv);
		mapin = connection(validateURL, mapout);
		return (boolean) mapin.get("bool");
	}

	// 返回公司所有的评论
	public static List<Apply> GetComment(int enID) throws Exception {
		String validateURL = http
				+ "InternshipProject02/EnterpriseCommentAction.action";
		Map<String, Object> mapout = new HashMap<String, Object>();
		Map<String, Object> mapin;
		mapout.put("enId", enID);
		mapin = connection(validateURL, mapout);
		return (List<Apply>) mapin.get("applylist");
	}

}
