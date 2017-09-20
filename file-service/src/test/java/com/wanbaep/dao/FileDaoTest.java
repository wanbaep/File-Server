package com.wanbaep.dao;

import com.wanbaep.config.RootApplicationContextConfig;
import com.wanbaep.domain.FileDomain;
import com.wanbaep.utils.JdbcEmptyResultException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootApplicationContextConfig.class)
@WebAppConfiguration
@Transactional
public class FileDaoTest {
    @Autowired
    private FileDao fileDao;

    private FileDomain getFileDomain() {
        FileDomain fileDomain = new FileDomain();
        fileDomain.setFileName("testFile");
        fileDomain.setSaveFileName("saveFileName");
        fileDomain.setFileLength(100L);
        fileDomain.setContentType("image/jpg");
        return fileDomain;
    }

    @Test
    public void shouldInsertAndSelectFileDomain() {
        FileDomain fileDomain = getFileDomain();
        FileDomain created = fileDao.saveFile(fileDomain);

        FileDomain selected = fileDao.selectFileById(created.getId());
        System.out.println(selected.toString());
        assertThat(selected.getId(),is(created.getId()));
        assertThat(selected.getFileName(), is("testFile"));
    }

    @Test
    public void shouldInsertAndSelectFileDomainException() {
        FileDomain fileDomain = getFileDomain();
        FileDomain created = fileDao.saveFile(fileDomain);

        FileDomain selected;
        try {
            selected = fileDao.selectFileById(created.getId() + 1);
            assertThat(selected.getId(),is(created.getId()));
            assertThat(selected.getFileName(), is("testFile"));
        } catch (JdbcEmptyResultException exception) {
            assertThat(exception.getMessage(), is("File Not Found Exception"));
        }
    }

    @Test
    public void shouldInsertAndSelectAll() {
        int iteratorNum = 2;
        for(int i = 0; i < iteratorNum; i++) {
            fileDao.saveFile(getFileDomain());
        }

        List<FileDomain> fileDomains = fileDao.selectAll();
        assertThat((fileDomains.size() >= iteratorNum), is(true));
    }

}