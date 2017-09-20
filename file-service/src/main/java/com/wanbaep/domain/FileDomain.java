package com.wanbaep.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileDomain {
    private Long id;
    private String fileName;
    private String saveFileName;
    private Long fileLength;
    private String contentType;
    private Date createDate;
    private Date modifyDate;
}
