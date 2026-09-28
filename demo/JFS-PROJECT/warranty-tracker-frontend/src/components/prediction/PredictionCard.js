import React from 'react';

export default function PredictionCard({ predictionHistory }) {
    if (!predictionHistory) return <div className="text-muted small mt-2">No AI prediction available yet.</div>;

    const getRiskColor = (level) => {
        if (level === 'Low') return 'bg-success';
        if (level === 'Medium') return 'bg-warning text-dark';
        if (level === 'High') return 'bg-danger';
        return 'bg-secondary';
    };

    const factors = predictionHistory.topFactors ? predictionHistory.topFactors.split(';') : [];

    return (
        <div className="card shadow-sm border-0 mt-3 result-card" style={{ backgroundColor: '#f8fafc' }}>
            <div className="card-body">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="card-title mb-0" style={{ color: '#1E293B' }}>
                        🤖 AI Prediction: <strong>{predictionHistory.prediction}</strong>
                    </h5>
                    <span className={`badge rounded-pill ${getRiskColor(predictionHistory.riskLevel)} px-3 py-2`}>
                        Risk: {predictionHistory.riskLevel}
                    </span>
                </div>
                
                <div className="mb-3">
                    <div className="d-flex justify-content-between small text-muted">
                        <span>Confidence Score</span>
                        <strong>{predictionHistory.confidenceScore.toFixed(1)}%</strong>
                    </div>
                    <div className="progress mt-1" style={{ height: '8px' }}>
                        <div 
                            className="progress-bar confidence-fill" 
                            role="progressbar" 
                            style={{ width: `${predictionHistory.confidenceScore}%`, backgroundColor: '#2563EB' }} 
                        />
                    </div>
                </div>

                <div className="alert alert-info py-2 small mb-3">
                    <strong>🎯 Recommended Action:</strong> {predictionHistory.recommendation}
                </div>

                <div>
                    <p className="mb-1 fw-bold small">Top Contributing Factors:</p>
                    <ul className="mb-0 text-muted small">
                        {factors.map((factor, idx) => <li key={idx}>{factor}</li>)}
                    </ul>
                </div>
            </div>
        </div>
    );
}
