package batch.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchDAO {
	private Logger logger = LoggerFactory.getLogger(BatchDAO.class);
	// singleton
	private static BatchDAO instance;
	private SqlSessionFactory factoryTarget;

	public static BatchDAO getInstance() {
		if (instance == null) {
			synchronized (BatchDAO.class) {
				instance = new BatchDAO();
			}
		}
		return instance;
	}

	// constructor
	public BatchDAO() {
		try {
			Reader reader = null;
			reader = Resources.getResourceAsReader("mybatis-config-target.xml");
			factoryTarget = new SqlSessionFactoryBuilder().build(reader);
			reader.close();
		} catch (IOException e) {
			String[] errStr = ExceptionUtils.getRootCauseStackTrace(e);
			for (int i = 0; i < errStr.length; i++) {
				logger.error(errStr[i]);
			}
		}
	}

	/**
	 * 실행
	 * @param tbName			테이블
	 * @param keyAr				키
	 * @param filePath			파일경로
	 * @throws Exception
	 */
	public void excute(String tbName,String[] keyAr,String filePath) throws Exception {
		int limit = 100;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info(sdf.format(cal.getTime())+" MIG START");

		SqlSession sqlSessionTarget = factoryTarget.openSession();
		BufferedReader br = null;
		FileReader fr = null;
		try{
			logger.info(tbName+" : START "+sdf.format(Calendar.getInstance().getTime()));
			//sqlSessionTarget.delete("batchTarget.delete_"+tbName,null);
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String str = null;
			List<Map<String,Object>> paramList = new ArrayList<>();
			Map<String,Object> parameter = new HashMap<>();
			int cnt = 0;
			while ((str = br.readLine()) != null) {
				cnt++;
				Map<String,Object> map = new HashMap<>();
				String[] strAr = str.split("\t");
				for (int i = 0; i < strAr.length; i++) {
					System.out.println("keyAr[i] : " + keyAr[i]);
					System.out.println("strAr[i] : " + strAr[i]);
					map.put(keyAr[i],strAr[i]);
				}
				paramList.add(map);
				if(paramList.size() == limit){
					parameter.put("paramList",paramList);
					sqlSessionTarget.update("batchTarget.update_"+tbName,parameter);
					sqlSessionTarget.commit();
					parameter.clear();
					paramList.clear();
				}
				logger.info(tbName+" complete "+cnt);
			}
			br.close();
			fr.close();
			if(paramList != null && paramList.size() > 0){
				parameter.put("paramList",paramList);
				sqlSessionTarget.update("batchTarget.update_"+tbName,parameter);
				sqlSessionTarget.commit();
				logger.info(tbName+" :  END "+sdf.format(Calendar.getInstance().getTime()));
			}
		}catch(Exception e){
			String[] errStr = ExceptionUtils.getRootCauseStackTrace(e);
			for (int i = 0; i < errStr.length; i++) {
				logger.error(errStr[i]);
			}
			sqlSessionTarget.rollback();
			sqlSessionTarget.close();
			throw new Exception();
		}finally{
			sqlSessionTarget.close();
		}
	}
}
