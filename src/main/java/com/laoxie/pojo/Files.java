package com.laoxie.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Files {
    private Integer id;
    private String filename;
    private String filecontent;
    private String owner;
    private Integer isprivate;
    private Date createtime;
}
