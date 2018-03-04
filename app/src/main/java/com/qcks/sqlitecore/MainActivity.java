package com.qcks.sqlitecore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.qcks.sqlitecore.bean.DbUser;
import com.qcks.sqlitecore.dao.BaseDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private MainActivity mActivity;
    private BaseDao<DbUser> dbUserDao;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        dbUserDao = new BaseDao<>(mActivity,DbUser.class);
        initView();
    }

    protected void initView() {
        Button  button1, button2, button3, button4;
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.d("插入数据");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long t0 = System.currentTimeMillis();
                        List<DbUser> dbUsers = new ArrayList<>();
                        for (int j = 0,i = 11; j < 10; j++) {
                            DbUser dbUser = new DbUser(i++);
                            dbUserDao.insert(dbUser);
                            dbUsers.add(dbUser);
                        }
                        long t1 = System.currentTimeMillis();
                        AppLog.d(TAG, "new耗时=" + (t1 - t0));
//                        dbUserDao.insert(dbUsers);
//                        long t2 = System.currentTimeMillis();
//                        AppLog.d(TAG, "插入耗时=" + (t2 - t1));
                    }
                }).start();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUserDao.delete(1);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbUser dbUser = new DbUser(2);
                dbUser.setString("更改数据哦" + System.currentTimeMillis());
                dbUserDao.updatePK(dbUser, 2345);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long t1 = System.currentTimeMillis();
                List<DbUser> dbUsers = dbUserDao.select();
                long t2 = System.currentTimeMillis();
                AppLog.d(TAG, "查询耗时=" + (t2 - t1));

                AppLog.d(TAG, "查询数据=" + gson.toJson(dbUsers));
            }
        });
    }
}
