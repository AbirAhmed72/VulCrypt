import re
from file_preprocessing import preprocess

def findCryptoLine(java_path, extracted_folder):
    # print(f"finding Crypto line of {extracted_folder}")
    java_file_path = ''
    
    rule_criteria = [
        {"import": ("javax.crypto.*", "javax.crypto.Cipher"), "regex": ("Cipher.getInstance\s*\(",)},  # Rule1
        {"import": ("javax.crypto.spec.*", "javax.crypto.spec.SecretKeySpec"), "regex": ("new\s*SecretKeySpec\s*\(",)},  # Rule2
        {"import": ("java.security.*", "java.security.KeyStore"), "regex": ()},  # Rule3
        {"import": ("java.security.*", "java.security.KeyPairGenerator"), "regex": ()},  # Rule4
        {"import": ("javax.crypto.spec.*", "javax.crypto.spec.IvParameterSpec"), "regex": ("new\s*IvParameterSpec\s*\(",)},  # Rule5
    ]
    
    with open(java_path, "r") as f:
        line_dic = {}
        code = f.read()
    
    for c_, rule in enumerate(rule_criteria):
        if any(imp in code for imp in rule["import"]):
            if c_ == 2:
                # Handle KeyStore rule
                var_list = set()
                var_list.update(re.findall(r'KeyStore\s+(\w+)\s*;', code))
                var_list.update(re.findall(r'KeyStore\s+(\w+)\s*=', code))
                
                if var_list:
                    for var in var_list:
                        rule["regex"] += (f"{var}.load\s*\(",)
                        rule["regex"] += (f"{var}.store\s*\(",)
                        rule["regex"] += (f"{var}.getKey\s*\(",)

            if c_ == 3:
                # Handle KeyPairGenerator rule
                var_list = set()
                var_list.update(re.findall(r'KeyPairGenerator\s+(\w+)\s*;', code))
                var_list.update(re.findall(r'KeyPairGenerator\s+(\w+)\s*=', code))
                
                if var_list:
                    for var in var_list:
                        rule["regex"] += (f"{var}.initialize\s*\(",)
   
            for regex in rule["regex"]:
                if re.search(regex, code):
                    java_file_path = preprocess(java_path, extracted_folder)
                    with open(java_file_path, "r") as f:
                        code_list = f.readlines()
                    
                    for n, line in enumerate(code_list, start=1):
                        for regex in rule["regex"]:
                            if re.search(regex, line):
                                if c_ == 2:
                                    # Assign specific rule numbers for KeyStore
                                    if 'load' in regex:
                                        rule_num = 31
                                    elif 'store' in regex:
                                        rule_num = 32
                                    else:
                                        rule_num = 33
                                else:
                                    # Generic rule number
                                    rule_num = c_ + 1
                                line_dic[n] = rule_num
    
    # Sort dictionary by line numbers
    sort_dic = {key: line_dic[key] for key in sorted(line_dic.keys())}
    return sort_dic, java_file_path
