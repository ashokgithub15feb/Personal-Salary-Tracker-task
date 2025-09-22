Salary Management REST APIs - Step by Step Explanation
======================================================

1. Upload Salary Excel
======================
   Purpose: Upload multi-year salary history bulk data from Excel sheets.

HTTP Method & Path:
POST /api/salaries/upload

Input:
Excel file in multipart/form-data containing sheets named by year (e.g., 2020, 2021, ...). Each sheet has columns: Company, Currency, Salary Component, Amount.

Processing:
Parse each sheet, extract rows, create salary records with year from sheet name, then store them in the database.

Output:
Success or error message.

2. Fetch Salary by Year
=======================
   Purpose: Get salary breakdown for a specific year including fixed, variable, deductions, currency, and converted salary.

HTTP Method & Path:
GET /api/salaries/{year}

Input:
Path variable specifying the year.

Processing:
Query database for all salary records of the given year. Convert all amounts to a base currency (default USD or configured) using exchange rates.

Output:
List of salary components with original and converted amounts.

3. Salary Growth Trend (Graph)
==============================
Purpose: Return year-wise aggregated salary trend converted to a requested base currency (e.g., USD).

HTTP Method & Path:
GET /api/salaries/trend?baseCurrency=USD

Input:
Query parameter baseCurrency for requested conversion.

Processing:
Aggregate salaries grouped by year, convert amounts to base currency using fixed/historical exchange rates.

Output:
List of year and total salary amount in requested currency, suitable for plotting graphs.

4. Latest Salary (Current Job)
==============================
   Purpose: Fetch the latest salary data for the most recent year, showing original and converted values.

HTTP Method & Path:
GET /api/salaries/latest?baseCurrency=AED

Input:
Query parameter baseCurrency (default AED in example).

Processing:
Find the max year in data, fetch records for that year, convert amounts to requested base currency.

Output:
List of latest salaries with original & converted amounts.

5. Update Salary Hike
=====================
   Purpose: Update all salaries for a specific year by applying a hike percentage.

HTTP Method & Path:
PUT /api/salaries/{year}/hike

Input:
Path variable year and query param percent (percentage hike).

Processing:
Find all salary records for the year, multiply amounts by (1 + percent/100), update records in DB.

Output:
Success message stating number of updated records or error if none.

6. Update Company Switch
========================
   Purpose: Insert a new salary record when an employee switches company.

HTTP Method & Path:
POST /api/salaries/switch

Input:
JSON payload containing new salary record details (company, currency, year, salary component, salary type, amount).

Processing:
Create and store new salary record in DB.

Output:
Success message with inserted company name.

7. Compare Salary Across Companies
==================================
   Purpose: Compare average salaries of companies normalized to a base currency.

HTTP Method & Path:
GET /api/salaries/compare?baseCurrency=USD

Input:
Query parameter baseCurrency for requested conversion.

Processing:
Group salary records by company, calculate average original amount per company, convert average to base currency.

Output:
List of companies with averages in original and base currencies.

==============================

This step-by-step API guide gives a clear understanding of your salary management REST 
endpoints purpose, input requirements, processing logic, and expected outputs. 
Specific implementation details like exchange rate management and DB schema are customizable.

==============================