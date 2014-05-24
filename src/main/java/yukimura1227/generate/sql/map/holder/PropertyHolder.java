package yukimura1227.generate.sql.map.holder;


import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
/**
 *
 * @author yukimura1227
 *
 */
@Getter
public class PropertyHolder {
    /**
     * ユーザからのインスタンス生成不可
     */
    private PropertyHolder() {};


    @Value("${inputpath}")
    private String inputPath;
    @Value("${input_yaml_path}")
    private String inputYamlPath;
    @Value("${outputpath}")
    private String outputPath;
    @Value("${generate.package.base}")
    private String packageBase;
    @Value("${generate.package.entity}")
    private String packageEntity;
    @Value("${generate.package.mapper}")
    private String packageMapper;
    @Value("${entity_template_path}")
    private String entityTemplatePath;
    @Value("${entity_interface_template_path}")
    private String entityInterfaceTemplatePath;
    @Value("${mappler_xml_template_path}")
    private String mapplerXmlTemplatePath;
    @Value("${common_mapper_xml_template}")
    private String commonXmlMaplerTemplate;
    @Value("${common_select_mapper_xml_template}")
    private String commonSelectMapperXmlTemplate;

}
