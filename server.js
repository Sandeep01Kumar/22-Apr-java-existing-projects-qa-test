const express = require('express'); // QA
const app = express(); // QA
const PORT = 3000; // QA

app.get('/', (req, res) => res.send('Hello world')); // QA
app.get('/good-evening', (req, res) => res.send('Good evening')); // QA

app.listen(PORT, () => console.log(`Server listening on port ${PORT}`)); // QA
