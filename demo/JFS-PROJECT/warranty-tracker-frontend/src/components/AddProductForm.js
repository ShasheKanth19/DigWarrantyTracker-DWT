import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { addProduct, getAllUsers } from "../services/api";

export default function AddProductForm() {
    const [product, setProduct] = useState({
        name: "",
        category: "",
        serialNumber: "",
        price: "",
        warrantyMonths: "",
        purchaseDate: "",
        user: { id: "" } // Store selected user ID
    });
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
            const result = await getAllUsers();
            setUsers(result.data);
        } catch (error) {
            console.error("Error loading users:", error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (name === "userId") {
            setProduct({ ...product, user: { id: value } });
        } else {
            setProduct({ ...product, [name]: value });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await addProduct(product);
            navigate("/dashboard");
        } catch (error) {
            console.error("Error adding product:", error);
            alert("Failed to add product.");
        }
    };

    return (
        <div className="container mt-5">
            <div className="card shadow-lg p-4">
                <h2 className="text-center mb-4">Add New Product</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label">Product Name</label>
                        <input
                            type="text"
                            className="form-control"
                            name="name"
                            value={product.name}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Category</label>
                        <input
                            type="text"
                            className="form-control"
                            name="category"
                            value={product.category}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Serial Number</label>
                        <input
                            type="text"
                            className="form-control"
                            name="serialNumber"
                            value={product.serialNumber}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label className="form-label">Price</label>
                            <input
                                type="number"
                                className="form-control"
                                name="price"
                                value={product.price}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label">Warranty (Months)</label>
                            <input
                                type="number"
                                className="form-control"
                                name="warrantyMonths"
                                value={product.warrantyMonths}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Purchase Date</label>
                        <input
                            type="date"
                            className="form-control"
                            name="purchaseDate"
                            value={product.purchaseDate}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Owner (User)</label>
                        <select
                            className="form-select"
                            name="userId"
                            value={product.user.id}
                            onChange={handleChange}
                            required
                        >
                            <option value="">Select User</option>
                            {users.map((user) => (
                                <option key={user.id} value={user.id}>
                                    {user.name} ({user.email})
                                </option>
                            ))}
                        </select>
                    </div>
                    <button type="submit" className="btn btn-primary w-100">
                        Save Product
                    </button>
                </form>
            </div>
        </div>
    );
}
