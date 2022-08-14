package cn.xxstudy.quickflutter.check;

import java.util.ArrayList;
import java.util.List;

/**
 * @date: 2022/8/2 20:48
 * @author: Sensi
 * @remark:
 */
public interface ICheck {
    /**
     * 校验方法
     * @param path 待检查路径
     * @return 校验结果，true 为 通过
     */
    CheckResult check(String path);

    class CheckResult {
        public boolean isOk = false;
        public final List<String> missingFiles = new ArrayList<>();
    }
}
