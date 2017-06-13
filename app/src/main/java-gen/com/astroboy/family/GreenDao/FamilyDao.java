package com.astroboy.family.GreenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.astroboy.family.GreenDao.Family;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FAMILY.
*/
public class FamilyDao extends AbstractDao<Family, Long> {

    public static final String TABLENAME = "FAMILY";

    /**
     * Properties of entity Family.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FM_ID = new Property(1, int.class, "FM_ID", false, "FM__ID");
        public final static Property User_ID = new Property(2, int.class, "User_ID", false, "USER__ID");
        public final static Property FM_Name = new Property(3, String.class, "FM_Name", false, "FM__NAME");
        public final static Property FM_Address = new Property(4, String.class, "FM_Address", false, "FM__ADDRESS");
        public final static Property Create_Time = new Property(5, String.class, "Create_Time", false, "CREATE__TIME");
        public final static Property Reserved_Tel = new Property(6, String.class, "Reserved_Tel", false, "RESERVED__TEL");
        public final static Property Member_Type = new Property(7, Integer.class, "Member_Type", false, "MEMBER__TYPE");
        public final static Property Member_ID = new Property(8, Integer.class, "Member_ID", false, "MEMBER__ID");
    };


    public FamilyDao(DaoConfig config) {
        super(config);
    }
    
    public FamilyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FAMILY' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'FM__ID' INTEGER NOT NULL ," + // 1: FM_ID
                "'USER__ID' INTEGER NOT NULL ," + // 2: User_ID
                "'FM__NAME' TEXT," + // 3: FM_Name
                "'FM__ADDRESS' TEXT," + // 4: FM_Address
                "'CREATE__TIME' TEXT," + // 5: Create_Time
                "'RESERVED__TEL' TEXT," + // 6: Reserved_Tel
                "'MEMBER__TYPE' INTEGER," + // 7: Member_Type
                "'MEMBER__ID' INTEGER);"); // 8: Member_ID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FAMILY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Family entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getFM_ID());
        stmt.bindLong(3, entity.getUser_ID());
 
        String FM_Name = entity.getFM_Name();
        if (FM_Name != null) {
            stmt.bindString(4, FM_Name);
        }
 
        String FM_Address = entity.getFM_Address();
        if (FM_Address != null) {
            stmt.bindString(5, FM_Address);
        }
 
        String Create_Time = entity.getCreate_Time();
        if (Create_Time != null) {
            stmt.bindString(6, Create_Time);
        }
 
        String Reserved_Tel = entity.getReserved_Tel();
        if (Reserved_Tel != null) {
            stmt.bindString(7, Reserved_Tel);
        }
 
        Integer Member_Type = entity.getMember_Type();
        if (Member_Type != null) {
            stmt.bindLong(8, Member_Type);
        }
 
        Integer Member_ID = entity.getMember_ID();
        if (Member_ID != null) {
            stmt.bindLong(9, Member_ID);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Family readEntity(Cursor cursor, int offset) {
        Family entity = new Family( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // FM_ID
            cursor.getInt(offset + 2), // User_ID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // FM_Name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // FM_Address
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Create_Time
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Reserved_Tel
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // Member_Type
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8) // Member_ID
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Family entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFM_ID(cursor.getInt(offset + 1));
        entity.setUser_ID(cursor.getInt(offset + 2));
        entity.setFM_Name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFM_Address(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCreate_Time(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setReserved_Tel(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMember_Type(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setMember_ID(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Family entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Family entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
