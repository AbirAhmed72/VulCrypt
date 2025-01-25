// import React, { useState } from "react";
// import axios from "axios";
// import "./App.css";

// const App = () => {
//   const [files, setFiles] = useState([]);
//   const [isLoading, setIsLoading] = useState(false);
//   const [errorMessage, setErrorMessage] = useState("");
//   const [downloadLink, setDownloadLink] = useState("");

//   const handleFileChange = (e) => {
//     setFiles([...e.target.files]);
//   };

//   const handleRunAndDownload = async () => {
//     setIsLoading(true);
//     setErrorMessage("");
//     setDownloadLink("");

//     const formData = new FormData();
//     files.forEach((file) => formData.append("files", file));

//     try {
//       const response = await axios.post("http://localhost:8000/run-and-download", formData, {
//         responseType: "blob",
//         headers: { "Content-Type": "multipart/form-data" },
//       });
//       const blob = new Blob([response.data]);
//       const url = window.URL.createObjectURL(blob);
//       setDownloadLink(url);
//     } catch (error) {
//       setErrorMessage(error.response?.data?.detail || "An error occurred.");
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   const handleDownloadGraphImages = async () => {
//     setIsLoading(true);
//     setErrorMessage("");
//     setDownloadLink("");

//     const formData = new FormData();
//     files.forEach((file) => formData.append("files", file));

//     try {
//       const response = await axios.post("http://localhost:8000/download-graph-images", formData, {
//         responseType: "blob",
//         headers: { "Content-Type": "multipart/form-data" },
//       });
//       const blob = new Blob([response.data]);
//       const url = window.URL.createObjectURL(blob);
//       setDownloadLink(url);
//     } catch (error) {
//       setErrorMessage(error.response?.data?.detail || "An error occurred.");
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   return (
//     <div className="app">
//       <h1>VulCrypt Tool</h1>
//       <div className="file-upload">
//         <label htmlFor="file-input" className="file-label">
//           Select Files
//         </label>
//         <input
//           id="file-input"
//           type="file"
//           multiple
//           onChange={handleFileChange}
//           className="file-input"
//         />
//       </div>
//       <div className="button-group">
//         <button onClick={handleRunAndDownload} disabled={isLoading || !files.length}>
//           Run and Download Results
//         </button>
//         <button onClick={handleDownloadGraphImages} disabled={isLoading || !files.length}>
//           Download Graph Images
//         </button>
//       </div>
//       {isLoading && <p className="loading">Processing... Please wait.</p>}
//       {errorMessage && <p className="error">{errorMessage}</p>}
//       {downloadLink && (
//         <a href={downloadLink} download="results.zip" className="download-link">
//           Download Results
//         </a>
//       )}
//     </div>
//   );
// };

// export default App;

import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { Container, AppBar, Toolbar, Typography, Button } from "@mui/material";
import Home from "./components/Home";
import Graphs from "./components/Graphs";

function App() {
  return (
    <Router>
      <AppBar position="static" sx={{ marginBottom: 4 }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            VulCrypt
          </Typography>
          <Button component={Link} to="/" color="inherit">
            Run Tool
          </Button>
          <Button component={Link} to="/graphs" color="inherit">
            Download Graphs
          </Button>
        </Toolbar>
      </AppBar>
      <Container>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/graphs" element={<Graphs />} />
        </Routes>
      </Container>
    </Router>
  );
}

export default App;
