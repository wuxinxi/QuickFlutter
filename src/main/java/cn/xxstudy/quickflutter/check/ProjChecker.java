package cn.xxstudy.quickflutter.check;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @date: 2022/8/2 20:49
 * @author: Sensi
 * @remark:
 */
public class ProjChecker implements ICheck {

    private final ArrayList<String> checkFiles;

    {
        checkFiles = new ArrayList<>();
        checkFiles.add("lib");
        checkFiles.add(".metadata");
        checkFiles.add(".packages");
        checkFiles.add("pubspec.lock");
        checkFiles.add("pubspec.yaml");
    }

    @Override
    public CheckResult check(String path) {
        CheckResult result = new CheckResult();
        if (path == null || path.isEmpty()) {
            return result;
        }
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return result;
        }
        String[] files = Objects.requireNonNull(dir.list());
        int cnt = 0;
        ArrayList<String> missingFiles = new ArrayList<>(checkFiles);
        for (String f : files) {
            if (checkFiles.contains(f)) {
                cnt++;
                missingFiles.remove(f);
            }
        }
        result.isOk = cnt == checkFiles.size();
        result.missingFiles.addAll(missingFiles);
        return result;
    }
}
