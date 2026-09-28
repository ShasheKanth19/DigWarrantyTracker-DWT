import React, { useState, useEffect } from "react";
import { getAllProducts } from "../../services/api";
import PredictionCard from "../prediction/PredictionCard";

export default function ProductList() {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {
        try {
            const res = await getAllProducts();

            console.log("Products API Response:", res.data);

            setProducts(res.data || []);
        } catch (error) {
            console.error("Error loading products", error);
        }
    };

    return (
        <div className="container mt-5 mb-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2 className="mb-0">My Warranties & Products</h2>
                <a href="/dashboard" className="btn btn-outline-secondary">
                    Back to Dashboard
                </a>
            </div>

            {products.length === 0 ? (
                <div className="alert alert-info">
                    No products found.
                </div>
            ) : (
                <div className="row">
                    {products.map(product => (
                        <div className="col-md-6 mb-4" key={product.id}>
                            <div className="card shadow p-4 h-100 border-0 rounded-4">

                                <h4 style={{ color: "#2563EB" }}>
                                    {product.name}
                                </h4>

                                <div className="row mt-3">
                                    <div className="col-6">
                                        <p className="text-muted mb-1 small">
                                            Category
                                        </p>
                                        <p className="fw-bold">
                                            {product.category}
                                        </p>
                                    </div>

                                    <div className="col-6">
                                        <p className="text-muted mb-1 small">
                                            Price
                                        </p>
                                        <p className="fw-bold">
                                            ₹ {product.price}
                                        </p>
                                    </div>

                                    <div className="col-6">
                                        <p className="text-muted mb-1 small">
                                            Warranty Expiry
                                        </p>
                                        <p className="fw-bold text-danger">
                                            {product.warrantyExpiryDate}
                                        </p>
                                    </div>

                                    <div className="col-6">
                                        <p className="text-muted mb-1 small">
                                            Owner
                                        </p>
                                        <p className="fw-bold">
                                            {product.user?.name || "Unknown"}
                                        </p>
                                    </div>
                                </div>

                                <hr />

                                {/* FIXED: Use predictionHistory directly from API */}
                                <PredictionCard
                                    predictionHistory={product?.predictionHistory}
                                />
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}