package com.github.rpc.core.load;

import com.github.rpc.core.ZkDiscover;

import java.util.*;

/**
 * @author JiHongYuan
 * @date 2022/2/7 16:50
 */
public class RandomLoadBalanceDiscover extends AbstractLoadBalanceDiscover {
    private final Random random = new Random();

    public RandomLoadBalanceDiscover(ZkDiscover discover) {
        super(discover);
    }

    @Override
    protected List<String> doSelect(List<String> services) {
        return Collections.singletonList(services.get(random.nextInt(services.size())));
    }

}
