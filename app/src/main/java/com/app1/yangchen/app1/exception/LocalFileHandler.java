package com.app1.yangchen.app1.exception;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;
import com.app1.yangchen.app1.util.JFileKit;

import java.io.*;
import java.util.Date;

/**
 * 本地异常处理类
 * 
 * @author blue
 *
 */
public class LocalFileHandler extends BaseExceptionHandler
{
	private Context context;

	public LocalFileHandler(Context context)
	{
		this.context = context;
	}

	/**
	 * 
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 * @throws IOException
	 * 
	 */
	@Override
	public boolean handleException(Throwable ex)
	{
		if (ex == null)
			return false;

		new Thread()
		{
			public void run()
			{
				Looper.prepare();
				Toast.makeText(context, "很抱歉,程序出现异常,正在从错误中恢复", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		// 保存错误日志
		saveLog(ex);

		return true;
	}

	// 保存错误日志
	public void saveLog(Throwable ex)
	{
		try
		{
			File errorFile = new File(JFileKit.getDiskCacheDir(context) + "/log/crash.log");

			if (!errorFile.exists())
			{
				errorFile.createNewFile();
			}

			OutputStream out = new FileOutputStream(errorFile, true);

			out.write(("\n\n------错误分割线  " + dateFormat.format(new Date()) + "------\n\n").getBytes());

			PrintStream stream = new PrintStream(out);

			ex.printStackTrace(stream);

			stream.flush();

			out.flush();

			stream.close();

			out.close();

		} catch (Exception exx)
		{
			exx.printStackTrace();
		}
	}
}
