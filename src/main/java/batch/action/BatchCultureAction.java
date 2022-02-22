package batch.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.dao.BatchDAO;

public class BatchCultureAction{
	private Logger logger = LoggerFactory.getLogger(BatchCultureAction.class);

	public void execute() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info(sdf.format(Calendar.getInstance().getTime())+" MIG START ");
		try {
			BatchDAO batchDao = BatchDAO.getInstance();
			
			//Ca_Case
			String [] keyAr = {
					"ManageNo"
					,"ImgUrl"
					,"OrgFileName"};
			batchDao.excute("Ca_Case", keyAr,"C:\\Users\\ji.seungmin\\Downloads\\CACASE_IMG.SQL");
		} catch (Exception e) {
			String[] errStr = ExceptionUtils.getRootCauseStackTrace(e);
			for (int i = 0; i < errStr.length; i++) {
				logger.error(errStr[i]);
			}
		}

		logger.info(sdf.format(Calendar.getInstance().getTime())+" MIG END ");
	}

}
