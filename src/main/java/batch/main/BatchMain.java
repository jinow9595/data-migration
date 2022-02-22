package batch.main;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import batch.action.BatchCultureAction;


public class BatchMain {

	static Logger logger = Logger.getLogger(BatchMain.class.getName());

	public static void main(String[] args) throws NumberFormatException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info(sdf.format(Calendar.getInstance().getTime())+" STARTSTART");

		BatchCultureAction batchAction = new BatchCultureAction();
		batchAction.execute();

		logger.info(sdf.format(Calendar.getInstance().getTime())+" ENDEND");
	}
}
