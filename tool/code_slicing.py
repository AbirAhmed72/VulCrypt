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
    
    file = file.replace('/', '_')
    dot_file = f'{dot_pth}/{file}.dot'
    png_file = f'{png_pth}/{file}.png'  # Corrected the extension to .png
            
    comex_cmd = f'comex --lang "java" --code-file {candidate_java_file_path} --graphs {type}'
    done = execute_comex_command(comex_cmd)
    
    if done:
        changeFormatDotFile(output_dot_file, dot_file)
        os.remove(output_dot_file)
        os.rename(output_png_file, png_file)  # This will now rename to the correct .png file
        return dot_file
    else:
        print(f"[Error in slicing] Cannot make graph from : {candidate_java_file_path}")
        return False

    
    

def splitDot(file, dot_file):
    cfg_path = './graph/cfg'
    dfg_path = './graph/dfg'
    
    if not os.path.exists(cfg_path):
        os.makedirs(cfg_path)
    if not os.path.exists(dfg_path):
        os.makedirs(dfg_path) 
    
    file = file.replace('/','_')
    cfg_file = f'graph/cfg/{file}.dot'
    dfg_file = f'graph/dfg/{file}.dot'
    
    with open(dot_file, "r") as input_f, open(cfg_file, "w") as cfg_f, open(dfg_file, "w") as dfg_f:
        for line in input_f:
            if '->' in line:
                if 'color=red' in line:
                    cfg_f.write(line)
                else:
                    dfg_f.write(line)
            else:
                cfg_f.write(line)
                dfg_f.write(line)
                
    return cfg_file, dfg_file



def extractFeature(dot_pth, candidate_java_file_path):
    print(f'extracting feature from graph of {candidate_java_file_path}')
    graph = pydotplus.graph_from_dot_file(dot_pth)
    graph_feature = []
    
    with open(candidate_java_file_path, "r") as f:
        java_code = f.readlines()

    for node in graph.get_node_list():
        node_num = node.get_name()
        if node_num.isdigit():
            if node_num == '1':
                break
        
        label = node.get_attributes().get('label').replace('"', '')
        line_num = label.split('_',1)[0]
        code = java_code[int(line_num)-1]
        type = node.get_attributes().get('type_label')
        graph_feature.append((node_num, line_num, code, type))

    graph_df = pd.DataFrame(graph_feature, columns=['node', 'line', 'code', 'type'])
    # print(graph_df)
    return graph_df



def findVAR(code, rule_num):
    var_list = []
    
    if rule_num == 1:
        api_fun = [r'getInstance\s*\(']
    elif rule_num == 2:
        api_fun = [r'new\s*SecretKeySpec\s*\(']
    elif rule_num == 31:
        api_fun = [r'load\s*\(']
    elif rule_num == 32:
        api_fun = [r'store\s*\(']
    elif rule_num == 33:
        api_fun = [r'getKey\s*\(']
    elif rule_num == 4:
        api_fun = [r'initialize\s*\(']
    else:
        api_fun = [r'new\s*IvParameterSpec\s*\(']
    
    for api in api_fun:
        match = re.search(api, code)
        if match is None:
            pass
        else:
            try:
                target = code[match.end():]
                t_count, bracket = 0, 1
                for t_ in list(target):
                    t_count += 1
                    if '(' == t_:
                        bracket += 1
                    elif ')' == t_:
                        bracket -= 1
                    if bracket == 0:
                        break
        
                params = ''.join(list(target)[0:t_count-1])
                
                if 'java.util.Locale.ENGLISH' in params:
                    var_list.append(params)

                else:
                    comma, brackets, p_count = 0, 0, 0
                    if ',' in params:
                        for p_ in list(params):
                            p_count += 1
                            if p_ in '([{':
                                brackets += 1
                            elif p_ in ')]}':
                                brackets -= 1
                            elif p_ == ',':
                                comma += 1
                                if comma > 0 and brackets == 0:
                                    if rule_num in [1,2,4,5]:
                                        params = ''.join(list(params)[0:p_count-1]).strip()
                                    else:
                                        params = ''.join(list(params)[p_count:]).strip()
                                    break
                            else:
                                continue 
                    else:
                        pass
                
                    if params.startswith('this.'):
                        this = params.split('this.')[1]
                        if '.' in this:
                            var_list.append(f"\"{this}\"")
                        else:
                            var_list.append(this)
                    else:                        
                        p_list = params.split('.')
                        for p in p_list:
                            if (p == 'this') or ('()' in p):
                                pass
                            elif ('(' and ')' in p):
                                var_list.append(p.split('(')[1].split(')')[0])
                            else:
                                var_list.append(p)
            except:
               pass 
    return var_list  



def dfgBW(target_node, dfg_path, var_list):
    graph = pydotplus.graph_from_dot_file(dfg_path)
    backward_dfg = {str(target_node)}
    src_list = {str(target_node)}
    visited_nodes = set()
    
    first = True
    while src_list:
        new_nodes = set()
        for src in src_list:
            visited_nodes.add(src)
            for edge in graph.get_edge_list():
                start = edge.get_source()
                end = edge.get_destination()
                if end == str(src):
                    if (start not in visited_nodes) and (start != '1'):
                        used_def = edge.get("used_def")
                        if first == True:
                            if (used_def == None) or (used_def in var_list):
                                new_nodes.add(start)
                        else:
                            new_nodes.add(start)
                            
            src_list = new_nodes
            backward_dfg.update(src_list)
            
            first = False
    backward_dfg = sorted(backward_dfg, key=int)
    return backward_dfg 
            


def cfgBW(backward_dfg, cfg_path, graph_df):
    graph = pydotplus.graph_from_dot_file(cfg_path)
    backward_cfg = set(backward_dfg)
    visited_nodes = set()
       
    for bw in backward_dfg:
        src_list = {str(bw)}
        while src_list:
            new_nodes = set()
            for src in src_list:
                visited_nodes.add(src)
                for edge in graph.get_edge_list():
                    start = edge.get_source()
                    end = edge.get_destination()
                    if (end == str(src)) and (start != '1'):
                        code = graph_df.loc[graph_df['node'] == start, 'code'].values[0]
                        type = graph_df.loc[graph_df['node'] == end, 'type'].values[0]
                        label = edge.get("label")
                        if (label == 'method_return' and start not in visited_nodes) or \
                            (start in visited_nodes) or \
                            (code.startswith('import')) or \
                            (label == 'class_next' and label == 'construct_next'):
                                continue
                        else:
                            new_nodes.add(start)
                            
                src_list = new_nodes
                backward_cfg.update(src_list)
    backward_cfg = sorted(backward_cfg, key=int)
    return backward_cfg     



def cfgFW(backward_dfg, cfg_path, graph_df):
    graph = pydotplus.graph_from_dot_file(cfg_path)
    forward_cfg = set(backward_dfg)
    visited_nodes = set()
    
    for bw in backward_dfg:
        src_list = {str(bw)}
        while src_list:
            new_nodes = set()
            for src in src_list:
                visited_nodes.add(src)
                for edge in graph.get_edge_list():
                    start = edge.get_source()
                    end = edge.get_destination()
                    if (start == str(src)) and (end != '1') :
                        code = graph_df.loc[graph_df['node'] == end, 'code'].values[0]
                        label = edge.get("label")
                        if (label == 'method_return' and end not in visited_nodes) or \
                            (end in visited_nodes) or \
                            (code.startswith('import')) or \
                            (label == 'construct_next' and label== 'class_next'):
                                continue
                        else:
                            new_nodes.add(end)
                          
                src_list = new_nodes
                forward_cfg.update(src_list)
    forward_cfg = sorted(forward_cfg, key=int)
    return forward_cfg   



def ifdfgBW(if_node, dfg_path):
    graph = pydotplus.graph_from_dot_file(dfg_path)
    if_dfg = set()
    
    for edge in graph.get_edge_list():
        start = edge.get_source()
        end = edge.get_destination()
        if end == str(if_node):
            if (start != '1'):
                if_dfg.add(start)
                        
    if_dfg = sorted(if_dfg, key=int)
    return if_dfg     
    


def toCode(node_snippet, graph_df, dfg_path):
    code_snippet = []
    for node in node_snippet:
        if node != '1':
            code = graph_df.loc[graph_df['node'] == node, 'code'].values[0]
            if code.strip().startswith('if'):
                if_dfg = ifdfgBW(node, dfg_path)
                for if_ in if_dfg:
                    if_code = graph_df.loc[graph_df['node'] == if_, 'code'].values[0]
                    if if_code not in code_snippet:
                        code_snippet.append(if_code)
            if code not in code_snippet:
                code_snippet.append(code)
        
    return code_snippet
    
    
    
def slicingCode(file, extracted_folder, candidate_java_file_path, crypto_line_dict):
    snippet_dir = f'{extracted_folder}/code_snippet/{file}'
    print(f"{snippet_dir} in slicingCode")
    if not os.path.exists(snippet_dir):
        os.makedirs(snippet_dir)
    
    dot_pth = makeGraph(file, candidate_java_file_path, 'cfg,dfg')
    
    if dot_pth == False:
        return False
    
    else:
        graph_df = extractFeature(dot_pth, candidate_java_file_path)
        print(graph_df)
        
        cfg_path, dfg_path = splitDot(file, dot_pth)
        
        for line_num, rule_num in crypto_line_dict.items():
            #line can be captured here
            candidate_code = graph_df.loc[graph_df['line'] == str(line_num), 'code'].values[0]
            print(f"candidate_code is {candidate_code}")
            var_list = findVAR(candidate_code, rule_num)
            print(f"variable list is {var_list}")
            
            target_node = graph_df.loc[graph_df['line'] == str(line_num), 'node'].values[0]
            print(f"target_node is {target_node}")
            
            backward_dfg = dfgBW(target_node, dfg_path, var_list)
            print(f"backward dfg: {backward_dfg}")
            backward_cfg = cfgBW(backward_dfg, cfg_path, graph_df)
            print(f"backward_cfg: {backward_cfg}")
            forward_cfg = cfgFW(backward_dfg, cfg_path, graph_df)
            print(f"forward_cfg: {forward_cfg}")
            
            node_snippet = list(set(backward_dfg + backward_cfg + forward_cfg))
            node_snippet = sorted(node_snippet, key=int)
            print(f"node_snippet: {node_snippet}")
            code_snippet = ''.join(toCode(node_snippet, graph_df, dfg_path))
            print(f"code_snippet: {code_snippet}")
            
            file = file.replace('/','_')
            snippet_path = f'{snippet_dir}/{file}_{line_num}.java'
            with open(snippet_path, 'w') as f:
                f.write(code_snippet)
                

            
    return snippet_path        
    
    