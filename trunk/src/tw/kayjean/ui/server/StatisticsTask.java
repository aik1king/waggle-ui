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
        //System.out.println(isRunning);
        if (!isRunning) 
        { 
            if (STATISTICS_SCHEDULE_HOUR == cal.get(Calendar.HOUR_OF_DAY)) //查看是否为凌晨
            { 
                isRunning = true; 
                context.log("开始执行指定任务");
                
                //TODO 添加自定义的详细任务
                executeTask();
                
                //指定任务执行结束
                isRunning = false;
                context.log("指定任务执行结束"); 
            } 
        } 
        else 
        {
            context.log("上一次任务执行还未结束");
        }
    
    }

    /** *//**
     * 执行任务
     */
    public void executeTask()
    {
        System.out.println("任务1");
        System.out.println("任务2");
    }
}
