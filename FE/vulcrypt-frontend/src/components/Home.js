import React, { useState } from "react";
import {
  Box,
  Button,
  Typography,
  CircularProgress,
  Alert,
  Stack,
} from "@mui/material";

function Home() {
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleFileChange = (event) => {
    console.log(event.target.files);
    setSelectedFiles(event.target.files); // Allow multiple file selection
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);
    setSuccess(false);
    if (!selectedFiles || selectedFiles.length === 0) {
      setError("Please select files to upload.");
      return;
    }
    setLoading(true);
    const formData = new FormData();
    Array.from(selectedFiles).forEach((file) => formData.append("files", file));

    try {
      const response = await fetch("http://localhost:8000/run-and-download", {
        method: "POST",
        body: formData,
      });
      if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "vulnerability scan results.zip"); // Custom filename
        document.body.appendChild(link);
        link.click();
        setSuccess(true);
      } else {
        setError("Failed to process files. Please try again.");
      }
    } catch (err) {
      setError("An error occurred. Please check the backend and try again.");
    }
    setLoading(false);
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Scan and Download Results
      </Typography>
      <Stack spacing={2}>
        <input
          type="file"
          multiple // Allow multiple file uploads
          onChange={handleFileChange}
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handleSubmit}
          disabled={loading}
        >
          {loading ? <CircularProgress size={24} /> : "Scan and Download"}
        </Button>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">Download successful!</Alert>}
      </Stack>
    </Box>
  );
}

export default Home;
