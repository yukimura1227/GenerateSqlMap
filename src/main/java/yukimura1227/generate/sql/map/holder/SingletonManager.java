package yukimura1227.generate.sql.map.holder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * Singletonのインスタンス生成をspringにまかせるためのクラス。
 * singleton実装のクラスを、本クラスで一元管理する。
 * @author yukimura1227
 *
 */
public class SingletonManager {
    /**
     * ユーザーからの生成は不可。（Spring管理の想定）
     */
    private SingletonManager() {}
    private static PropertyHolder propertyHolder;

    @Autowired
    public void setPropertyHolder(PropertyHolder propertyHolder) {
        SingletonManager.propertyHolder = propertyHolder;
    }

    public static PropertyHolder getPropertyHolder() {
        return propertyHolder;
    }

}
