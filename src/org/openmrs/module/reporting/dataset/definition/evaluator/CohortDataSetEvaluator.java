/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.reporting.dataset.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.MapDataSet;
import org.openmrs.module.reporting.dataset.column.DataSetColumn;
import org.openmrs.module.reporting.dataset.definition.CohortDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;

/**
 * The logic that evaluates a {@link CohortDataSetDefinition} and produces a {@link MapDataSet<Cohort>}
 * 
 * @see CohortDataSetDefinition
 * @see MapDataSet<Cohort>
 */
@Handler(supports={CohortDataSetDefinition.class})
public class CohortDataSetEvaluator implements DataSetEvaluator {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	public CohortDataSetEvaluator() { }
	
	/**
	 * @see DataSetEvaluator#evaluate(DataSetDefinition, EvaluationContext)
	 */
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext context) {
		
		if (context == null) {
			context = new EvaluationContext();
		}
		
		MapDataSet data = new MapDataSet(dataSetDefinition, context);
		data.setName(dataSetDefinition.getName());

		CohortDataSetDefinition d = (CohortDataSetDefinition) dataSetDefinition;
		for (DataSetColumn c : d.getColumns()) {
			Mapped<? extends CohortDefinition> mapped = d.getDefinitions().get(c);
			Cohort cohort = Context.getService(CohortDefinitionService.class).evaluate(mapped, context);
			data.addData(c, cohort);
		}

		return data;
	}
}