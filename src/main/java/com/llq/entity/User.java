package com.llq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 李林麒
 * @date 2023/1/15 23:23
 * @Description 用户类
 */
@Data
@EqualsAndHashCode(of = "username")
@AllArgsConstructor
public class User implements Serializable {
    String username;

}
