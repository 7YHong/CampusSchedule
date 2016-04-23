package cn.qiyanghong.campusschedule.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

import cn.qiyanghong.campusschedule.receiver.TodoReceiver;
import cn.qiyanghong.campusschedule.model.TodoData;

/**
 * Created by QYH on 2016/4/23.
 * 负责CURD
 * 以及各个Todo的注册
 */
public class TodoManager {
    private static final long INTERVALMILLIS = 5 * 60 * 1000;
    private static final String TAG = TodoManager.class.getName();

    private static Context context;
    private static TodoManager mInstance;

    //它存储的数值都是按键值从小到大的顺序排列好的,因此只要用时间作为键即可
    private static SparseArray<TodoData> todoList;
    private static AlarmManager alarmManager;
    private static Gson gson;
    private static File file;

    private TodoManager(Context context) {
        L.i(TAG,"Construct progress");
        this.context = context;
        todoList = new SparseArray<>();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        gson = new Gson();

        File dir = context.getExternalFilesDir(null);
        file = new File(dir, "TodoList.JSON");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        readTodos();
    }

    public static TodoManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (TodoManager.class) {
                if (mInstance == null) {
                    mInstance = new TodoManager(context);
                }
            }
        }
        return mInstance;
    }

    private static void saveTodos() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < todoList.size(); i++) {
            appendTodo(todoList.valueAt(i));
            stringBuffer.append(gson.toJson(todoList.valueAt(i))).append(",");
        }
        L.i(TAG, "write:" + stringBuffer.toString());
    }

    private static void appendTodo(TodoData todo) {
        synchronized (file) {
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(file, true));
                String s = gson.toJson(todo);
                writer.append(s + "\n");
                writer.close();
                L.i(TAG, "append:" + s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void readTodos() {
        synchronized (file) {
            try {
                Scanner reader = new Scanner(new FileReader(file));
                StringBuffer stringBuffer = new StringBuffer();
                while (reader.hasNext()) {
                    String t = reader.nextLine();
                    stringBuffer.append(t).append(",");
                    TodoData todo = gson.fromJson(t, TodoData.class);
                    registerTodo(todo);
                }
                L.i(TAG, "read:" + stringBuffer.toString());
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void registerTodo(TodoData todo) {
        Calendar calendar = Calendar.getInstance();
        todoList.put(todo.getId(), todo);
        //如果闹钟过期则不加入提醒
        if (calendar.getTimeInMillis() >= todo.getTime()) return;

        Intent alarmIntent = new Intent(TodoReceiver.WAKE_ACTION);
        PendingIntent operation = PendingIntent.getBroadcast(context, todo.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, todo.getTime(), INTERVALMILLIS, operation);
    }

    public static void add(TodoData todo) {
        registerTodo(todo);
        appendTodo(todo);
    }

    public static void del(TodoData todo) {
        int id = todo.getId();
        alarmManager.cancel(PendingIntent.getBroadcast(context, id, new Intent(TodoReceiver.WAKE_ACTION), PendingIntent.FLAG_CANCEL_CURRENT));
        todoList.delete(id);
        saveTodos();
    }

    public static void update(int previous, TodoData todo) {
        alarmManager.cancel(PendingIntent.getBroadcast(context, previous, new Intent(TodoReceiver.WAKE_ACTION), PendingIntent.FLAG_CANCEL_CURRENT));
        todoList.delete(previous);
        add(todo);
    }

    public static SparseArray<TodoData> getAllTodo() {
        L.i(TAG,"getAllTodo:"+todoList.size()+"items");
        return todoList;
    }
}
