package com.novacloud.data.datacleaner.extra;

import org.eobjects.analyzer.data.InputColumn;
import org.eobjects.analyzer.data.InputRow;
import org.eobjects.analyzer.data.MetaModelInputColumn;
import org.eobjects.analyzer.data.MockInputRow;
import org.eobjects.analyzer.storage.AbstractRowAnnotationFactory;
import org.eobjects.analyzer.storage.RowAnnotation;
import org.eobjects.analyzer.storage.RowAnnotationFactory;
import org.eobjects.analyzer.storage.RowAnnotationImpl;
import org.eobjects.analyzer.util.ImmutableEntry;
import org.eobjects.analyzer.util.ReflectionUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filter all source column ,map column( columnfamily column) and  byte[] column.
 * Created by yong on 11/20/14.
 */
public class NovaInMemoryRowAnnotationFactory extends AbstractRowAnnotationFactory implements RowAnnotationFactory {
    private static final long serialVersionUID = 1L;
    // contains annotations, mapped to row-ids
    private final Map<RowAnnotation, Set<Integer>> _annotatedRows = new ConcurrentHashMap<RowAnnotation, Set<Integer>>();

    // contains row id's mapped to rows mapped to distinct counts
    private final Map<Integer, Map.Entry<InputRow, Integer>> _distinctCounts = new ConcurrentHashMap<Integer, Map.Entry<InputRow, Integer>>();

    /**
     *  default   com.novacloud.data.datacleaner.extra.Config.StoredAnnotatedRowsThreshold
     */
    public NovaInMemoryRowAnnotationFactory() {
        this(Config.StoredAnnotatedRowsThreshold);
    }

    public NovaInMemoryRowAnnotationFactory(int storedRowsThreshold) {
        super(storedRowsThreshold);
    }
    protected int getInMemoryRowCount(RowAnnotation annotation) {
        Set<Integer> rows = _annotatedRows.get(annotation);
        if (rows == null) {
            return 0;
        }
        return rows.size();
    }

    @Override
    protected void resetRows(RowAnnotation annotation) {
        _annotatedRows.remove(annotation);
    }

    @Override
    protected int getDistinctCount(InputRow row) {
        return _distinctCounts.get(row.getId()).getValue();
    }

    @Override
    protected void storeRowAnnotation(int rowId, RowAnnotation annotation) {
        Set<Integer> rowIds = getRowIds(annotation);
        rowIds.add(rowId);
    }

    private Set<Integer> getRowIds(RowAnnotation annotation) {
        Set<Integer> rowIds = _annotatedRows.get(annotation);
        if (rowIds == null) {
            rowIds = Collections.synchronizedSet(new LinkedHashSet<Integer>());
            _annotatedRows.put(annotation, rowIds);
        }
        return rowIds;
    }

    @Override
    protected void storeRowValues(int rowId, InputRow row, int distinctCount) {
        MockInputRow mockInputRow = new MockInputRow(rowId);
        for (InputColumn<?> inputColumn : row.getInputColumns()) {
            //TODO XXX NOTE: ignore all source column ,for hbase: ignore rowkey column
            if(inputColumn instanceof MetaModelInputColumn){;}
            //ignore immediated byte array column
            else if (ReflectionUtils.isByteArray(inputColumn.getDataType()) ){;}
            else if(row.getValue(inputColumn) != null && ReflectionUtils.isByteArray(row.getValue(inputColumn).getClass())
                    ){;}
            else if (ReflectionUtils.isMap(inputColumn.getDataType())){;}//ignore columnfamily column
            else {
                mockInputRow.put(inputColumn,row.getValue(inputColumn));
            }
        }

        _distinctCounts.put(rowId, new ImmutableEntry<InputRow, Integer>(mockInputRow, distinctCount));
    }

    @Override
    public InputRow[] getRows(RowAnnotation annotation) {
        Set<Integer> rowIds = _annotatedRows.get(annotation);
        if (rowIds == null) {
            return new InputRow[0];
        }
        InputRow[] rows = new InputRow[rowIds.size()];
        int i = 0;
        for (Integer rowId : rowIds) {
            rows[i] = _distinctCounts.get(rowId).getKey();
            i++;
        }
        return rows;
    }

    @Override
    public void transferAnnotations(RowAnnotation from, RowAnnotation to) {
        final int rowCountToAdd = from.getRowCount();
        ((RowAnnotationImpl) to).incrementRowCount(rowCountToAdd);
    }

}
