package cn.xxstudy.quickflutter.generator;

import cn.xxstudy.quickflutter.services.StorageService;
import cn.xxstudy.quickflutter.utils.Utils;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @date: 2022/7/31 00:08
 * @author: Sensi
 * @remark:
 */
public class DartGenerator {
    private String name;
    private boolean createStateful;

    private String templateString;
    private Map<String, String> templateMap = new HashMap<>();


    public DartGenerator(Project project, String name, boolean createStateful) {
        this.name = name;
        this.createStateful = createStateful;
        templateMap.put("className", className());
        templateMap.put("fileName", fileName());
        templateMap.put("date_time", Utils.createCurrentTime());
        templateMap.put("user_name", StorageService.getInstance(project).getState().userName);
        init();
    }

    private void init() {
        try {
            String fileName = (createStateful) ? "dart_stateful.dart.template" : "dart_stateless.dart.template";
            String resource = "/templates/" + fileName;

            InputStream resourceAsStream = DartGenerator.class.getResourceAsStream(resource);
            templateString = CharStreams.toString(new InputStreamReader(resourceAsStream, Charsets.UTF_8));
        } catch (Exception e) {
        }
    }

    public String generate() {
        return new StrSubstitutor(templateMap).replace(templateString);
    }

    public String fileName() {
        return String.format("%s.dart", name);
    }

    private String className() {
        return Utils.underlineToHump(name);
    }

}
