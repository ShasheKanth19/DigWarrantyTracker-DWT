import React, { useState, useEffect } from 'react';
import { getMLMetrics, getDashboardStats } from '../../services/mlAnalyticsService';
import "bootstrap/dist/css/bootstrap.min.css";

export default function MLDashboardPage() {
    const [metrics, setMetrics] = useState(null);
    const [stats, setStats] = useState(null);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const metricsRes = await getMLMetrics();
            const statsRes = await getDashboardStats();
            setMetrics(metricsRes.data);
            setStats(statsRes.data);
        } catch (error) {
            console.error("Failed to load ML data", error);
        }
    };

    if (!metrics || !stats) return <div className="container mt-5 text-center">Loading ML Insights...</div>;

    return (
        <div className="container mt-5 mb-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2 className="fw-bold" style={{ color: "#1E293B" }}>
    🧠 ML Intelligence Dashboard
</h2>
                <a href="/dashboard" className="btn btn-outline-secondary">Back to Home</a>
            </div>
            <div className="alert alert-primary border-0 shadow-sm rounded-4 mb-4">
    <h5 className="fw-bold mb-2">
        AI Renewal Intelligence Engine
    </h5>

    <p className="mb-0">
         AI-powered warranty renewal prediction platform using a
    K-Nearest Neighbors (KNN) model trained on 3000 historical
    records. Provides churn-risk analysis, explainable AI insights,
    confidence scoring, and actionable renewal recommendations.
    </p>
</div>

            {/* Top Stats Cards */}
            <div className="card shadow-sm border-0 rounded-4 mb-4">
    <div className="card-body">

        <h5 className="fw-bold mb-3">
            ⚙️ Model Configuration
        </h5>

        <div className="row text-center">

            <div className="col-md-3">
                <small className="text-muted d-block">
                    Algorithm
                </small>

                <h5 className="fw-bold text-primary">
                    KNN
                </h5>
            </div>

            <div className="col-md-3">
                <small className="text-muted d-block">
                    K Value
                </small>

                <h5 className="fw-bold text-success">
                    5
                </h5>
            </div>

            <div className="col-md-3">
                <small className="text-muted d-block">
                    Training Records
                </small>

                <h5 className="fw-bold text-info">
                    {metrics.datasetSize}
                </h5>
            </div>

            <div className="col-md-3">
                <small className="text-muted d-block">
                    Validation Split
                </small>

                <h5 className="fw-bold text-warning">
                    80 / 20
                </h5>
            </div>

        </div>

    </div>
</div>
            <div className="row mb-4">
                <div className="col-md-3">
                    <div className="card shadow-sm border-0 text-center bg-primary text-white h-100 rounded-4">
                        <div className="card-body">
                            <h6 className="card-title text-uppercase opacity-75">Dataset Size</h6>
                            <h2 className="display-5 fw-bold">{metrics.datasetSize}</h2>
                        </div>
                    </div>
                </div>
                <div className="col-md-3">
                    <div className="card shadow-sm border-0 text-center bg-success text-white h-100 rounded-4">
                        <div className="card-body">
                            <h6 className="card-title text-uppercase opacity-75">Predicted Renewals</h6>
                            <h2 className="display-5 fw-bold">{stats.predictedRenewals}</h2>
                        </div>
                    </div>
                </div>
                <div className="col-md-3">
                    <div className="card shadow-sm border-0 text-center bg-warning h-100 rounded-4">
                        <div className="card-body">
                            <h6 className="card-title text-uppercase opacity-75">High-Risk Customers</h6>
                            <h2 className="display-5 fw-bold">{stats.highRiskProducts}</h2>
                        </div>
                    </div>
                </div>
                <div className="col-md-3">
                    <div className="card shadow-sm border-0 text-center bg-dark text-white h-100 rounded-4">
                        <div className="card-body">
                            <h6 className="card-title text-uppercase opacity-75">Model Accuracy</h6>
                            <h2 className="display-5 fw-bold">
                                {Number(stats.modelAccuracy || 0).toFixed(1)}%
                            </h2>
                        </div>
                    </div>
                </div>
            </div>

            {/* Performance and Feature Importance */}
            <div className="row">
                {/* KNN Model Performance */}
                <div className="col-md-6 mb-4">
                    <div className="card shadow-sm border-0 h-100 rounded-4 p-2">
                        <div className="card-body">
                            <h4 className="card-title mb-4" style={{color: '#1E293B'}}>KNN Model Performance</h4>
                            {metrics.modelComparison && metrics.modelComparison.length > 0 && (
                                <div className="row text-center mb-4">
                                    <div className="col-6 mb-3">
                                        <div className="p-3 bg-light rounded-3">
                                            <div className="small text-muted text-uppercase mb-1">Accuracy</div>
                                            <div className="fs-4 fw-bold text-primary">
                                                {Number(metrics.modelComparison[0].accuracy).toFixed(1)}%
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-6 mb-3">
                                        <div className="p-3 bg-light rounded-3">
                                            <div className="small text-muted text-uppercase mb-1">Precision</div>
                                            <div className="fs-4 fw-bold text-success">
                                                {Number(metrics.modelComparison[0].precision).toFixed(1)}%
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-6">
                                        <div className="p-3 bg-light rounded-3">
                                            <div className="small text-muted text-uppercase mb-1">Recall</div>
                                            <div className="fs-4 fw-bold text-warning">
                                                {Number(metrics.modelComparison[0].recall).toFixed(1)}%
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-6">
                                        <div className="p-3 bg-light rounded-3">
                                            <div className="small text-muted text-uppercase mb-1">F1 Score</div>
                                            <div className="fs-4 fw-bold text-info">
                                                {Number(metrics.modelComparison[0].f1Score).toFixed(1)}%
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            )}

                            <h4 className="card-title mb-3 mt-4" style={{color: '#1E293B'}}>Data Quality Pipeline</h4>
                            <ul className="list-group list-group-flush">
                                <li className="list-group-item d-flex justify-content-between align-items-center border-0 px-0 mb-1">
                                    <span className="fw-medium text-muted">Quality Score</span>
                                    <span className={`badge ${metrics.dataQualityScore > 90 ? 'bg-success' : 'bg-warning'} text-white rounded-pill px-3 py-2`}>
                                        {metrics.dataQualityScore}%
                                    </span>
                                </li>
                                <li className="list-group-item d-flex justify-content-between align-items-center border-0 px-0 mb-1">
                                    <span className="fw-medium text-muted">Missing Values</span>
                                    <span className="fw-bold">{metrics.missingValues}</span>
                                </li>
                                <li className="list-group-item d-flex justify-content-between align-items-center border-0 px-0 mb-1">
                                    <span className="fw-medium text-muted">Duplicate Record</span>
                                    <span className="fw-bold">{metrics.duplicateRecords}</span>
                                </li>
                                <li className="list-group-item d-flex justify-content-between align-items-center border-0 px-0 mb-1">
                                    <span className="fw-medium text-muted">Outlier Record</span>
                                    <span className="fw-bold">{metrics.outliersTreated}</span>
                                </li>
                            </ul>
                            <div className="mt-4">

    <div className="d-flex justify-content-between align-items-center mb-2">
        <span className="fw-semibold">
            Dataset Health Status
        </span>

        <span
            className={`badge ${
                metrics.dataQualityScore >= 95
                    ? "bg-success"
                    : metrics.dataQualityScore >= 80
                    ? "bg-warning text-dark"
                    : "bg-danger"
            } rounded-pill px-3 py-2`}
        >
            {metrics.dataQualityScore >= 95
                ? "Excellent"
                : metrics.dataQualityScore >= 80
                ? "Good"
                : "Needs Attention"}
        </span>
    </div>

    <div className="progress" style={{ height: "12px" }}>
        <div
            className={`progress-bar ${
                metrics.dataQualityScore >= 95
                    ? "bg-success"
                    : metrics.dataQualityScore >= 80
                    ? "bg-warning"
                    : "bg-danger"
            }`}
            role="progressbar"
            style={{
                width: `${metrics.dataQualityScore}%`
            }}
        >
            {metrics.dataQualityScore}%
        </div>
    </div>

</div>
                        </div>
                    </div>
                </div>

                {/* Feature Importance */}
                <div className="col-md-6 mb-4">
                    <div className="card shadow-sm border-0 h-100 rounded-4 p-2">
                        <div className="card-body">
                            <h4 className="card-title mb-4" style={{color: '#1E293B'}}>Feature Importance</h4>
                           <div className="mt-2">
    {metrics.featureImportance.map((feature, idx) => {

        const match = feature.match(/\(([^)]+)\)/);

        const percText = match ? match[1] : "0%";

        const percValue = parseFloat(
            percText.replace("%", "")
        );

        const name = feature.split("(")[0].trim();

        return (
            <div key={idx} className="mb-4">

                <div className="d-flex justify-content-between mb-1">
                    <span className="fw-medium">
                        {name}
                    </span>

                    <strong>
                        {percText}
                    </strong>
                </div>

                <div
                    className="progress"
                    style={{ height: "12px" }}
                >
                    <div
    className={`progress-bar ${
        idx === 0
            ? "bg-primary"
            : idx === 1
            ? "bg-success"
            : idx === 2
            ? "bg-info"
            : "bg-secondary"
    }`}
                        role="progressbar"
                        style={{
                            width: `${percValue}%`
                        }}
                    />
                </div>

            </div>
        );
    })}
</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
