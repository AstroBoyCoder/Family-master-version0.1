package com.example.astroboy.family_master_version01.Util;

import android.content.Context;
import android.util.Log;

import com.astroboy.family.GreenDao.DaoSession;
import com.astroboy.family.GreenDao.User;
import com.astroboy.family.GreenDao.UserDao;
import com.example.astroboy.family_master_version01.App;

import java.util.List;


/**
 * Created by Administrator on 2016/6/27.
 */
public class Db_UserService {

    private static final String TAG = Db_UserService.class.getSimpleName();
    private static Db_UserService instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private UserDao userDao;


    private Db_UserService() {
    }

    public static Db_UserService getInstance(Context context) {
        if (instance == null) {
            instance = new Db_UserService();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = App.getDaoSession(context);
            instance.userDao = instance.mDaoSession.getUserDao();
        }
        return instance;
    }


    public User loadUser(long id) {
        return userDao.load(id);
    }

    public List<User> loadAllUser(){
        return userDao.loadAll();
    }

    /**
     * query list with where clause
     * ex: begin_date_time >= ? AND end_date_time <= ?
     * @param where where clause, include 'where' word
     * @param params query parameters
     * @return
     */

    public List<User> queryUser(String where, String... params){
        return userDao.queryRaw(where, params);
    }


    /**
     * insert or update user
     * @param user
     * @return insert or update user id
     */
    public long saveUser(User user){
        return userDao.insertOrReplace(user);
    }


    /**
     * insert or update noteList use transaction
     * @param list
     */
    public void saveUserLists(final List<User> list){
        if(list == null || list.isEmpty()){
            return;
        }
        userDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    User user = list.get(i);
                    userDao.insertOrReplace(user);
                }
            }
        });

    }


    /**
     * delete all user
     */
    public void deleteAllUser(){
        userDao.deleteAll();
    }

    /**
     * delete user by id
     * @param id
     */
    public void deleteUser(long id){
        userDao.deleteByKey(id);
        Log.i(TAG, "delete");
    }

    public void deleteUser(User user){
        userDao.delete(user);
    }

}
