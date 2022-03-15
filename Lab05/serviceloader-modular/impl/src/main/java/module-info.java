import ex.api.ClusterAnalysisService;
import serviceloader.impl.ClusterAnalysisDate;
import serviceloader.impl.ClusterAnalysisDayOfWeek;

module impl {
	requires api;
	exports serviceloader.impl;
	
	provides ClusterAnalysisService
		with ClusterAnalysisDate, ClusterAnalysisDayOfWeek;
}