package com.diboot.core.binding.parser;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.diboot.core.util.BeanUtils;
import com.diboot.core.util.S;
import com.diboot.core.util.V;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean相关信息缓存
 *
 * @author JerryMa
 * @version v2.2.1
 * @date 2021/4/20
 * Copyright © diboot.com
 */
@Getter
@Setter
public class PropInfo implements Serializable {
    private static final long serialVersionUID = 5921667308129991326L;

    private String idColumn;

    private String deletedColumn;

    /**
     * 列集合
     */
    private List<String> columns;
    /**
     * 字段-列的映射
     */
    private Map<String, String> fieldToColumnMap;
    /**
     * 列-字段的映射
     */
    private Map<String, String> columnToFieldMap;

    /**
     * 初始化
     *
     * @param beanClass
     */
    public PropInfo(Class<?> beanClass) {
        // 初始化字段-列名的映射
        this.fieldToColumnMap = new HashMap<>();
        this.columnToFieldMap = new HashMap<>();
        this.columns = new ArrayList<>();
        List<Field> fields = BeanUtils.extractAllFields(beanClass);
        if (V.notEmpty(fields)) {
            for (Field fld : fields) {
                String fldName = fld.getName();
                String columnName = null;
                TableField tableField = fld.getAnnotation(TableField.class);
                if (tableField != null) {
                    if (tableField.exist() == false) {
                        columnName = null;
                    } else {
                        if (V.notEmpty(tableField.value())) {
                            columnName = tableField.value();
                        } else {
                            columnName = S.toSnakeCase(fldName);
                        }
                    }
                }
                // 主键
                TableId tableId = fld.getAnnotation(TableId.class);
                if (tableId != null && this.idColumn == null) {
                    if (V.notEmpty(tableId.value())) {
                        columnName = tableId.value();
                    } else if (columnName == null) {
                        columnName = S.toSnakeCase(fldName);
                    }
                    this.idColumn = columnName;
                } else {
                    TableLogic tableLogic = fld.getAnnotation(TableLogic.class);
                    if (tableLogic != null) {
                        if (V.notEmpty(tableLogic.value())) {
                            columnName = tableLogic.value();
                        } else if (columnName == null) {
                            columnName = S.toSnakeCase(fldName);
                        }
                        this.deletedColumn = columnName;
                    }
                }
                this.fieldToColumnMap.put(fldName, columnName);
                if (V.notEmpty(columnName)) {
                    this.columnToFieldMap.put(columnName, fldName);
                    this.columns.add(columnName);
                }
            }
        }

    }

}
