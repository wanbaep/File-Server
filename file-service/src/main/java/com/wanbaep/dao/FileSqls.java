package com.wanbaep.dao;

public class FileSqls {
    public static final String SELECT_FILE_BY_ID = "SELECT id," +
            " file_name," +
            " save_file_name," +
            " file_length," +
            " content_type," +
            " create_date," +
            " modify_date" +
            " FROM file WHERE id = :id";

    public static final String SELECT_ALL = "SELECT id," +
            " file_name," +
            " save_file_name," +
            " file_length," +
            " content_type," +
            " create_date," +
            " modify_date" +
            " FROM file ORDER BY id DESC";

    public static final String DELETE_FILE_BY_ID = "DELETE FROM file WHERE id = :id";

    public static final String UPDATE_FILE_BY_ID = "UPDATE file SET file_name = :fileName," +
            " save_file_name = :saveFileName," +
            " file_length = :fileLength," +
            " content_type = :contentType," +
            " modify_date = :modifyDate WHERE id = :id";
}
