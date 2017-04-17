package com.xcommon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ZhangTian on 2016/5/26.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "photostars";
    private static final int DATABASE_VERSION = 6;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_owntopic" +
                "(_id INTEGER,userID INTEGER,socialID INTEGER ,socialName VERCHAR,level INTEGER,iconUrl VERCHAR,properieter INTEGER,modifyTime LONG,hide INTEGER,uploadStatus INTEGER,upStatus INTEGER,Primary key(userID,socialID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_topicHeader" +
                "(_id INTEGER,socialID integer PRIMARY KEY AUTOINCREMENT, backImage varchar(50), headIcon varchar(50), introduction text, socialName varchar(10), careNum int, number int, userName varchar(15), timeNum integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_themeSql" +
                "(_id INTEGER,socialID integer, themeID integer, userName varchar(15), iconUrl varchar(50), commentID integer , commentNum integer, supportNum integer, contentImageUrl varchar(50), contentText text, editImageStatus int, timeNum Long, userID integer, supportStatus int,Primary key(socialID,themeID,commentID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_support" +
                "(_id INTEGER,userID integer, socialID integer, themeID integer, commentID integer, authorID integer, imageName varchar(20), replyText text,timeNum long, uploadStatus int, status int,Primary key(userID,socialID,themeID,commentID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_mycomment" +
                "(_id INTEGER,type integer,muserID integer ,userID integer,userName varchar(15),avatPath varchar(50),socialID integer,socialName varchar(20),themeID integer,ptID integer,prID integer,file varchar(50),text text,tgfl varchar(50),tgtx text,time long,status integer,url varchar(50),icon varchar(50),Primary key(muserID,userID,socialID,themeID,ptID,prID,type))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_downloadImage" +
                "(_id INTEGER,status int,file varchar(50)  PRIMARY KEY )");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_uploadImage" +
                "(_id INTEGER,status int,file varchar(50)  PRIMARY KEY )");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_searchhistory" +
                "(_id INTEGER,time Long,name varchar(50)  PRIMARY KEY )");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_mycollect" +
                "(_id INTEGER,mID integer ,userID integer,userName varchar(15),socialID integer,socialName varchar(20),themeID integer,file varchar(50),text text,time long,url varchar(50),icon varchar(50),Primary key(mID,socialID,themeID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_templatelist" +
                "(_id INTEGER, status  integer,  etid  integer  Primary key, file  varchar(50))");

    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_owntopic" +
                "(_id INTEGER,userID INTEGER,socialID INTEGER ,socialName VERCHAR,level INTEGER,iconUrl VERCHAR,properieter INTEGER,modifyTime LONG,hide INTEGER,uploadStatus INTEGER,upStatus INTEGER,Primary key(userID,socialID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_topicHeader" +
                "(_id INTEGER,socialID integer PRIMARY KEY AUTOINCREMENT, backImage varchar(50), headIcon varchar(50), introduction text, socialName varchar(10), careNum int, number int, userName varchar(15), timeNum integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_themeSql" +
                "(_id INTEGER,socialID integer, themeID integer, userName varchar(15), iconUrl varchar(50), commentID integer , commentNum integer, supportNum integer, contentImageUrl varchar(50), contentText text, editImageStatus int, timeNum Long, userID integer, supportStatus int,Primary key(socialID,themeID,commentID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_support" +
                "(_id INTEGER,userID integer, socialID integer, themeID integer, commentID integer, authorID integer, imageName varchar(20), replyText text,timeNum long, uploadStatus int, status int,Primary key(userID,socialID,themeID,commentID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_mycomment" +
                "(_id INTEGER,type integer,muserID integer ,userID integer,userName varchar(15),avatPath varchar(50),socialID integer,socialName varchar(20),themeID integer,ptID integer,prID integer,file varchar(50),text text,tgfl varchar(50),tgtx text,time long,status integer,url varchar(50),icon varchar(50),Primary key(muserID,userID,socialID,themeID,ptID,prID,type))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_downloadImage" +
                "(_id INTEGER,status int,file varchar(50)  PRIMARY KEY )");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_uploadImage" +
                "(_id INTEGER,status int,file varchar(50)  PRIMARY KEY )");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_searchhistory" +
                "(_id INTEGER,time Long,name varchar(50)  PRIMARY KEY )");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_mycollect" +
                "(_id INTEGER,mID integer ,userID integer,userName varchar(15),socialID integer,socialName varchar(20),themeID integer,file varchar(50),text text,time long,url varchar(50),icon varchar(50),Primary key(mID,socialID,themeID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_templatelist" +
                "(_id INTEGER, status  integer,  etid  integer  Primary key, file  varchar(50))");

//        db.execSQL("ALTER TABLE tbl_themeUrl ADD COLUMN other STRING");

    }
}