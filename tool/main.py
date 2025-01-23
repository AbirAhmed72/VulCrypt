import argparse
import subprocess
from datetime import datetime
import os
import sys
import time
import zipfile
import shutil

import torch
from tqdm import tqdm

from extract_crypto_line import findCryptoLine
from code_slicing import slicingCode

from detect_codebert import model_setting_codebert, detect_codebert
from detect_codegpt import model_setting_codegpt, detect_codegpt
from detect_electra import model_setting_electra, detect_electra
from detect_codet5 import model_setting_codet5, detect_codet5

jadx_path = 'jadx/build/jadx/bin/jadx'

def execute_jadx_command(cmd_):
    proc = subprocess.Popen(cmd_, shell=True, bufsize=256, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    try:
        proc.communicate(timeout=60)
        proc.terminate()
        proc.wait()
    except subprocess.TimeoutExpired:
        proc.terminate()
        proc.wait()
        time.sleep(10)
    
    return proc.poll()

def result(ap, args, java_path=None, preds=[]):
    print('processing result')
    with open(f"{args.o}/{ap}.txt", "a") as f:
        if (java_path == None) and (len(preds) == 0):
            f.write(f"Benign\n")
        elif (java_path != None) and (len(preds) == 0):
            try:
                pth = java_path.split('sources/')[1] if 'sources/' in java_path else java_path
                f.write(f"{pth} -> X\n")
            except IndexError:
                f.write(f"Unknown path -> X\n")
        else:
            preds = preds.tolist()
            r_ = 'MALICIOUS' if 1 in preds else 'BENIGN'
            try:
                pth = java_path.split('sources/')[1] if 'sources/' in java_path else java_path
                f.write(f"{pth} -> {r_}\n")
            except IndexError:
                f.write(f"Unknown path -> {r_}\n")

def unzip_source(zip_path, unzip_path):
    print(f"unzipping {unzip_path}")
    with zipfile.ZipFile(zip_path, 'r') as zip_ref:
        zip_ref.extractall(unzip_path)
    return unzip_path

def process_java_files(java_files, base_folder, app, args, model, tokenizer):
    """
    Processes Java files: finding crypto lines, slicing code, and making ML predictions.
    """
    for java_path in java_files:
        start_time = time.time()
        try:
            crypto_line_dict, candidate_java_file_path = findCryptoLine(java_path, base_folder)
            if crypto_line_dict:
                print(f"{crypto_line_dict} found from findCryptoLine")
                print(f"candidate_java_file_path is {candidate_java_file_path}")
                code_normalization_directory = slicingCode(java_path, base_folder, candidate_java_file_path, crypto_line_dict)
                if code_normalization_directory:
                    print(f"code_normalization_directory is: {code_normalization_directory}")
                    print("ML Starts here")
                    preds = (
                        detect_codebert(code_normalization_directory, model, tokenizer, args) if args.m == 'codebert' else
                        detect_codegpt(code_normalization_directory, model, tokenizer, args) if args.m == 'codegpt' else
                        detect_codet5(code_normalization_directory, model, tokenizer, args) if args.m == 'codet5' else
                        detect_electra(code_normalization_directory, model, tokenizer, args)
                    )
                    result(app, args, java_path, preds)
                else:
                    result(app, args, java_path)
            print(f"[INFO] Processed {java_path} in {time.time() - start_time:.2f} seconds")
        except Exception as e:
            print(f"[ERROR] {e} for file {java_path}")
            result(app, args, java_path)

def process_zip_file(app_path, extracted_path, app, args, model, tokenizer):
    """
    Processes ZIP files: extracts content and processes Java files within.
    """
    extracted_folder = os.path.join(extracted_path, app.replace('.zip', ''))
    os.makedirs(extracted_folder, exist_ok=True)
    unzip_source(app_path, extracted_folder)

    java_files = [
        os.path.join(root, f)
        for root, _, files in os.walk(extracted_folder)
        for f in files if f.endswith('.java')
    ]
    process_java_files(java_files, extracted_folder, app, args, model, tokenizer)

def process_apk_file(app_path, extracted_path, app, args, model, tokenizer, jadx_path):
    """
    Processes APK files: decompiles using JADX and processes Java files within.
    """
    apk_decompiled_folder = os.path.join(extracted_path, app)
    os.makedirs(apk_decompiled_folder, exist_ok=True)
    jadx_cmd = f'{jadx_path} -d {apk_decompiled_folder} {app_path} -r'
    done = execute_jadx_command(jadx_cmd)

    if done == 0:
        java_files = [
            os.path.join(root, f)
            for root, _, files in os.walk(apk_decompiled_folder)
            for f in files if f.endswith('.java')
        ]
        process_java_files(java_files, apk_decompiled_folder, app, args, model, tokenizer)


def main():
    parser = argparse.ArgumentParser(description='VulCrypt')
    parser.add_argument('--f', '--folder', type=str, required=True, help='Target APK/Java Source Code Folder or Zip File Path')
    parser.add_argument('--o', '--output', type=str, required=True, help='Output Folder Path')
    parser.add_argument('--p', '--model_path', type=str, required=True, help='Path of Trained Model')
    parser.add_argument('--m', '--model', type=str, required=True, help='Model to Use')

    args = parser.parse_args()

    if not os.path.exists(args.f):
       print("*** Folder/Zip file does not exist. Provide the correct path.")
       sys.exit()
       
    if args.m not in ['codebert', 'codegpt', 'codet5', 'electra']:
       print("*** Invalid model. Use 'codebert', 'codegpt', 'codet5', or 'electra'.")
       sys.exit()         
       
    if not os.path.exists(args.p):
       print("*** Path of trained model does not exist. Provide the correct path.")
       sys.exit()                 

    os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
    os.environ["CUDA_VISIBLE_DEVICES"] = "0"
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    args.device = device
    
    if args.m == 'codebert':
        model, tokenizer = model_setting_codebert(args)
    elif args.m == 'codegpt':
        model, tokenizer = model_setting_codegpt(args)
    elif args.m == 'codet5':
        model, tokenizer = model_setting_codet5(args)
    else:
        model, tokenizer = model_setting_electra(args)
       
    extracted_path = './extracted_code'    
    if not os.path.exists(extracted_path):
        os.makedirs(extracted_path) 
        
    if not os.path.exists(args.o):
        os.makedirs(args.o)
        
    print("====================================================================================================")
    print(f"** Start: {datetime.now().strftime('%Y.%m.%d - %H:%M:%S')}")
    
    for app in tqdm(os.listdir(args.f), desc="Processing APK/ZIP Files", position=0, leave=True):
        app_path = os.path.join(args.f, app)

        if os.path.isfile(app_path) and app_path.endswith('.zip'):
            process_zip_file(app_path, extracted_path, app, args, model, tokenizer)
        elif os.path.isfile(app_path) and app_path.endswith('.apk'):
            process_apk_file(app_path, extracted_path, app, args, model, tokenizer, jadx_path)

    # if os.path.exists('./extracted_code'):
    #     shutil.rmtree('./extracted_code')
        
    # if os.path.exists('./graph'):
    #     shutil.rmtree('./graph')
        
    print(f"** End: {datetime.now().strftime('%Y.%m.%d - %H:%M:%S')}")

if __name__ == '__main__':
    main()