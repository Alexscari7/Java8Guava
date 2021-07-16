package headfirst.strategy_pattern;

import java.util.HashMap;

/**
 * N
 *
 * @author wusd
 * @date : 2021/07/15 15:31
 */
public class PromotionStrategyPattern {
    public static void main(String[] args) {
        PromotionStrategy strategy = PromotionStrategyFactory.getStrategy("WEIXIN");
        strategy.doPromotion();
    }
}

interface PromotionStrategy {
    void doPromotion();
}

class ZfbPromotionStrategy implements PromotionStrategy {
    @Override
    public void doPromotion() {
        System.out.println("支付宝促销折扣");
    }
}

class WxPromotionStrategy implements PromotionStrategy {
    @Override
    public void doPromotion() {
        System.out.println("微信促销折扣");
    }
}

class PddPromotionStrategy implements PromotionStrategy {
    @Override
    public void doPromotion() {
        System.out.println("拼多多折扣");
    }
}

class EmptyStrategy implements PromotionStrategy {
    @Override
    public void doPromotion() {
        System.out.println("无折扣");
    }
}

enum PromotionEnum {
    ZHIFUBAO,
    WEIXIN,
    PDD
}

class PromotionStrategyFactory {
    private static HashMap<PromotionEnum, PromotionStrategy> strategyMap;

    public static final PromotionStrategy EMPTYPROMOTION = new EmptyStrategy();

    static {
        strategyMap = new HashMap<>();
        strategyMap.put(PromotionEnum.ZHIFUBAO, new ZfbPromotionStrategy());
        strategyMap.put(PromotionEnum.WEIXIN, new WxPromotionStrategy());
        strategyMap.put(PromotionEnum.PDD, new PddPromotionStrategy());
    }

    public static void addStrategy(PromotionEnum promotionEnum, PromotionStrategy promotionStrategy) {
        strategyMap.put(promotionEnum, promotionStrategy);
    }

    public static PromotionStrategy getStrategy(String strategyName) {
        PromotionEnum promotionEnum;
        try {
            promotionEnum = Enum.valueOf(PromotionEnum.class, strategyName);
        } catch (Exception e) {
            return EMPTYPROMOTION;
        }
        return strategyMap.get(promotionEnum);
    }
}

