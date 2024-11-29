import os
import subprocess
import pydotplus
import pandas as pd
import re
import signal
# from normalization import normalization



# def execute_comex_command(cmd_):
#     proc = subprocess.Popen(cmd_, shell=True, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, preexec_fn=os.setsid)
    
#     try:
#         proc.communicate(timeout=120)
#         proc.wait()
#         if proc.returncode == 0:
#             return True
#         else:
#             return False
        
#     except subprocess.TimeoutExpired:
#         os.killpg(os.getpgid(proc.pid), signal.SIGKILL)
#         proc.wait()
#         return False
    
def execute_comex_command(cmd_):
    proc = subprocess.Popen(cmd_, shell=True, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, preexec_fn=os.setsid)
    
    try:
        stdout, stderr = proc.communicate(timeout=120)
        proc.wait()
        
        if proc.returncode == 0:
            return True
        else:
            print(f"Error: {stderr.decode('utf-8')}")
            return False
        
    except subprocess.TimeoutExpired:
        os.killpg(os.getpgid(proc.pid), signal.SIGKILL)
        proc.wait()
        return False
    
    
def changeFormatDotFile(output_dot_file, dot_file):
    with open(output_dot_file, "r") as input_f, open(dot_file, "w") as output_f:
        for line in input_f:
            if 'label=' in line:
                line = line.replace('\\','')
                split_line = line.split('"')
                for i in range(1,len(split_line)):
                    if i==1 or i==len(split_line)-1:
                        split_line[i] = '"'+split_line[i]
                    else:
                        split_line[i] = "'"+split_line[i]
                output_f.write(''.join(split_line))
                
            elif '->' in line:
                split_line = line.split('"')
                for i in range(1,len(split_line)):
                    if i==1 or i==len(split_line)-1:
                        split_line[i] = '"'+split_line[i]
                    else:
                        split_line[i] = "'"+split_line[i]
                output_f.write(''.join(split_line))
            
            else:
                output_f.write(line)
       
                

def makeGraph(file, candidate_java_file_path, type):
    print(f"making graph of {candidate_java_file_path}")
    output_dot_file = './output.dot'
    output_png_file = './output.png'
    dot_pth = './graph/dot'
    png_pth = './graph/png'
    
    if not os.path.exists(dot_pth):
        os.makedirs(dot_pth)
    if not os.path.exists(png_pth):
        os.makedirs(png_pth)    
    
    file = file.replace('/','_')
    dot_file = f'{dot_pth}/{file}.dot'
    png_file = f'{png_pth}/{file}.dot'
            
    comex_cmd = f'comex --lang "java" --code-file {candidate_java_file_path} --graphs {type}'
    done = execute_comex_command(comex_cmd)
    
    if done == True:
        changeFormatDotFile(output_dot_file, dot_file)
        os.remove(output_dot_file)
        os.rename(output_png_file, png_file)
        return dot_file
    else:
        print(f"[Error in slicing] Cannot make graph from : {candidate_java_file_path}")
        return False
    
    
    
    
    
def slicingCode(file, extracted_folder, candidate_java_file_path, crypto_line_dict):
    snippet_dir = f'{extracted_folder}/code_snippet/{file}'
    print(f"{snippet_dir} in slicingCode")
    if not os.path.exists(snippet_dir):
        os.makedirs(snippet_dir)
    
    dot_pth = makeGraph(file, candidate_java_file_path, 'cfg,dfg')
    
    if dot_pth == False:
        return False
    

            
    return snippet_pth        
    # return code_nz_pth        
    
    