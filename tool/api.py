from fastapi import FastAPI, HTTPException, File, UploadFile
from fastapi.responses import FileResponse
from pathlib import Path
import subprocess
import shutil
import os
import time
import zipfile
from typing import List

app = FastAPI()

# Paths
SAMPLE_APP_PATH = "../sample_app"
RESULT_FOLDER = "../sample_app_result"
ZIP_OUTPUT = "output.zip"
RUN_SCRIPT = "./run.sh"

@app.post("/run-and-download")
async def run_and_download(files: List[UploadFile] = File(...)):
    # Clean previous results
    if os.path.exists(RESULT_FOLDER):
        shutil.rmtree(RESULT_FOLDER)
        
    if os.path.exists(SAMPLE_APP_PATH):
        shutil.rmtree(SAMPLE_APP_PATH)
        
    if os.path.exists(ZIP_OUTPUT):
        os.remove(ZIP_OUTPUT)
    
    if os.path.exists('./extracted_code'):
        shutil.rmtree('./extracted_code')
        
    if os.path.exists('./graph'):
        shutil.rmtree('./graph')
    
    # Ensure sample_app directory exists and is empty
    if os.path.exists(SAMPLE_APP_PATH):
        shutil.rmtree(SAMPLE_APP_PATH)
    os.makedirs(SAMPLE_APP_PATH)
    
    # Save uploaded files
    for uploaded_file in files:
        file_path = os.path.join(SAMPLE_APP_PATH, uploaded_file.filename)
        with open(file_path, "wb") as buffer:
            buffer.write(await uploaded_file.read())
    
    # Run the tool
    try:
        start_time = time.time()
        subprocess.run([RUN_SCRIPT], check=True, shell=True)
        print(f"Tool execution completed in {time.time() - start_time:.2f} seconds")
    except subprocess.CalledProcessError as e:
        raise HTTPException(status_code=500, detail=f"Error while running the tool: {e}")
    
    # Check if results are generated
    if not os.path.exists(RESULT_FOLDER) or not os.listdir(RESULT_FOLDER):
        raise HTTPException(status_code=404, detail="Results were not generated. Check the tool's execution.")
    
    # Create ZIP of results
    zip_path = Path(ZIP_OUTPUT)
    with zipfile.ZipFile(zip_path, "w") as zipf:
        for root, _, files in os.walk(RESULT_FOLDER):
            for file in files:
                file_path = os.path.join(root, file)
                arcname = os.path.relpath(file_path, RESULT_FOLDER)
                zipf.write(file_path, arcname)
    
    # Serve the ZIP file
    return FileResponse(zip_path, media_type="application/zip", filename=ZIP_OUTPUT)