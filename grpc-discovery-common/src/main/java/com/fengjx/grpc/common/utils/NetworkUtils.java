package com.fengjx.grpc.common.utils;


import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Set;

import static cn.hutool.core.net.NetUtil.LOCAL_IP;


/**
 * @author fengjianxin
 */
public class NetworkUtils {


    public static String getLocalInnerIp() {
        Set<String> ips = NetUtil.localIpv4s();
        for (String ip : ips) {
            if (NetUtil.isInnerIP(ip) && !ip.equals(LOCAL_IP)) {
                return ip;
            }
        }
        return ips.iterator().next();
    }


    /**
     * 查找1024~65535范围内的可用端口<br>
     * 此方法只检测给定范围内的随机一个端口，检测65535-1024次<br>
     * 来自org.springframework.util.SocketUtils
     *
     * @return 可用的端口
     * @since 4.5.4
     */
    public static int getUsableLocalPort() {
        return getUsableLocalPort(NetUtil.PORT_RANGE_MIN);
    }

    /**
     * 查找指定范围内的可用端口，最大值为65535<br>
     * 此方法只检测给定范围内的随机一个端口，检测65535-minPort次<br>
     * 来自org.springframework.util.SocketUtils
     *
     * @param minPort 端口最小值（包含）
     * @return 可用的端口
     * @since 4.5.4
     */
    public static int getUsableLocalPort(int minPort) {
        return getUsableLocalPort(minPort, NetUtil.PORT_RANGE_MAX);
    }

    /**
     * 查找指定范围内的可用端口<br>
     * 此方法只检测给定范围内的随机一个端口，检测maxPort-minPort次<br>
     * 来自org.springframework.util.SocketUtils
     *
     * @param minPort 端口最小值（包含）
     * @param maxPort 端口最大值（包含）
     * @return 可用的端口
     * @since 4.5.4
     */
    public static int getUsableLocalPort(int minPort, int maxPort) {
        for (int i = minPort; i <= maxPort; i++) {
            int candidatePort = RandomUtil.randomInt(minPort, maxPort + 1);
            if (NetUtil.isUsableLocalPort(candidatePort)) {
                return candidatePort;
            }
        }
        throw new UtilException("Could not find an available port in the range [{}, {}] after {} attempts", minPort, maxPort, maxPort - minPort);
    }

}
