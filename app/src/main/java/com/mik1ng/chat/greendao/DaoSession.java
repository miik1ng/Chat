package com.mik1ng.chat.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.mik1ng.chat.greendao.entity.ChatRecordEntity;
import com.mik1ng.chat.greendao.entity.MessageEntity;

import com.mik1ng.chat.greendao.ChatRecordEntityDao;
import com.mik1ng.chat.greendao.MessageEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatRecordEntityDaoConfig;
    private final DaoConfig messageEntityDaoConfig;

    private final ChatRecordEntityDao chatRecordEntityDao;
    private final MessageEntityDao messageEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatRecordEntityDaoConfig = daoConfigMap.get(ChatRecordEntityDao.class).clone();
        chatRecordEntityDaoConfig.initIdentityScope(type);

        messageEntityDaoConfig = daoConfigMap.get(MessageEntityDao.class).clone();
        messageEntityDaoConfig.initIdentityScope(type);

        chatRecordEntityDao = new ChatRecordEntityDao(chatRecordEntityDaoConfig, this);
        messageEntityDao = new MessageEntityDao(messageEntityDaoConfig, this);

        registerDao(ChatRecordEntity.class, chatRecordEntityDao);
        registerDao(MessageEntity.class, messageEntityDao);
    }
    
    public void clear() {
        chatRecordEntityDaoConfig.clearIdentityScope();
        messageEntityDaoConfig.clearIdentityScope();
    }

    public ChatRecordEntityDao getChatRecordEntityDao() {
        return chatRecordEntityDao;
    }

    public MessageEntityDao getMessageEntityDao() {
        return messageEntityDao;
    }

}
