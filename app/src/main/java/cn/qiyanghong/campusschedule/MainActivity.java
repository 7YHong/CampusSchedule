package cn.qiyanghong.campusschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import cn.qiyanghong.campusschedule.model.TodoData;
import cn.qiyanghong.campusschedule.service.DaemonService;
import cn.qiyanghong.campusschedule.utils.TodoManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private TodoManager todoManager;
    Calendar calendar = Calendar.getInstance();

    TextView todoList;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_addsimple: {
                    TodoData todo = new TodoData(calendar.getTimeInMillis()+2*60*1000, "title", "detail");
                    todoManager.add(todo);
                }
                break;
                case R.id.btn_showlist: {
                    StringBuffer str = new StringBuffer();
                    SparseArray array = todoManager.getAllTodo();
                    for (int i = 0; i < array.size(); i++)
                        str.append(array.valueAt(i).toString()).append("\n");
                    todoList.setText(str.toString());
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent daemonIntent = new Intent(this, DaemonService.class);
        startService(daemonIntent);
        todoManager = TodoManager.getInstance(getApplicationContext());
        todoList = (TextView) findViewById(R.id.todolist);
        findViewById(R.id.btn_showlist).setOnClickListener(onClickListener);
        findViewById(R.id.btn_addsimple).setOnClickListener(onClickListener);
    }

}
