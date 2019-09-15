
package com.fengjx.grpc.common.springboot.config;

import com.fengjx.grpc.common.config.ZkConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fengjianxin
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "grpc.discovery.zk")
public class DiscovertZkProperties extends ZkConfiguration {


}
