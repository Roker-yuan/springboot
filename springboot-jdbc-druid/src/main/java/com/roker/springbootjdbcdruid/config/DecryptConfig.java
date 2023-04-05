package com.roker.springbootjdbcdruid.config;

import com.alibaba.druid.filter.config.ConfigTools;

public class DecryptConfig {
    public static void main(String[] args) throws Exception {
        // 需要加密的明文命名
        String password = "yuan@9826"; // 【注意：这里要改为你自己的密码】
        // 调用 druid 生成私钥、公钥、密文
        ConfigTools.main(new String[]{password});
    }
}
