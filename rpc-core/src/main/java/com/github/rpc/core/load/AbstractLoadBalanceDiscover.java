package com.github.rpc.core.load;

import com.github.rpc.core.IDiscover;
import com.github.rpc.core.ZkDiscover;

import java.util.List;

/**
 * @author JiHongYuan
 * @date 2022/2/7 16:47
 */
public abstract class AbstractLoadBalanceDiscover implements IDiscover {

    private final ZkDiscover discover;

    protected AbstractLoadBalanceDiscover(ZkDiscover discover) {
        this.discover = discover;
    }

    @Override
    public List<String> discover(String serviceName) {
        List<String> services = this.discover.discover(serviceName);
        return doSelect(services);
    }

    /**
     * select service
     *
     * @param services service list
     * @return list
     */
    protected abstract List<String> doSelect(List<String> services);

}
