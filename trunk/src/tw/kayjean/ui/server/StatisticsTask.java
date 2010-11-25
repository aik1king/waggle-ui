package tw.kayjean.ui.server;

import java.util.Calendar;
import java.util.TimerTask;

import javax.servlet.ServletContext;


/** *//**
 * 统计任务
 * @author westd
 *
 */
public class StatisticsTask extends TimerTask
{

    private static final int STATISTICS_SCHEDULE_HOUR = 0;
    private static boolean isRunning = false;
    private ServletContext context = null;

    public StatisticsTask(ServletContext context)
    {
        this.context = context;
    }
    
    @Override
    public void run()
    {
        Calendar cal = Calendar.getInstance();
        System.out.println(isRunning);
        if (!isRunning)
        {
            //if (STATISTICS_SCHEDULE_HOUR == cal.get(Calendar.HOUR_OF_DAY)) //查看是否为凌晨
          //{ 
        	isRunning = true; 
        	context.log("開始執行指定任務");
        	executeTask();
        	//指定任务执行结束
        	isRunning = false;
        	context.log("指定任務執行結束"); 
            //} 
        } 
        else 
        {
            context.log("上一次任務執行還未結束");
        }
    }

    /** *//**
     * 执行任务
     */
    public void executeTask()
    {
    	System.out.println( "任務開始" );
        //印出目前mcache裡面內容
        MemCache.showdata();
        System.out.println( "任務結束" );
    }
}
