import argparse
from datetime import datetime
import os
import sys
import time
import zipfile

from tqdm import tqdm

from extract_crypto_line import findCryptoLine

def unzip_source(zip_path, unzip_path):
    print(f"unzipping {unzip_path}")
    with zipfile.ZipFile(zip_path, 'r') as zip_ref:
        zip_ref.extractall(unzip_path)
    return unzip_path


def main():
    parser = argparse.ArgumentParser(description='VulCrypt')
    parser.add_argument('--f', '--folder', type=str, required=True, help='Target APK/Java Source Code Folder or Zip File Path')
    parser.add_argument('--o', '--output', type=str, required=True, help='Output Folder Path')
    
    args = parser.parse_args()

    if not os.path.exists(args.f):
       print("*** Folder/Zip file does not exist. Provide the correct path.")
       sys.exit()
       
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
            extracted_folder = os.path.join(extracted_path, app.replace('.zip', ''))
            os.makedirs(extracted_folder)
            unzip_source(app_path, extracted_folder)

            java_files = [os.path.join(root, f) for root, _, files in os.walk(extracted_folder) for f in files if f.endswith('.java')]
            java_progress = tqdm(total=len(java_files), desc=f"Java Files in {app}", position=1, leave=True)

            for java_path in java_files:
                start_time = time.time()
                try:
                    line_dic, pre_java_path = findCryptoLine(java_path, extracted_folder)
                    if line_dic:
                        print(f"{line_dic} found from findCryptoLine")
                    print(f"pre_java_path is {pre_java_path}")
                    java_progress.update(1)
                    print(f"[INFO] Processed {java_path} in {time.time() - start_time:.2f} seconds")
                except Exception as e:
                    print(f"[ERROR] {e} for file {java_path}")

            # java_progress.close()
        elif os.path.isfile(app_path) and app_path.endswith('.apk'):
            # ToDO: implement apk decompilation
            print('ToDO: implement apk decompilation')


    print(f"** End: {datetime.now().strftime('%Y.%m.%d - %H:%M:%S')}")

if __name__ == '__main__':
    main()