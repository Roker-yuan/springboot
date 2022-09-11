package com.roker.springbootminio.domain;

import lombok.Data;

@Data
public class ObjectItem {
    private String objectName;
    private Long size;
}