package com.wanbaep.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanbaep.config.RootApplicationContextConfig;
import com.wanbaep.domain.FileDomain;
import com.wanbaep.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootApplicationContextConfig.class)
@WebAppConfiguration
@Transactional
public class FileControllerTest {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    FileService fileService;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Test
    public void createFile() throws Exception {
        String content = "Test file content";

        ResultActions result = mockMvc.perform(fileUpload("/files")
                        .file("file",content.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON));

        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.fileLength",is(content.length())));
    }

    @Test
    public void getFiles() throws Exception {
        ResultActions result = mockMvc.perform(get("/files"));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void shouldInsertAndDelete() throws Exception {
        String content = "Test file content";

        ResultActions result = mockMvc.perform(fileUpload("/files")
                .file("file",content.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));

        MockHttpServletResponse response =  result.andReturn().getResponse();
        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.fileLength",is(content.length())));

        FileDomain created = null;
        try {
            created = new ObjectMapper().readValue(response.getContentAsString(), FileDomain.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        Long fileId = created.getId();

        result = mockMvc.perform(delete("/files/" + fileId));

        result.andDo(print());
        result.andExpect(status().isNoContent());
    }

    @Test
    public void shouldInsertAndUpdate() throws Exception {
        String beforeContent = "Test file content";

        ResultActions result = mockMvc.perform(fileUpload("/files")
                .file("file",beforeContent.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));

        MockHttpServletResponse response =  result.andReturn().getResponse();
        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.fileLength",is(beforeContent.length())));

        FileDomain created = null;
        try {
            created = new ObjectMapper().readValue(response.getContentAsString(), FileDomain.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        Long fileId = created.getId();

        String afterContent = "Update file data";
        result = mockMvc.perform(fileUpload("/files")
                .file("file",afterContent.getBytes())
                .param("update", "true")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.fileLength",is(afterContent.length())));

        response = result.andReturn().getResponse();
        FileDomain updated = null;
        try {
            updated = new ObjectMapper().readValue(response.getContentAsString(), FileDomain.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        result = mockMvc.perform(put("/files/"+ fileId)
                .content(new ObjectMapper().writeValueAsString(updated))
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.fileLength",not(beforeContent.length())));

    }
}