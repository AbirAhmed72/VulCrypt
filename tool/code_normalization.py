import subprocess
import os
import pydotplus
import re
import shutil
import signal



def execute_command(cmd_):
    proc = subprocess.Popen(cmd_, shell=True, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, preexec_fn=os.setsid)
    
    try:
        proc.communicate(timeout=120)
        proc.wait()
        if proc.returncode == 0:
            return True
        else:
            return False
        
    except subprocess.TimeoutExpired:
        os.killpg(os.getpgid(proc.pid), signal.SIGKILL)
        proc.wait()
        return False

                
                
def makeASTGraph(file, snippet_path):
    print(f"making graph in norm of {snippet_path}")
    output_dot_file = './output.dot'
    output_png_file = './output.png'
    
    dot_path = './graph/ast/dot'
    png_pth = './graph/ast/png'
    if not os.path.exists(dot_path):
        os.makedirs(dot_path)
    if not os.path.exists(png_pth):
        os.makedirs(png_pth)  
    
    dot_file = f"{dot_path}/{file}.dot"
    png_file = f"{png_pth}/{file}.png"
            
    comex_cmd = f'comex --lang "java" --code-file {snippet_path} --graphs ast'
    done = execute_command(comex_cmd)
    
    if done == True:
        os.rename(output_dot_file, dot_file)
        os.rename(output_png_file, png_file)
        return dot_file
    else:
        return False



def extractASTFeature(dot_path):
    print("extracting feature inside norm")
    target_word = []
    excep_target_word = []

    graph = pydotplus.graph_from_dot_file(dot_path)

    for node in graph.get_node_list():
        node_num = node.get_name()
        if node_num.isdigit():
            if node_num == '1':
                break
        
        node_type = node.get_attributes().get('node_type')
        if node_type == 'identifier' or node_type == 'this':
            target = node.get_attributes().get('label')
            if target == 'Exception' or target == 'new':
                continue
            if ('StringUtil\.' in target) or ('AESUtils\.' in target):
                continue
            if '.' in target:
                if 'this\.' in target:
                    target = target.replace('"','').split('.')[-1]
                else:
                    target = target.replace('"','').replace('\\','')
            if target not in target_word:
                target_word.append(target)            
        
        if node_type == 'type_identifier':
            excep = node.get_attributes().get('label')
            if excep not in excep_target_word:
                excep_target_word.append(excep)
                
    target_word = [x for x in target_word if x not in excep_target_word]
    return target_word


def normalizeCode(file, extracted_folder, snippet_path, line_num):    
    # Create the directory under the decompile path instead of inside the Java source code file
    code_normalization_directory = f'{extracted_folder}/code_snippet_nz/{file}'
    print(f'normalization of {extracted_folder}') 
    if not os.path.exists(code_normalization_directory):
        os.makedirs(code_normalization_directory)
        
    # Path for the normalized code snippet
    code_normalization_path = f'{code_normalization_directory}/{file}_{line_num}.java'
            
    dot_path = makeASTGraph(file, snippet_path)
    
    if dot_path == False:
        # If the AST graph creation fails, copy the snippet as is into the decompile folder
        shutil.copy(snippet_path, code_normalization_path)
    else:
        target_word = extractASTFeature(dot_path)
        print(f"target_word: {target_word}")
        
        # Filter out irrelevant targets
        if 'java.util.Locale.ENGLISH' in target_word:
            target_word.remove('java.util.Locale.ENGLISH')
        if 'Math' in target_word:
            target_word.remove('Math')
        if 'Integer.MAX_VALUE' in target_word:
            target_word.remove('Integer.MAX_VALUE')
        if 'Integer' in target_word:
            target_word.remove('Integer')
        if 'System' in target_word:
            target_word.remove('System')
        if 'Base64' in target_word:
            target_word.remove('Base64')
        
        with open(snippet_path, "r") as f:
            code = f.readlines()
            code_str = ''.join(code)
            
        print(f"code_str_before_fn_norm: {code_str}")
        # ============================= FUNCTION normalization =============================
        normalizable_function_list = []
        fnum = 0
        for word in target_word:
            if re.search(r'\b{}\b\s*\('.format(re.escape(word)), code_str):
                normalizable_function_list.append(word)            
        fun_patterns = [re.compile(r"\b{}\b\s*\(".format(re.escape(fun))) for fun in normalizable_function_list]
        print(f"fun_patterns: {fun_patterns}")
        
        for c_ in code:
            if c_.startswith(('public', 'private', 'protected', 'void')) and '{' in c_:
                for p_ in fun_patterns:
                    if re.search(p_, c_):
                        fnum += 1
                        code_str = re.sub(p_, f'FUN{fnum}(', code_str)

        print(f"code_str_after_fn_norm: {code_str}")
        
        # ============================= VARIABLE normalization =============================
        normalizable_variable_list = []
        for word in target_word:
            if re.search(r'\b{}\b\s*=\s*'.format(re.escape(word)), code_str) \
                or re.search(r'\b{}\b\s*[,]'.format(re.escape(word)), code_str) \
                or re.search(r'\b{}\b\s*[)]'.format(re.escape(word)), code_str) \
                or re.search(r'[(]\s*\b{}\b'.format(re.escape(word)), code_str) \
                or re.search(r'\b{}\b\s*[.]'.format(re.escape(word)), code_str) \
                or re.search(r'\b{}\b\s*;\s*'.format(re.escape(word)), code_str):
                    normalizable_variable_list.append(word)
        print(f'normalizable_variable_list: {normalizable_variable_list}')
        var_patterns = [re.compile(r'(?<!")\b{}\b(?!["(])'.format(re.escape(var))) for var in normalizable_variable_list]
        print(f'var_patterns: {var_patterns}')
        
        for num in range(len(var_patterns)):
            code_str = re.sub(var_patterns[num], f'VAR{num+1}', code_str)
        
        # Write the normalized code snippet to the specified path in the decompile directory
        with open(code_normalization_path, 'a') as f:
            f.write(''.join(code_str))
        print(f'normalized code snippet: {code_str}')
        
    return code_normalization_directory
