package com.mik1ng.chat.greendao.manager;

import android.content.Context;

import com.mik1ng.chat.greendao.ChatRecordEntityDao;
import com.mik1ng.chat.greendao.entity.ChatRecordEntity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DBOperate {
    private static final String TAG = DBOperate.class.getSimpleName();
    private DaoManager mManager;

    public DBOperate(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成IdentificationResultsEntry记录的插入，如果表未创建，先创建IdentificationResultsEntry表
     *
     * @param ident
     * @return
     */
    public boolean insertIdent(ChatRecordEntity ident, Class<?> clz, Object o) {
        boolean flag = false;
        flag = mManager.getDaoSession().getChatRecordEntityDao().insert(ident) == -1 ? false : true;
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     *
     * @param identList
     * @return
     */
    public boolean insertMultIdent(final List<ChatRecordEntity> identList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (ChatRecordEntity ident : identList) {
                        mManager.getDaoSession().insertOrReplace(ident);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     *
     * @param ident
     * @return
     */
    public boolean updateIdent(ChatRecordEntity ident) {
        boolean flag = false;
        try {
            mManager.getDaoSession().update(ident);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @param ident
     * @return
     */
    public boolean deleteIdent(ChatRecordEntity ident) {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(ident);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(ChatRecordEntity.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<ChatRecordEntity> queryAllIdent() {
        return mManager.getDaoSession().loadAll(ChatRecordEntity.class);
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public ChatRecordEntity queryIdentById(long key) {
        return mManager.getDaoSession().load(ChatRecordEntity.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<ChatRecordEntity> queryIdentByNativeSql(String sql, String[] conditions) {
        return mManager.getDaoSession().queryRaw(ChatRecordEntity.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<ChatRecordEntity> queryIdentByQueryBuilder(long id) {
        QueryBuilder<ChatRecordEntity> queryBuilder = mManager.getDaoSession().queryBuilder(ChatRecordEntity.class);
        return queryBuilder.where(ChatRecordEntityDao.Properties.Password.eq(id)).list();
    }
}
