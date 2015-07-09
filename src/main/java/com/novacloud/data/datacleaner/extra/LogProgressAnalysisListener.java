package com.novacloud.data.datacleaner.extra;

import org.apache.metamodel.schema.Table;
import org.eobjects.analyzer.data.InputRow;
import org.eobjects.analyzer.job.AnalysisJob;
import org.eobjects.analyzer.job.AnalyzerJob;
import org.eobjects.analyzer.job.runner.AnalysisListenerAdaptor;
import org.eobjects.analyzer.job.runner.AnalyzerMetrics;
import org.eobjects.analyzer.job.runner.RowProcessingMetrics;
import org.eobjects.analyzer.result.AnalyzerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LogProgressAnalysisListener extends AnalysisListenerAdaptor {

	private static final int INTERVAL_ROWS = 1000;

	private static final Logger logger = LoggerFactory
			.getLogger(LogProgressAnalysisListener.class);

	private Map<Table, AtomicInteger> rowCounts = new HashMap<Table, AtomicInteger>();

	@Override
	public void rowProcessingBegin(AnalysisJob job,
			RowProcessingMetrics metrics) {
		Table table = metrics.getTable();
		logger.info("Analyzing rows from table: {}", table.getName());
		rowCounts.put(table, new AtomicInteger(0));
	}

	@Override
	public void rowProcessingProgress(AnalysisJob job, RowProcessingMetrics metrics, InputRow row, int currentRow) {
		Table table = metrics.getTable();
		AtomicInteger rowCount = rowCounts.get(table);
		if (rowCount != null) {
			int countBefore = rowCount.get();
			rowCount.lazySet(currentRow);
			int fiveHundredsBefore = countBefore / INTERVAL_ROWS;
			int fiveHundredsAfter = currentRow / INTERVAL_ROWS;
			if (fiveHundredsAfter != fiveHundredsBefore) {
				logger.info(
						 "{} rows processed from table: {}" ,currentRow, table.getName());
			}
		}
	}

	@Override
	public void rowProcessingSuccess(AnalysisJob job,
			RowProcessingMetrics metrics) {
        this.
		logger.info("Done processing rows from table: {}", metrics
				.getTable().getName());
	}
    @Override
    public void analyzerBegin(AnalysisJob job, AnalyzerJob analyzerJob, AnalyzerMetrics metrics) {
    }

    @Override
    public void analyzerSuccess(AnalysisJob job, AnalyzerJob analyzerJob, AnalyzerResult result) {
        //TODO:
    }
}