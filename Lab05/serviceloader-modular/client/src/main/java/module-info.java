import ex.api.ClusterAnalysisService;

module client {
	exports serviceloader.client;

	requires java.desktop;
	requires api;
	uses ClusterAnalysisService;
}