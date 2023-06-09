package com.mik1ng.chat.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mik1ng.chat.greendao.entity.MessageEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE_ENTITY".
*/
public class MessageEntityDao extends AbstractDao<MessageEntity, Long> {

    public static final String TABLENAME = "MESSAGE_ENTITY";

    /**
     * Properties of entity MessageEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MyID = new Property(1, int.class, "myID", false, "MY_ID");
        public final static Property UserID = new Property(2, int.class, "userID", false, "USER_ID");
        public final static Property Avatar = new Property(3, String.class, "avatar", false, "AVATAR");
        public final static Property Name = new Property(4, String.class, "name", false, "NAME");
        public final static Property Date = new Property(5, String.class, "date", false, "DATE");
        public final static Property Content = new Property(6, String.class, "content", false, "CONTENT");
        public final static Property Count = new Property(7, int.class, "count", false, "COUNT");
        public final static Property Timestamp = new Property(8, long.class, "timestamp", false, "TIMESTAMP");
    }


    public MessageEntityDao(DaoConfig config) {
        super(config);
    }
    
    public MessageEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"MY_ID\" INTEGER NOT NULL ," + // 1: myID
                "\"USER_ID\" INTEGER NOT NULL ," + // 2: userID
                "\"AVATAR\" TEXT," + // 3: avatar
                "\"NAME\" TEXT," + // 4: name
                "\"DATE\" TEXT," + // 5: date
                "\"CONTENT\" TEXT," + // 6: content
                "\"COUNT\" INTEGER NOT NULL ," + // 7: count
                "\"TIMESTAMP\" INTEGER NOT NULL );"); // 8: timestamp
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MessageEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getMyID());
        stmt.bindLong(3, entity.getUserID());
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(4, avatar);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(6, date);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(7, content);
        }
        stmt.bindLong(8, entity.getCount());
        stmt.bindLong(9, entity.getTimestamp());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MessageEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getMyID());
        stmt.bindLong(3, entity.getUserID());
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(4, avatar);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(6, date);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(7, content);
        }
        stmt.bindLong(8, entity.getCount());
        stmt.bindLong(9, entity.getTimestamp());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MessageEntity readEntity(Cursor cursor, int offset) {
        MessageEntity entity = new MessageEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // myID
            cursor.getInt(offset + 2), // userID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // avatar
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // date
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // content
            cursor.getInt(offset + 7), // count
            cursor.getLong(offset + 8) // timestamp
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MessageEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMyID(cursor.getInt(offset + 1));
        entity.setUserID(cursor.getInt(offset + 2));
        entity.setAvatar(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDate(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setContent(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCount(cursor.getInt(offset + 7));
        entity.setTimestamp(cursor.getLong(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MessageEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MessageEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MessageEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
