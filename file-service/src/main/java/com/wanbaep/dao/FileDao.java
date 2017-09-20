package com.wanbaep.dao;

import com.wanbaep.domain.FileDomain;
import com.wanbaep.utils.JdbcEmptyResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FileDao {

    private NamedParameterJdbcTemplate jdbc;
    private RowMapper<FileDomain> fileDomainRowMapper;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public FileDao(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.fileDomainRowMapper = BeanPropertyRowMapper.newInstance(FileDomain.class);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .usingGeneratedKeyColumns("id")
                .withTableName("file");
    }

    public FileDomain saveFile(FileDomain fileDomain) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(fileDomain);
        Long fileId = jdbcInsert.executeAndReturnKey(params).longValue();
        return this.selectFileById(fileId);
    }

    public FileDomain selectFileById(Long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        try {
            return this.jdbc.queryForObject(FileSqls.SELECT_FILE_BY_ID, params, fileDomainRowMapper);
        } catch (EmptyResultDataAccessException exception) {
            throw new JdbcEmptyResultException(exception, "File Not Found Exception", id);
        }
    }

    public List<FileDomain> selectAll() {
        return this.jdbc.query(FileSqls.SELECT_ALL, fileDomainRowMapper);
    }

    public Long deleteFileById(Long id) {
        Map<String, Object> params = Collections.singletonMap("id",id);
        Integer fileId = this.jdbc.update(FileSqls.DELETE_FILE_BY_ID, params);
        return fileId.longValue();
    }

    public Long updateFileById(FileDomain fileDomain) {
        Map<String, Object> params = new HashMap<>();
        params.put("fileName", fileDomain.getFileName());
        params.put("saveFileName", fileDomain.getSaveFileName());
        params.put("fileLength", fileDomain.getFileLength());
        params.put("contentType", fileDomain.getContentType());
        params.put("modifyDate", fileDomain.getModifyDate());
        params.put("id", fileDomain.getId());

        Integer fileId = this.jdbc.update(FileSqls.UPDATE_FILE_BY_ID, params);
        return fileId.longValue();
    }
}
