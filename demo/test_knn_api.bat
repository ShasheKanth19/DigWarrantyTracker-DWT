@echo off
echo Testing KNN Renewal Prediction API...
echo.

echo Test 1: Electronics product (12000, 30 days to expiry)
curl -X POST http://localhost:8080/api/predict/renewal -H "Content-Type: application/json" -d "{\"category\":\"electronics\",\"price\":12000,\"days_to_expiry\":30,\"previous_renewals\":1,\"extended\":0,\"k\":5}"
echo.
echo.

echo Test 2: Low-value appliance (likely won't renew)
curl -X POST http://localhost:8080/api/predict/renewal -H "Content-Type: application/json" -d "{\"category\":\"appliance\",\"price\":3000,\"days_to_expiry\":5,\"previous_renewals\":0,\"extended\":0,\"k\":5}"
echo.
echo.

echo Test 3: High-value electronics (likely will renew)
curl -X POST http://localhost:8080/api/predict/renewal -H "Content-Type: application/json" -d "{\"category\":\"electronics\",\"price\":25000,\"days_to_expiry\":90,\"previous_renewals\":2,\"extended\":1,\"k\":5}"
echo.
echo.

echo Done!
pause
