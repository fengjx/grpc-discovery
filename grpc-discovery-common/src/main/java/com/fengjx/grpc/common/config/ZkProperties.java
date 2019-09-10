package com.fengjx.grpc.common.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fengjianxin
 */
@Getter
@Setter
public class ZkProperties {

    private String connectString;
    private String namespace;

}
