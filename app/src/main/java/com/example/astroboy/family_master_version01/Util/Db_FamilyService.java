package com.example.astroboy.family_master_version01.Util;

import android.content.Context;
import android.util.Log;

import com.astroboy.family.GreenDao.DaoSession;
import com.astroboy.family.GreenDao.Family;
import com.astroboy.family.GreenDao.FamilyDao;
import com.example.astroboy.family_master_version01.App;

import java.util.List;


/**
 * Created by Administrator on 2016/6/27.
 */
public class Db_FamilyService {

    private static final String TAG = Db_FamilyService.class.getSimpleName();
    private static Db_FamilyService instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private FamilyDao familyDao;


    private Db_FamilyService() {
    }

    public static Db_FamilyService getInstance(Context context) {
        if (instance == null) {
            instance = new Db_FamilyService();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = App.getDaoSession(context);
            instance.familyDao = instance.mDaoSession.getFamilyDao();
        }
        return instance;
    }


    public Family loadFamily(long id) {
        return familyDao.load(id);
    }

    public List<Family> loadAllFamily(){
        return familyDao.loadAll();
    }

    /**
     * query list with where clause
     * ex: begin_date_time >= ? AND end_date_time <= ?
     * @param where where clause, include 'where' word
     * @param params query parameters
     * @return
     */

    public List<Family> queryFamily(String where, String... params){
        return familyDao.queryRaw(where, params);
    }


    /**
     * insert or update family
     * @param family
     * @return insert or update family id
     */
    public long saveFamily(Family family){
        return familyDao.insertOrReplace(family);
    }


    /**
     * insert or update noteList use transaction
     * @param list
     */
    public void saveFamilyLists(final List<Family> list){
        if(list == null || list.isEmpty()){
            return;
        }
        familyDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    Family family = list.get(i);
                    familyDao.insertOrReplace(family);
                }
            }
        });

    }


    /**
     * delete all family
     */
    public void deleteAllFamily(){
        familyDao.deleteAll();
    }

    /**
     * delete family by id
     * @param id
     */
    public void deleteFamily(long id){
        familyDao.deleteByKey(id);
        Log.i(TAG, "delete");
    }

    public void deleteFamily(Family family){
        familyDao.delete(family);
    }

}
