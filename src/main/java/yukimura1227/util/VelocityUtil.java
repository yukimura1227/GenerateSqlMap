package yukimura1227.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import yukimura1227.generate.sql.map.util.EntityGenerater;

public class VelocityUtil {
    private static VelocityEngine engine = new VelocityEngine();
    static {
        // 基本的にエンドユーザが変更する必要が無いためpropertiesのパスは固定でOK
        Properties velocityProperties = new Properties();
        try
        (
            InputStream is = EntityGenerater.class.getResourceAsStream("/sample/config/velocity.properties");
        )
        {
            velocityProperties.load(is);

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();

        }

        engine.init(velocityProperties);

    }
    /**
     * velocityTemplateとveclotiyContextのマージを行い、結果の文字列を返却する。
     * @param template
     * @param velocityContext
     * @return
     */
    public static String templateMerge(String templatePath, VelocityContext velocityContext) {
        Template template = engine.getTemplate(templatePath);
        String mergedString = "";
        try
        (
            StringWriter writer = new StringWriter();
        )
        {
            template.merge(velocityContext, writer);
            mergedString = writer.toString();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

        return mergedString;

    }

}
