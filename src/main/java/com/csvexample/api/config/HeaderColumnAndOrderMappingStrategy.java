package com.csvexample.api.config;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.apache.commons.collections4.comparators.NullComparator;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class HeaderColumnAndOrderMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    public HeaderColumnAndOrderMappingStrategy(Class<T> type) {
        setType(type);
    }

    @Override
    @SuppressWarnings({"rawtypes" })
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        // substituindo esse método para nos permitir preservar a caixa do nome da coluna do cabeçalho

        String[] header = super.generateHeader(bean);
        final int numColumns = headerIndex.findMaxIndex();
        if (numColumns == -1) {
            return header;
        }

        header = new String[numColumns + 1];

        BeanField beanField;
        for (int i = 0; i <= numColumns; i++) {
            beanField = findField(i);
            String columnHeaderName = extractHeaderName(beanField);
            header[i] = columnHeaderName;
        }
        return header;
    }


    @Override
    protected void loadFieldMap() throws CsvBadConverterException {
        // sobrescrevendo este método para aceitar a configuração de ordem de coluna customizada pela anotação `CsvBindByNameOrder`
        if (writeOrder == null && type.isAnnotationPresent(CsvBindByNameOrder.class)) {
            List<String> predefinedList = Arrays.stream(type.getAnnotation(CsvBindByNameOrder.class).value())
                .map(String::toUpperCase).collect(Collectors.toList());
            FixedOrderComparator<String> fixedComparator = new FixedOrderComparator<>(predefinedList);
            fixedComparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            Comparator<String> comparator = new ComparatorChain<>(Arrays.asList(
                fixedComparator,
                new NullComparator<>(false),
                new ComparableComparator<>()));
            setColumnOrderOnWrite(comparator);
        }
        super.loadFieldMap();
    }

    @SuppressWarnings({"rawtypes"})
    private String extractHeaderName(final BeanField beanField) {
        if (beanField == null || beanField.getField() == null
                || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
            return StringUtils.EMPTY;
        }

        if (beanField.getField().isAnnotationPresent(CsvBindByName.class)) {
            return beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class)[0].column();
        } else if (beanField.getField().isAnnotationPresent(CsvCustomBindByName.class)) {
            return beanField.getField().getDeclaredAnnotationsByType(CsvCustomBindByName.class)[0].column();
        }
        return StringUtils.EMPTY;

    }
}
